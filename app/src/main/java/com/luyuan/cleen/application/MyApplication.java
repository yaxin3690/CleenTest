package com.luyuan.cleen.application;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;

/**
 * Author: Leaf
 * Date：2016/12/22
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this, new NoHttp.Config()
                // 设置全局连接超时时间，单位毫秒
                .setConnectTimeout(30 * 1000)
                // 设置全局服务器响应超时时间，单位毫秒
                .setReadTimeout(30 * 1000)
        );
    }
}
