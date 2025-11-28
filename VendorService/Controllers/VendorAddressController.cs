using Microsoft.AspNetCore.Mvc;
using VendorService.Entity;
using VendorService.Service;

namespace VendorService.Controllers;

[ApiController]
[Route("/api/v1/address")]
public class VendorAddressController(IVendorAddressService service) : ControllerBase
{
    [HttpGet]
    public async Task<ActionResult<IEnumerable<VendorAddressModel>>> GetAllVendors()
    {
        return Ok(await service.GetVendorAddresses());        
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<VendorAddressModel>> GetVendorById(Guid id)
    {
        
        var vendor = await service.GetVendorAddressById(id);
        
        return vendor == null ? NotFound() : Ok(vendor);
    }

    [HttpPost]
    public async Task<ActionResult<VendorAddressModel>> CreateVendor([FromForm] VendorAddressModel vendor)
    {
        try
        {
            return await service.CreateVendorAddress(vendor);
        }
        catch (Exception ex)
        {
            return BadRequest(ex.Message);
        }
    }

    [HttpPut("{id}")]
    public async Task<ActionResult<VendorAddressModel>> UpdateVendor([FromRoute] Guid id,[FromForm] VendorAddressModel vendor)
    {
        try
        {
           return Ok(await service.UpdateVendorAddress(id, vendor));
        }
        catch (Exception ex)
        {
            return BadRequest(ex.Message);
        }
    }

    [HttpDelete("{id}")]
    public async Task<ActionResult<string>> DeleteVendor([FromRoute] Guid id)
    {
        try
        {
            await service.DeleteVendorAddress(id);
            return Ok("Vendor address is deleted successfully.");

        }
        catch (Exception ex)
        {
            return BadRequest(ex.Message);
        }    
    }
}