package com.sa.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomerDTO {
    private Long customerId;
    private String customerName;
    private Integer customerAge;
    private String customerHome;
}
