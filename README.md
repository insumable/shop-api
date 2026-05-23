# Shop API

A Spring Boot REST API for managing products with image upload support, backed by MySQL.

---

## Tech Stack

- **Java** + **Spring Boot 3**
- **Spring Data JPA** (Hibernate)
- **MySQL** (snake_case schema)
- **Lombok**
- **Multipart file upload** for product images

---

## Project Structure

```
com.soham.shopapi
├── controller
│   └── ProductController.java
├── entity
│   └── Product.java
├── repository
│   └── ProductRepository.java
└── service
    ├── ProductService.java
    └── ProductServiceImpl.java
```

---

## Database Setup

```sql
CREATE TABLE product
(
    id            INT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)       NULL,
    description   VARCHAR(255)       NULL,
    brand         VARCHAR(255)       NULL,
    price         DECIMAL            NULL,
    category      VARCHAR(255)       NULL,
    release_date  DATETIME           NULL,
    available     BIT(1)             NULL,
    quantity      INT                NULL,
    image_name    VARCHAR(255)       NULL,
    image_type    VARCHAR(255)       NULL,
    image_date    MEDIUMBLOB         NULL,   -- MEDIUMBLOB supports up to 16MB
    CONSTRAINT pk_product PRIMARY KEY (id)
);
```

> ⚠️ Use `MEDIUMBLOB` for `image_date`, not `BLOB` (which is limited to 65KB).

---

## Configuration

`src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

> Set `ddl-auto=none` to prevent Hibernate from overriding your schema (e.g. reverting `MEDIUMBLOB` back to `BLOB`).

---

## API Endpoints

| Method   | Endpoint                      | Description              | Body                        |
|----------|-------------------------------|--------------------------|-----------------------------|
| `GET`    | `/api/products`               | Get all products         | —                           |
| `GET`    | `/api/product/{id}`           | Get product by ID        | —                           |
| `GET`    | `/api/product/{id}/image`     | Get product image        | —                           |
| `GET`    | `/api/products/search?keyword=` | Search products        | —                           |
| `POST`   | `/api/product`                | Add new product          | `multipart/form-data`       |
| `PUT`    | `/api/product/{id}`           | Update product           | `multipart/form-data`       |
| `DELETE` | `/api/product/{id}`           | Delete product           | —                           |

---

## Testing with Postman

### POST / PUT (multipart/form-data)

`POST http://localhost:8080/api/product`

Set Body → **form-data**:

| Key         | Type | Value                          |
|-------------|------|--------------------------------|
| `product`   | Text | JSON string (see below)        |
| `imageFile` | File | Select a `.jpg` or `.png` file |

**Sample `product` value (Text field):**

```json
{
    "name": "Sony WH-1000XM5",
    "description": "Industry-leading noise cancelling wireless headphones",
    "brand": "Sony",
    "price": 349.99,
    "category": "Electronics",
    "releaseDate": "2022-05-20",
    "available": true,
    "quantity": 35
}
```

> The `product` field is sent as a plain text JSON string and deserialized server-side using `ObjectMapper`. Do **not** set it to `application/json` Content-Type in Postman — leave it as plain Text.

### Search

```
GET http://localhost:8080/api/products/search?keyword=sony
```

---

## Known Issues & Fixes Applied

| Issue | Fix |
|---|---|
| `ProductService` interface missing `throws IOException` | Added to `addProduct` and `updateProduct` signatures |
| `updateProduct` ignored path variable `id` | Added `product.setId(id)` before `repo.save()` |
| `@Column` names didn't match snake_case DB schema | Updated all `@Column(name=...)` values |
| `int id` / `int quantity` couldn't hold `null` | Changed to `Integer` wrapper type |
| `415 Unsupported Media Type` on multipart requests | Controller now accepts `product` as `String`, deserializes with `ObjectMapper` |
| `BLOB` too small for images > 65KB | Altered column to `MEDIUMBLOB` (16MB limit) |
| Image endpoint NPE if product has no image | Added null checks for `imageDate` and `imageType` |

---

## Running the App

```bash
mvn spring-boot:run
```

API will be available at `http://localhost:8080`.

---

## Notes

- Images are stored directly in the database as `MEDIUMBLOB`. For production, consider using object storage (AWS S3, GCS) and storing only the URL.
- The `@CrossOrigin` annotation on the controller allows all origins. Restrict this in production.
- `releaseDate` is formatted as `yyyy-MM-dd` in JSON via `@JsonFormat`.
