package com.sa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xujin
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer {
    private Long customerId;
    private String customerName;
    private Integer customerAge;
    private String customerHome;
}
