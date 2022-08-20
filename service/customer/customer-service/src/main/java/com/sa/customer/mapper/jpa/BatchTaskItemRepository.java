package com.sa.customer.mapper.jpa;

import com.sa.common.dto.job.Status;
import com.sa.customer.domain.BatchTaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author starttimesxj
 */
@Repository
public interface BatchTaskItemRepository extends JpaRepository<BatchTaskItem,Long>, JpaSpecificationExecutor<BatchTaskItem> {
    /**
     * 通过任务状态和人物ID 查询任务明细, 按照Id排序
     * @param state
     * @param taskId
     * @return
     */
    List<BatchTaskItem> findBatchTaskItemsByStateAndTaskIdOrderById(Status state, Long taskId);

    @Transactional
    @Modifying
    @Query(value = "update BATCH_TASK_ITEM set STATE=:status,MSG=:msg where ID=:id",nativeQuery = true)
    void setStatusAndMsgById(@Param("id") Long id, @Param("status") Integer status, @Param("msg") String msg);

    //根据TaskId 和 State 查询完成数或者失败数
    Integer countBatchTaskItemByStateAndTaskId(Status state, Long taskId);

    List<BatchTaskItem> getByCustomerAgeAndCustomerNameAndCustomerHome(Integer customerAge, String customerName, String customerHome);

    List<BatchTaskItem> findAllByTaskId(Long taskId);

    List<BatchTaskItem> findAllByCustomerNameIn(List<String> customerName);

}
