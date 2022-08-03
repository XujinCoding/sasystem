package com.sa.product.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class ProductDTO {
    @ExcelProperty(index = 0)
    private Long productId;
    private String productName;
    private Integer productPrice;
    private Integer productNum;
    private String productRemark;

    public ProductDTO(Long productId, String productName, Integer productPrice, Integer productNum, String productRemark) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productNum = productNum;
        this.productRemark = productRemark;
    }
}
