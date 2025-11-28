using VendorService.Entity;

namespace VendorService.Service;

public interface IVendorAddressService
{
    Task<IEnumerable<VendorAddressModel>> GetVendorAddresses();
    Task<VendorAddressModel?> GetVendorAddressById(Guid id);
    Task<VendorAddressModel> CreateVendorAddress(VendorAddressModel vendorAddress);
    Task<VendorAddressModel> UpdateVendorAddress(Guid id, VendorAddressModel vendorAddress);
    Task DeleteVendorAddress(Guid id);
}