package com.example.whacmole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    public static final int CODE = 123;
    public static final int RANDOM_NUMBER = 500;
    private TextView mResultTextView;
    private ImageView mDiglettImageView;
    private Button mStartButton;

    //地鼠显示的坐标
    public int[][] mPosition = new int[][]{
            {342, 180}, {432, 880},
            {521, 256}, {429, 780},
            {456, 976}, {145, 665},
            {123, 678}, {564, 567},
    };

    //出现地鼠个数
    private int mTotalCount = 0;
    //打到地鼠数量
    private int mSuccessCount = 0;
    //设置需要打的地鼠数量
    public static final int MAX_COUNT = 10;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE:
                    if (mTotalCount > MAX_COUNT) {
                        clear();
                        Toast.makeText(MainActivity.this,"地鼠打完了",Toast.LENGTH_SHORT).show();
                        return;
                    }
                        //获取到地鼠位置
                        int position = msg.arg1;
                        mDiglettImageView.setX(mPosition[position][0]);
                        mDiglettImageView.setY(mPosition[position][1]);
                        mDiglettImageView.setVisibility(View.VISIBLE);

                        //显示下一个地鼠的延迟时间
                        int randomTime = new Random().nextInt(500) + RANDOM_NUMBER;
                        next(randomTime);

                    break;
            }
        }
    };

    private void clear() {
        mTotalCount = 0;
        mSuccessCount = 0;
        mDiglettImageView.setVisibility(View.GONE);
        mStartButton.setText("点击开始");
        mStartButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mResultTextView = (TextView) findViewById(R.id.text_view);
        mDiglettImageView = (ImageView) findViewById(R.id.image_view);
        mStartButton = (Button) findViewById(R.id.start_button);

        mStartButton.setOnClickListener(this);
        mDiglettImageView.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                start();
                break;
        }

    }

    //开始游戏
    private void start() {
        mResultTextView.setText("开始了");
        mStartButton.setText("游戏中");
        mStartButton.setEnabled(false);
        //利用Handler 发送延迟消息
        next(0);
    }

    //打完这个地鼠之后 还会有下一个地鼠
    private void next(int delayTime) {
        int position = new Random().nextInt(mPosition.length);

        Message message = new Message();
        message.what = CODE;
        //发送消息带数据  发送 地鼠将要出现的坐标位置
        message.arg1 = position;
        mHandler.sendMessageDelayed(message, delayTime);
        mTotalCount ++;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //打中地鼠逻辑
        v.setVisibility(View.GONE);
        mSuccessCount ++;
        mResultTextView.setText("打到了"+ mSuccessCount +"只,共" + MAX_COUNT +"只.");
        return false;
    }
}
