package com.item.jiejie;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuzongjie on 2017/11/7.
 * 短信验证码截取
 */

public class SMSContentObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;
    private String code;

    public SMSContentObserver(Context context, Handler handler) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
    }

    /**
     * 回调函数，当所监听的Uri发生变化时，就会回调此方法
     * 注意当收到短信的时候会回调2次
     *
     * @param selfChange 此值意义不大 一般情况下该回调值false
     */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        // 第一次回调不是我们想要的  直接返回
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
        // 第二次回调 查询收件箱内容
        Uri inboxUri = Uri.parse("content://sms/inbox");
        // 按时间顺序短信数据库
        Cursor c = mContext.getContentResolver().query(inboxUri,
                null,
                null,
                null,
                "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                // 获取发送方手机号
                String address = c.getString(c.getColumnIndex("address"));
                // 获取短信内容
                String body = c.getString(c.getColumnIndex("body"));
                // 正则表达式截取短信中的4位验证码
                Pattern pattern = Pattern.compile("(\\d{4})");
                Matcher matcher = pattern.matcher(body);
                // 如果找到通过Handler发送给主线程
                if (matcher.find()) {
                    code = matcher.group(0);
                    mHandler.obtainMessage(1001, code).sendToTarget();
                }
            }
        }
        c.close();
    }
}
