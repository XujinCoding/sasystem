package com.sa.customer.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SystemDTO {
    public static final Integer successCode = 200;

    public static final Integer failCode = 500;



    public Integer status;
    private Object data;
    public String message;

    public static SystemDTO success(Integer status, Object data, String message){
        return new SystemDTO(status,data,message);
    }
    public static SystemDTO fail(Integer status, String message){
        return new SystemDTO(status,null,message);
    }
}
