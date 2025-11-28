using Microsoft.EntityFrameworkCore;
using VendorService.Entity;

namespace VendorService.Data;

public class VendorDbContext : DbContext
{
    public VendorDbContext(DbContextOptions<VendorDbContext> options) : base(options)
    { }
    
    public DbSet<VendorModel> Vendors { get; set; }
    public DbSet<VendorAddressModel> VendorAddresses { get; set; }
    public DbSet<VendorPayoutModel> VendorPayout { get; set; }
}