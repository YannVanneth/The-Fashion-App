package repository

import (
	"context"
	"errors"
	e "product_service/internal/entity"
	"product_service/pkg/utils"
	"time"

	"product_service/internal/transport/dto"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

type ProductRepository struct {
	collection *mongo.Collection
}

type ProductRepositoryInterface interface {
	GetAllProducts(pagination *dto.MetaData, sort *string, filter *map[string]string) (*dto.MetaData, []e.Product, error)
	GetProductByID(id string) (*e.Product, error)
	CreateProduct(product *e.Product) error
	UpdateProduct(id string, product *e.Product) error
	DeleteProduct(id string) error
}

func NewProductRepository(db *mongo.Client) ProductRepositoryInterface {
	return &ProductRepository{
		collection: db.Database("product_service_db").Collection("products"),
	}
}

func (r *ProductRepository) GetAllProducts(pagination *dto.MetaData, sort *string, filter *map[string]string) (*dto.MetaData, []e.Product, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	bsonFilter := bson.M{}

	if pagination == nil {
		pagination = &dto.MetaData{
			Page:  1,
			Limit: 10,
		}
	}

	if pagination.Limit < 1 {
		pagination.Limit = 10
	}

	if pagination.Page < 1 {
		pagination.Page = 1
	}

	skip := (pagination.Page - 1) * pagination.Limit

	sortOptions := bson.D{}

	if sort != nil && *sort != "" {

		sortField := "created_at"

		if *sort == "asc" {
			sortOptions = bson.D{{Key: sortField, Value: 1}}
		} else {
			sortOptions = bson.D{{Key: sortField, Value: -1}}
		}
	}

	findOptions := options.FindOptions{
		Sort:  sortOptions,
		Skip:  func(i int64) *int64 { return &i }(int64(skip)),
		Limit: func(i int64) *int64 { return &i }(int64(pagination.Limit)),
	}

	for key, value := range *filter {
		bsonFilter[key] = value
	}

	cursor, err := r.collection.Find(ctx, bsonFilter, &findOptions)
	if err != nil {
		return nil, nil, err
	}
	defer cursor.Close(ctx)

	var products []e.Product
	if err = cursor.All(ctx, &products); err != nil {
		return nil, nil, err
	}

	total, err := r.collection.CountDocuments(ctx, bson.M{})

	if err != nil {
		return nil, nil, err
	}

	return &dto.MetaData{
		Limit: pagination.Limit,
		Page:  pagination.Page,
		Total: int(total),
	}, products, nil
}

func (r *ProductRepository) GetProductByID(id string) (*e.Product, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	var product e.Product
	filter := bson.M{"product_id": id}

	err := r.collection.FindOne(ctx, filter).Decode(&product)
	if err != nil {
		if errors.Is(mongo.ErrNoDocuments, err) {
			return nil, nil
		}
		return nil, err
	}

	return &product, nil
}

func (r *ProductRepository) CreateProduct(product *e.Product) error {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if product.ID == "" {
		product.ID = primitive.NewObjectID().Hex()
	}

	if product.ProductID == "" {
		product.ProductID = utils.GenerateProductID()
	}

	product.CreatedAt = time.Now()

	if product.Status == "" {
		product.Status = "active"
	}

	_, err := r.collection.InsertOne(ctx, product)
	return err
}

func (r *ProductRepository) UpdateProduct(id string, product *e.Product) error {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	product.UpdatedAt = time.Now()

	filter := bson.M{"product_id": id}
	update := bson.M{
		"$set": bson.M{
			"vendor_id":   product.VendorID,
			"category_id": product.CategoryID,
			"name":        product.Name,
			"description": product.Description,
			"price":       product.Price,
			"stock":       product.Stock,
			"status":      product.Status,
			"updated_at":  product.UpdatedAt,
		},
	}

	result, err := r.collection.UpdateOne(ctx, filter, update)
	if err != nil {
		return err
	}

	if result.MatchedCount == 0 {
		return mongo.ErrNoDocuments
	}

	return nil
}

func (r *ProductRepository) DeleteProduct(id string) error {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	filter := bson.M{"product_id": id}
	result, err := r.collection.DeleteOne(ctx, filter)
	if err != nil {
		return err
	}

	if result.DeletedCount == 0 {
		return mongo.ErrNoDocuments
	}

	return nil
}
