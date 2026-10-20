# TalentNext MS2 Assignment — Order & Product Microservices

Two independent Spring Boot (Java 21) services, each with its own H2 in-memory database.

## Services

| Service | Port | DB | H2 Console |
|---|---|---|---|
| product-service | 8081 | `productdb` | http://localhost:8081/h2-console |
| order-service | 8082 | `orderdb` | http://localhost:8082/h2-console |

`order-service` calls `product-service` over REST (`ProductClient`, configured via `product.service.url` in `order-service/src/main/resources/application.properties`) to validate the product referenced by an order and enrich the response with product details.

## Running

Start `product-service` first, then `order-service` (each in its own terminal):

```bash
cd product-service
mvn spring-boot:run
```

```bash
cd order-service
mvn spring-boot:run
```

## Product Service API

| Method | Path | Description |
|---|---|---|
| GET | /api/products | List all products |
| GET | /api/products/{id} | Get product by id |
| POST | /api/products | Create product |
| PUT | /api/products/{id} | Update product |
| DELETE | /api/products/{id} | Delete product |

Sample body:

```json
{
  "name": "Laptop",
  "description": "15-inch business laptop",
  "price": 55000.00,
  "quantity": 25
}
```

Sample data is seeded on startup via `data.sql`.

## Order Service API

| Method | Path | Description |
|---|---|---|
| GET | /api/orders | List all orders |
| GET | /api/orders/{id} | Get order by id (includes product details) |
| POST | /api/orders | Create an order (validates product via product-service) |
| PUT | /api/orders/{id}/status?status=SHIPPED | Update order status |
| DELETE | /api/orders/{id} | Delete order |

Sample body (use a product id that exists in product-service, e.g. `1`):

```json
{
  "productId": 1,
  "quantity": 2
}
```

## Hello World app for AWS ECS

`hello-world-app/` is a separate Spring Boot Hello World REST API. It comes with a `Dockerfile`, `buildspec.yaml`, `appspec.yaml` and `taskdef.json` for deploying to Amazon ECS through CodeCommit, CodeBuild, ECR, CodeDeploy and CodePipeline. See [hello-world-app/README.md](hello-world-app/README.md) for the step-by-step guide. [hello-world-app.zip](hello-world-app.zip) contains the same code, ready to push to a new CodeCommit repository.
