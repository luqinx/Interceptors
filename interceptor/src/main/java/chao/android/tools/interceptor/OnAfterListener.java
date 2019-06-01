package chao.android.tools.interceptor;

import java.lang.reflect.Method;

/**
 * @author qinchao
 * @since 2019/4/15
 */
public interface OnAfterListener<T> {
    Object onAfterInterceptor(T source, Method method, Object[] args, Object result);
}
