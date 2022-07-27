package com.sa.domain;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)

@Entity
@Table(name = "PRODUCT")
public class Product {
    @Id
//    @SequenceGenerator(name = "PRODUCT_ID_GENERATOR",sequenceName = "SEQ_PRODUCT",allocationSize = 1)//自定义的自增策略
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PRODUCT")//设置主键自增
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_PRICE")
    private Integer productPrice;

    @Column(name = "PRODUCT_NUM")
    private Integer productNum;

    @Column(name = "PRODUCT_REMARK")
    private String productRemark;
}
