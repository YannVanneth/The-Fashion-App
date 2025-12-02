package repository

import (
	"context"
	"errors"
	e "product_service/internal/entity"
	"product_service/internal/transport/dto"
	"product_service/pkg/utils"
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

type ProductCategoryRepository struct {
	collection *mongo.Collection
}

type ProductCategoryInterface interface {
	GetAllProductCategory(pagination *dto.MetaData, sort *string, filter *map[string]string) (*dto.MetaData, []e.CategoryModel, error)
	GetCategoryByID(id string) (*e.CategoryModel, error)
	CreateProductCategory(category *e.CategoryModel) error
	UpdateProductCategory(id string, category *e.CategoryModel) error
	DeleteProductCategory(id string) error
}

func NewProductCategoryRepository(db *mongo.Client) ProductCategoryInterface {
	return &ProductCategoryRepository{
		collection: db.Database("product_service_db").Collection("product_categories"),
	}
}

func (r *ProductCategoryRepository) GetAllProductCategory(pagination *dto.MetaData, sort *string, filter *map[string]string) (*dto.MetaData, []e.CategoryModel, error) {
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

	var products []e.CategoryModel
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

func (r *ProductCategoryRepository) GetCategoryByID(id string) (*e.CategoryModel, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	var category e.CategoryModel
	filter := bson.M{"category_id": id}

	err := r.collection.FindOne(ctx, filter).Decode(&category)
	if err != nil {
		if errors.Is(mongo.ErrNoDocuments, err) {
			return nil, nil
		}
		return nil, err
	}

	return &category, nil
}

func (r *ProductCategoryRepository) CreateProductCategory(category *e.CategoryModel) error {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if category.ID == "" {
		category.ID = primitive.NewObjectID().Hex()
	}

	if category.ProductID == "" {
		category.ProductID = utils.GenerateProductID()
	}

	category.CreatedAt = time.Now()

	_, err := r.collection.InsertOne(ctx, category)
	return err
}

func (r *ProductCategoryRepository) UpdateProductCategory(id string, category *e.CategoryModel) error {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	category.UpdatedAt = time.Now()

	filter := bson.M{"product_id": id}
	update := bson.M{
		"$set": bson.M{
			"product_id": category.ProductID,
			"name":       category.Name,
			"updated_at": category.UpdatedAt,
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

func (r *ProductCategoryRepository) DeleteProductCategory(id string) error {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	filter := bson.M{"category_id": id}
	result, err := r.collection.DeleteOne(ctx, filter)
	if err != nil {
		return err
	}

	if result.DeletedCount == 0 {
		return mongo.ErrNoDocuments
	}

	return nil
}
