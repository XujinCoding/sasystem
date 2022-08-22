package com.sa.common.mapper;

import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author starttimesxj
 * 自动进行字段映射
 */
@Component
public class BeanMapper extends ConfigurableMapper implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private MapperFactory factory;


    public BeanMapper(){
        //禁用构造初始化
        super(false);
    }

    @Override
    protected void configure(MapperFactory factory) {
        this.factory = factory;
        addAllSpringBeans(applicationContext);
    }

    @Override
    protected void configureFactoryBuilder(DefaultMapperFactory.Builder factoryBuilder) {
        //Nothing to configure for now
    }


    private void addAllSpringBeans(ApplicationContext applicationContext) {
        Map<String, Mapper> mappers = applicationContext.getBeansOfType(Mapper.class);
        mappers.values().forEach(this::addMapper);
        Map<String, Converter> beansOfType = applicationContext.getBeansOfType(Converter.class);
        beansOfType.values().forEach(this::addConvert);
    }

    private void addConvert(Converter convert) {
        factory.getConverterFactory().registerConverter(convert);
    }

    /**
     * MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
     * factory.classMap(Person.class, PersonDto.class) // <2>
     *       .field("id","personId")
     *       .field("name.first", "firstName")
     *       .field("name.last", "lastName")
     *       .field("knownAliases{first}", "aliases{[0]}")
     *       .field("knownAliases{last}", "aliases{[1]}")
     *       .byDefault() //<1>
     *       .register();
     * addMapper 等同于 上边的操作, 之不过是
     * @param mapper
     */
    private void addMapper(Mapper<?,?> mapper) {
        ClassMapBuilder<?,?> builder = factory.classMap(mapper.getAType(), mapper.getBType());
        if (mapper instanceof ICustomFieldMap){
            ICustomFieldMap customFieldMap = (ICustomFieldMap) mapper;
            Map<String, String> fieldMap = customFieldMap.getFieldMap();
            fieldMap.keySet().forEach((key)->{
                builder.field(key,fieldMap.get(key));
            });
        }

        //将映射字段注册到MapperFactory中
        builder.byDefault()
                .mapNulls(false).mapNullsInReverse(false)
                .customize((Mapper)mapper)
                .register();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(this.applicationContext)){
            this.applicationContext = applicationContext;
        }
        //在Spring 注入的时候进行初始化
        init();
    }

    @Override
    public <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass) {
        return super.mapAsList(source, destinationClass);
    }
}
