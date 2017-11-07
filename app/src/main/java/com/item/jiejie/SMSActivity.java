package com.item.jiejie;

import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.ref.SoftReference;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class SMSActivity extends AppCompatActivity implements TextWatcher {

    private EditText edtPhone; // 输入的手机号
    private EditText edtCode; // 输入的验证码
    private Button btnCode; // 获取验证码
    private Button btnLogin; // 登录

    private MyHandler myHandler;
    private SMSContentObserver smsContentObserver; // 回调接口
    private MyCountDownTimer countDownTimer = new MyCountDownTimer(60000, 1000); // 计时器

    private static class MyHandler extends Handler {
        SoftReference<SMSActivity> mActivity;

        private MyHandler(SMSActivity activity) {
            mActivity = new SoftReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SMSActivity activity = mActivity.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case 1001: // 短信的返回
                    activity.edtCode.setText(msg.obj.toString());
                    break;
                case 1002: // 这个是EventHandler发送短信的返回
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE) { // 回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            // 提交验证码成功
                            Toast.makeText(activity, "提交验证码成功", Toast.LENGTH_SHORT).show();
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 获取验证码成功
                            Toast.makeText(activity, "验证码发送成功", Toast.LENGTH_SHORT).show();
                            activity.countDownTimer.start(); // 发送成功，开启倒计时
                          //  activity.setSmsContentObserver(); // 发送成功了，要开始读取短信了
                        }
                    } else {
                        // 报错了
                        try {
                            Throwable throwable = (Throwable) data;
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                Toast.makeText(activity, des, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 这里好像要加个动态的权限
     */
    private void setSmsContentObserver() {
        if (smsContentObserver == null) {
            smsContentObserver = new SMSContentObserver(SMSActivity.this, myHandler);
            getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContentObserver);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        edtPhone = (EditText) findViewById(R.id.edt_login_phone);
        edtCode = (EditText) findViewById(R.id.edt_login_code);
        btnCode = (Button) findViewById(R.id.btn_login_code);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCode.setEnabled(false);
        btnLogin.setEnabled(false);
        edtPhone.addTextChangedListener(this);
        myHandler = new MyHandler(this);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击获取验证码,获取短信目前支持国家列表,在监听中返回
                //getContentResolver().registerContentObserver();
                SMSSDK.getVerificationCode("86", edtPhone.getText().toString());
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 提交验证码 submitVerificationCode(String country, String phone, String code)
                SMSSDK.submitVerificationCode("86", edtPhone.getText().toString().trim(), edtCode.getText().toString().trim());
            }
        });
        EventHandler mEventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.what = 1002;
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                myHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(mEventHandler);
        edtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2 && edtPhone.getText().toString().length() >= 11) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        countDownTimer.cancel(); // 取消定时器
        if (smsContentObserver != null) {
            getContentResolver().unregisterContentObserver(smsContentObserver);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 11) {
            btnCode.setEnabled(true);
        } else {
            btnCode.setEnabled(false);
        }
    }

    /**
     * 定时器
     */
    private class MyCountDownTimer extends CountDownTimer {

        private MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            btnCode.setEnabled(false);
            btnCode.setText(l / 1000 + "s");
        }

        @Override
        public void onFinish() {
            btnCode.setEnabled(true);
            btnCode.setText("获取验证码");
            cancel();
        }
    }
}
