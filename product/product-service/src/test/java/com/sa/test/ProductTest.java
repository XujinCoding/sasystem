package com.sa.test;

import com.sa.ProductApplication;
import com.sa.product.api.business.IProductService;
import com.sa.product.dto.ProductDTO;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProductApplication.class})
public class ProductTest {

    @Autowired
    private IProductService productService;

    public void testXing(){
        List<ProductDTO> all = productService.getAll();
        System.out.println(all.size());
    }

}
