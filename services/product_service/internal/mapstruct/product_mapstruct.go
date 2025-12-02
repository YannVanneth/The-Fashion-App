package mapstruct

import (
	"product_service/internal/entity"

	"github.com/mitchellh/mapstructure"
)

func MapToProduct(input map[string]interface{}, product *entity.Product) error {
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

