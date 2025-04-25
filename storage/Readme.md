# Техническая документация: Модуль `storage`

## Общая информация
Модуль `storage` является частью системы DMS и отвечает за загрузку, скачивание, удаление и отображение файлов пользователей. Он работает с хранилищем объектов MinIO и взаимодействует через REST API.

---

## Технологии
- Java 17+
- Spring Boot 3+
- Spring Web
- Spring Actuator
- MinIO Java SDK
- Docker

---

## Основные конечные точки API

### 1. Загрузка файла
**POST** `/api/files/upload`

**Параметры запроса**:
- `file`: `multipart/form-data` — файл для загрузки

**Ответ**: `200 OK`
```json
{
  "filename": "example.pdf",
  "url": "http://storage:8080/api/files/example.pdf"
}
```

**Ошибки**:
- `400 Bad Request` — файл отсутствует в запросе
- `500 Internal Server Error` — ошибка загрузки в MinIO

---

### 2. Скачивание файла
**GET** `/api/files/{filename}`

**Ответ**: `200 OK` (бинарный контент)

**Ошибки**:
- `404 Not Found` — файл не найден в хранилище
- `500 Internal Server Error` — ошибка доступа к MinIO

---

### 3. Удаление файла
**DELETE** `/api/files/{filename}`

**Ответ**: `204 No Content`

**Ошибки**:
- `404 Not Found` — файл не найден
- `500 Internal Server Error` — ошибка при удалении

---

### 4. Отображение списка файлов (если реализовано)
**GET** `/api/files/list`

**Ответ**: `200 OK`
```json
[
  {
    "filename": "file1.pdf",
    "size": 123456,
    "lastModified": "2024-04-25T10:30:00Z"
  },
  ...
]
```

---

## Конфигурация MinIO

### application.properties
```properties
minio.url=http://minio:9000
minio.access-key=your-access-key
minio.secret-key=your-secret-key
minio.bucket=dms-files
```

### Docker-compose пример сервиса MinIO
```yaml
minio:
  image: minio/minio
  ports:
    - "9000:9000"
    - "9001:9001"
  environment:
    MINIO_ROOT_USER: your-access-key
    MINIO_ROOT_PASSWORD: your-secret-key
  command: server /data --console-address ":9001"
  volumes:
    - minio_data:/data

volumes:
  minio_data:
```

### Инициализация бакета
При старте приложения `FileServiceImpl` проверяет наличие бакета `dms-files`, и если он отсутствует — создаёт его.

---

## Особенности и рекомендации
- Объекты в MinIO хранятся с уникальными ключами, во избежание конфликтов имен можно использовать UUID.
- Рекомендуется проверка размеров и типов файлов на клиентской стороне.
- Для MinIO желательно использовать отдельный persistent volume.
- Сервис должен запускаться после MinIO, можно настроить `depends_on` в `docker-compose.yml`.

---

## Пример зависимости в `pom.xml`
```xml
<dependency>
  <groupId>io.minio</groupId>
  <artifactId>minio</artifactId>
  <version>8.5.6</version>
</dependency>
```

---

## Возможные ошибки
| Код | Сообщение | Причина |
|-----|-----------|---------|
| 400 | Invalid file | Запрос не содержит файла |
| 404 | File not found | Файл отсутствует в MinIO |
| 500 | Upload error | Ошибка соединения или сохранения |

---

## Контакты
Если возникают проблемы или предложения — обращаться к команде backend разработки.

