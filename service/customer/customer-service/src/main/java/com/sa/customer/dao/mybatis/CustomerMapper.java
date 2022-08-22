package com.sa.customer.dao.mybatis;

import com.sa.customer.domain.ProductInstance;
import com.sa.customer.dto.ProductInstanceDTO;
import com.sa.common.dto.job.BatchTaskDTO;
import com.sa.customer.domain.Customer;
import com.sa.common.dto.job.Status;
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
    List<ProductInstanceDTO> getProductListByCustomerId(@Param("customerId")Long customerId);

    List<BatchTaskDTO> getTaskByStatus(@Param("status") Status status);

    Customer findByAgeAndName(Customer customer);
}
