using Microsoft.EntityFrameworkCore;
using VendorService.Data;
using VendorService.Entity;

namespace VendorService.Repository.Impl;

public class VendorAddressRepository(VendorDbContext context) : IVendorAddressRepository
{
    public async Task<IEnumerable<VendorAddressModel>> GetAllVendorAddresses()
    {
        return await context.VendorAddresses.ToListAsync();
    }

    public async Task<VendorAddressModel?> GetVendorAddressById(Guid id)
    {
        return await context.VendorAddresses.FindAsync(id);
    }

    public async Task<VendorAddressModel> CreateVendorAddress(VendorAddressModel vendor)
    {
        await context.VendorAddresses.AddAsync(vendor);
        await context.SaveChangesAsync();
        return vendor;
    }

    public async Task<VendorAddressModel> UpdateVendorAddress(Guid id, VendorAddressModel vendor)
    {
        context.VendorAddresses.Update(vendor);
        await context.SaveChangesAsync();
        
        return vendor;
    }

    public async Task DeleteVendorAddress(Guid id)
    {
        try
        {
            var vendorAddress = await context.VendorAddresses.FindAsync(id);

            if (vendorAddress != null)
            {
                context.VendorAddresses.Remove(vendorAddress);
                await context.SaveChangesAsync();
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Vendor address with id {id} was not found"); 
        }
    }
}