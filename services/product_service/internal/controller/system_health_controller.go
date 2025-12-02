package controller

import (
	"time"

	"github.com/gin-gonic/gin"
)

// GetSystemHealth godoc
// @Summary Get system health status
// @Description Returns the current health status of the system
// @Tags System
// @Accept json
// @Produce json
// @Success 200 {object} dto.SuccessResponse
// @Router /system/health [get]
func GetSystemHealth(c *gin.Context) {

	currentDate := time.Now().Format(time.RFC3339)

	c.JSON(200, map[string]string{
		"code":  "200",
		"status": "healthy",
		"message": "System is healthy",
		"timestamp": currentDate,
	})
}

