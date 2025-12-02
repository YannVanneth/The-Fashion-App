package http

import (
	c "product_service/internal/controller"

	"github.com/gin-gonic/gin"
)

func ProductCategoryRouter(engine *gin.Engine, relativePath string){

	productCategory := engine.Group(relativePath)
	
	productCategory.GET("/", c.GetAllProductCategory)
	productCategory.GET("/:id", c.GetProductCategoryById)
	productCategory.POST("/", c.CreateProductCategory)
	productCategory.PUT("/:id", c.UpdateProductCategory)
	productCategory.DELETE("/:id", c.DeleteProductCategory)
}