package com.sa.customer.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

/**
 * 与前端交互的对象
 *     private int status; 状态码
 *     private Object data; 数据
 *     private String message; 消息
 */
public class SystemDTO implements Serializable {

    private int status;
    private Object data;
    private String message;


    public SystemDTO(int status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }


}
