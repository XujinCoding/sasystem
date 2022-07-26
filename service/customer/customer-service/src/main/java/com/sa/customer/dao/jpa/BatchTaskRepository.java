package com.sa.customer.dao.jpa;

import com.sa.common.dto.job.Status;
import com.sa.common.dto.job.TaskLevel;
import com.sa.customer.domain.BatchTask;
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
public interface BatchTaskRepository extends JpaRepository<BatchTask,Long>, JpaSpecificationExecutor<BatchTask> {


    @Modifying
    @Transactional
    @Query(value = "UPDATE BATCH_TASK set SA_DB.BATCH_TASK.STATE=:status,SA_DB.BATCH_TASK.TOTAL=:total where TASK_ID=:taskId",nativeQuery = true)
    void modifyTaskStatusAndTotal( @Param("taskId")Long taskId, @Param("status")Integer status, @Param("total")Integer total);




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

    List<BatchTask> findByTaskId(Long taskId);
    @Modifying
    @Transactional
    @Query(value = "UPDATE BATCH_TASK set TASK_LEVEL=:level where TASK_ID=:taskId",nativeQuery = true)
    void changeTaskLevelByTaskId(@Param("taskId")Long taskId, @Param("level")TaskLevel level);

    List<BatchTask> getByStateOrderByTaskLevelDescTaskIdAsc(Status state);

    @Query(value = "select TASK_ID FROM BATCH_TASK WHERE STATE=:state",nativeQuery = true)
    List<Long> findTasIdByState(@Param("state")Integer state);


}
