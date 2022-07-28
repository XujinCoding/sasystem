package com.sa.product.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)

public class ProductDTO {
    private Long productId;
    private String productName;
    private Integer productPrice;
    private Integer productNum;
    private String productRemark;
}
