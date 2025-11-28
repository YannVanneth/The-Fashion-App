using System.Text.Json.Serialization;

namespace VendorService.Entity;

public class BaseEntity
{
    [JsonPropertyName("created_at")]
    public DateTime? CreatedAt { get; set; }
    
    [JsonPropertyName("updated_at")]
    public DateTime? UpdatedAt { get; set; }
}