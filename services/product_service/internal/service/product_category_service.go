package service

import (
	"product_service/internal/entity"
	"product_service/internal/repository"
	"product_service/internal/transport/dto"
)

type ProductCategoryService struct {
	repo repository.ProductCategoryInterface
}

type ProductCategoryInterface interface {
	GetAllProductCategory(pagination *dto.MetaData, sort *string, filter *map[string]string) (*dto.MetaData, []entity.CategoryModel, error)
	GetProductCategoryByID(id string) (*entity.CategoryModel, error)
	CreateProductCategory(product *entity.CategoryModel) error
	UpdateProductCategory(id string, product *entity.CategoryModel) error
	DeleteProductCategory(id string) error
}

func NewProductCategoryService(repo repository.ProductCategoryInterface) ProductCategoryInterface {
	return &ProductCategoryService{
		repo: repo,
	}
}

func (r *ProductCategoryService) GetAllProductCategory(pagination *dto.MetaData, sort *string, filter *map[string]string) (*dto.MetaData, []entity.CategoryModel, error) {
	return r.repo.GetAllProductCategory(pagination, sort, filter)
}

func (r *ProductCategoryService) GetProductCategoryByID(id string) (*entity.CategoryModel, error) {
	return r.repo.GetCategoryByID(id)
}

func (r *ProductCategoryService) CreateProductCategory(product *entity.CategoryModel) error {
	return r.repo.CreateProductCategory(product)
}

func (r *ProductCategoryService) UpdateProductCategory(id string, product *entity.CategoryModel) error {
	return r.repo.UpdateProductCategory(id, product)
}

func (r *ProductCategoryService) DeleteProductCategory(id string) error {
	return r.repo.DeleteProductCategory(id)
}
