package com.ole.driver.recorder;

import android.app.Application;

import com.ole.driver.recorderlib.RecordManager;

/**
 * @author zhangzheng
 * @Date 2020-02-21 15:24
 * @ClassName App
 * <p>
 * Desc :
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 参数1： Application 实例
         * 参数2： 是否打印日志
         */
        RecordManager.getInstance().init(this);
    }
}
