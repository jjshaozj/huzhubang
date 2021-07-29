package com.a8plus1.seen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //postDelayed延迟响应函数,参数为runnable接口类和延迟时间
                final Intent mainIntent = new Intent(StartActivity.this,MainActivity.class);
                StartActivity.this.startActivity(mainIntent);   //响应意图
                StartActivity.this.finish();              //结束
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                //overridePendingTransition必须在startActivity()或者 finish()方法的后面。
            }
        }, 1000);
    }
}
