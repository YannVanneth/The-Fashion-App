using VendorService.Entity;

namespace VendorService.Service;

public interface IVendorPayoutService
{
    Task<VendorPayoutModel?> GetVendorPayoutById(Guid id);
    Task<IEnumerable<VendorPayoutModel>> GetVendorPayouts();
    Task<VendorPayoutModel> CreateVendorPayout(VendorPayoutModel vendorPayoutModel);
    Task<VendorPayoutModel> UpdateVendorPayout(Guid id, VendorPayoutModel vendorPayoutModel);
    Task DeleteVendorPayout(Guid id);
}