package com.sa.customer.dao.jpa;

import com.sa.customer.domain.Customer;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {
    Customer findByCustomerName(String customerName);
    @Transactional
    @Modifying
    @Query(value = "update CUSTOMER set CUSTOMER_NAME = :customerName where CUSTOMER_ID = :customerId  ",nativeQuery = true)
    void updateCustomerNameByCustomerId(@Param("customerName") String customerName,@Param("customerName") Long customerId);

}
