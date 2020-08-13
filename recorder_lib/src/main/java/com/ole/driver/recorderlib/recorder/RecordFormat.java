package com.ole.driver.recorderlib.recorder;

/**
 * @author zhangzheng
 * @Date 2020/7/7 3:25 PM
 * @ClassName RecordFormat
 * <p>
 * Desc :
 */
public enum RecordFormat {

    /**
     * mar格式
     */
    AMR(".amr"),

    /**
     * mar格式
     */
    MP3(".mp3");

    private String extension;

    public String getExtension() {
        return extension;
    }

    RecordFormat(String extension) {
        this.extension = extension;
    }
}
