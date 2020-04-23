package com.ole.driver.recorderlib.recorder;

import android.app.Notification;
import android.media.AudioFormat;
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
     * 录音格式 默认MP3格式
     */
    private RecordFormat format = RecordFormat.MP3;

    /**
     * 通道数:默认单通道
     */
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;

    /**
     * 位宽
     */
    private int encodingConfig = AudioFormat.ENCODING_PCM_16BIT;

    /**
     * 音源
     */
    private int sourceConfig = MediaRecorder.AudioSource.MIC;

    /**
     * 采样率
     */
    private int sampleRate = 44100;

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
    /**
     * 通知栏配置
     */
    private Notification notificationConfig;

    public RecordConfig() {
    }

    public RecordConfig(RecordFormat format) {
        this.format = format;
    }

    /**
     * @param format         录音文件的格式
     * @param channelConfig  声道配置
     *                       单声道：See {@link AudioFormat#CHANNEL_IN_MONO}
     *                       双声道：See {@link AudioFormat#CHANNEL_IN_STEREO}
     * @param encodingConfig 位宽配置
     *                       8Bit： See {@link AudioFormat#ENCODING_PCM_8BIT}
     *                       16Bit: See {@link AudioFormat#ENCODING_PCM_16BIT},
     * @param sampleRate     采样率 hz: 8000/16000/44100
     */
    public RecordConfig(RecordFormat format, int channelConfig, int encodingConfig, int sampleRate) {
        this.format = format;
        this.channelConfig = channelConfig;
        this.encodingConfig = encodingConfig;
        this.sampleRate = sampleRate;
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

    /**
     * 获取当前录音的采样位宽 单位bit
     *
     * @return 采样位宽 0: error
     */
    public int getEncoding() {
        if (format == RecordFormat.MP3) {//mp3后期转换
            return 16;
        }

        if (encodingConfig == AudioFormat.ENCODING_PCM_8BIT) {
            return 8;
        } else if (encodingConfig == AudioFormat.ENCODING_PCM_16BIT) {
            return 16;
        } else {
            return 0;
        }
    }

    /**
     * 获取当前录音的采样位宽 单位bit
     *
     * @return 采样位宽 0: error
     */
    public int getRealEncoding() {
        if (encodingConfig == AudioFormat.ENCODING_PCM_8BIT) {
            return 8;
        } else if (encodingConfig == AudioFormat.ENCODING_PCM_16BIT) {
            return 16;
        } else {
            return 0;
        }
    }

    /**
     * 当前的声道数
     *
     * @return 声道数： 0：error
     */
    public int getChannelCount() {
        if (channelConfig == AudioFormat.CHANNEL_IN_MONO) {
            return 1;
        } else if (channelConfig == AudioFormat.CHANNEL_IN_STEREO) {
            return 2;
        } else {
            return 0;
        }
    }

    //get&set

    public RecordFormat getFormat() {
        return format;
    }

    public RecordConfig setFormat(RecordFormat format) {
        this.format = format;
        return this;
    }


    public int getChannelConfig() {
        return channelConfig;
    }

    public RecordConfig setChannelConfig(int channelConfig) {
        this.channelConfig = channelConfig;
        return this;
    }

    public int getEncodingConfig() {
        if (format == RecordFormat.MP3) {//mp3后期转换
            return AudioFormat.ENCODING_PCM_16BIT;
        }
        return encodingConfig;
    }

    public RecordConfig setEncodingConfig(int encodingConfig) {
        this.encodingConfig = encodingConfig;
        return this;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public RecordConfig setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public Notification getNotificationConfig() {
        return notificationConfig;
    }

    public void setNotificationConfig(Notification notificationConfig) {
        this.notificationConfig = notificationConfig;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "录制格式： %s,采样率：%sHz,位宽：%s bit,声道数：%s", format, sampleRate, getEncoding(), getChannelCount());
    }

    public enum RecordFormat {
        /**
         * mp3格式
         */
        MP3(".mp3"),
        /**
         * wav格式
         */
        WAV(".wav"),
        /**
         * pcm格式
         */
        PCM(".pcm");

        private String extension;

        public String getExtension() {
            return extension;
        }

        RecordFormat(String extension) {
            this.extension = extension;
        }
    }
}
