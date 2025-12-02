package response

import (
	"time"
)

type ProductResponse struct {
    ID          string    `bson:"_id,omitempty" json:"id"`
    ProductID   string    `bson:"product_id" json:"product_id"`
    VendorID    string    `bson:"vendor_id" json:"vendor_id"`
    CategoryID  string    `bson:"category_id" json:"category_id"`
    Name        string    `bson:"name" json:"product_name"` 
    Description string    `bson:"description" json:"description"` 
    Price       float64   `bson:"price" json:"price"`
    Stock       int32     `bson:"stock" json:"stock_quantity"`
    Status      string    `bson:"status" json:"status"`
    CreatedAt   time.Time `bson:"created_at" json:"created_at"`
    UpdatedAt   time.Time `bson:"updated_at" json:"updated_at"`
}