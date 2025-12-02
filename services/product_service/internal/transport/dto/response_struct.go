package dto

import "reflect"

type MetaData struct {
	Page  int `json:"page"`
	Limit int `json:"limit"`
	Total int `json:"total"`
}

type ApiResponse[T any] struct {
	Status int       `json:"status"`
	Message string    `json:"message"`
	Data   T 		 `json:"data"`
	IsSuccess bool   `json:"is_success"`
	Meta   *MetaData `json:"meta_data,omitempty"`
}

func Created[T any](message string, data *T) ApiResponse[T] {
	if message == "" {
		message = "Resource Created Successfully"
	}	
	if data == nil {
		var empty T
		data = &empty
	}

	return ApiResponse[T]{
		Status: 201,
		Message: message,
		Data: *data,
		IsSuccess: true,
	}
}

func Error(message string) map[string]interface{} {
	return map[string]any{
		"status": 500,
		"message": message,
		"is_success": false,
	}
}

func Ok[T any](message string, data *T, metaData *MetaData) ApiResponse[T] {

    if message == "" {
		message = "Success"
	}

    if data == nil {

        var empty T
        
		switch any(empty).(type) {
        
		case []any: 
            s := reflect.MakeSlice(reflect.TypeOf(empty), 0, 0)
            v := s.Interface().(T)
            data = &v
        default:
            data = &empty
        }
    }

	return ApiResponse[T]{
		Status: 200,
		Message: message,
		Data: *data,
		IsSuccess: true,
		Meta: metaData,
	}
}

func BadRequest[T any](message T) map[string]interface{} {
		
	return map[string]any{
		"status": 400,
		"message": message,
		"is_success": false,
	}
}

func NotFound(message string) map[string]interface{} {

	if message == "" {
		message = "Resource Not Found"
	}

	return map[string]any{
		"status": 404,
		"message": message,
		"is_success": false,
	}
}


