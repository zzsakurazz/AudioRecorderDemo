## **common_recorder**

司机端录音组件，通过Android自带的AudioRecord来实现录音，支持保存wav、mp3、pcm格式

### 使用方式

- ##### 添加依赖

```groovy
api 'com.ole.travel:recorder:1.0.0'
```

- ##### 添加权限

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

- ##### 初始化

```java
/**
 * 参数1： Application 实例
 * 参数2： 是否打印日志
 */
RecordManager.getInstance().init(this,true);
```

- ##### 注册service

```xml
<service android:name="com.ole.driver.recorderlib.recorder.RecordService" />
```

- ##### 配置录音参数

```java
//设置保存格式
recordManager.changeFormat(RecordConfig.RecordFormat.MP3);
//设置帧率
recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(44100));
//设置编码格式
recordManager.changeRecordConfig(recordManager.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_16BIT));
//设置保存路径
recordManager.changeRecordDir(recordDir);
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
    public void onResult(File result) {
       
    }
});
```

- ##### 录音操作

```java
//开始录音
RecordManager.getInstance().start();
//暂停录音
RecordManager.getInstance().pause();
//恢复录音
RecordManager.getInstance().resume();
//停止录音
RecordManager.getInstance().stop();
```

### 备注

其他操作详见demo