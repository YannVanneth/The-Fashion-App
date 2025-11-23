# The-Fashion-App
<p>
  This project is a multi-vendor marketplace built using microservices architecture . 
It allows vendors to manage products and sales, and customers to browse and purchase products. 
The system is designed with scalability, security, and microservice best practices in mind.
</p>

## Team Members
- **Sanom Rin** – Full-Stack Developer
- **Vanneth Yann** – Full-Stack Developer

## Features
- Multi-vendor user registration and authentication
- Vendor product management (CRUD)
- Customer product browsing and purchase
- Payment processing and revenue tracking
- API Gateway with JWT authentication
- Service discovery using Eureka
- Optional: Asynchronous notifications (Kafka/RabbitMQ)

## Tech Stack
- **Backend:** Spring Boot 3, Python , javaScript, ..... , Spring Cloud (Eureka, Gateway, Feign)  
- **Database:** PostgreSQL , MongoDB
- **Security:** JWT  
- **Messaging:** Kafka / RabbitMQ (optional)  
- **Build Tool:** gradle  
- **Containerization:** Docker

## Architecture
                    ┌────────────────────────┐
                    │      API GATEWAY       │
                    └───────────┬────────────┘
                                │
            ┌───────────────────┼──────────────────────┐
            │                   │                      │
     USER SERVICE        PRODUCT SERVICE        ORDER SERVICE
            │                   │                      │
            └───────┐     ┌────┘                      │
                    ▼     ▼                            ▼
               VENDOR SERVICE                    PAYMENT SERVICE

  
## Project Structure

- user-service/

    ├─ src/main/java/com/project/user/
  
    ├─ src/main/resources/application.yml
- product-service/
  
    ├─ src/main/java/com/project/product/

    ├─ src/main/resources/application.yml
- payment-service/
  
    ├─ src/main/java/com/project/payment/

    ├─ src/main/resources/application.yml
- api-gateway/
  
    ├─ src/main/java/com/project/gateway/

    ├─ src/main/resources/application.yml
- eureka-server/
  
    ├─ src/main/java/com/project/eureka/
  
    ├─ src/main/resources/application.yml

## Database Design
- Each service has its own PostgreSQL database
- Tables:
  - users (user_service)
  - vendors (vendor_service)
  - products (product_service)
  - payments (payment_service)
  - orders (order_service)




