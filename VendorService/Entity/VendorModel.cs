using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
using VendorService.Entity.Enumerations;

namespace VendorService.Entity;

public class VendorModel : BaseEntity
{
    [Key]
    public Guid Id { get; set; }
    
    [Required, JsonPropertyName("user_id")]
    public Guid UserId { get; set; }
    
    [Required, MinLength(3), MaxLength(50), JsonPropertyName("vendor_name")]
    public string Name { get; set; } = "";
    
    [Required, MinLength(3), MaxLength(50), JsonPropertyName("owner_name")]
    public string OwnerName { get; set; } = "";
    
    [Required, EmailAddress, MaxLength(89)]
    public string Email { get; set; } = "";
    
    [Required, MinLength(6), MaxLength(15),  JsonPropertyName("phone_number")]
    public string Phone { get; set; } = "";
   
    [EnumDataType(typeof(VendorStatus))] public VendorStatus Status { get; set; } = VendorStatus.InActive;
}