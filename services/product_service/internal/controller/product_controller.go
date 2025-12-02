package controller

import (
	"product_service/internal/config/database"
	"product_service/internal/entity"
	"product_service/internal/mapstruct"
	"product_service/internal/repository"
	"product_service/internal/service"
	"product_service/internal/transport/dto"
	req "product_service/internal/transport/dto/request"
	"product_service/pkg/validator"

	"strconv"

	"github.com/gin-gonic/gin"
)

var productService service.ProductServiceInterface = service.NewProductService(
	repository.NewProductRepository(database.GetClient()))

// GetProducts godoc
// @Summary Get all products
// @Description Get a paginated list of all products with optional filtering and sorting
// @Tags Products
// @Accept json
// @Produce json
// @Param filter query string false "Filter parameters (e.g., filter[status]=available)"
// @Param sort query string false "Sort parameter (e.g., 'price', '-created_at')"
// @Param limit query int false "Number of items per page" default(10)
// @Param page query int false "Page number" default(1)
// @Success 200 {object} dto.SuccessResponse
// @Failure 500 {object} dto.ErrorResponse
// @Router /products/ [get]
func GetProducts(c *gin.Context) {

	filterQuery := c.QueryMap("filter")

	sortQuery := c.Query("sort")
	
	limit, err := strconv.Atoi(c.DefaultQuery("limit", "10"))

	if err != nil {
		c.JSON(500, dto.Error("Something went worng"))
		return
	}

	page, err := strconv.Atoi(c.DefaultQuery("page", "1"))

		if err != nil {
		c.JSON(500, dto.Error("Internal Server Error"))
		return
	}

	if page < 1{ page = 1 }

	if limit < 1 { limit = 10 }

	meta, products , err := productService.GetAllProducts(&dto.MetaData{
		Page: page,
		Limit: limit,
	}, &sortQuery, &filterQuery)

	if err != nil {
		c.JSON(500, dto.Error("Failed to fetch products"))
		return
	}

	if len(products) < 1 {
		products = []entity.Product{}
	}

	c.JSON(200, dto.Ok("Product fetch successfully", &products, meta))
}

// GetProductByID godoc
// @Summary Get product by ID
// @Description Get a single product by its ID
// @Tags Products
// @Accept json
// @Produce json
// @Param id path string true "Product ID"
// @Success 200 {object} dto.SuccessResponse
// @Failure 400 {object} dto.ErrorResponse
// @Failure 404 {object} dto.ErrorResponse
// @Failure 500 {object} dto.ErrorResponse
// @Router /products/{id} [get]
func GetProductByID(c *gin.Context) {
	id := c.Param("id")

	if id == "" {
		c.JSON(400, dto.BadRequest("invalid ID"))
		return
	}

	product, err := productService.GetProductByID(id)
	if err != nil {
		c.JSON(500, dto.Error("Failed to fetch product"))
		return
	}

	if product == nil {
		c.JSON(404, dto.NotFound("Product not found"))
		return
	}

	c.JSON(200, dto.Ok("Product fetched successfully", product, nil))
}

// CreateProduct godoc
// @Summary Create a new product
// @Description Create a new product with multipart form data
// @Tags Products
// @Accept multipart/form-data
// @Produce json
// @Param vendor_id formData string true "Vendor ID"
// @Param category_id formData string true "Category ID"
// @Param product_name formData string true "Product name (3-100 characters)"
// @Param description formData string true "Product description (10-1000 characters)"
// @Param price formData number true "Product price (must be >= 0)"
// @Param stock_quantity formData integer true "Stock quantity (must be >= 0)"
// @Param status formData string true "Product status" Enums(available, out_of_stock, discontinued)
// @Success 201 {object} dto.SuccessResponse
// @Failure 400 {object} dto.ValidationErrorResponse
// @Failure 500 {object} dto.ErrorResponse
// @Router /products/ [post]
func CreateProduct(c *gin.Context) {
	dtoProduct := req.ProductRequest{}

	form, err := c.MultipartForm()

	if err != nil {
		c.JSON(400, dto.BadRequest("Failed to bind multipart form"))
		return
	}

	newProduct := make(map[string]interface{})
	for key, values := range form.Value {
		newProduct[key] = values[0]
	}

	err = mapstruct.MapToProduct(newProduct, &dtoProduct)
	if err != nil {
		c.JSON(400, dto.BadRequest("Invalid product data"))
		return
	}

	validationErr := validator.ProductValidator(dtoProduct)

	if validationErr != nil {

		errMsg := validator.ExtractValidationErrors(validationErr)

		c.JSON(400, map[string]any{
			"status": 400,
			"message": "Validation error",
			"errors": errMsg.Errors,
			"is_success": false,
		})

		return
	}

	product := entity.Product{}

	err = mapstruct.MapDtoToProductModel(dtoProduct, &product)
	if err != nil {
		c.JSON(400, dto.BadRequest("Invalid product data"))
		return
	}

	err = productService.CreateProduct(&product)
	if err != nil {
		c.JSON(500, dto.Error("Failed to create product"))
		return
	}

	c.JSON(201, dto.Created("Product created successfully", &product))
}

