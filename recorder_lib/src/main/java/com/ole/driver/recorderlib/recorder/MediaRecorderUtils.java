package com.ole.driver.recorderlib.recorder;

import android.media.MediaRecorder;
import android.util.Log;


import com.ole.driver.recorderlib.listener.RecordInfoListener;
import com.ole.driver.recorderlib.listener.RecordResultListener;
import com.ole.driver.recorderlib.listener.RecordStateListener;
import com.ole.driver.recorderlib.utils.FileUtils;

import java.io.File;
import java.io.IOException;


/**
 * @author zhangzheng
 * @Date 2020/7/7 10:57 AM
 * @ClassName MediaRecorderUtils
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
    /**
     * 录音结果回调
     */
    private RecordInfoListener mRecordInfoListener;

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
        if (mCurrentConfig.getMaxFileSize() > 0) {
            mMediaRecorder.setMaxFileSize(mCurrentConfig.getMaxFileSize());
        }
        if (mCurrentConfig.getMaxRecordDuration() > 0) {
            mMediaRecorder.setMaxDuration(mCurrentConfig.getMaxRecordDuration());
        }
        if (mCurrentConfig.getFormat() == RecordFormat.MP3) {
            // 设置默认音频输出格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置设置音频编码器
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        } else if (mCurrentConfig.getFormat() == RecordFormat.AMR) {
            // 设置默认音频输出格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            // 设置设置音频编码器
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        }
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
            String mFilePath = FileUtils.getFilePath(mCurrentConfig.getRecordDir(), mCurrentConfig.getFileName(), mCurrentConfig.getFormat().getExtension());
            if (mFilePath == null) {
                if (mRecordStateListener != null) {
                    mRecordStateListener.onError("文件创建失败，目的地址：" + mCurrentConfig.getRecordDir() + mCurrentConfig.getFileName() + mCurrentConfig.getFormat().getExtension(), ErrorCode.RECORD_ERROR);
                }
            }
            mFile = new File(mFilePath);
            if (!mFile.exists()) {
                mFile.createNewFile();
            }
            path = mFile.getAbsolutePath();
            //指定音频输出文件路径
            mMediaRecorder.setOutputFile(mFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {
                        //到达最大文件大小
                        Log.e("zz", "文件大小到啦~~");
                        if (mRecordInfoListener != null) {
                            mRecordInfoListener.onInfo(what);
                        } else {
                            stop();
                        }
                    } else if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        //到达最大时间限制
                        Log.e("zz", "时间长度到啦~~");
                        if (mRecordInfoListener != null) {
                            mRecordInfoListener.onInfo(what);
                        } else {
                            stop();
                        }
                    }
                }
            });
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
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mRecordResultListener != null) {
                mRecordResultListener.onResult(mFile);
            }
            state = RecordState.IDLE;
            release();
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
            if (state == RecordState.RECORDING) {
                mMediaRecorder.stop();
            }
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

    public void setRecordInfoListener(RecordInfoListener mRecordInfoListener) {
        this.mRecordInfoListener = mRecordInfoListener;
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

    public void changeFormat(RecordFormat format) {
        mCurrentConfig.setFormat(format);
    }

    public void changeSource(int source) {
        mCurrentConfig.setSource(source);
    }

    public void changeFileSize(long maxSize) {
        mCurrentConfig.setMaxFileSize(maxSize);
    }

    public void changeRecordTime(int maxDuration) {
        mCurrentConfig.setMaxRecordDuration(maxDuration);
    }
}
