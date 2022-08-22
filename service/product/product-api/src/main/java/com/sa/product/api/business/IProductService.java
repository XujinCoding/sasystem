package com.sa.product.api.business;

import com.sa.common.dto.PageResult;
import com.sa.product.conditon.ProductQueryCondition;
import com.sa.product.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
/**
 * @author xujin
 */
@FeignClient(name = "product-service", url = "localhost:8090")
@Path("/product")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface IProductService {
    /**
     * 获取所有的productDTO
     * @return 所有的productDTO
     */
    @GET
    @Path("/find-all")
    List<ProductDTO> getAll();

    /**
     * 根据productId 修改productDTO
     * @param productDTO 修改的productDTO
     * @return 修改完成的DTO
     */
    @PUT
    @Path("/update")
    ProductDTO update(ProductDTO productDTO);

    /**
     * 查询product通过productId
     * @param productId 商品ID
     * @return 商品
     */
    @GET
    @Path("/find-by-Id/{productId}")
    ProductDTO findById(@PathParam("productId") Long productId);

    /**
     * 保存List中所有的信息
     * @param productDTOS product列表
     * @return product列表
     */
    @POST
    @Path("/batch-save")
    List<ProductDTO> saveAll(List<ProductDTO> productDTOS);


    /**
     * 根据前端填写的信息,查询值返回
     * 为什么使用post 因为jersey报错, get请求不能消耗一个实体
     *
     * @param condition 参数
     * @return 查询结果
     */
    @POST
    @Path("/find-by-parameters")
    PageResult findByParameters(ProductQueryCondition condition);



    @POST
    @Path("find-by-parameters-useJPA")
    PageResult findByParametersUseJPA(ProductQueryCondition condition);

}
