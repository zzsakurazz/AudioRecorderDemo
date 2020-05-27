package com.ole.driver.recorderlib.recorder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ole.driver.recorderlib.R;
import com.ole.driver.recorderlib.recorder.listener.RecordDataListener;
import com.ole.driver.recorderlib.recorder.listener.RecordFftDataListener;
import com.ole.driver.recorderlib.recorder.listener.RecordResultListener;
import com.ole.driver.recorderlib.recorder.listener.RecordSoundSizeListener;
import com.ole.driver.recorderlib.recorder.listener.RecordStateListener;
import com.ole.driver.recorderlib.utils.FileUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ola.com.travel.log.logger.Logger;

/**
 * 录音服务
 *
 * @author ole
 */
public class RecordService extends Service {

    /**
     * 录音配置
     */
    private static RecordConfig currentConfig = new RecordConfig();

    private final static String ACTION_NAME = "action_type";

    private final static int ACTION_INVALID = 0;

    private final static int ACTION_START_RECORD = 1;

    private final static int ACTION_STOP_RECORD = 2;

    private final static int ACTION_RESUME_RECORD = 3;

    private final static int ACTION_PAUSE_RECORD = 4;

    private final static String PARAM_PATH = "path";
    private final static int NOTICE_ID = 123;

    public RecordService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = currentConfig.getNotificationConfig();
            if (notification != null) {
                startForeground(NOTICE_ID, notification);
            } else {
                throw new NullPointerException("notification不能为空!!!");
            }
        } else {
            startForeground(NOTICE_ID, new Notification());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(ACTION_NAME)) {
            switch (bundle.getInt(ACTION_NAME, ACTION_INVALID)) {
                case ACTION_START_RECORD:
                    doStartRecording(bundle.getString(PARAM_PATH));
                    break;
                case ACTION_STOP_RECORD:
                    doStopRecording();
                    break;
                case ACTION_RESUME_RECORD:
                    doResumeRecording();
                    break;
                case ACTION_PAUSE_RECORD:
                    doPauseRecording();
                    break;
                default:
                    break;
            }
            return START_REDELIVER_INTENT;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static void startRecording(Context context) {
        Intent intent = new Intent(context, RecordService.class);
        intent.putExtra(ACTION_NAME, ACTION_START_RECORD);
        intent.putExtra(PARAM_PATH, getFilePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stopRecording(Context context) {
        Intent intent = new Intent(context, RecordService.class);
        intent.putExtra(ACTION_NAME, ACTION_STOP_RECORD);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void resumeRecording(Context context) {
        Intent intent = new Intent(context, RecordService.class);
        intent.putExtra(ACTION_NAME, ACTION_RESUME_RECORD);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void pauseRecording(Context context) {
        Intent intent = new Intent(context, RecordService.class);
        intent.putExtra(ACTION_NAME, ACTION_PAUSE_RECORD);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    /**
     * 改变录音格式
     */
    public static boolean changeFormat(RecordConfig.RecordFormat recordFormat) {
        if (getState() == RecordHelper.RecordState.IDLE) {
            currentConfig.setFormat(recordFormat);
            return true;
        }
        return false;
    }

    /**
     * 改变录音配置
     */
    public static boolean changeRecordConfig(RecordConfig recordConfig) {
        if (getState() == RecordHelper.RecordState.IDLE) {
            currentConfig = recordConfig;
            return true;
        }
        return false;
    }

    /**
     * 获取录音配置参数
     */
    public static RecordConfig getRecordConfig() {
        return currentConfig;
    }

    public static void changeRecordDir(String recordDir) {
        currentConfig.setRecordDir(recordDir);
    }

    public static void changeFileName(String fileName) {
        currentConfig.setFileName(fileName);
    }

    public static void changeSource(int source) {
        currentConfig.setSource(source);
    }

    public static void setNotification(Notification notification) {
        currentConfig.setNotificationConfig(notification);
    }

    /**
     * 获取当前的录音状态
     */
    public static RecordHelper.RecordState getState() {
        return RecordHelper.getInstance().getState();
    }

    public static void setRecordStateListener(RecordStateListener recordStateListener) {
        RecordHelper.getInstance().setRecordStateListener(recordStateListener);
    }

    public static void setRecordDataListener(RecordDataListener recordDataListener) {
        RecordHelper.getInstance().setRecordDataListener(recordDataListener);
    }

    public static void setRecordSoundSizeListener(RecordSoundSizeListener recordSoundSizeListener) {
        RecordHelper.getInstance().setRecordSoundSizeListener(recordSoundSizeListener);
    }

    public static void setRecordResultListener(RecordResultListener recordResultListener) {
        RecordHelper.getInstance().setRecordResultListener(recordResultListener);
    }

    public static void setRecordFftDataListener(RecordFftDataListener recordFftDataListener) {
        RecordHelper.getInstance().setRecordFftDataListener(recordFftDataListener);
    }

    private void doStartRecording(String path) {
        Logger.v("doStartRecording path: %s", path);
        RecordHelper.getInstance().start(path, currentConfig);
    }

    private void doResumeRecording() {
        Logger.v("doResumeRecording");
        RecordHelper.getInstance().resume();
    }

    private void doPauseRecording() {
        Logger.v("doResumeRecording");
        RecordHelper.getInstance().pause();
    }

    private void doStopRecording() {
        Logger.v("doStopRecording");
        RecordHelper.getInstance().stop();
        stopSelf();
    }

    public static RecordConfig getCurrentConfig() {
        return currentConfig;
    }

    public static void setCurrentConfig(RecordConfig currentConfig) {
        RecordService.currentConfig = currentConfig;
    }

    /**
     * 根据当前的时间生成相应的文件名
     * 实例 record_20160101_13_15_12
     */
    private static String getFilePath() {

        String fileDir =
                currentConfig.getRecordDir();
        if (!FileUtils.createOrExistsDir(fileDir)) {
            Log.e("zz", (String.format("文件夹创建失败：%s", fileDir)));
            return null;
        }
        String fileName = currentConfig.getFileName();
        if (fileName.isEmpty()) {
            Log.e("zz", ("文件创建失败：文件名不合法"));
            return null;
        }
        return String.format(Locale.getDefault(), "%s%s%s", fileDir, fileName, currentConfig.getFormat().getExtension());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
