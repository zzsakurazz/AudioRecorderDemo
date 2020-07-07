package com.ole.driver.recorderlib.recorder;

import android.media.MediaRecorder;


import com.ole.driver.recorderlib.listener.RecordResultListener;
import com.ole.driver.recorderlib.listener.RecordStateListener;

import java.io.File;
import java.io.IOException;

/**
 * @author zhangzheng
 * @Date 2020/7/7 10:57 AM
 * @ClassName MediaRecordService
 * <p>
 * Desc :
 */
public class MediaRecorderUtils {
    /**
     * 录音MediaRecorder
     */
    private MediaRecorder mMediaRecorder;
    /**
     * 已完成录音路径
     */
    private String path;
    /**
     * 输出录音文件
     */
    private File mFile;
    /**
     * 录音配置
     */
    private RecordConfig mCurrentConfig;
    /**
     * 当前录音状态
     */
    private volatile RecordState state = RecordState.IDLE;

    /**
     * 录音状态回调
     */
    private RecordStateListener mRecordStateListener;

    /**
     * 录音结果回调
     */
    private RecordResultListener mRecordResultListener;

    public MediaRecorderUtils() {
        mCurrentConfig = new RecordConfig();
    }

    private void initMediaRecorder() {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        state = RecordState.IDLE;
        // 设置音频来源MIC
        mMediaRecorder.setAudioSource(mCurrentConfig.getSourceConfig());
        // 设置默认音频输出格式
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
        // 设置默认音频编码方式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
    }


    /**
     * 获取录音文件路径
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * 开始录音
     */
    public void start() {
        if (mMediaRecorder == null) {
            initMediaRecorder();
        }
        if (state != RecordState.IDLE && state != RecordState.STOP) {
            if (mRecordStateListener != null) {
                mRecordStateListener.onError((String.format("状态异常当前状态:%s", state.name())), ErrorCode.STATE_ERROR);
            }
            return;
        }
        try {
            mFile = new File(mCurrentConfig.getRecordDir(), mCurrentConfig.getFileName() + mCurrentConfig.getFormat().getExtension());
            if (!mFile.exists()) {
                mFile.createNewFile();
            }
            path = mFile.getAbsolutePath();
            //指定音频输出文件路径
            mMediaRecorder.setOutputFile(mFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();  //开始录制
            state = RecordState.RECORDING;
            //开始录制
            if (mRecordStateListener != null) {
                mRecordStateListener.onStateChange(state);
            }
        } catch (IOException e) {
            if (mRecordStateListener != null) {
                mRecordStateListener.onError(e.toString(), ErrorCode.RECORD_ERROR);
            }
            e.printStackTrace();
        } catch (Exception e) {
            if (mRecordStateListener != null) {
                mRecordStateListener.onError(e.toString(), ErrorCode.FILE_ERROR);
            }
            e.printStackTrace();
        }
    }

    /**
     * 结束录音
     */
    public void stop() {
        if (mMediaRecorder != null && state == RecordState.RECORDING) {
            state = RecordState.STOP;
            if (mRecordStateListener != null) {
                mRecordStateListener.onStateChange(state);
            }
            if (mRecordResultListener != null) {
                mRecordResultListener.onResult(mFile);
            }
            mMediaRecorder.stop();
            mMediaRecorder.release();
            state = RecordState.IDLE;
            mMediaRecorder = null;

        } else {
            if (mRecordStateListener != null) {
                mRecordStateListener.onError((String.format("状态异常当前状态:%s", state.name())), ErrorCode.STATE_ERROR);
            }
        }
    }

    /**
     * 必须在onDestroy调用此方法，否则会消耗资源
     */
    public void release() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
        }
        mFile = null;
        mMediaRecorder = null;
    }


    public void setRecordStateListener(RecordStateListener mMediaRecorderCallBack) {
        this.mRecordStateListener = mMediaRecorderCallBack;
    }

    public void setRecordResultListener(RecordResultListener mRecordResultListener) {
        this.mRecordResultListener = mRecordResultListener;
    }

    public boolean changeRecordConfig(RecordConfig recordConfig) {
        if (state == RecordState.IDLE) {
            mCurrentConfig = recordConfig;
            return true;
        }
        return false;

    }

    public RecordState getState() {
        return state;
    }

    public void changeRecordDir(String recordDir) {
        mCurrentConfig.setRecordDir(recordDir);
    }

    public void changeFileName(String fileName) {
        mCurrentConfig.setFileName(fileName);
    }

    public void changeSource(int source) {
        mCurrentConfig.setSource(source);
    }
}
