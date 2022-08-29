package com.sa.customer.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author starttimesxj
 * 全局一事务处理
 */
@Aspect
@Configuration
public class EnableTransactionManager {
    @Autowired
    PlatformTransactionManager transactionManager;
    /*
     * 1.PROPAGATION_REQUIRED如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是最常见的选择。
     * 2.PROPAGATION_SUPPORTS支持当前事务，如果当前没有事务，就以非事务方式执行。
     * 3.PROPAGATION_MANDATORY使用当前的事务，如果当前没有事务，就抛出异常。
     * 4.PROPAGATION_REQUIRES_NEW新建事务，如果当前存在事务，把当前事务挂起。
     * 5.PROPAGATION_NOT_SUPPORTED以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
     * 6.PROPAGATION_NEVER以非事务方式执行，如果当前存在事务，则抛出异常。
     * 7.PROPAGATION_NESTED如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。
     */


    public String AOP_POINTCUT_EXPRESSION = "execution(public * com.sa.customer.api.business.job.*.*(..))";

    @Bean
    public TransactionInterceptor txAdvice() {
        //只读事务，不做更新操作
        var readOnlyTx = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_SUPPORTS, null);
        readOnlyTx.setReadOnly(true);
        //可写事务
        var requiredTx = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, null);
        //子事务
        var sonTx = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_NESTED, null);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        Map<String, TransactionAttribute> txMap = new HashMap<>();



        //配置只读事务
        txMap.put("add*", readOnlyTx);
        txMap.put("do*", readOnlyTx);
        txMap.put("find*", readOnlyTx);

        //配置子事务
        txMap.put("update*", sonTx);

        //配置事务方法的前缀
        txMap.put("*", requiredTx);

        source.setNameMap(txMap);
        return new TransactionInterceptor(transactionManager, source);
    }

    /**
     * 注册事务
     *
     * @return
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

}
