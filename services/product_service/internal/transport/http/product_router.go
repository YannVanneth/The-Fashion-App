package http

import (
	c "product_service/internal/controller"

	"github.com/gin-gonic/gin"
)

func ProductRouter(engine *gin.Engine, relativePath string){

	product := engine.Group(relativePath)

	product.GET("/", c.GetProducts)
	product.GET("/:id", c.GetProductByID)
	product.POST("/", c.CreateProduct)
	product.PUT("/:id", c.UpdateProduct)
	product.DELETE("/:id", c.DeleteProduct)
}
