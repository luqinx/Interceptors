package chao.android.tools.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author qinchao
 * @since 2019/4/14
 */
class InterceptorManager<T> implements InvocationHandler {

    private InterceptorBuilder<T> mBuilder;

    private T mT;

    InterceptorManager(InterceptorBuilder<T> builder) {
        mBuilder = builder;
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        ClassLoader classLoader = mBuilder.mClassLoader;
        if (classLoader == null) {
            if (mBuilder.mSource != null) {
                classLoader = mBuilder.mSource.getClass().getClassLoader();
            } else if (mBuilder.mSourceInterfaces.length > 0) {
                classLoader = mBuilder.mSourceInterfaces[0].getClassLoader();
            } else if (mBuilder.mInterfaces.length > 0) {
                classLoader = mBuilder.mInterfaces[0].getClassLoader();
            } else {
                classLoader = getClass().getClassLoader();
            }
        }
        if (mBuilder.mInterfaces == null) {
            mBuilder.mInterfaces = new Class[0];
        }
        int count = mBuilder.mInterfaces.length + mBuilder.mSourceInterfaces.length + 1;
        Class<?>[] interfaces = new Class<?>[count];
        interfaces[0] = IInterceptor.class;
        System.arraycopy(mBuilder.mInterfaces, 0, interfaces, 1, mBuilder.mInterfaces.length);
        System.arraycopy(mBuilder.mSourceInterfaces, 0, interfaces, 1 + mBuilder.mInterfaces.length, mBuilder.mSourceInterfaces.length);
        mT = (T) Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    T instance() {
        return mT;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (mBuilder.mBeforeListener != null) {
            result = mBuilder.mBeforeListener.onBeforeInterceptor(mBuilder.mSource, method, args);
        }
        if (mBuilder.mSource != null && !mBuilder.mIntercepted) {
            result = method.invoke(mBuilder.mSource, args);
        } else if (mBuilder.mInvoke != null) {
            result = mBuilder.mInvoke.onInvoke(mBuilder.mSource, method, args);
        }
        if (mBuilder.mAfterListener != null) {
            result = mBuilder.mAfterListener.onAfterInterceptor(mBuilder.mSource, method, args, result);
        }
        Class returnType = method.getReturnType();
        if (result == null) {
            if (Number.class.isAssignableFrom(returnType)) {
                if (Float.class.isAssignableFrom(returnType)) {
                    return 0.0f;
                } else if (Long.class.isAssignableFrom(returnType)) {
                    return 0L;
                } else if (Short.class.isAssignableFrom(returnType)) {
                    return (short) 0;
                } else if (Double.class.isAssignableFrom(returnType)) {
                    return 0.0;
                }
                return 0;
            } else if (boolean.class.isAssignableFrom(returnType)) {
                return false;
            } else if (Object.class.isAssignableFrom(returnType)) {
                return null;
            }
            return 0;
        }
        return result;
    }

    T getSourceListener() {
        return mBuilder.mSource;
    }
}
