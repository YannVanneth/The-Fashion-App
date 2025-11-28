using VendorService.Entity;

namespace VendorService.Repository;

public interface IVendorPayoutRepository
{
    Task<IEnumerable<VendorPayoutModel>> GetAllVendorsPayouts();
    Task<VendorPayoutModel?> GetVendorPayoutById(Guid id);
    
    Task<VendorPayoutModel> CreateVendorPayout(VendorPayoutModel vendorPayoutModel);
    Task<VendorPayoutModel> UpdateVendorPayout(Guid id, VendorPayoutModel vendorPayoutModel);
    Task DeleteVendorPayoutById(Guid id);
}