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

    @ExcelProperty(index = 1)
    private String productName;

    @ExcelProperty(index = 2)
    private Integer productPrice;

    @ExcelProperty(index = 3)
    private Integer productNum;

    @ExcelProperty(index = 4)
    private String productRemark;

}
