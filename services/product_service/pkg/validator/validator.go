package validator

import (
	req "product_service/internal/transport/dto/request"

	"github.com/go-playground/validator/v10"
)

func ProductValidator(product req.ProductRequest) validator.ValidationErrors {

	validate := validator.New()

	err := validate.Struct(product)
	
	if err != nil {
		return err.(validator.ValidationErrors)
	}

	return nil
}

func CategoryValidator(category req.CategoryRequest) validator.ValidationErrors {

	validate := validator.New()

	err := validate.Struct(category)
	
	if err != nil {
		return err.(validator.ValidationErrors)
	}

	return nil
}

type ValidationError struct {
	Field   string `json:"field"`
	Message string `json:"message"`
}

type ValidationErrorResponse struct {
	Errors []ValidationError `json:"errors"`
}

func ExtractValidationErrors(err validator.ValidationErrors) ValidationErrorResponse {
	errors := make([]ValidationError, 0, len(err))
	
	for _, e := range err {
		errors = append(errors, ValidationError{
			Field:   e.Field(),
			Message: formatValidationMessage(e),
		})
	}

	return ValidationErrorResponse{Errors: errors}
}

func formatValidationMessage(e validator.FieldError) string {
	switch e.Tag() {
	case "required":
		return e.Field() + " is required"
	case "email":
		return e.Field() + " must be a valid email"
	case "min":
		return e.Field() + " must be at least " + e.Param()
	case "max":
		return e.Field() + " must be at most " + e.Param()
	default:
		return e.Field() + " failed " + e.Tag() + " validation"
	}
}