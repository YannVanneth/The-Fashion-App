package database

import (
	"context"
	"log"
	"os"
	"product_service/internal/config"
	"time"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var dbClient *mongo.Client
var db *mongo.Database

func GetInitDB() *mongo.Database{
	if db == nil {
		Connect()
	}
	return db
}

func GetClient() *mongo.Client {
	
	if dbClient == nil {

		Connect()
	}
	
	return dbClient
}

func Connect() {
	
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
    defer cancel()

	
	cfg := config.LoadConfig()


	client, err := mongo.Connect(ctx, options.Client().ApplyURI(cfg.DB_URL))
    
	if err != nil {
		log.Fatal("Error creating database client: ", err)
	}
	
	if err := client.Ping(ctx, nil); err != nil {
		log.Fatal("Error connecting to database: ", err)
	}

	log.Println("Successfully connected to MongoDB")

	dbClient = client
	db = client.Database(os.Getenv("PRODUCT_SERVICE_DB_NAME"))
}