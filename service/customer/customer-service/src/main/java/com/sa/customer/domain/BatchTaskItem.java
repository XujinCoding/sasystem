package com.sa.customer.domain;


import com.sa.common.dto.job.Status;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author starttimesxj
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BATCH_TASK_ITEM")
@Accessors(chain = true)
@Entity
public class BatchTaskItem {
    @Id
    @SequenceGenerator(name = "TASK_DETAIL_ID_GENERATOR", sequenceName = "SEQ_BATCH_TASK_ITEM", allocationSize = 1)
//自定义的自增策略
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BATCH_TASK_ITEM")//设置主键自增
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
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        BatchTaskItem that = (BatchTaskItem) o;
        return id != null && Objects.equals(id, that.id);
    }

    public static BatchTaskItem getCustomerTaskItem(Long taskId, Status state, String customerName, Integer customerAge, String customerHome) {
        return new BatchTaskItem().setTaskId(taskId).setCustomerName(customerName).setCustomerAge(customerAge).setCustomerHome(customerHome).setState(state);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
