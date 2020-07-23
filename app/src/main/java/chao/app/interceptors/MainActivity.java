package chao.app.interceptors;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import chao.android.tools.interceptor.Interceptor;

/**
 * @author luqin  qinchao@mochongsoft.com
 * @project: zmjx-Interceptors
 * @description:
 * @date 2019-07-03
 */
public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        I1 i1 = Interceptor.of(null, I1.class).intercepted(true).newInstance();

        I1 i11 = Interceptor.of(i1).intercepted(true).newInstance();
    }
}
