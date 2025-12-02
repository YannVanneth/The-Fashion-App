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


var productCategoryService service.ProductCategoryInterface = service.NewProductCategoryService(
	repository.NewProductCategoryRepository(database.GetClient()))

// GetAllProductCategory godoc
// @Summary Get all product categories
// @Description Get a paginated list of all product categories with optional filtering and sorting
// @Tags Categories
// @Accept json
// @Produce json
// @Param filter query string false "Filter parameters (e.g., filter[name]=example)"
// @Param sort query string false "Sort parameter (e.g., 'name', '-created_at')"
// @Param limit query int false "Number of items per page" default(10)
// @Param page query int false "Page number" default(1)
// @Success 200 {object} dto.SuccessResponse
// @Failure 500 {object} dto.ErrorResponse
// @Router /product-categories/ [get]
func GetAllProductCategory(c *gin.Context) {
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

	meta, products , err := productCategoryService.GetAllProductCategory(&dto.MetaData{
		Page: page,
		Limit: limit,
	}, &sortQuery, &filterQuery)

	if err != nil {
		c.JSON(500, dto.Error("Failed to fetch products"))
		return
	}

	if len(products) < 1 {
		products = []entity.CategoryModel{}
	}

	c.JSON(200, dto.Ok("Product fetch successfully", &products, meta))
}

// GetProductCategoryById godoc
// @Summary Get product category by ID
// @Description Get a single product category by its ID
// @Tags Categories
// @Accept json
// @Produce json
// @Param id path string true "Category ID"
// @Success 200 {object} dto.SuccessResponse
// @Failure 400 {object} dto.ErrorResponse
// @Failure 404 {object} dto.ErrorResponse
// @Failure 500 {object} dto.ErrorResponse
// @Router /product-categories/{id} [get]
func GetProductCategoryById(c *gin.Context) {
	id := c.Param("id")

	if id == "" {
		c.JSON(400, dto.BadRequest("invalid ID"))
		return
	}

	category, err := productCategoryService.GetProductCategoryByID(id)
	if err != nil {
		c.JSON(500, dto.Error("Failed to fetch category"))
		return
	}

	if category == nil {
		c.JSON(404, dto.NotFound("Category not found"))
		return
	}

	c.JSON(200, dto.Ok("Category fetched successfully", category, nil))
}

// CreateProductCategory godoc
// @Summary Create a new product category
// @Description Create a new product category with multipart form data
// @Tags Categories
// @Accept multipart/form-data
// @Produce json
// @Param category_name formData string true "Category name (3-100 characters)"
// @Param product_id formData string true "Product ID"
// @Success 201 {object} dto.SuccessResponse
// @Failure 400 {object} dto.ValidationErrorResponse
// @Failure 500 {object} dto.ErrorResponse
// @Router /product-categories/ [post]
func CreateProductCategory(c *gin.Context) {
   dtoCategory := req.CategoryRequest{}

	form, err := c.MultipartForm()
	if err != nil {
		c.JSON(400, dto.BadRequest("Failed to bind multipart form"))
		return
	}

	newProduct := make(map[string]interface{})
	for key, values := range form.Value {
		newProduct[key] = values[0]
	}

	err = mapstruct.MapToCategory(newProduct, &dtoCategory)
	if err != nil {
		c.JSON(400, dto.BadRequest("Invalid category data"))
		return
	}

	validationErr := validator.CategoryValidator(dtoCategory)

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

	category := entity.CategoryModel{}

	err = mapstruct.MapDtoToCategoryModel(dtoCategory, &category)

	if err != nil {
		c.JSON(500, dto.Error("Failed to create category"))
		return
	}

	err = productCategoryService.CreateProductCategory(&category)

	if err != nil {
		c.JSON(500, dto.Error("Failed to create category"))
		return
	}

	c.JSON(201, dto.Created("Category created successfully", &category))
}

// UpdateProductCategory godoc
// @Summary Update an existing product category
// @Description Update a product category by ID with multipart form data
// @Tags Categories
// @Accept multipart/form-data
// @Produce json
// @Param id path string true "Category ID"
// @Param category_name formData string true "Category name (3-100 characters)"
// @Param product_id formData string true "Product ID"
// @Success 200 {object} dto.SuccessResponse
// @Failure 400 {object} dto.ValidationErrorResponse
// @Failure 500 {object} dto.ErrorResponse
// @Router /product-categories/{id} [put]
func UpdateProductCategory(c *gin.Context) {
	dtoCategory := req.CategoryRequest{}

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

	err = mapstruct.MapToCategory(newProduct, &dtoCategory)
	if err != nil {
		c.JSON(400, gin.H{"error": "Invalid category data"})
		return
	}

	validationErr := validator.CategoryValidator(dtoCategory)

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

	category := entity.CategoryModel{}

	err = mapstruct.MapDtoToCategoryModel(dtoCategory, &category)

	if err != nil {
		c.JSON(400, dto.BadRequest("Invalid category data"))
		return
	}

	err = productCategoryService.UpdateProductCategory(id,&category)
	
	if err != nil {
		c.JSON(400, dto.NotFound("category not found : "))
		return
	}

	updatedCategory , err := productCategoryService.GetProductCategoryByID(id)
	if err != nil {
		c.JSON(500, dto.Error("Failed to fetch updated category"))
		return
	}
	
	category = *updatedCategory
	
	c.JSON(200, dto.Ok("Category update successfully", &category, nil))
}

// DeleteProductCategory godoc
// @Summary Delete a product category
// @Description Delete a product category by its ID
// @Tags Categories
// @Accept json
// @Produce json
// @Param id path string true "Category ID"
// @Success 200 {object} dto.SuccessResponse
// @Failure 400 {object} dto.ErrorResponse
// @Failure 404 {object} dto.ErrorResponse
// @Router /product-categories/{id} [delete]
func DeleteProductCategory(c *gin.Context) {
	id := c.Param("id")

	if id == "" {
		c.JSON(400, dto.BadRequest("Invalid ID"))
		return
	}

	err := productCategoryService.DeleteProductCategory(id)
	
	if err != nil {
		c.JSON(404, dto.NotFound("Category not found"))
		return
	}

	c.JSON(200, dto.Ok[string]("Category deleted successfully", nil, nil))
}
