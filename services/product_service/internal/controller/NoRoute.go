package controller

import (
	"product_service/internal/transport/dto"

	"github.com/gin-gonic/gin"
)

// NoRoute godoc
// @Summary Handle undefined routes
// @Description Returns 404 error for routes that don't exist
// @Tags System
// @Accept json
// @Produce json
// @Success 404 {object} dto.ErrorResponse
// @Router /undefined [get]
func NoRoute(c *gin.Context) {
	c.JSON(404, dto.NotFound("Route not found"))
}