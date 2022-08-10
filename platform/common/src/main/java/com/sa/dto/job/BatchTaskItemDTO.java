package com.sa.dto.job;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class BatchTaskItemDTO {

    private Long id;

    private Long taskId;

    private Operate operate;

    private Integer state;

    private Long customerId;

    private String customerName;

    private Integer customerAge;

    private String customerHome;

    private String productName;

    private Integer productPrice;

    private Integer productNum;

    private String productRemark;

    public BatchTaskItemDTO(Long id, Long taskId, Operate operate, Integer state, Long customerId, String customerName, Integer customerAge, String customerHome, String productName, Integer productPrice, Integer productNum, String productRemark) {
        this.id = id;
        this.taskId = taskId;
        this.operate = operate;
        this.state = state;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.customerHome = customerHome;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productNum = productNum;
        this.productRemark = productRemark;
    }
}
