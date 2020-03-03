package com.ole.driver.recorderlib.recorder.listener;

import java.io.File;

/**
 * 录音完成回调
 */
public interface RecordResultListener {

    /**
     * 录音文件
     *
     * @param resultFile   录音文件
     * @param resultBase64 录音Base64
     */
    void onResult(File resultFile, String resultBase64);

}
