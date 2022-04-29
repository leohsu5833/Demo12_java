package com.example.demo12_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;
    private int counter = 0;
    private int counter2 = 0;
    private static final int SWITCH_LAYOUT = 888;
    private static final int ADD_COUNTER = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        setupCounter();
        setupCounter2();
        findViewById(R.id.button).setOnClickListener(v->{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.activity_main2);
                        }
                    });
                }
            }).start();
        });
        findViewById(R.id.button2).setOnClickListener(v -> new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(() -> setContentView(R.layout.activity_main2));
                }).start()
        );
        findViewById(R.id.button3).setOnClickListener(v->new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            handler.sendEmptyMessage(888);
            handler.sendEmptyMessage(SWITCH_LAYOUT);
        }).start());



    }
    private void setupCounter() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> textView1.setText(String.format("counter=%d", counter++)));

            }
        }).start();
    }

    private void setupCounter2() {
        new Thread(()->{
            while(true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(ADD_COUNTER);
            }
        }).start();
    }
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            setContentView(R.layout.activity_main2);
//        }
//    };

//    private Handler handler = new Handler(message -> {
//        setContentView(R.layout.activity_main2);
//        return false;
//    });

    private Handler handler = new Handler(message -> {
        switch(message.what){
            case SWITCH_LAYOUT:
                setContentView(R.layout.activity_main2);
            case ADD_COUNTER:
                textView2.setText(String.format("counter2=%d", counter2++));
        }
        return false;
    });



//    private Handler handler = new MyHandler(this);
//    private static class MyHandler extends Handler {
//        private WeakReference<Activity> activity = null;
//        public MyHandler(MainActivity mainActivity) {
//            activity = new WeakReference<Activity>(mainActivity);
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            activity.get().setContentView(R.layout.activity_main2);
//        }
//    }
}