package com.sa.mapper;

import com.sa.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,String>, JpaSpecificationExecutor<Product> {
    Product findByProductId(Long productId);



    //使用@Query(value = "SQL",nativeQuery = true)的方式进行查询
//    @Modifying()//使用这个注解, @Query注解中就可以写修改语句
    @Query(value = "select * from PRODUCT where PRODUCT_ID=:productId",nativeQuery = true)
    Product getProduct1(@Param("productId") Long productId);



}
