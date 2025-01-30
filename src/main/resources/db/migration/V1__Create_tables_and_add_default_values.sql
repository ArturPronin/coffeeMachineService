-- Таблица ингредиентов
CREATE TABLE ingredients
(
    ingredient_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ingredient_name  VARCHAR(30) NOT NULL UNIQUE,
    amount_available INT         NOT NULL CHECK (amount_available >= 0),
    unit             VARCHAR(10) NOT NULL
);

-- Таблица рецептов
CREATE TABLE recipes
(
    recipe_id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipe_name VARCHAR(30) NOT NULL UNIQUE
);

-- Связь рецептов и ингредиентов
CREATE TABLE recipe_ingredients
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipe_id          UUID NOT NULL REFERENCES recipes (recipe_id) ON DELETE CASCADE,
    ingredient_id      UUID NOT NULL REFERENCES ingredients (ingredient_id),
    quantity_on_recipe INT  NOT NULL CHECK (quantity_on_recipe > 0),
    UNIQUE (recipe_id, ingredient_id)
);

-- Таблица напитков
CREATE TABLE drinks
(
    drink_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    drink_name   VARCHAR(30) NOT NULL UNIQUE,
    recipe_id    UUID        NOT NULL REFERENCES recipes (recipe_id) ON DELETE CASCADE,
    orders_count INT              DEFAULT 0 CHECK (orders_count >= 0)
);

-- Таблица заказов
CREATE TABLE orders
(
    order_id   UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    drink_id   UUID      NOT NULL REFERENCES drinks (drink_id),
    status     VARCHAR   NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Наполнение таблиц данными
-- Добавление ингредиентов
INSERT INTO ingredients (ingredient_name, amount_available, unit)
VALUES ('Кофе', 1000, 'г'),
       ('Вода', 5000, 'мл'),
       ('Молоко', 3000, 'мл');

-- Добавление рецептов
INSERT INTO recipes (recipe_name)
VALUES ('Эспрессо'),
       ('Американо'),
       ('Капучино');

-- Добавление ингредиентов в рецепты
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_on_recipe)
SELECT r.recipe_id,
       i.ingredient_id,
       CASE
           WHEN r.recipe_name = 'Эспрессо' THEN 7
           WHEN r.recipe_name = 'Американо' THEN 7
           WHEN r.recipe_name = 'Капучино' THEN 7
           END
FROM recipes r,
     ingredients i
WHERE i.ingredient_name = 'Кофе';

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_on_recipe)
SELECT r.recipe_id,
       i.ingredient_id,
       CASE
           WHEN r.recipe_name = 'Эспрессо' THEN 30
           WHEN r.recipe_name = 'Американо' THEN 100
           WHEN r.recipe_name = 'Капучино' THEN 30
           END
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