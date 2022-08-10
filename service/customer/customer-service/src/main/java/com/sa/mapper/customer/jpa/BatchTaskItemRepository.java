package com.sa.mapper.customer.jpa;

import com.sa.domain.BatchTaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BatchTaskItemRepository extends JpaRepository<BatchTaskItem,Long>, JpaSpecificationExecutor<BatchTaskItem> {
    List<BatchTaskItem> findBatchTaskItemsByState(Integer state);

    @Transactional
    @Modifying
    @Query(value = "update BATCH_TASK_ITEM set STATE=:status,MSG=:msg where ID=:id",nativeQuery = true)
    void setStatusAndMsgById(@Param("id") Long id, @Param("status") int status, @Param("msg") String msg);

    //根据TaskId 和 State 查询完成数或者失败数
    Integer countBatchTaskItemByStateAndTaskId(Integer state, Long taskId);
}
