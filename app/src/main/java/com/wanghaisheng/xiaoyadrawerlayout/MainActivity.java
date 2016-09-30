package com.wanghaisheng.xiaoyadrawerlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wanghaisheng.view.drawerlayout.SlidingView;

public class MainActivity extends AppCompatActivity {

    SlidingView mSlidingView;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlidingView = (SlidingView) findViewById(R.id.slidingview);
        mTextView = (TextView) findViewById(R.id.tv_toggle);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingView.toggleMenu();
            }
        });


    }
}