// UpdateProduct godoc
// @Summary Update an existing product
// @Description Update a product by ID with multipart form data
// @Tags Products
// @Accept multipart/form-data
// @Produce json
// @Param id path string true "Product ID"
// @Param vendor_id formData string true "Vendor ID"
// @Param category_id formData string true "Category ID"
// @Param product_name formData string true "Product name (3-100 characters)"
// @Param description formData string true "Product description (10-1000 characters)"
// @Param price formData number true "Product price (must be >= 0)"
// @Param stock_quantity formData integer true "Stock quantity (must be >= 0)"
// @Param status formData string true "Product status" Enums(available, out_of_stock, discontinued)
// @Success 200 {object} dto.SuccessResponse
// @Failure 400 {object} dto.ValidationErrorResponse
// @Failure 500 {object} dto.ErrorResponse
// @Router /products/{id} [put]
func UpdateProduct(c *gin.Context) {

	dtoProduct := req.ProductRequest{}

	id := c.Param("id")

	form, err := c.MultipartForm()
	if err != nil {
		c.JSON(400, dto.BadRequest("Failed to bind multipart form"))
		return
	}

	newProduct := make(map[string]interface{})
	for key, values := range form.Value {
		newProduct[key] = values[0]
	}

	err = mapstruct.MapToProduct(newProduct, &dtoProduct)

	if err != nil {
		c.JSON(400, dto.BadRequest("Invalid product data"))
		return
	}

	validationErr := validator.ProductValidator(dtoProduct)

	if validationErr != nil {

		errMsg := validator.ExtractValidationErrors(validationErr)

		c.JSON(400, map[string]any{
			"status": 400,
			"message": "Validation error",
			"errors": errMsg.Errors,
			"is_success": false,
		})

		return
	}

	product := entity.Product{}

	err = mapstruct.MapDtoToProductModel(dtoProduct, &product)
	
	if err != nil {
		c.JSON(400, dto.BadRequest("Invalid product data"))
		return
	}

	err = productService.UpdateProduct(id,&product)
	if err != nil {
		c.JSON(400, dto.NotFound("product not found : "))
		return
	}

	updatedProduct , err := productService.GetProductByID(id)

	if err != nil {
		c.JSON(500, dto.Error("Failed to fetch updated product"))
		return
	}
	
	product = *updatedProduct

	
	c.JSON(200, dto.Ok("Product update successfully", &product, nil))
}

// DeleteProduct godoc
// @Summary Delete a product
// @Description Delete a product by its ID
// @Tags Products
// @Accept json
// @Produce json
// @Param id path string true "Product ID"
// @Success 200 {object} dto.SuccessResponse
// @Failure 400 {object} dto.ErrorResponse
// @Failure 404 {object} dto.ErrorResponse
// @Router /products/{id} [delete]
func DeleteProduct(c *gin.Context) {
	id := c.Param("id")

	if id == "" {
		c.JSON(400, dto.BadRequest("Invalid ID"))
		return
	}

	err := productService.DeleteProduct(id)
	
	if err != nil {
		c.JSON(404, dto.NotFound("Product not found"))
		return
	}

	c.JSON(200, dto.Ok[string]("Product deleted successfully", nil, nil))
}