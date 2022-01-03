package com.luqinx.interceptor;

import java.lang.reflect.Method;

/**
 * @author qinchao
 * @since 2019/5/6
 */
public interface OnInvoke<T> {
    Object onInvoke(T source, Method method, Object[] args);
}
