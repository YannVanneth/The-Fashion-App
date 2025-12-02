package dto

// Response models for Swagger documentation

// ErrorResponse represents an error response
type ErrorResponse struct {
	Status    int    `json:"status" example:"400"`
	Message   string `json:"message" example:"Error message"`
	IsSuccess bool   `json:"is_success" example:"false"`
}

// ValidationErrorResponse represents a validation error response
type ValidationErrorResponse struct {
	Status    int      `json:"status" example:"400"`
	Message   string   `json:"message" example:"Validation error"`
	Errors    []string `json:"errors"`
	IsSuccess bool     `json:"is_success" example:"false"`
}

// SuccessResponse represents a generic success response
type SuccessResponse struct {
	Status    int         `json:"status" example:"200"`
	Message   string      `json:"message" example:"Success"`
	Data      interface{} `json:"data"`
	IsSuccess bool        `json:"is_success" example:"true"`
	Meta      *MetaData   `json:"meta_data,omitempty"`
}
