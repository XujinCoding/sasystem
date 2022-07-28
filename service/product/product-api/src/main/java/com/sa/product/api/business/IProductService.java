package com.sa.product.api.business;

import com.sa.product.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
@FeignClient(name = "product-service", url = "localhost:8091")
@Path("/product")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface IProductService {
    //TODO 重构项目, JPA操作增加和修改, Openfeign远程调用
    @GET
    @Path("/find-all")
    List<ProductDTO> getAll();

    @POST
    @Path("/update")
    ProductDTO update(ProductDTO productDTO);
    @POST
    @Path("/find-by-Id/{productId}")
    ProductDTO findById(@PathParam("productId") Long productId);
}
