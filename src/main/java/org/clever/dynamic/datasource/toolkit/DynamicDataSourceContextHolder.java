package org.clever.dynamic.datasource.toolkit;

import org.springframework.core.NamedThreadLocal;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 核心基于ThreadLocal的切换数据源工具类
 */
public final class DynamicDataSourceContextHolder {

    /**
     * 为什么要用链表存储(准确的是栈)
     * <pre>
     * 为了支持嵌套切换，如ABC三个service都是不同的数据源
     * 其中A的某个业务要调B的方法，B的方法需要调用C的方法。一级一级调用切换，形成了链。
     * 传统的只设置当前线程的方式不能满足此业务需求，必须模拟栈，后进先出。
     * </pre>
     */
    private static final ThreadLocal<LinkedBlockingDeque<String>> LOOKUP_KEY_HOLDER = new NamedThreadLocal<>("DynamicDataSourceLookupKey");

    private DynamicDataSourceContextHolder() {
    }

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String getDataSourceLookupKey() {
        LinkedBlockingDeque<String> deque = LOOKUP_KEY_HOLDER.get();
        if (deque == null) {
            return null;
        }
        return deque.isEmpty() ? null : deque.getFirst();
    }

    /**
     * 设置当前线程数据源
     *
     * @param dataSourceLookupKey 数据源名称
     */
    public static void setDataSourceLookupKey(String dataSourceLookupKey) {
        LinkedBlockingDeque<String> deque = LOOKUP_KEY_HOLDER.get();
        if (deque == null) {
            deque = new LinkedBlockingDeque<>();
            LOOKUP_KEY_HOLDER.set(deque);
        }
        deque.addFirst(dataSourceLookupKey);
    }

    /**
     * 清空当前线程数据源
     * <p>
     * 如果当前线程是连续切换数据源
     * 只会移除掉当前线程的数据源名称
     * </p>
     */
    public static void clearDataSourceLookupKey() {
        LinkedBlockingDeque<String> deque = LOOKUP_KEY_HOLDER.get();
        if (deque == null) {
            return;
        }
        if (deque.isEmpty()) {
            LOOKUP_KEY_HOLDER.remove();
        } else {
            deque.pollFirst();
            if (deque.isEmpty()) {
                LOOKUP_KEY_HOLDER.remove();
            }
        }
    }

    /**
     * 清除ThreadLocal变量(以免发生内存泄漏)
     */
    public static void remove() {
        LOOKUP_KEY_HOLDER.remove();
    }
}
