## **common_recorder**

司机端录音组件，通过Android自带的MediaRecorder来实现录音，保存为AMR格式

### 更新日志

- 1.1.6修改了录音方式，采用MediaRecorder来进行录音，避免了启动service和手动转换格式等消耗资源的问题。删除了暂停与恢复功能，删除了音量回调
- 1.1.5版本修改了录音结束后返回的接口，目前版本不会返回Bast64，需要用户手动通过返回的文件和提供的工具类自行转换

### 使用方式

- ##### 添加依赖

```groovy
api 'com.ole.travel:recorder:1.1.6'
```

- ##### 添加权限

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

- ##### 初始化

```java
/**
 * 参数1： Application 实例
 */
RecordManager.getInstance().init(this);
```

- ##### 配置录音参数


```java
//(可选)设置保存格式 目前仅支持AMR
recordManager.changeFormat(RecordConfig.RecordFormat.AMR);
//(可选)设置音源     默认为MIC
recordManager.changeSource(MediaRecorder.AudioSource.MIC);
//(必须)设置保存路径
recordManager.changeRecordDir(recordDir);
//(必须)设置文件名 注意每次录音时请重新设置，避免文件覆盖
recordManager.changeFileName(fileName);
```

- ##### 录音状态监听

```java
recordManager.setRecordStateListener(new RecordStateListener() {
    @Override
    public void onStateChange(RecordHelper.RecordState state) {

    }

    @Override
    public void onError(String error) {

    }
});
```

- ##### 录音结果监听

```java
   recordManager.setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File resultFile) {

            }
        });
```

- ##### 录音操作

```java
//开始录音
RecordManager.getInstance().start();
//停止录音
RecordManager.getInstance().stop();
```

- ##### 录音状态

```java
//获取当前录音状态
recordManager.getState();
```
- Bast64转换

```java
//推荐在子线程转换，防止大文件转换耗时
Bast64Utils.fileToBase64(resultFile)
```

### 备注

其他操作详见[demo](https://gitlab.olafuwu.com/ole-terminal/ole-arc/android/common_recorder)