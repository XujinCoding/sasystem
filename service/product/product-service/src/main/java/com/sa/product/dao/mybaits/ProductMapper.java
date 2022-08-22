package com.sa.product.dao.mybaits;


import com.sa.common.dto.job.BatchTaskDTO;
import com.sa.common.dto.job.Status;
import com.sa.common.dto.job.Type;
import com.sa.product.conditon.ProductQueryCondition;
import com.sa.product.domain.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    void setTaskTotal(@Param("taskId") Long taskId, @Param("size") int size);

    List<Product> getAll();

    Product getProductById(@Param("productId") Long id);

    List<Product> findByParameters(ProductQueryCondition condition);

    boolean submitTask(BatchTaskDTO batchTaskDTO);

    void changeTaskTableState(@Param("taskId") Long taskId, @Param("state") Status state);

    Integer getSuccessNumber(@Param("taskId") Long taskId);

    Integer getFailNumber(@Param("taskId") Long taskId);

    Integer getTotalNumber(@Param("taskId") Long taskId);

    void setTaskStatus(@Param("taskId") Long taskId, @Param("status") Status status);

    Type getTaskType(@Param("taskId") Long taskId);

    void changeTaskState(@Param("id") Long id, @Param("status") Status status, @Param("msg") String msg);

    void setSuccessNumber(@Param("successNum") Integer successNum, @Param("taskId") Long taskId);

    void setFailNumber(@Param("failNum") Integer failNum, @Param("taskId") Long taskId);

    Product getProductDistinct(Product map);

    void setItemStatus(@Param("taskId") Long taskId, @Param("status") Integer status, @Param("msg") String msg);





}
