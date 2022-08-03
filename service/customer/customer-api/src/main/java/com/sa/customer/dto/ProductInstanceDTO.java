package com.sa.customer.dto;

import com.sa.product.dto.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * @author xujin
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductInstanceDTO {
    private Long productId;

    private Long customerId;

    private ProductDTO product;

    private ZonedDateTime createTime;
}
