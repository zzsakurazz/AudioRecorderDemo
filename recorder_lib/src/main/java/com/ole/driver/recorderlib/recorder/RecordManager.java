package com.ole.driver.recorderlib.recorder;


import com.ole.driver.recorderlib.listener.RecordInfoListener;
import com.ole.driver.recorderlib.listener.RecordResultListener;
import com.ole.driver.recorderlib.listener.RecordStateListener;


public class RecordManager {
    private volatile static RecordManager instance;
    private MediaRecorderUtils mediaRecorderUtils;

    private RecordManager() {
        mediaRecorderUtils = new MediaRecorderUtils();
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
     * 录音状态监听回调
     */
    public void setRecordInfoListener(RecordInfoListener listener) {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.setRecordInfoListener(listener);
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
     * 修改录音文件名
     */
    public void changeFormat(RecordFormat format) {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.changeFormat(format);
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
     * 修改文件最大大小
     */
    public void changeMaxFIleSize(long maxSize) {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.changeFileSize(maxSize);
        }
    }

    /**
     * 修改文件最大时间长度
     */
    public void changeMaxRecordTime(int maxDuration) {
        if (mediaRecorderUtils != null) {
            mediaRecorderUtils.changeRecordTime(maxDuration);
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
