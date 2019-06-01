package chao.app.interceptor.test.merterial;

/**
 * @author qinchao
 * @since 2019/4/14
 */
public interface OnTestListener {
    void onTest();
    void onTest(int i);
    void onTest(int i, String s);
    int onTestRi();
    String onTestRs();
}
