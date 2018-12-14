package org.clever.dynamic.datasource.aop;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.clever.dynamic.datasource.annotation.DataSource;
import org.clever.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * 动态数据源AOP核心拦截器
 */
@Slf4j
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    private DynamicDataSourceClassResolver dynamicDataSourceClassResolver = new DynamicDataSourceClassResolver();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dataSourceName = determineDatasource(invocation);
        if (dataSourceName == null) {
            return invocation.proceed();
        }
        try {
            DynamicDataSourceContextHolder.setDataSourceLookupKey(dataSourceName);
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceLookupKey();
        }
    }

    private String determineDatasource(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        DynamicDataSourceClassResolver.TargetClass targetClass = dynamicDataSourceClassResolver.targetClass(invocation);
        Class<?> declaringClass = targetClass.getClzz();
        DataSource ds;
        if (!targetClass.isEnable()) {
            ds = null;
        } else if (method.isAnnotationPresent(DataSource.class)) {
            ds = method.getAnnotation(DataSource.class);
        } else {
            ds = AnnotationUtils.findAnnotation(declaringClass, DataSource.class);
        }
        if (ds == null) {
            log.warn("### @DataSource配置无效，建议删除@DataSource注解，位置 -> {}#{}", declaringClass.getName(), invocation.getMethod().getName());
        }
        return ds == null ? null : ds.value();
    }
}