package com.ole.driver.recorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ole.driver.recorderlib.RecordManager;
import com.ole.driver.recorderlib.recorder.RecordConfig;
import com.ole.driver.recorderlib.recorder.RecordHelper;
import com.ole.driver.recorderlib.recorder.listener.RecordResultListener;
import com.ole.driver.recorderlib.recorder.listener.RecordSoundSizeListener;
import com.ole.driver.recorderlib.recorder.listener.RecordStateListener;

import java.io.File;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author zhangzheng
 */
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R2.id.main_but_record)
    Button mainButRecord;
    @BindView(R2.id.main_but_stop)
    Button mainButStop;
    @BindView(R2.id.main_tv_volume)
    TextView mainTvVolume;
    String PERMISSION_STORAGE_MSG = "请授予录音权限，不然就别玩了";
    @BindView(R2.id.main_tv_time)
    TextView mainTvTime;
    private int PERMISSION_STORAGE_CODE = 10001;
    private int time = 1;
    private final RecordManager recordManager = RecordManager.getInstance();
    private String[] PERMS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handler.removeMessages(0);
                    // app的功能逻辑处理
                    // 再次发出msg，循环更新
                    handler.sendEmptyMessageDelayed(0, 1000);
                    mainTvTime.setText(String.format("时间：%d", time));
                    time++;
                    break;
                case 1:
                    // 直接移除，定时器停止
                    handler.removeMessages(0);
                    mainTvTime.setText("时间：--");
                    time = 1;
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecord();
    }

    private void initRecord() {
        String recordDir = String.format(Locale.getDefault(), "%s/Record/com.zz.main/",
                this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        recordManager.changeFormat(RecordConfig.RecordFormat.MP3);
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(44100));
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_16BIT));
        recordManager.changeRecordDir(recordDir);
        recordManager.setRecordStateListener(new RecordStateListener() {
            @Override
            public void onStateChange(RecordHelper.RecordState state) {

            }

            @Override
            public void onError(String error) {

            }
        });
        recordManager.setRecordResultListener(new RecordResultListener() {

            @Override
            public void onResult(File resultFile, String resultBase64) {
                Toast.makeText(MainActivity.this, "录音文件： " + resultFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                Log.e("zz", "resultBase64:" + resultBase64);
            }

        });
        recordManager.setRecordSoundSizeListener(new RecordSoundSizeListener() {
            @Override
            public void onSoundSize(int soundSize) {
                mainTvVolume.setText("音量:" + soundSize);
            }
        });
    }

    @OnClick({R.id.main_but_record, R.id.main_but_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_but_record:
                if (EasyPermissions.hasPermissions(this, PERMS)) {
                    // 已经申请过权限，做想做的事
                    RecordManager.getInstance().start();
                    handler.sendEmptyMessage(0);
                } else {
                    // 没有申请过权限，现在去申请
                    /**
                     *@param host Context对象
                     *@param rationale  权限弹窗上的提示语。
                     *@param requestCode 请求权限的唯一标识码
                     *@param perms 一系列权限
                     */
                    EasyPermissions.requestPermissions(this, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMS);
                }
                break;
            case R.id.main_but_stop:
                RecordManager.getInstance().stop();
                mainTvVolume.setText("音量:--");
                handler.sendEmptyMessage(1);
                break;
            default:
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将结果转发给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //从设置页面返回，判断权限是否申请。
            if (EasyPermissions.hasPermissions(this, PERMS)) {
                Toast.makeText(this, "权限申请成功!", Toast.LENGTH_SHORT).show();
                RecordManager.getInstance().start();
            } else {
                Toast.makeText(this, "权限申请失败!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        /**
         * 若是在权限弹窗中，用户勾选了'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
