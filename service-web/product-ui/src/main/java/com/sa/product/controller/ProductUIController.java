package com.sa.product.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sa.common.dto.PageResult;
import com.sa.common.listener.ExcelListener;
import com.sa.product.api.business.IProductService;
import com.sa.product.conditon.ProductQueryCondition;
import com.sa.product.dto.ProductDTO;
import com.sa.product.dto.SystemDTO;
import com.sa.product.guava.cache.GuavaCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * @author Administrator
 */
@RestController
@RequestMapping("/product-ui")
@Api(value = "商品模块")
public class ProductUIController {

    @Autowired
    private IProductService productService;


    /**
     * 获取所有的Product
     * @return 所有的Product
     */
    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    @ApiOperation(value = "获取所有的Product")
    public SystemDTO getAll() {
      return new SystemDTO(200,productService.getAll(),"");
    }

    /**
     * 修改product信息
     * @param productDTO 修改的信息
     * @return 修改后的信息
     */

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @ApiOperation(value = "修改product信息")
    public SystemDTO update(ProductDTO productDTO){
        return new SystemDTO(200,productService.update(productDTO),"");
    }

    /**
     * 根据ID 查询商品数据
     * @param productId 商品的ID
     * @return 包含商品信息的SystemDTO
     */

    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    @ApiOperation(value = "根据ID 查询商品数据")
    public SystemDTO findById(@RequestParam("productId") Long productId) throws ExecutionException {
        ProductDTO productDTO;
        try {
            LoadingCache<Long, ProductDTO> cache = GuavaCache.getCache();
            productDTO = cache.get(productId);
        }catch (CacheLoader.InvalidCacheLoadException e){
            return  new SystemDTO(200,null,"查询数据为空");
        }
        return  new SystemDTO(200,productDTO,"");
}


    /**
     * 根据所有的字段查询条件
     * 使用这种方式传递参数的时候, 必须每一个参数都有值, 不能为null , 也不能为 "" 空串, 不符合复杂查询的需求, 弃用!!!!!!!!!!!!!!!!!!
     * @param condition 所有的参数
     * @return 包含商品信息的SystemDTO
     */
    @RequestMapping(value = "/findByParameters",method = RequestMethod.GET)
    @ApiOperation(value = "根据所有的字段查询条件")
    public SystemDTO findByParameters(ProductQueryCondition condition) throws ExecutionException {
        PageResult byParameters = productService.findByParameters(condition);

        return  new SystemDTO(200,byParameters,"");
    }


    @RequestMapping(value = "/findByParametersUseJPA",method = RequestMethod.GET)
    @ApiOperation(value = "使用Jpa进行查询, 并且分页")
    public SystemDTO findByParametersUseJPA(ProductQueryCondition condition) throws ExecutionException {
        PageResult pageResult= productService.findByParametersUseJPA(condition);
        return  new SystemDTO(200,pageResult,"");
    }



    /**
     * 读取一个excel文件, 将数据映射称DTO存到数据库中
     * @return 所有存入的DTO 的数据
     */
    @RequestMapping(value = "/saveAll",method = RequestMethod.POST)
    @ApiOperation(value = "读取一个excel文件, 将数据映射称DTO存到数据库中")
     public SystemDTO saveAll(){
        /**
         * 工作薄: 一个 excel 文件就是一个工作薄
         * 工作表: 一个工作薄中可以有多个工作表（sheet）
         */
        /**
         * 构建一个工作簿
         * pathName 要读的文件的路径
         * head 文件中每一行数据要存储到实体的类型的 class
         * readListener 读取监听，每读取一行内容，都会调用该对象的 invoke，在 invoke 可以操作使用读取到的数据
         * sheet方法参数： 工作表的顺序号（从0开始）或者工作表的名字，不传默认为0
         */
        //获取一个监听对象
        ExcelListener<ProductDTO> excelListener = new ExcelListener<>();
        //获取到一个工作簿对象
        ExcelReaderBuilder read = EasyExcel.read("ProductDTO.xlsx", ProductDTO.class, excelListener);
        //获得一个工作表对象
        ExcelReaderSheetBuilder sheet = read.sheet();
        //读取工作表的内容
        sheet.doRead();
        //获取Excel中的数据并映射到DTO上
        List<ProductDTO> list = excelListener.getList();
        if (list.size()==0){
            return new SystemDTO(200,null,"excel中没有数据或数据解析失败");
        }

        //将List 中的数据存储到数据库中
        List<ProductDTO> productDTOS = productService.saveAll(list);
        return new SystemDTO(200,productDTOS,"成功");

    }


//    @RequestMapping(value = "/submitTask",method = RequestMethod.POST)
//    @ApiOperation(value = "提交一个定时批量任务 不需要传递ID 和 state 由后台自动生成")
//    public SystemDTO submitTask(@RequestBody BatchTaskDTO batchTaskDTO){
//        batchTaskDTO.setState(Status.PREPARING);
//        return new SystemDTO(200,productService.submitTask(batchTaskDTO),"");
//    }


    /**
     * 根据所有的字段查询条件
     * 使用这种方式传递参数的时候, 必须每一个参数都有值, 不能为null , 也不能为 "" 空串, 不符合复杂查询的需求, 弃用!!!!!!!!!!!!!!!!!!
     * @param productDTO 所有的参数
     * @return 包含商品信息的SystemDTO
     */
//    @RequestMapping(value = "/findByParameters",method = RequestMethod.GET)
//    @ApiOperation(value = "根据所有的字段查询条件")
//    public SystemDTO findByParameters(ProductQueryCondition productDTO) throws ExecutionException {
//        List<ProductDTO> byParameters = productService.findByParameters(
//                productDTO.getProductId(),
//                productDTO.getProductName(),
//                productDTO.getProductPrice(),
//                productDTO.getProductNum(),
//                productDTO.getProductRemark()
//        );
////        List<ProductDTO> byParameters = productService.findByParameters(productDTO);
//
//        return  new SystemDTO(200,byParameters,"");
//    }


}

