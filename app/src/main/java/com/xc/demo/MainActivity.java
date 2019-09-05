package com.xc.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xc.framework.https.HttpParam;
import com.xc.framework.https.interfaces.RequestCallback;
import com.xc.framework.utils.XCHttpUtil;
import com.xc.framework.utils.XCStringUtil;

public class MainActivity extends AppCompatActivity {
    static final int HTTP_RESULT = 0x123;

    RequestCallback requestCallback = new RequestCallback() {
        @Override
        public void onResult(int what, String result) {
            switch (what) {
                case HTTP_RESULT:
                    if (!XCStringUtil.isEmpty(result)) {

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        String url = "http://m.test.com.cn/ispushcode.php";
        HttpParam params = new HttpParam(url);
        params.addGetParam("key", "value");
        params.addPostParam("key", "value");
        XCHttpUtil.sendRequest(this, params, HTTP_RESULT, requestCallback);
    }

}
