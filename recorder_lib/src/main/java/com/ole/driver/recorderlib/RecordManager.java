package com.ole.driver.recorderlib;


import android.annotation.SuppressLint;
import android.app.Application;

import com.ole.driver.recorderlib.recorder.RecordConfig;
import com.ole.driver.recorderlib.recorder.RecordHelper;
import com.ole.driver.recorderlib.recorder.RecordService;
import com.ole.driver.recorderlib.recorder.listener.RecordDataListener;
import com.ole.driver.recorderlib.recorder.listener.RecordFftDataListener;
import com.ole.driver.recorderlib.recorder.listener.RecordResultListener;
import com.ole.driver.recorderlib.recorder.listener.RecordSoundSizeListener;
import com.ole.driver.recorderlib.recorder.listener.RecordStateListener;

import ola.com.travel.log.OLELogManager;
import ola.com.travel.log.conofig.OLELoggerConfig;
import ola.com.travel.log.logger.Logger;


/**
 * @author ole on 2018/7/10.
 */
public class RecordManager {
    @SuppressLint("StaticFieldLeak")
    private volatile static RecordManager instance;
    private Application context;

    private RecordManager() {
    }

    public static RecordManager getInstance() {
        if (instance == null) {
            synchronized (RecordManager.class) {
                if (instance == null) {
                    instance = new RecordManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param application Application
     * @param showLog     是否开启日志
     */
    public void init(Application application, boolean showLog) {
        this.context = application;
        OLELogManager.init(application, new OLELoggerConfig.Builder()
                //是否debug模式 默认true
                .isDebug(showLog)
                //是否展示线程信息
                .isThreadInfo(false)
                //方法深度 打印多少行
                .methodCount(1)
                //方法偏移量
                .methodOffset(1)
                //tag标识
                .tag("Audio_Recorder")
                .build());
    }

    public void init(Application application) {
        init(application, false);
    }

    public void start() {
        if (context == null) {
            Logger.e(  "未进行初始化");
            return;
        }
        Logger.i(  "start...");
        RecordService.startRecording(context);
    }

    public void stop() {
        if (context == null) {
            return;
        }
        RecordService.stopRecording(context);
    }

    public void resume() {
        if (context == null) {
            return;
        }
        RecordService.resumeRecording(context);
    }

    public void pause() {
        if (context == null) {
            return;
        }
        RecordService.pauseRecording(context);
    }

    /**
     * 录音状态监听回调
     */
    public void setRecordStateListener(RecordStateListener listener) {
        RecordService.setRecordStateListener(listener);
    }

    /**
     * 录音数据监听回调
     */
    public void setRecordDataListener(RecordDataListener listener) {
        RecordService.setRecordDataListener(listener);
    }

    /**
     * 录音可视化数据回调，傅里叶转换后的频域数据
     */
    public void setRecordFftDataListener(RecordFftDataListener recordFftDataListener) {
        RecordService.setRecordFftDataListener(recordFftDataListener);
    }

    /**
     * 录音文件转换结束回调
     */
    public void setRecordResultListener(RecordResultListener listener) {
        RecordService.setRecordResultListener(listener);
    }

    /**
     * 录音音量监听回调
     */
    public void setRecordSoundSizeListener(RecordSoundSizeListener listener) {
        RecordService.setRecordSoundSizeListener(listener);
    }


    public boolean changeFormat(RecordConfig.RecordFormat recordFormat) {
        return RecordService.changeFormat(recordFormat);
    }


    public boolean changeRecordConfig(RecordConfig recordConfig) {
        return RecordService.changeRecordConfig(recordConfig);
    }

    public RecordConfig getRecordConfig() {
        return RecordService.getRecordConfig();
    }

    /**
     * 修改录音文件存放路径
     */
    public void changeRecordDir(String recordDir) {
        RecordService.changeRecordDir(recordDir);
    }   /**
     * 修改录音文件存放路径
     */
    public void changeSource(int source) {
        RecordService.changeSource(source);
    }

    /**
     * 获取当前的录音状态
     *
     * @return 状态
     */
    public RecordHelper.RecordState getState() {
        return RecordService.getState();
    }

}
