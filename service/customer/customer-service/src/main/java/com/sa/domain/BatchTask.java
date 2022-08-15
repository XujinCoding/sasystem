package com.sa.domain;


import com.sa.dto.job.Status;
import com.sa.dto.job.TaskLevel;
import com.sa.dto.job.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * 对应数据库中的任务表, 并和BatchAddCustomerDTO进行映射
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "BATCH_TASK")
@Entity
@Component
public class BatchTask {
    @Id
    @SequenceGenerator(name = "TASK_ID_GENERATOR",sequenceName = "TASK_SEQ",allocationSize = 1)//自定义的自增策略
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TASK_SEQ")//设置主键自增

    @Column(name = "TASK_ID")
    private Long taskId;
    @Column(name = "TYPE")
    private Type type;
    @Column(name = "DATA")
    private String data;
    @Column(name = "STATE")
    private Status state;
    @Column(name = "TASK_LEVEL")
    @Enumerated
    private TaskLevel taskLevel;


    public BatchTask(Long taskId, Type type, String data, Status state) {
        this.taskId = taskId;
        this.type = type;
        this.data = data;
        this.state = state;
    }
}
