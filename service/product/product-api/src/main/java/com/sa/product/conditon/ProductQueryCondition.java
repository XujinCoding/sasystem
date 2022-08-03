package com.sa.product.conditon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductQueryCondition{

    private Long productId;
    private String productName;
    private Integer productPrice;
    private Integer productNum;
    private String productRemark;

    private Integer pageNum;

    private Integer pageSize;

    public ProductQueryCondition(Long productId, String productName, Integer productPrice, Integer productNum, String productRemark, Integer pageNum, Integer pageSize) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productNum = productNum;
        this.productRemark = productRemark;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
