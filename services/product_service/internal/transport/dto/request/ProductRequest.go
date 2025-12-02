package request

type ProductRequest struct {
    VendorID    string    `bson:"vendor_id" json:"vendor_id" validate:"required"`
    CategoryID  string    `bson:"category_id" json:"category_id" validate:"required"`
    Name        string    `bson:"name" json:"product_name" validate:"required,min=3,max=100"` 
    Description string    `bson:"description" json:"description" validate:"required,min=10,max=1000"`
    Price       float64   `bson:"price" json:"price" validate:"required,gte=0"`
    Stock       int32     `bson:"stock" json:"stock_quantity" validate:"required,gte=0"`
    Status      string    `bson:"status" json:"status" validate:"required,oneof=available out_of_stock discontinued"`
}