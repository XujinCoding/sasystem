package com.sa.customer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xujin
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ProductCustomerRel {
    private Long customerId;
    private List<Long> productId;
}
