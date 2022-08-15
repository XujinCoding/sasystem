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
//@Accessors(chain = true)
public class BatchTaskDTO {
    private Long taskId;
    private Type type;
    private String data;
    private Status state;
    private TaskLevel taskLevel;
}
