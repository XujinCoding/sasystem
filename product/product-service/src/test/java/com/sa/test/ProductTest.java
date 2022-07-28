package com.sa.test;

import com.sa.ProductApplication;
import com.sa.product.api.business.IProductService;
import com.sa.product.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = {ProductApplication.class})
public class ProductTest {

    @Autowired
    private IProductService productService;
    @Test
    public void testXing(){
        List<ProductDTO> all = productService.getAll();
        System.out.println(all.size());
    }

}
