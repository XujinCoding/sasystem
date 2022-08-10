package com.sa.dto.job;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 异步批量添加用户的DTO
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BatchTaskDTO {
    private Long taskId;
    private Operate operate;
    private String data;
    private Integer state;

    public BatchTaskDTO(Long taskId, Operate operate, String data, Integer state) {
        this.taskId = taskId;
        this.operate = operate;
        this.data = data;
        this.state = state;
    }
}
