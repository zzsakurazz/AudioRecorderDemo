package com.ole.driver.recorderlib.recorder;

import android.app.Application;

import com.ole.driver.recorderlib.listener.RecordResultListener;
import com.ole.driver.recorderlib.listener.RecordStateListener;

import ola.com.travel.log.OLELogManager;
import ola.com.travel.log.conofig.OLELoggerConfig;


/**
 * @author ole on 2018/7/10.
 */
public class RecordManager {
    private volatile static RecordManager instance;
    private MediaRecorderUtils mediaRecorderUtils;

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
     */
    public void init(Application application) {
        mediaRecorderUtils = new MediaRecorderUtils();
        OLELogManager.getInstance().init(application, new OLELoggerConfig.Builder()
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

    public void start() {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.start();
        }
    }

    public void stop() {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.stop();
        }
    }

    public void release() {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.release();
        }
    }


    /**
     * 录音状态监听回调
     */
    public void setRecordStateListener(RecordStateListener listener) {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.setRecordStateListener(listener);
        }
    }


    /**
     * 录音文件转换结束回调
     */
    public void setRecordResultListener(RecordResultListener listener) {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.setRecordResultListener(listener);
        }
    }


    public boolean changeRecordConfig(RecordConfig recordConfig) {
        if (mediaRecorderUtils != null) {
            return mediaRecorderUtils.changeRecordConfig(recordConfig);
        } else {
            return false;
        }
    }


    /**
     * 修改录音文件存放路径
     */
    public void changeRecordDir(String recordDir) {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.changeRecordDir(recordDir);
        }
    }

    /**
     * 修改录音文件名
     */
    public void changeFileName(String fileName) {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.changeFileName(fileName);
        }
    }

    /**
     * 修改音源
     */
    public void changeSource(int source) {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.changeSource(source);
        }
    }


    /**
     * 获取当前的录音状态
     *
     * @return 状态
     */
    public RecordState getState() {
        if (mediaRecorderUtils != null) {
            return mediaRecorderUtils.getState();
        }
        return RecordState.ERROR;
    }

}
