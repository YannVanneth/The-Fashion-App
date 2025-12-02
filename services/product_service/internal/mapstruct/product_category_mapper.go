package mapstruct

import (
	"product_service/internal/entity"

	req "product_service/internal/transport/dto/request"

	"github.com/mitchellh/mapstructure"
)

func MapToCategory(input map[string]interface{}, category *req.CategoryRequest) error {
	config := &mapstructure.DecoderConfig{
		WeaklyTypedInput: true,
		Result:           category,
		TagName:          "json",
	}

	decoder, err := mapstructure.NewDecoder(config)
	if err != nil {
		return err
	}

	if err = decoder.Decode(input); err != nil {
		return err
	}

	return nil
}

func MapDtoToCategoryModel(dto req.CategoryRequest, category *entity.CategoryModel) ( error) {

	config := &mapstructure.DecoderConfig{
		WeaklyTypedInput: true,
		Result:           category,
		TagName:          "json",
	}

	decoder, err := mapstructure.NewDecoder(config)
	if err != nil {
		return err
	}

	if err = decoder.Decode(dto); err != nil {
		return err
	}

	return nil
	
}
