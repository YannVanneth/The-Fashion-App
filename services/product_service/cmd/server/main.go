package main

import (
	"product_service/internal/config"
	eurekaclient "product_service/internal/config/eureka_client"
	"product_service/internal/transport/http"

	"github.com/go-playground/validator/v10"
)

// @title Product Service API
// @version 1.0
// @description API for managing products and product categories in The Fashion App

// @host localhost:9006
// @BasePath /api/v1
// @schemes http https

func main() {

	eurekaclient.EurekaClient()

	validator.New()

	cfg := config.LoadConfig()

	app := http.NewRouter()

	if err := app.Run(cfg.PORT); err != nil {
		panic(err)
	}
}
