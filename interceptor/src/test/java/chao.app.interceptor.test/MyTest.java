package chao.app.interceptor.test;

import chao.app.interceptor.test.merterial.OnTempListener;
import com.android.tools.testsuit.Checker;
import com.android.tools.testsuit.internal.Sample;
import com.android.tools.testsuit.internal.SampleCase;
import org.junit.Test;

/**
 * @author qinchao
 * @since 2019/4/29
 */
public class MyTest extends SampleCase {

    private OnTempListener tempListener = new OnTempListener() {
        @Override
        public void onTemp() {

        }
    };

    @Test
    public void testInterceptorOf() {
        assertInstanceOf(tempListener, OnTempListener.class);
        assertInstanceOf(new Checker.Builder(""), Checker.Builder.class);

        Sample.of(tempListener).instanceOf(OnTempListener.class).and.notNull().asserts();
    }
}
