package com.luqinx.interceptor;

import java.lang.reflect.Method;

/**
 * @author qinchao
 * @since 2019/4/15
 */
public interface OnBeforeListener<T> {
    Object onBeforeInterceptor(T source, Method method, Object[] args);
}
