# Техническая документация: API Gateway

## Общая информация

Этот проект реализует API-шлюз с использованием FastAPI, предназначенный для маршрутизации запросов к внутренним сервисам.
Шлюз работает как единая точка входа и перенаправляет HTTP-запросы к соответствующим микросервисам, запущенным через `docker-compose`.

## Особенности
- Реализация через [FastAPI](https://fastapi.tiangolo.com/)
- Асинхронная пересылка HTTP-запросов с использованием `httpx`
- Универсальная маршрутизация всех HTTP-методов: `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`
- Запуск в контейнере Docker
- Открыт на физическом порту: `3457`

## Структура проекта

```bash
.
├── docker-compose.yml      # основной файл для запуска всех сервисов
├── gateway
│   ├── main.py             # основной код шлюза
│   └── Dockerfile          # сборка образа шлюза
├── search                  # сервис поиска
├── storage                 # файловый сервис
└── ... другие сервисы
```

## Маршруты

Все запросы к шлюзу имеют базовый путь `/api/{service}/...`, где `{service}` — имя сервиса, зарегистрированное в `SERVICE_MAP`.

Примеры:
```
GET /api/search?q=тест        → перенаправляется в search-сервис
POST /api/files/upload        → перенаправляется в storage-сервис
```

## Поддерживаемые сервисы

В `main.py` определены следующие маршруты:

```python
SERVICE_MAP = {
    "search": "http://search:8080/api/search",
    "files":  "http://storage:8080/api/files",
}
```

Чтобы добавить новый сервис:
1. Добавьте его в `docker-compose.yml`
2. Зарегистрируйте маршрут в `SERVICE_MAP`

Пример:
```python
"ocr": "http://ocr:5000/api/ocr"
```

## Запуск через Docker Compose

Убедитесь, что у вас установлен Docker и Docker Compose. Запуск осуществляется одной командой:

```bash
docker compose up --build
```

По умолчанию шлюз будет доступен по адресу: [http://localhost:3457](http://localhost:3457)

## Dockerfile для gateway

```Dockerfile
FROM python:3.11-slim

WORKDIR /app
COPY main.py .
RUN pip install fastapi uvicorn httpx

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
```

## docker-compose.yml (фрагмент)

```yaml
gateway:
  build: ./gateway
  ports:
    - "3457:8000"
  depends_on:
    - search
    - storage
  networks:
    - app
```

## Безопасность
На текущем этапе шлюз не реализует авторизацию или аутентификацию. Все запросы перенаправляются напрямую. Вы можете расширить функциональность с помощью FastAPI Depends, JWT или OAuth2 позже.

## Заключение

API Gateway обеспечивает централизованную маршрутизацию между микросервисами, работает через Docker Compose и доступен на порту `3457`. Поддерживает добавление новых сервисов с минимальными изменениями в коде.

