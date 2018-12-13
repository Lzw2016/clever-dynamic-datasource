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
        Class<?> declaringClass = invocation.getMethod().getDeclaringClass();
        DataSource ds;
        if (method.isAnnotationPresent(DataSource.class)) {
            ds = method.getAnnotation(DataSource.class);
        } else {
            ds = AnnotationUtils.findAnnotation(declaringClass, DataSource.class);
        }
        if (ds == null) {
            log.warn("### 当前@DataSource配置无效，建议删除@DataSource注解 {}", invocation.getMethod().toString());
        }
        // Mapper 上面使用 @DataSource 不起作用报警告
        return ds == null ? null : ds.value();
    }
}