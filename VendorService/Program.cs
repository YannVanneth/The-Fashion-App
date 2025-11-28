using Microsoft.EntityFrameworkCore;
using VendorService.Data;
using VendorService.Repository;
using VendorService.Repository.Impl;
using VendorService.Service;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();

// Repositories
builder.Services.AddScoped<IVendorRepository, VendorRepository>();
builder.Services.AddScoped<IVendorAddressRepository, VendorAddressRepository>();
builder.Services.AddScoped<IVendorPayoutRepository, VendorPayoutRepository>();

// Services
builder.Services.AddScoped<IVendorService, VendorService.Service.Impl.VendorService>();
builder.Services.AddScoped<IVendorAddressService, VendorService.Service.Impl.VendorAddressService>();
builder.Services.AddScoped<IVendorPayoutService, VendorService.Service.Impl.VendorPayoutService>();

builder.Services.AddSwaggerGen();
builder.Services.AddAutoMapper(typeof(Program));

builder.Services.AddDbContext<VendorDbContext>(options => options.UseNpgsql(
    builder.Configuration.GetConnectionString("DefaultConnection")));

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
  app.UseSwagger();
  app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run("http://localhost:9006");