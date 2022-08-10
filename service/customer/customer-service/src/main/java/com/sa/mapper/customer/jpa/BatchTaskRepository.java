package com.sa.mapper.customer.jpa;

import com.sa.domain.BatchTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BatchTaskRepository extends JpaRepository<BatchTask,Long>, JpaSpecificationExecutor<BatchTask> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE BATCH_TASK set STATE=:status where TASK_ID=:taskId",nativeQuery = true)
    void changeTaskStatus(@Param("taskId")Long taskId, @Param("status") Integer status);


    @Modifying
    @Transactional
    @Query(value = "UPDATE BATCH_TASK set TOTAL=:total where TASK_ID=:taskId",nativeQuery = true)
    void setTaskTotal(@Param("taskId")Long taskId, @Param("total")Integer total);





    @Modifying
    @Transactional
    @Query(value = "UPDATE BATCH_TASK set SUCCESS_NUM=:successNum,FAIL_NUM=:failNum where TASK_ID=:taskId",nativeQuery = true)
    void setSuccessAndFailNum(@Param("taskId")Long taskId, @Param("successNum")Integer successNum, @Param("failNum")Integer failNum);


    @Modifying
    @Transactional
    @Query(value = "UPDATE BATCH_TASK set DATA='OK' where TASK_ID=:taskId",nativeQuery = true)
    void setDataOkByTaskId(@Param("taskId")Long taskId);

    @Query(value = "select TOTAL FROM BATCH_TASK WHERE TASK_ID=:taskId",nativeQuery = true)
    Integer findTotalByTaskId(@Param("taskId") Long taskId);

    @Query(value = "select TYPE FROM BATCH_TASK WHERE TASK_ID=:taskId",nativeQuery = true)
    Integer findTypeByTaskId(@Param("taskId") Long taskId);
}
