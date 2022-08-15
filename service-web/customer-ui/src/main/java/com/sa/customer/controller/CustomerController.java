package com.sa.customer.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sa.customer.api.business.ICustomerService;
import com.sa.customer.dto.ProductInstanceDTO;
import com.sa.common.dto.job.BatchTaskDTO;
import com.sa.product.api.business.IProductService;
import com.sa.product.dto.ProductDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author xujin
 */
@RestController
@RequestMapping("customer/")
@Api(value = "客户模块")
public class CustomerController {
    @Autowired
    private IProductService productService;

    @Autowired
    private ICustomerService customerService;

    /**
     * 创建一个缓存对象
     */
    LoadingCache<Long, ProductDTO> products = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build(
                    new CacheLoader<Long, ProductDTO>() {
                        @Override
                        //用于指定参数,返回值不为空
                        @Nonnull
                        public ProductDTO load(@Nonnull Long key) {
                            return productService.findById(key);
                        }
                    });

    /**
     * 用于测试两个服务之间的连通性, 获取所有的product
     * @return 所有的product
     */
    @ApiOperation(value = "获取所有的Product")
    @RequestMapping(value = "product/all", method = RequestMethod.GET)
    @ResponseBody
    public List<ProductDTO> getProductInfos(){
        return productService.getAll();
    }

    /**
     * 根据customerId查询product列表
     *
     * @param customerId 客户id
     * @return
     */
    @ResponseBody
    @ApiOperation(value = "使用缓存根据customerId查询product列表")
    @RequestMapping(value = "by-customerId/{customerId}", method = RequestMethod.GET)
    public List<ProductInstanceDTO> findProductByCustomerId(@PathVariable Long customerId){
        List<ProductInstanceDTO> productIdList = customerService.getProductListByProduct(customerId);
        productIdList.forEach(instanceDTO -> {
            try {
                ProductDTO productDTO = products.get(instanceDTO.getProductId());
                instanceDTO.setProduct(productDTO);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(productIdList);
        return productIdList;
    }

    /**
     * 直接从数据库中获取商品列表信息
     * @param customerId 客户ID
     */
    @ResponseBody
    @ApiOperation(value = "直接从数据库中获取商品列表信息")
    @RequestMapping(value = "getProduct/{customerId}", method = RequestMethod.GET)
    public List<ProductInstanceDTO> findProductInstanceByCustomerId( @PathVariable Long customerId){
        List<ProductInstanceDTO> list = customerService.findOfferByCustomerId(customerId);
        System.out.println(list);
        return list;
    }

    /**
     * 根据商品Id查询商品
     * @param id 商品Id
     * @return
     */
    @RequestMapping(value = "product/findById", method = RequestMethod.GET)
    @ApiOperation(value = "根据商品Id查询商品")
    @ResponseBody
    public ProductDTO findById(Long id){
        return productService.findById(id);
    }

    /**
     * 购买商品
     * @param list 购买的商品信息列表
     */
    @ResponseBody
    @ApiOperation(value = "购买商品")
    @RequestMapping(value = "/customer/buyProduct", method = RequestMethod.POST,produces = "application/json")
    public void buyProduct(@RequestBody List<ProductInstanceDTO> list) {
        System.out.println(list);
        customerService.buyProduct(list);

        //TODO : 功能性验证

        //查看用户是否存在

        //查看商品是否存在

    }



    @ResponseBody
    @ApiOperation(value = "异步批量添加客户")
    @RequestMapping(value = "/asynchronouslyAddCustomer", method = RequestMethod.POST,produces = "application/json")
    public BatchTaskDTO asynchronouslyAddCustomer(@RequestBody BatchTaskDTO batchTaskDTO) {
        /**
         * {
         *     "type":"0",
         *     "data":"顾客1,123,2 顾客2,3,2"
         * }
         */
        return customerService.createAsynchronouslyTask(batchTaskDTO);
    }
}
