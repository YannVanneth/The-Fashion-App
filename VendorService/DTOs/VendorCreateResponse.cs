using VendorService.Entity.Enumerations;

namespace VendorService.DTOs;

public record VendorCreateResponse(
    Guid Id,
    Guid UserId,
    string VendorName,
    String OwnerName,
    String Email,
    String PhoneNumber,
    DateTime? UpdatedAt,
    DateTime? CreatedAt,
    VendorStatus Status = VendorStatus.Active
    );  