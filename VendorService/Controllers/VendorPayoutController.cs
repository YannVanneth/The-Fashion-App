using Microsoft.AspNetCore.Mvc;
using VendorService.Entity;
using VendorService.Service;

namespace VendorService.Controllers;

[ApiController]
[Route("/api/v1/payout")]
public class VendorPayoutController(IVendorPayoutService service) : ControllerBase
{
    [HttpGet]
    public Task<IEnumerable<VendorPayoutModel>> GetAllVendors()
    {
        return service.GetVendorPayouts();        
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<VendorPayoutModel>> GetVendorById(Guid id)
    {
        var result = await service.GetVendorPayoutById(id);
        return result == null ? NoContent() : Ok(result) ;
    }

    [HttpPost]
    public async Task<ActionResult<VendorPayoutModel>> CreateVendor([FromForm] VendorPayoutModel vendor)
    {
        try
        { 
            return Created( "Vendor payout created successfully.", await service.CreateVendorPayout(vendor));
        }
        catch (Exception ex)
        {
            return BadRequest(ex.Message);
        }
    }

    [HttpPut("{id}")]
    public async Task<ActionResult<VendorPayoutModel>> UpdateVendor([FromRoute] Guid id,[FromForm] VendorPayoutModel vendor)
    {
        try
        {
            return Ok(await service.UpdateVendorPayout(id, vendor));
        }
        catch (Exception ex)
        {
             return BadRequest(ex.Message);
        }
    }

    [HttpDelete("{id}")]
    public  async Task<ActionResult<string>> DeleteVendor([FromRoute] Guid id)
    {
        try
        {
            await service.DeleteVendorPayout(id);
            return Ok("Delete Successfully");
        }
        catch (Exception ex)
        {
            return  BadRequest(ex.Message);
        }
    }
}