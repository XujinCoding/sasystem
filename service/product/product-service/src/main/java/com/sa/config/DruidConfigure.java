package com.sa.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 配置文件主要作用: 使用druid的数据源将原来的数据源替换掉,以后访问数据库使用的都是druid数据源
 */
@Configuration
public class DruidConfigure {
    @Bean
    //指定从配置中加载"spring.datasource"开头的配置，因为我们的Druid的配置是写在这个前缀下面的。
    @ConfigurationProperties(prefix = "spring.datasource")
    //指定条件：必须配置了spring.datasource.type属性，并且属性的值必须是"com.alibaba.druid.pool.DruidDataSource"才加载DruidDataSource。
    @ConditionalOnProperty(prefix = "spring.datasource", name = "type", havingValue = "com.alibaba.druid.pool.DruidDataSource")
    public DataSource getDataSource() {
        return new DruidDataSource();
    }
}
