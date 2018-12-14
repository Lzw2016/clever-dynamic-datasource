package org.clever.dynamic.datasource.aop;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * 获取对mybatis-plus的支持
 */
@Slf4j
class DynamicDataSourceClassResolver {

    private boolean mpEnabled = false;

    private Field mapperInterfaceField;

    DynamicDataSourceClassResolver() {
        Class<?> proxyClass = null;
        try {
            proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.PageMapperProxy");
        } catch (ClassNotFoundException e) {
            try {
                proxyClass = Class.forName("org.apache.ibatis.binding.MapperProxy");
            } catch (ClassNotFoundException ignored) {
            }
        }
        if (proxyClass != null) {
            try {
                mapperInterfaceField = proxyClass.getDeclaredField("mapperInterface");
                mapperInterfaceField.setAccessible(true);
                mpEnabled = true;
            } catch (NoSuchFieldException e) {
                mpEnabled = false;
                log.warn("@DataSource 支持mybatis-plus失败", e);
            }
        }
    }

    TargetClass targetClass(MethodInvocation invocation) {
        Class<?> clzz = null;
        boolean enable = true;
        if (mpEnabled) {
            Object target = invocation.getThis();
            if (Proxy.isProxyClass(target.getClass())) {
                try {
                    enable = false;
                    clzz = (Class) mapperInterfaceField.get(Proxy.getInvocationHandler(target));
                } catch (IllegalAccessException ignored) {
                }
            } else {
                clzz = target.getClass();
            }
        }
        if (clzz == null) {
            clzz = invocation.getMethod().getDeclaringClass();
        }
        return new TargetClass(clzz, enable);
    }

    @Data
    static class TargetClass implements Serializable {
        private Class<?> clzz;
        private boolean enable;

        TargetClass(Class<?> clzz, boolean enable) {
            this.clzz = clzz;
            this.enable = enable;
        }
    }
}
