package com.ole.driver.recorderlib.recorder;

/**
 * @author zhangzheng
 * @Date 2020/7/7 2:35 PM
 * @ClassName RecordState
 * <p>
 * Desc :
 */
public enum RecordState {
    /**
     * 空闲状态
     */
    IDLE,
    /**
     * 录音中
     */
    RECORDING,
    /**
     * 暂停中
     */
    PAUSE,
    /**
     * 正在停止
     */
    STOP,
    /**
     * 异常
     */
    ERROR

}
