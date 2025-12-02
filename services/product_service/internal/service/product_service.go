package service

import (
	"product_service/internal/entity"
	"product_service/internal/repository"
	"product_service/internal/transport/dto"
)

type ProductServiceInterface interface {
	GetAllProducts(pagination *dto.MetaData,sort *string, filter *map[string]string) (*dto.MetaData, []entity.Product, error)
	GetProductByID(id string) (*entity.Product, error)
	CreateProduct(product *entity.Product) error
	UpdateProduct(id string, product *entity.Product) error
	DeleteProduct(id string) error
}

type ProductService struct {
	repo repository.ProductRepositoryInterface
}

func NewProductService(repo repository.ProductRepositoryInterface) ProductServiceInterface {
	return &ProductService{repo: repo}
}

func (s *ProductService) GetAllProducts(pagination *dto.MetaData,sort *string, filter *map[string]string) (*dto.MetaData,[]entity.Product, error) {
	return s.repo.GetAllProducts(pagination, sort,filter)
}

func (s *ProductService) GetProductByID(id string) (*entity.Product, error) {
	return s.repo.GetProductByID(id)
}

func (s *ProductService) CreateProduct(product *entity.Product) error {
	return s.repo.CreateProduct(product)
}

func (s *ProductService) UpdateProduct(id string,product *entity.Product) error {
	return s.repo.UpdateProduct(id,product)
}

func (s *ProductService) DeleteProduct(id string) error {
	return s.repo.DeleteProduct(id)
}