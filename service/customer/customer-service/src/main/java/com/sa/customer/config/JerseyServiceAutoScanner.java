package com.sa.customer.config;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JerseyServiceAutoScanner {
    private JerseyServiceAutoScanner() {}

    public static Class[] getPublishJerseyServiceClasses(ApplicationContext applicationContext, String... scanPackages) {
        // 传入applicationContext对象，在整个spring容器中捞我们需要的controller
        // 传入的第二个参数是可变参数，字符串，用于传入需要扫描的包路径
        List<Class> jerseyServiceClasses = new ArrayList<>();
        if (scanPackages == null || scanPackages.length == 0) {
            return jerseyServiceClasses.toArray(new Class[jerseyServiceClasses.size()]);
        }
        ClassPathScanningCandidateComponentProvider scanner = new JerseyScanningComponentProvider(false);
        // 我只需要扫描使用了@RestController注解的controller，如果还有其他的组合条件，可以在这里增加
        scanner.addIncludeFilter(new AnnotationTypeFilter(Path.class));
        for (var scanPackage : scanPackages) {
            jerseyServiceClasses.addAll(scanner.findCandidateComponents(scanPackage).stream()
                    .map(beanDefinition -> ClassUtils
                            .resolveClassName(beanDefinition.getBeanClassName(), applicationContext.getClassLoader()))
                    .collect(Collectors.toSet()));
        }
        // 返回符合条件的spring容器中的全部的类对象
        return jerseyServiceClasses.toArray(new Class[jerseyServiceClasses.size()]);
    }

    private static class JerseyScanningComponentProvider extends ClassPathScanningCandidateComponentProvider {
        public JerseyScanningComponentProvider(boolean useDefaultFilters) {
            super(useDefaultFilters);
        }
        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            AnnotationMetadata metadata = beanDefinition.getMetadata();
            return (metadata.isIndependent() && metadata.isAbstract() && !beanDefinition.getMetadata().isAnnotation());
        }
    }
}