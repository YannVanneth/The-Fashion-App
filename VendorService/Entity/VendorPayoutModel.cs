using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
using VendorService.Entity.Enumerations;

namespace VendorService.Entity;

public class VendorPayoutModel : BaseEntity
{
    [Key]
    public Guid PayoutId { get; set; }
    
    [Required, JsonPropertyName("vendor_id")]
    public Guid VendorId { get; set; }
    
    [Required, JsonPropertyName("amount")]
    public Decimal Amount { get; set; }
    
    [JsonPropertyName("payout_status")]
    public PayoutStatus Status { get; set; } = PayoutStatus.Pending;
}