package com.ole.driver.recorderlib.recorder;

import android.app.Notification;
import android.media.MediaRecorder;
import android.os.Environment;

import com.ole.driver.recorderlib.utils.FileUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author ole on 2018/7/11.
 */
public class RecordConfig implements Serializable {
    /**
     * 录音格式
     */
    private RecordFormat format = RecordFormat.MP3;

    /**
     * 音源
     */
    private int sourceConfig = MediaRecorder.AudioSource.MIC;

    /**
     * 最大文件大小 单位字节
     */
    private long maxFileSize = 0L;
    /**
     * 最长文件时间 单位毫秒
     */
    private int maxRecordDuration = 0;

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public int getMaxRecordDuration() {
        return maxRecordDuration;
    }

    public void setMaxRecordDuration(int maxRecordDuration) {
        this.maxRecordDuration = maxRecordDuration;
    }

    /**
     * 文件名
     */
    private String fileName = String.format(Locale.getDefault(), "record_%s", FileUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.SIMPLIFIED_CHINESE)));

    /**
     * 录音文件存放路径，默认sdcard/Record
     */
    private String recordDir = String.format(Locale.getDefault(),
            "%s/Record/",
            Environment.getExternalStorageDirectory().getAbsolutePath());

    public RecordConfig() {
    }

    public RecordConfig(RecordFormat format) {
        this.format = format;
    }


    public String getRecordDir() {
        return recordDir;
    }

    public void setRecordDir(String recordDir) {
        this.recordDir = recordDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSourceConfig() {
        return sourceConfig;
    }

    public void setSource(int source) {
        this.sourceConfig = source;
    }


    //get&set

    public RecordFormat getFormat() {
        return format;
    }

    public RecordConfig setFormat(RecordFormat format) {
        this.format = format;
        return this;
    }


}
