using VendorService.Entity;

namespace VendorService.Repository;

public interface IVendorRepository
{
    Task<List<VendorModel>> GetAllVendors();
    Task<VendorModel?> GetVendorById(Guid id);
    Task<VendorModel> CreateVendor(VendorModel vendor);
    Task<VendorModel> UpdateVendor(Guid id, VendorModel vendor);
    Task DeleteVendor(Guid id);
}