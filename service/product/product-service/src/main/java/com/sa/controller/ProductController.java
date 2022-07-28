package com.sa.controller;

import com.alibaba.druid.pool.DruidDataSource;

import com.sa.dto.SystemDTO;
import com.sa.product.api.business.IProductService;
import com.sa.product.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;


@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private com.sa.product.api.business.IProductService IProductService;

    @Autowired
    DataSource dataSource;

    @RequestMapping("/getAll")
    public SystemDTO getAll() {
        System.out.println("controller");
        log.info("--------------------------------------------------------------------------------------------");
//        return SystemDTO.success(SystemDTO.successCode,IProductService.getAll(),"查询成功");
        return null;
    }

    @PostMapping("/update")
    public SystemDTO update(ProductDTO productDTO){
        ProductDTO update = IProductService.update(productDTO);
        if (update == null){
            return SystemDTO.fail(SystemDTO.failCode,"修改商品失败");
        }else {
            return SystemDTO.success(SystemDTO.successCode,update,"修改商品成功");
        }
    }



}

