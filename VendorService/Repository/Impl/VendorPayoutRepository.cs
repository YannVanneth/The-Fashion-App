using Microsoft.EntityFrameworkCore;
using VendorService.Data;
using VendorService.Entity;

namespace VendorService.Repository.Impl;

public class VendorPayoutRepository(VendorDbContext context) : IVendorPayoutRepository
{
    public async Task<IEnumerable<VendorPayoutModel>> GetAllVendorsPayouts()
    {
        return await context.VendorPayout.ToListAsync();
    }

    public async Task<VendorPayoutModel?> GetVendorPayoutById(Guid id)
    {
        return await context.VendorPayout.FindAsync(id);
    }

    public async Task<VendorPayoutModel> CreateVendorPayout(VendorPayoutModel vendorPayoutModel)
    {
        await context.VendorPayout.AddAsync(vendorPayoutModel);
        await context.SaveChangesAsync();
        return vendorPayoutModel;
    }

    public async Task<VendorPayoutModel> UpdateVendorPayout(Guid id, VendorPayoutModel vendorPayoutModel)
    {
        context.VendorPayout.Update(vendorPayoutModel);
        await context.SaveChangesAsync();
        return vendorPayoutModel;
    }

    public async Task DeleteVendorPayoutById(Guid id)
    {
        try
        {
            var vendorPayout = await context.VendorPayout.FindAsync(id);

            if (vendorPayout != null)
            {
                context.VendorPayout.Remove(vendorPayout);
                await context.SaveChangesAsync();
            }

            throw new Exception($"VendorPayout with id {id} was deleted.");

        }catch(Exception ex)
        {
            throw new Exception($"Vendor payout with id {id} was not found");
        }
    }
}