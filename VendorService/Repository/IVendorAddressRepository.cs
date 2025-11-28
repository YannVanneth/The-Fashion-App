using VendorService.Entity;

namespace VendorService.Repository;

public interface IVendorAddressRepository
{
    Task<IEnumerable<VendorAddressModel>> GetAllVendorAddresses();
    Task<VendorAddressModel?> GetVendorAddressById(Guid id);
    Task<VendorAddressModel> CreateVendorAddress(VendorAddressModel vendor);
    Task<VendorAddressModel> UpdateVendorAddress(Guid id, VendorAddressModel vendor);
    Task DeleteVendorAddress(Guid id);
}