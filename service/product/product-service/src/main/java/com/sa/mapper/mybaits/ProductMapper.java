package com.sa.mapper.mybaits;

import com.sa.domain.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper{
    public List<Product> getAll();
    public Product getProductById(@Param("productId") Long id);


    List<Product> findByParameters(Product product);
}
