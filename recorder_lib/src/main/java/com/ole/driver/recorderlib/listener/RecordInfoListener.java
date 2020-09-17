package com.ole.driver.recorderlib.listener;


import com.ole.driver.recorderlib.recorder.RecordState;

/**
 * @author ole on 2018/7/11.
 */
public interface RecordInfoListener {

    /**
     * 当前的录音状态发生变化
     *
     * @param what 状态
     */
    void onInfo(int what);


}
