package com.sa.controller;


import com.sa.product.api.business.IProductService;
import com.sa.product.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("customer/")
public class LoginController {
    @Autowired
    private IProductService productService;

    @RequestMapping(value = "product/all", method = RequestMethod.GET)
    @ResponseBody
    public List<ProductDTO> getProductInfos(){
        List<ProductDTO> dtos = productService.getAll();
        return dtos;
    }


    @RequestMapping(value = "product/findById", method = RequestMethod.GET)
    @ResponseBody
    public ProductDTO finById(Long id){
        ProductDTO productDTO = productService.findById(id);
        return productDTO;
    }
}
