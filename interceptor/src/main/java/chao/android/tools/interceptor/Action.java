package chao.android.tools.interceptor;


/**
 * @author qinchao
 * @since 2019/4/16
 */
public interface Action<T> {
    void onAction(T t);
}
