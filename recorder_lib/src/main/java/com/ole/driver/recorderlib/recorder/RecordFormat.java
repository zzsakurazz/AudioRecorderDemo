package com.ole.driver.recorderlib.recorder;

/**
 * @author zhangzheng
 * @Date 2020/7/7 3:25 PM
 * @ClassName RecordFormat
 * <p>
 * Desc :
 */
enum RecordFormat {

    /**
     * mar格式
     */
    AMR(".amr");

    private String extension;

    public String getExtension() {
        return extension;
    }

    RecordFormat(String extension) {
        this.extension = extension;
    }
}
