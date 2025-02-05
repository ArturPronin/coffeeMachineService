# Coffee Machine Service

## 1. Инструкция по запуску (docker)

1. Установить и запустить Docker [Скачать](https://www.docker.com/products/docker-desktop/).

2. Склонировать репозиторий:

```bash
git clone https://github.com/ArturPronin/coffeeMachineService.git
```

3. Перейти в папку с репозиторием:
```bash
cd "<your-path>\coffee-machine-service"
```

4. **(Опционально):** В папке с проектом есть файл `.env` в котором нужно указать логин и пароль для БД:
```
DB_USER=your_db_user
DB_PASSWORD=your_db_password
```

5. В папке с проектом запустить терминал и выполнить команду:
```bash
docker-compose up --build
```

6. После скачивания всех зависимостей и установки JDK и Postgres, приложение запустится и будет доступно:
- Web: [http://localhost:8081]
- Swagger: [http://localhost:8081/swagger-ui.html]
- PostgreSQL: localhost:5432

#### или использовать команду для быстрого старта:
```bash
git clone https://github.com/ArturPronin/coffeeMachineService.git && cd coffee-machine-service && echo "DB_USER=postgres\nDB_PASSWORD=postgres" > .env && docker compose up --build
```

## 2. Инструкция по запуску (maven)

1. **Требования к проекту:**
- Java 17
- Maven
- PostgreSQL 16

2. Создать БД с любым названием


3. Добавить переменные окружения:
- Хост на котором запущена БД:
  `DB_HOST=localhost`
- Порт на котором запущена БД:
  `DB_PORT=5432`
- Название БД, которую создали:
  `DB_COFFEE_MACHINE_NAME=coffee_machine_db`
- Логин доступа к БД:
  `DB_USER=postgres`
- Пароль доступа к БД):
  `DB_PASSWORD=postgres`

4. В папке с проектом запустить терминал и выполнить команду:
```bash
mvn spring-boot:run
```

5. Приложение запустится и будет доступно:
- Web: [http://localhost:8081]
- Swagger: [http://localhost:8081/swagger-ui.html]
