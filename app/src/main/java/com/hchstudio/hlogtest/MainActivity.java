package com.hchstudio.hlogtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hchstudio.hlog.HLog;
import com.hchstudio.hlog.logfile.BaseLogFileManager;

import java.util.Arrays;

/**
 * Created by hech on 2017/3/2.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_main;
    private int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HLog.setLogFileManager(new BaseLogFileManager(this));
        HLog.setLogLevel(HLog.MUTWHOLE);
        HLog.startLog(this);
        HLog.v("测\n试");
        i = 0;
        bt_main = (Button) findViewById(R.id.bt_main);
        bt_main.setOnClickListener(this);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
//                Log.i("OK", "shouldShowRequestPermissionRationale");
//            } else {
//                Log.i("OK", "requestPermissions");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 123);
//            }
//        } else {
//            Log.i("OK", "权限已拥有！");
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_main:
//                HLog.v("点击button" + i++);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("OK", requestCode + "---" + Arrays.toString(permissions) + "---" + Arrays.toString(grantResults));
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
