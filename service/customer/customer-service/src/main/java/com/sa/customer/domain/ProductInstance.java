package com.sa.customer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductInstance {
    private Long productId;
    private String productName;
    private Integer productPrice;
    private Integer productNum;
    private String productRemark;
    private Long customerId;
    private ZonedDateTime createTime;
}
