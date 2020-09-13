package com.dong.antest1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    private TextView mTextView;
    private final int MSG_1 = 1001;
    private final int MSG_2 = 1002;
    private Button mBtn;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
            case MSG_1:
                mTextView.setText("aaa");
                mTextView.invalidate();
                break;
            case MSG_2:
                mTextView.setText("bbb");
                mTextView.invalidate();
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
        mTextView = (TextView) findViewById(R.id.title_view);
        mBtn = (Button) findViewById(R.id.btn);
        mBtn.setOnClickListener(this);
    }

    private void update1() {
        mHandler.sendEmptyMessage(MSG_1);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update2() {
        mHandler.sendEmptyMessage(MSG_2);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void update() {
        mHandler.sendEmptyMessage(MSG_1);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("dong", "aaa");
        mHandler.sendEmptyMessage(MSG_2);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("dong", "bbb");
    }

    @Override
    public void onClick(View view) {
        update();
    }
}
