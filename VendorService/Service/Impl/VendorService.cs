using AutoMapper;
using VendorService.Entity;
using VendorService.Repository;

namespace VendorService.Service.Impl;

public class VendorService(IVendorRepository repository) : IVendorService
{
    public async Task<IEnumerable<VendorModel>> GetAllVendors()
    {
        return await repository.GetAllVendors();
    }

    public async Task<VendorModel?> GetVendorById(Guid id)
    {
        return await repository.GetVendorById(id);
    }

    public Task<VendorModel> CreateVendor(VendorModel vendor)
    {
        return repository.CreateVendor(vendor);
    }

    public Task<VendorModel> UpdateVendor(Guid id, VendorModel vendor)
    {
        return repository.UpdateVendor(id, vendor);
    }

    public Task DeleteVendor(Guid id)
    {
        return repository.DeleteVendor(id);
    }
}