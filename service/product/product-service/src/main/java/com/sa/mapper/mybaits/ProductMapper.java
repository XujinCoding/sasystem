package com.sa.mapper.mybaits;

import com.sa.domain.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductMapper{
    public List<Product> getAll();
    public Product getProductById(@Param("productId") Long id);


}
