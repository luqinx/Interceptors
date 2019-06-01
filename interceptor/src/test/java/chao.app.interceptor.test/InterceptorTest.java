package chao.app.interceptor.test;

import chao.android.tools.interceptor.Action;
import chao.android.tools.interceptor.Interceptor;
import chao.android.tools.interceptor.OnAfterListener;
import chao.android.tools.interceptor.OnBeforeListener;
import chao.android.tools.interceptor.OnInvoke;
import chao.app.interceptor.test.merterial.OnTempListener;
import chao.app.interceptor.test.merterial.OnTestListener;
import com.android.tools.testsuit.Checker;
import com.android.tools.testsuit.CheckerManager;
import java.io.Serializable;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author qinchao
 * @since 2019/4/14
 */
public class InterceptorTest {

    private OnTestListener testListener;

    private Listener listener;

    private class Listener implements OnTestListener, OnTempListener {

        @Override
        public void onTemp() {
            print("Listener: onTemp");
        }

        @Override
        public void onTest() {
            print("Listener: onTest");
        }

        @Override
        public void onTest(int i) {

        }

        @Override
        public void onTest(int i, String s) {

        }

        @Override
        public int onTestRi() {
            return 0;
        }

        @Override
        public String onTestRs() {
            return null;
        }
    }

    @Before
    public void init() {
        testListener = new OnTestListener() {
            @Override
            public void onTest() {
                Checker checker = CheckerManager.checkBuilder().build();
                checker.shouldNotCall();
                print("onTest");
            }

            @Override
            public void onTest(int i) {
                print("onTest(i)");
            }

            @Override
            public void onTest(int i, String s) {
                print("onTest(i,s)");
            }

            @Override
            public int onTestRi() {
                print("onTestRi");
                return 0;
            }

            @Override
            public String onTestRs() {
                final Checker checker = CheckerManager
//                    .checkBuilder()
                    .checkBuilder("xxx")
                    .build();
                checker.shouldCall(2);
                return "onTestRs";
            }
        };

        listener = new Listener();

    }

    @Test
    public void testOf() {

        OnTempListener l = Interceptor.of(listener).newInstance();
        l.onTemp();

        OnTestListener t = Interceptor.of(listener).newInstance();
        t.onTest();

        Assert.assertNotNull(Interceptor.of(listener).newInstance());

        Assert.assertNotNull(Interceptor.of(listener, OnTempListener.class).newInstance());

        Assert.assertNotNull(Interceptor.of(listener, OnTestListener.class).newInstance());

        Assert.assertTrue(Interceptor.of(listener, OnTempListener.class).newInstance() instanceof OnTestListener);

        Assert.assertTrue(Interceptor.of(listener, OnTestListener.class).newInstance() instanceof OnTempListener);

        Assert.assertNotNull(Interceptor.of(null).newInstance());

    }

    @Test
    public void testListener() {
        final Checker checker = CheckerManager
            .checkBuilder("xxx")
            .withPriority()
            .build();
        OnTestListener t = Interceptor.of(testListener).before(new OnBeforeListener() {
            @Override
            public Object onBeforeInterceptor(Object proxy, Method method, Object[] args) {
                checker.shouldCall(1);
                return null;
            }
        }).after(new OnAfterListener() {
            @Override
            public Object onAfterInterceptor(Object proxy, Method method, Object[] args, Object result) {
                checker.shouldCall(3);
                return result;
            }
        }).newInstance();
        t.onTestRs();
        checker.check(3);


        final Checker checker2 = checker.reset();
        t = Interceptor.of(testListener).before(new OnBeforeListener() {
            @Override
            public Object onBeforeInterceptor(Object proxy, Method method, Object[] args) {
                checker2.shouldCall(1);
                return null;
            }
        }).after(new OnAfterListener() {
            @Override
            public Object onAfterInterceptor(Object proxy, Method method, Object[] args, Object result) {
                checker2.shouldCall(2);
                return result;
            }
        }).intercepted(true)
            .newInstance();
        t.onTest();
        checker2.check(2);
    }

    @Test
    public void testIsProxy() {
        OnTempListener t = Interceptor.of(listener).newInstance();
        Assert.assertTrue(Interceptor.isProxyInstance(t));

        Assert.assertFalse(Interceptor.isProxyInstance(null));

        Assert.assertFalse(Interceptor.isProxyInstance(listener));
    }

    @Test
    public void testGetSourceListener() {
        OnTempListener t = Interceptor.of(listener).newInstance();
        Assert.assertSame(Interceptor.getSourceListener(t), listener);

        Assert.assertNull(Interceptor.getSourceListener(null));

        Assert.assertNull(Interceptor.getSourceListener(listener));
    }

    @Test
    public void testInterfaces() {
        OnTempListener l = Interceptor.of(listener, OnTempListener.class).interfaces(Serializable.class).newInstance();
        Assert.assertTrue(l instanceof Serializable);
        Assert.assertTrue(l instanceof OnTestListener);
    }

    @Test
    public void testAction() {
        final Checker checker = CheckerManager.checkBuilder().build();
        Interceptor.of(listener, OnTempListener.class).interfaces(Serializable.class).action(new Action<OnTempListener>() {
            @Override
            public void onAction(OnTempListener onTempListener) {
                Assert.assertTrue(onTempListener instanceof Serializable);
                Assert.assertTrue(onTempListener instanceof OnTestListener);
                checker.shouldCall();
            }
        });
        checker.check();
    }

    @Test
    public void testInvoke() {
        final Checker checker = CheckerManager.checkBuilder().build();
        Interceptor.of(testListener).intercepted(true).invoke(new OnInvoke<OnTestListener>() {
            @Override
            public Object onInvoke(OnTestListener source, Method method, Object[] args) {
                checker.shouldCall();
                return null;
            }
        }).newInstance().onTest();

        checker.check();
    }

    private void print(String message) {
        System.out.println(message);
    }
}
