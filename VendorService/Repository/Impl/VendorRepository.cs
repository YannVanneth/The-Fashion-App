using Microsoft.EntityFrameworkCore;
using VendorService.Data;
using VendorService.Entity;

namespace VendorService.Repository.Impl;

public class VendorRepository(VendorDbContext context) : IVendorRepository
{
    public async Task<List<VendorModel>> GetAllVendors()
    {
        return await context.Vendors.ToListAsync();
    }

    public async Task<VendorModel?> GetVendorById(Guid id)
    {
        return await context.Vendors.FindAsync(id);
    }

    public async Task<VendorModel> CreateVendor(VendorModel vendor)
    {
        await context.Vendors.AddAsync(vendor);
        await context.SaveChangesAsync();
        return vendor;
    }

    public async Task<VendorModel> UpdateVendor(Guid id, VendorModel vendor)
    {
        context.Vendors.Update(vendor);
        await context.SaveChangesAsync();
        return vendor;
    }

    public async Task DeleteVendor(Guid id)
    {
        try
        {
            var vendor = await context.Vendors.FindAsync(id);

            if (vendor != null)
            {
                context.Vendors.Remove(vendor);
                await context.SaveChangesAsync();
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Vendor with id {id} was not found");    
        }
    }
}