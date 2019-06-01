package chao.android.tools.interceptor;

/**
 * @author qinchao
 * @since 2019/4/14
 */
public class InterceptorBuilder<T> {

    T mSource;

    Class<?>[] mSourceInterfaces;

    Class<?>[] mInterfaces;

    OnBeforeListener<T> mBeforeListener;

    OnInvoke<T> mInvoke;

    OnAfterListener<T> mAfterListener;

    boolean mIntercepted = false;

    ClassLoader mClassLoader;

    Action mAction;
    /**
     *
     * @param t 拦截对象
     *
     * @param tInterface 拦截对象类型, 用来限定泛型T的类型
     */
    @SuppressWarnings("unused")
    public InterceptorBuilder(T t, Class<T> tInterface) {
        mSource = t;
        if (t != null) {
            mSourceInterfaces = t.getClass().getInterfaces();
        } else if (tInterface != null) {
            mSourceInterfaces = new Class<?>[] {tInterface};
        } else {
            mSourceInterfaces = new Class<?>[0];
        }

        mInterfaces = new Class<?>[0];
    }

    public InterceptorBuilder<T> interfaces(Class<?>... interfaces) {
        mInterfaces = interfaces;
        return this;
    }

    public InterceptorBuilder<T> intercepted(boolean intercepted) {
        mIntercepted = intercepted;
        return this;
    }

    public InterceptorBuilder<T> classLoader(ClassLoader classLoader) {
        mClassLoader = classLoader;
        return this;
    }

    public InterceptorBuilder<T> before(OnBeforeListener beforeListener) {
        mBeforeListener = beforeListener;
        return this;
    }

    public InterceptorBuilder<T> after(OnAfterListener afterListener) {
        mAfterListener = afterListener;
        return this;
    }

    public InterceptorBuilder<T> invoke(OnInvoke<T> invokeListener) {
        mInvoke = invokeListener;
        return this;
    }

    public T newInstance() {
        return new InterceptorManager<>(this).instance();
    }

    public void action(Action<T> action) {
        action.onAction(newInstance());
    }
}
