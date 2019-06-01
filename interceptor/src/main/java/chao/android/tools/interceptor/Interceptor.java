package chao.android.tools.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Interceptor {

    private Interceptor() {}

    public static <T> InterceptorBuilder<T> of(T t) {
        return new InterceptorBuilder<>(t, null);
    }

    public static <T> InterceptorBuilder<T> of(T t, Class<T> tClass) {
        return new InterceptorBuilder<>(t, tClass);
    }


    public static boolean isProxyInstance(Object object) {
        return object instanceof IInterceptor && Proxy.isProxyClass(object.getClass()) ;
    }

    public static <T> T getSourceListener(Object obj) {
        if (obj == null) {
            return null;
        }
        InterceptorManager manager;
        if (Proxy.isProxyClass(obj.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(obj);
            if (handler instanceof InterceptorManager) {
                manager = (InterceptorManager) handler;
                return (T) manager.getSourceListener();
            }
        }
        return null;
    }
}
