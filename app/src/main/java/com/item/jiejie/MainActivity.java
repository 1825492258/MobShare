package com.item.jiejie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.item.jiejie.share.ShareActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_main_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 调转到发送短信的界面
                startActivity(new Intent(MainActivity.this, SMSActivity.class));
            }
        });
        findViewById(R.id.btn_main_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 调转到分享
                startActivity(new Intent(MainActivity.this, ShareActivity.class));
            }
        });
    }
}
