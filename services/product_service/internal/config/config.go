package config

import (
	"log"
	"os"
	s "product_service/pkg/utils"

	"github.com/joho/godotenv"
)

type Config struct {
	PORT       string
	DB_URL     string
	EUREKA_URL string
	APP_IP     string
}

func LoadConfig() *Config {

	err := godotenv.Load()
	if err != nil {
		return nil
	}

	port := os.Getenv("PRODUCT_SERVICE_PORT")
	dbURL := os.Getenv("PRODUCT_SERVICE_DB_URL")
	eurekaUrl := os.Getenv("PRODUCT_SERVICE_EUREKA_URL")
	appIp := os.Getenv("PRODUCT_SERVICE_APP_IP")

	if port == "" {
		port = ":9006"
	} else if !s.HasPrefixConlon(port) {
		port = ":" + port
	}

	if s.IsEmpty(dbURL) {
		log.Fatal("error : DB_URL are not provided")
	}

	return &Config{PORT: port, DB_URL: dbURL, EUREKA_URL: eurekaUrl, APP_IP: appIp}
}
