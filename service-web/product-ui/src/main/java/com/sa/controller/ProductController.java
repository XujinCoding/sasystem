package com.sa.controller;

import com.sa.dto.SystemDTO;
import com.sa.product.api.business.IProductService;
import com.sa.product.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;


@RestController
@RequestMapping("/product-ui")
@Slf4j
public class ProductController {

//    @Autowired
//    private IProductService productService;

    @RequestMapping("/getAll")
    public SystemDTO getAll() {
      return null;
    }

    @PostMapping("update")
    public SystemDTO update(ProductDTO productDTO){
        return null;
    }



}

