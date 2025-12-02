package mapstruct

import (
	"product_service/internal/entity"

	req "product_service/internal/transport/dto/request"

	"github.com/mitchellh/mapstructure"
)

func MapToProduct(input map[string]interface{}, product *req.ProductRequest) error {
	config := &mapstructure.DecoderConfig{
		WeaklyTypedInput: true,
		Result:           product,
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

func MapDtoToProductModel(dto req.ProductRequest, product *entity.Product) error {

	config := &mapstructure.DecoderConfig{
		WeaklyTypedInput: true,
		Result:           product,
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
