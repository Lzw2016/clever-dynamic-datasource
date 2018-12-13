package org.clever.dynamic.datasource.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-12-13 21:07 <br/>
 */
@Slf4j
public class ThreadLocalRemoveInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        DynamicDataSourceContextHolder.remove();
        log.debug("清除 ThreadLocal DynamicDataSourceLookupKey");
    }
}
