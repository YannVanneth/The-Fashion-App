using VendorService.Entity;
using VendorService.Repository;

namespace VendorService.Service.Impl;

public class VendorAddressService(IVendorAddressRepository repository) : IVendorAddressService
{
    public Task<IEnumerable<VendorAddressModel>> GetVendorAddresses()
    {
        return repository.GetAllVendorAddresses();
    }

    public Task<VendorAddressModel?> GetVendorAddressById(Guid id)
    {
        return repository.GetVendorAddressById(id);
    }

    public Task<VendorAddressModel> CreateVendorAddress(VendorAddressModel vendorAddress)
    {
        return  repository.CreateVendorAddress(vendorAddress);
    }

    public Task<VendorAddressModel> UpdateVendorAddress(Guid id, VendorAddressModel vendorAddress)
    {
        return repository.UpdateVendorAddress(id, vendorAddress);
    }

    public Task DeleteVendorAddress(Guid id)
    {
        return repository.DeleteVendorAddress(id);
    }
}