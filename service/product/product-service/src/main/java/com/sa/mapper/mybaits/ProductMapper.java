package com.sa.mapper.mybaits;

import com.sa.domain.BatchTask;
import com.sa.domain.BatchTaskItem;
import com.sa.domain.Product;
import com.sa.dto.job.BatchTaskDTO;
import com.sa.dto.job.Operate;
import com.sa.product.conditon.ProductQueryCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    void setTaskTotal(@Param("taskId") Long taskId, @Param("size") int size);

    List<Product> getAll();

    Product getProductById(@Param("productId") Long id);

    List<Product> findByParameters(ProductQueryCondition condition);

    boolean submitTask(BatchTaskDTO batchTaskDTO);

    void changeTaskTableState(@Param("taskId") Long taskId, @Param("state") int state);

    Integer getSuccessNumber(@Param("taskId") Long taskId);

    Integer getFailNumber(@Param("taskId") Long taskId);

    Integer getTotalNumber(@Param("taskId") Long taskId);

    void setTaskStatus(@Param("taskId") Long taskId, @Param("status") Integer status);

    Operate getTaskOperate(@Param("taskId") Long taskId);

    void changeTaskState(@Param("id") Long id, @Param("status") int status, @Param("msg") String msg);

    void setSuccessNumber(@Param("successNum") Integer successNum, @Param("taskId") Long taskId);

    void setFailNumber(@Param("failNum") Integer failNum, @Param("taskId") Long taskId);

    Product getProductDistinct(Product map);

    void setItemStatus(@Param("taskId") Long taskId, @Param("status") Integer status, @Param("msg") String msg);

    void addItem(BatchTaskItem batchTaskItem);

    List<BatchTaskItem> getAllTasking();

    List<BatchTask> getNotRunningTask();

    List<BatchTask> getRunningTask();
}
