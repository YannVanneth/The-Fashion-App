using VendorService.Entity.Enumerations;

namespace VendorService.dtos;

public record VendorCreateRequest(
      Guid UserId,
      string VendorName,
      String OwnerName,
      String Email,
      String PhoneNumber,
      VendorStatus Status = VendorStatus.Active);