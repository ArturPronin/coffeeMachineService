-- Таблица рецептов
CREATE TABLE recipes
(
    recipe_id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipe_name VARCHAR(30) NOT NULL UNIQUE
);

-- Таблица ингредиентов
CREATE TABLE ingredients
(
    ingredient_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ingredient_name  VARCHAR(30) NOT NULL UNIQUE,
    amount_available INT         NOT NULL CHECK (amount_available >= 0),
    unit             VARCHAR(10) NOT NULL
);

-- Связь рецептов и ингредиентов
CREATE TABLE recipe_ingredients
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipe_id          UUID NOT NULL REFERENCES recipes (recipe_id) ON DELETE RESTRICT,
    ingredient_id      UUID NOT NULL REFERENCES ingredients (ingredient_id) ON DELETE RESTRICT,
    quantity_on_recipe INT  NOT NULL CHECK (quantity_on_recipe > 0),
    UNIQUE (recipe_id, ingredient_id)
);

-- Таблица напитков
CREATE TABLE drinks
(
    drink_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    drink_name   VARCHAR(30) NOT NULL UNIQUE,
    recipe_id    UUID        NOT NULL REFERENCES recipes (recipe_id) ON DELETE RESTRICT,
    orders_count INT              DEFAULT 0 CHECK (orders_count >= 0)
);

-- Таблица заказов
CREATE TABLE orders
(
    order_id   UUID,
    drink_id   UUID        NOT NULL REFERENCES drinks (drink_id) ON DELETE RESTRICT,
    status     VARCHAR(20) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id, created_at)
) PARTITION BY RANGE (created_at);

-- Дефолтная партиция для данных без подходящей партиции
CREATE TABLE orders_default PARTITION OF orders DEFAULT;

-- Процедура для создания партиций на несколько лет вперед
CREATE OR REPLACE PROCEDURE prepare_partition_years(years_ahead INT)
    LANGUAGE plpgsql
AS
$$
DECLARE
    current_year INT;
    target_year  INT;
BEGIN
    current_year := EXTRACT(YEAR FROM CURRENT_DATE);

    FOR i IN 0..years_ahead
        LOOP
            target_year := current_year + i;
            IF NOT EXISTS (SELECT 1
                           FROM pg_tables
                           WHERE tablename = 'orders_' || target_year) THEN
                EXECUTE format(
                        'CREATE TABLE orders_%s PARTITION OF orders ' ||
                        'FOR VALUES FROM (%L) TO (%L)',
                        target_year,
                        make_date(target_year, 1, 1),
                        make_date(target_year + 1, 1, 1)
                        );
            END IF;
        END LOOP;
END;
$$;

-- Процедура для удаления партиций старше 5 лет
CREATE OR REPLACE PROCEDURE cleanup_old_partitions()
    LANGUAGE plpgsql
AS
$$
DECLARE
    partition_name TEXT;
    year_threshold INT;
BEGIN
    year_threshold := EXTRACT(YEAR FROM NOW()) - 5;

    FOR partition_name IN
        SELECT inhrelid::regclass::text
        FROM pg_inherits
        WHERE inhparent = 'orders'::regclass
          AND inhrelid::regclass::text ~ 'orders_(\d{4})'
          AND substring(inhrelid::regclass::text FROM 'orders_(\d{4})')::int < year_threshold
        LOOP
            EXECUTE format('DROP TABLE %I', partition_name);
            RAISE NOTICE 'Удалена партиция %', partition_name;
        END LOOP;
END;
$$;

-- Наполнение таблиц данными
-- Добавление ингредиентов
INSERT INTO ingredients (ingredient_name, amount_available, unit)
VALUES ('Кофе', 1000, 'г'),
       ('Вода', 5000, 'мл'),
       ('Молоко', 500, 'мл');

-- Добавление рецептов
INSERT INTO recipes (recipe_name)
VALUES ('Эспрессо'),
       ('Американо'),
       ('Капучино');

-- Добавление ингредиентов в рецепты
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_on_recipe)
SELECT r.recipe_id, i.ingredient_id, 7
FROM recipes r,
     ingredients i
WHERE i.ingredient_name = 'Кофе';

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_on_recipe)
SELECT r.recipe_id,
       i.ingredient_id,
       CASE
           WHEN r.recipe_name = 'Эспрессо' THEN 30
           WHEN r.recipe_name = 'Американо' THEN 100
           WHEN r.recipe_name = 'Капучино' THEN 30 END
FROM recipes r,
     ingredients i
WHERE i.ingredient_name = 'Вода';

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_on_recipe)
SELECT r.recipe_id, i.ingredient_id, 150
FROM recipes r,
     ingredients i
WHERE r.recipe_name = 'Капучино'
  AND i.ingredient_name = 'Молоко';

-- Добавление напитков
INSERT INTO drinks (drink_name, recipe_id)
SELECT recipe_name, recipe_id
FROM recipes;