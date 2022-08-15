package com.sa.customer.api.business;

import com.sa.customer.dto.CustomerDTO;
import com.sa.customer.dto.ProductInstanceDTO;
import com.sa.common.dto.job.BatchTaskDTO;
import com.sa.common.dto.job.Status;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author xujin
 */
@FeignClient(name = "customer-service", url = "localhost:8093")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/customer-service")
public interface ICustomerService {

    @GET
    @Path("/find-by-id/{customerId}" )
    CustomerDTO findById(@PathParam("customerId") Long customerId);
    @POST
    @Path("/buyProduct")
    List<ProductInstanceDTO> buyProduct(@RequestBody List<ProductInstanceDTO> list);
    @GET
    @Path("/by-customerId/{customerId}")
    List<ProductInstanceDTO> findOfferByCustomerId(@PathParam("customerId") Long customerId);

    /**
     * 根据ProductId 获取对应的商品Id列表
     * @param customerId
     * @return
     */
    @GET
    @Path("/getProductList/{customerId}")
    List<ProductInstanceDTO> getProductListByProduct(@PathParam("customerId") Long customerId);

    @POST
    @Path("/asynchronouslyAddCustomer")
    BatchTaskDTO createAsynchronouslyTask(@RequestBody BatchTaskDTO batchTaskDTO);
    @POST
    @Path("/getAllTaskByCustomerModel")
    List<BatchTaskDTO> getTaskByStatus(Status status);
}
