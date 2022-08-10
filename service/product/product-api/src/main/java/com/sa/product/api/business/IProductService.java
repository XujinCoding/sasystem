package com.sa.product.api.business;

import com.sa.dto.PageResult;
import com.sa.dto.job.BatchTaskDTO;
import com.sa.product.conditon.ProductQueryCondition;
import com.sa.product.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
/**
 * @author xujin
 */
@FeignClient(name = "product-service", url = "localhost:8091")
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
    @Path("/find-by-Id/{productId : \\d+}")
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
     * 我什么使用post 因为jesery报错, get请求不能消耗一个实体
     *
     * @param condition 参数
     * @return 查询结果
     */
    @POST
    @Path("/find-by-parameters")
    PageResult findByParameters(ProductQueryCondition condition);



    /**
     * 根据前端填写的信息,查询值返回
     * 弃用原因见controller
     * 我什么使用post 因为jesery报错, get请求不能消耗一个实体
     *
     * @param productId     商品Id
     * @param productName   商品名
     * @param productPrice  商品价格
     * @param productNum    商品数量
     * @param productRemark 商品备注
     * @return 查询结果
     */
//    @GET
//    @Path("/find-by-parameters/{productId}/{productName}/{productPrice}/{productNum}/{productRemark}")
//    List<ProductDTO> findByParameters(@PathParam("productId") Long productId,
//                                      @PathParam("productName") String productName,
//                                      @PathParam("productPrice") Integer productPrice,
//                                      @PathParam("productNum") Integer productNum,
//                                      @PathParam("productRemark") String productRemark);



    @POST
    @Path("find-by-parameters-useJPA")
    PageResult findByParametersUseJPA(ProductQueryCondition condition);

    /**
     * 提交批量定时任务并不去处理
     * @param batchTaskDTO 定时任务的DTO
     * @return 创建的定时任务
     */
    @POST
    @Path("submit_task")
    boolean submitTask(BatchTaskDTO batchTaskDTO);
}
