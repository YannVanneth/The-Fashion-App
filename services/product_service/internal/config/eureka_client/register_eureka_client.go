package eurekaclient

import (
	"log"
	"product_service/internal/config"
	"strconv"

	"github.com/ArthurHlt/go-eureka-client/eureka"
)

func EurekaClient() *eureka.Client {

	cfg := config.LoadConfig()

	port, err := strconv.Atoi(cfg.PORT)

	client := eureka.NewClient([]string{cfg.EUREKA_URL})
	instance := eureka.NewInstanceInfo(
		"product-service",
		"product-service",
		cfg.APP_IP,
		port,
		30,
		false)

	err = client.RegisterInstance("product-service", instance)

	if err != nil {
		log.Println(err)
	}

	return client
}
