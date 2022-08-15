package com.sa.aspect;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.HashMap;
import java.util.Map;

//@Aspect
//@Configuration
public class TransactionManager {
    @Autowired
    PlatformTransactionManager transactionManager;

    public String AOP_POINTCUT_EXPRESSION = "execution(public * com.sa.customer.api.business.job.*.*(..))";

    @Bean
    public TransactionInterceptor txAdvice() {
        //只读事务，不做更新操作
        var readOnlyTx = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_SUPPORTS, null);
        readOnlyTx.setReadOnly(true);
        //可写事务
        var requiredTx = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, null);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        Map<String, TransactionAttribute> txMap = new HashMap<>();

        //配置只读事务
        txMap.put("get*", readOnlyTx);
        txMap.put("parse*", readOnlyTx);
        txMap.put("find*", readOnlyTx);

        //配置事务方法的前缀
        txMap.put("insert*", requiredTx);

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
