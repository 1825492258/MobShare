# MobShare
ShareSDk的分享以及发送短信

### ShareSDk分享
    http://wiki.mob.com/%e5%88%86%e4%ba%ab%e5%88%b0%e6%8c%87%e5%ae%9a%e5%b9%b3%e5%8f%b0/
        使用
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

### ShareSDK SMS短息验证
    http://wiki.mob.com/sms-android-%e6%97%a0gui%e6%8e%a5%e5%8f%a3%e8%b0%83%e7%94%a8/
         使用
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