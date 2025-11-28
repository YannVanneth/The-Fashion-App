using System.ComponentModel.DataAnnotations;

namespace VendorService.Entity;

public class VendorAddressModel
{
    [Key]
    public Guid Id { get; set; }
    
    [Required]
    public Guid VendorId { get; set; }
    
    [Required, MaxLength(100)]
    public string AddressLine1 { get; set; } = "";
    
    [MaxLength(100)]
    public string AddressLine2 { get; set; } = "";
    
    [Required, MinLength(3), MaxLength(50)]
    public string City { get; set; } = "";
    
    [Required, MinLength(3), MaxLength(50)]
    public string Country { get; set; } = "";
    
    [Required, MinLength(3), MaxLength(30)]
    public string PostalCode { get; set; } = ""; 
}