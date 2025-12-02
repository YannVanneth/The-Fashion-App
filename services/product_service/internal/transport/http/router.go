package http

import (
	c "product_service/internal/controller"

	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"

	_ "product_service/docs" // Import generated docs
)

func NewRouter() *gin.Engine {
	
	r := gin.Default()

	// Swagger documentation route
	r.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	ProductRouter(r, "/api/v1/products")

	ProductCategoryRouter(r, "/api/v1/categories")

	SystemHealthRouter(r, "/api/v1/system/health")

	r.NoRoute(c.NoRoute)

	return r
}
