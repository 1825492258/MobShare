package com.item.jiejie.share;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.item.jiejie.R;

public class ShareActivity extends AppCompatActivity {

    ShareDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        findViewById(R.id.btn_share_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击弹出一个弹出 用来展示分享的奥
                if (dialog == null) {
                    dialog = new ShareDialog(ShareActivity.this);
                }
                dialog.show();
            }
        });
    }
}
