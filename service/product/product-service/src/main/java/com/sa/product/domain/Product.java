package com.sa.product.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name = "PRODUCT")
public class Product {
    @Id
    @SequenceGenerator(name = "PRODUCT_ID_GENERATOR",sequenceName = "SEQ_PRODUCT",allocationSize = 1)//自定义的自增策略
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PRODUCT")//设置主键自增
    @Column(name = "PRODUCT_ID")
    @ExcelProperty(index = 0)
    private Long productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_PRICE")
    private Integer productPrice;

    @Column(name = "PRODUCT_NUM")
    private Integer productNum;

    @Column(name = "PRODUCT_REMARK")
    private String productRemark;

    public Product(Long productId, String productName, Integer productPrice, Integer productNum, String productRemark) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productNum = productNum;
        this.productRemark = productRemark;
    }
}
