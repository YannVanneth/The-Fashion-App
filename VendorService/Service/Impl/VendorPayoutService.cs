using VendorService.Entity;
using VendorService.Repository;

namespace VendorService.Service.Impl;

public class VendorPayoutService(IVendorPayoutRepository repository) : IVendorPayoutService
{
    public Task<VendorPayoutModel?> GetVendorPayoutById(Guid id)
    {
        return repository.GetVendorPayoutById(id);
    }
    
    public Task<IEnumerable<VendorPayoutModel>> GetVendorPayouts()
    {
        return repository.GetAllVendorsPayouts();
    }

    public Task<VendorPayoutModel> CreateVendorPayout(VendorPayoutModel vendorPayoutModel)
    {
        return repository.CreateVendorPayout(vendorPayoutModel);
    }

    public Task<VendorPayoutModel> UpdateVendorPayout(Guid id, VendorPayoutModel vendorPayoutModel)
    {
        return repository.UpdateVendorPayout(id, vendorPayoutModel);
    }

    public Task DeleteVendorPayout(Guid id)
    {
        return repository.DeleteVendorPayoutById(id);
    }
}