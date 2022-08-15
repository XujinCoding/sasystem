package com.sa.product.domain;


import com.sa.common.dto.job.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "BATCH_TASK_ITEM")
//@Component
@Entity
public class BatchTaskItem {
    @Id
    @SequenceGenerator(name = "TASK_DETAIL_ID_GENERATOR",sequenceName = "TASK_DETAIL",allocationSize = 1)//自定义的自增策略
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TASK_DETAIL")//设置主键自增
    private Long id;

    @Column(name = "TASK_ID")
    private Long taskId;

    @Column(name = "STATE")
    private Status state;

    @Column(name = "MSG")
    private String msg;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_AGE")
    private Integer customerAge;

    @Column(name = "CUSTOMER_HOME")
    private String customerHome;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_PRICE")
    private Integer productPrice;

    @Column(name = "PRODUCT_NUM")
    private Integer productNum;

    @Column(name = "PRODUCT_REMARK")
    private String productRemark;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BatchTaskItem that = (BatchTaskItem) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
