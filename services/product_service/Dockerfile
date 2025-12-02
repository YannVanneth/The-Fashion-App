
FROM golang:1.24-alpine AS builder

LABEL authors="yannvanneth"

ENV PRODUCT_SERVICE_PORT=9006
ENV PRODUCT_SERVICE_DB_NAME=product_service_db
ENV PRODUCT_SERVICE_DB_URL=mongodb://product_service_:product_service_@product-service-db:27017/product_service_db?authSource=admin
ENV PRODUCT_SERVICE_EUREKA_URL=http://127.0.0.1:8761/eureka
ENV PRODUCT_SERVICE_APP_IP=127.0.0.1

ENV GO111MODULE=on

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod tidy

COPY . ./

RUN go build -o product-service ./cmd/server

FROM alpine:latest

RUN apk add --no-cache ca-certificates

WORKDIR /root/

COPY --from=builder /app/product-service .

EXPOSE 9006

# Run the app
CMD ["./product-service"]
