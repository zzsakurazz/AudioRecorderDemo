package com.ole.driver.recorderlib.recorder.listener;

import java.io.File;

/**
 * 录音完成回调
 */
public interface RecordResultListener {

    /**
     * 录音文件
     *
     * @param resultFile 录音文件
     */
    void onResult(File resultFile);

}
