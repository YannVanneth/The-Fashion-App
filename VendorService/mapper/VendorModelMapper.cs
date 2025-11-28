using AutoMapper;
using VendorService.Entity;

namespace VendorService.mapper;

public class VendorModelMapper : Profile
{
    VendorModelMapper()
    {
        CreateMap<VendorModel, VendorModel>()
            .ForAllMembers(opts => opts.Condition((src, dest, srcMember) => srcMember != null));
    }

}