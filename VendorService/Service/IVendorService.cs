using VendorService.Entity;

namespace VendorService.Service;

public interface IVendorService
{
    Task<IEnumerable<VendorModel>> GetAllVendors();
    Task<VendorModel?> GetVendorById(Guid id);
    Task<VendorModel> CreateVendor(VendorModel vendor);
    Task<VendorModel> UpdateVendor(Guid id, VendorModel vendor);
    Task DeleteVendor(Guid id);
}