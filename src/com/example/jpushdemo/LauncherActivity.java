package com.example.jpushdemo;

import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dtr.zxing.activity.CaptureActivity;
import com.example.wifilink.WifiLinkActivity;

import cn.alauncher.ioth.client.R;

public class LauncherActivity extends Activity{
    private Button addDeviceBtn;
    private Button linkWifiBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        addDeviceBtn = (Button) findViewById(R.id.add_devcie);
        linkWifiBtn = (Button) findViewById(R.id.link_wifi);
        linkWifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LauncherActivity.this, WifiLinkActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(SharedPreferencesUtils.getDeviceID(this) != null){
            addDeviceBtn.setText(R.string.my_device);
            addDeviceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LauncherActivity.this, DeviceActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            addDeviceBtn.setText(R.string.add_device);
            addDeviceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LauncherActivity.this, CaptureActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
