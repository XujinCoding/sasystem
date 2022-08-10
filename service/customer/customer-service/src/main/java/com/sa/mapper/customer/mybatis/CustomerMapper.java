package com.sa.mapper.customer.mybatis;

import com.sa.customer.dto.ProductInstanceDTO;
import com.sa.dto.job.BatchTaskDTO;
import com.sa.domain.Customer;
import com.sa.domain.ProductInstance;
import com.sa.dto.job.Status;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xujin
 */

public interface CustomerMapper {

    Customer findById(@Param("customerId") Long customerId);

    void buyProduct(ProductInstance productInstances);

    List<ProductInstanceDTO> findOfferByCustomerId(@Param("customerId") Long customerId);

    /**
     * 根据客户ID 查询对应商品Id列表
     * @param customerId 客户ID
     * @return 商品ID 列表
     */
    List<ProductInstanceDTO> getProductListByProduct(Long customerId);

    List<BatchTaskDTO> getTaskByStatus(@Param("status") Status status);

    Customer findByAgeAndName(Customer customer);
}
