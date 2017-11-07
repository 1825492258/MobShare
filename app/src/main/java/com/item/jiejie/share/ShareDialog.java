package com.item.jiejie.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.item.jiejie.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * 这是分享弹窗
 * Created by Administrator on 2017/8/8.
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private DisplayMetrics dm;

    /**
     * UI
     */
    private TextView mDialogOne;
    private TextView mDialogTwo;
    private TextView mDialogThree;
    private TextView mDialogFour;

    public ShareDialog(Context context) {
        super(context, R.style.SheetDialogStyle);
        mContext = context;
        dm = mContext.getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        initView();
    }

    private void initView() {
        /**
         * 通过获得的Dialog的Window来控制Dialog的宽高及位置
         */
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dm.widthPixels;//设置宽度
        dialogWindow.setAttributes(lp);
        mDialogOne = (TextView) findViewById(R.id.dialog_item_one);
        mDialogTwo = (TextView) findViewById(R.id.dialog_item_two);
        mDialogThree = (TextView) findViewById(R.id.dialog_item_three);
        mDialogFour = (TextView) findViewById(R.id.dialog_item_four);
        mDialogOne.setOnClickListener(this);
        mDialogTwo.setOnClickListener(this);
        mDialogThree.setOnClickListener(this);
        mDialogFour.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_item_one: // 分享微信
                shareData(Wechat.NAME);
                break;
            case R.id.dialog_item_two: // 微信朋友圈
                shareData(WechatMoments.NAME);
                break;
            case R.id.dialog_item_three: // QQ
                shareData(QQ.NAME);
                break;
            case R.id.dialog_item_four: // QQ空间
                shareData(QZone.NAME);
                break;
        }
        dismiss();
    }

    private PlatformActionListener mListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.d("jiejie", "onComplete");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.d("jiejie", "onError");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Log.d("jiejie", "onCancel");
        }
    };

    private void shareData(String name) {
        Platform.ShareParams params = new Platform.ShareParams();
        params.setTitle("测试分享的标题");
        params.setTitleUrl("https://www.baidu.com/"); // 标题的超链接
        params.setText("测试的分享文本啊啊啊啊啊啊啊啊啊啊啊"); // text是分享文本
        params.setUrl("http://sharesdk.cn"); // url仅在微信（包括好友和朋友圈）中使用
        params.setImageUrl("https://qlogo4.store.qq.com/qzone/2524921779/2524921779/100"); //分享网络图片
        params.setSite("发布分享的网站名称"); // site是分享此内容的网站名称，仅在QQ空间使用
        params.setSiteUrl("https://github.com/"); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        Platform platform = ShareSDK.getPlatform(name);
        platform.setPlatformActionListener(mListener);
        platform.share(params);
    }
}
