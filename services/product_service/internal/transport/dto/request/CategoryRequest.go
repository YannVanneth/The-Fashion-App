package request

type CategoryRequest struct {
	Name      string    `bson:"name" json:"category_name" validate:"required,min=3,max=100"`
	ProductID string    `bson:"product_id" json:"product_id" validate:"required"`
}