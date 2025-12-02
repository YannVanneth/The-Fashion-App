package http

import (
	c "product_service/internal/controller"

	"github.com/gin-gonic/gin"
)

func SystemHealthRouter(engine *gin.Engine, relativePath string){
	
	systemHealth := engine.Group(relativePath)

	systemHealth.GET("/", c.GetSystemHealth)
}
