using Microsoft.AspNetCore.Mvc;
using VendorService.Entity;
using VendorService.Service;

namespace VendorService.Controllers;


[ApiController]
[Route("/api/v1/vendors")]
public class VendorController(IVendorService service) : ControllerBase
{
    [HttpGet]
    public async Task<ActionResult<IEnumerable<VendorModel>>> GetAllVendors()
    {
       return Ok(await service.GetAllVendors());        
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<VendorModel?>> GetVendorById(Guid id)
    {
        
        var vendor = await service.GetVendorById(id);
        
        return vendor == null ? NoContent() :  Ok(vendor);
    }

    [HttpPost]
    public async Task<ActionResult<VendorModel>> CreateVendor([FromForm] VendorModel vendor)
    {
        try
        {
            return Created("Vendor created Successfully",await service.CreateVendor(vendor));
        }
        catch (Exception ex)
        {
            return BadRequest(ex.Message);
        }
    }

    [HttpPut("{id}")]
    public async Task<ActionResult<VendorModel>> UpdateVendor([FromRoute] Guid id,[FromForm] VendorModel vendor)
    {
        try
        {
            return Ok(await service.UpdateVendor(id, vendor));
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
            await service.DeleteVendor(id);
            return Ok("Vendor deleted successfully");
        }
        catch (Exception ex)
        {
            return BadRequest(ex.Message);
        }
        
    }
}