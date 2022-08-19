package com.sa.customer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author xujin
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CUSTOMER")
public class Customer {
    @Id
    @SequenceGenerator(name = "TASK_DETAIL_ID_GENERATOR",sequenceName = "SEQ_CUSTOMER",allocationSize = 1)//自定义的自增策略
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CUSTOMER")//设置主键自增
    private Long customerId;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_AGE")
    private Integer customerAge;

    @Column(name = "CUSTOMER_HOME")
    private String customerHome;

    public Customer(Long customerId, String customerName, Integer customerAge, String customerHome) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.customerHome = customerHome;
    }
}
