package entity

import "time"

type CategoryModel struct {
	ID        string    `bson:"_id,omitempty" json:"category_id"`
	Name      string    `bson:"name" json:"category_name"`
	ProductID string    `bson:"product_id" json:"product_id"`
	CreatedAt time.Time `bson:"created_at" json:"created_at"`
	UpdatedAt time.Time `bson:"updated_at" json:"updated_at"`
}
