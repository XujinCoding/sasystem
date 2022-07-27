package com.sa.product.api.business;

import com.sa.product.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
//@FeignClient(name = "product-service", url = "localhost:8091")
@Path("/product")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface IProductService {
    //TODO 重构项目, JPA操作增加和修改, Openfeign远程调用
    @GET
    @Path("/getAll1")
    List<ProductDTO> getAll();

    @POST
    @Path("/update")
    ProductDTO update(ProductDTO productDTO);
}
