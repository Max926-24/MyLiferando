# MyLiferando

Бэкенд сервиса доставки, по мотивам Lieferando. Заведения (рестораны, магазины, аптеки) создают заказы, диспетчер назначает на них курьеров, курьер забирает заказ и доставляет получателю.

Учебный проект на Spring Boot. Рассчитан на работу в России: клиенты идентифицируются по ИНН.

> **Статус:** в разработке. Готовы сущности, репозитории, DTO и мапперы, дальше сервисы. REST API пока нет.

## Стек

- Java 17
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- MapStruct (преобразование сущностей в DTO и обратно)
- PostgreSQL 16 в Docker
- Maven (через `mvnw`, ставить Maven не нужно)

## Быстрый старт

Нужны **Java 17+** и **Docker**.

1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/Max926-24/MyLiferando.git
   cd MyLiferando
   ```
2. Запустить базу данных:
   ```bash
   docker compose up -d
   ```
   PostgreSQL поднимется на порту **5433** (база `myliferando`, пользователь и пароль `postgres`).
3. Запустить приложение:
   ```bash
   ./mvnw spring-boot:run
   ```
   Приложение стартует на http://localhost:8080. Таблицы Hibernate создаст сам при первом запуске.
4. Запустить тесты (база должна быть запущена):
   ```bash
   ./mvnw test
   ```

Пересоздать базу с нуля (все данные удалятся):
```bash
docker compose down -v && docker compose up -d
```

## Модель данных

```
Client (заведение) 1 ──── * Order (заказ) * ──── 0..1 Courier (курьер)
```

### Client — заведение-отправитель

| Поле | Описание |
|---|---|
| `name` | название |
| `address` | адрес, откуда курьер забирает заказ |
| `email` | уникальный |
| `phone` | до 20 символов |
| `clientType` | чем занимается: ресторан, магазин, автосервис, аптека, алкомаркет, другое |
| `legalType` | кто по закону: юрлицо, ИП, физлицо |
| `inn` | ИНН, уникальный: 10 цифр у юрлица, 12 у ИП, у физлица необязателен |

### Courier — курьер

| Поле | Описание |
|---|---|
| `name` | имя |
| `phone` | уникальный |
| `transport` | пешком, велосипед, машина |
| `status` | не на смене, свободен, везёт заказ |

### Order — заказ

| Поле | Описание |
|---|---|
| `client` | кто отправляет (обязательно) |
| `courier` | кто везёт (пусто, пока курьер не назначен) |
| `description` | что везём (необязательно) |
| `deliveryAddress` | куда везём |
| `recipientName`, `recipientPhone` | кому отдать |
| `status` | статус заказа |
| `createdAt`, `deliveredAt` | время создания и доставки, ставит система |

## Жизненный цикл заказа

```
CREATED ──► ASSIGNED ──► PICKED_UP ──► DELIVERED
   │            │            │
   └────────────┴────────────┴──► CANCELLED
```

| Действие | Заказ | Курьер |
|---|---|---|
| курьер начал смену | — | `OFF_DUTY` → `AVAILABLE` |
| курьер назначен на заказ | `CREATED` → `ASSIGNED` | `AVAILABLE` → `BUSY` |
| курьер забрал заказ | `ASSIGNED` → `PICKED_UP` | — |
| заказ доставлен | `PICKED_UP` → `DELIVERED` | `BUSY` → `AVAILABLE` |
| заказ отменён | любой, кроме `DELIVERED` → `CANCELLED` | `BUSY` → `AVAILABLE` |
| курьер закончил смену | — | `AVAILABLE` → `OFF_DUTY` |

Курьер везёт не больше одного заказа одновременно.

## Структура проекта

```
src/main/java/de/ait/myliferando/
├── domain/          сущности JPA: Client, Courier, Order
│   └── enums/       ClientType, LegalType, CourierStatus, OrderStatus, Transport
├── repository/      репозитории Spring Data JPA
└── dto/             объекты запросов и ответов API: client/, courier/, order/
    └── mapping/     мапперы MapStruct: ClientMapper, CourierMapper, OrderMapper
```

## Планы

- [x] Сущности и связи
- [x] Репозитории
- [x] DTO запросов и ответов
- [x] Мапперы (MapStruct)
- [ ] Сервисы с правилами смены статусов
- [ ] REST-контроллеры
- [ ] Обработка ошибок (400, 404, 409)
- [ ] Миграции базы (Liquibase или Flyway) вместо `ddl-auto: update`
- [ ] Авторизация (JWT), роли: заведение, курьер, диспетчер
- [ ] Тесты сервисов
