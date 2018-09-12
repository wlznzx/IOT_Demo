package com.example.wifilink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.alauncher.ioth.client.R;


public class WifiLinkActivity extends Activity implements OnClickListener ,OnDataFinishedListener{
    private EditText ssidTV, psdTV;
    private Button bt_send,cancelBtn;
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_link_layout);
        InitView();
        MobileServer mobileServer = new MobileServer();
        mobileServer.setHandler(handler);
        new Thread(mobileServer).start();  
  
    }  
  
    private void InitView() {
        ssidTV = (EditText) findViewById(R.id.ssid_ed);
        psdTV = (EditText) findViewById(R.id.psd_ed);
        bt_send = (Button) findViewById(R.id.wifi_link);
        bt_send.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);

    }
  
    @Override  
    public void onClick(View v) {  
        switch (v.getId()) {  
        case R.id.wifi_link:
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            try {
            	object.put("password", psdTV.getText().toString().trim());
    			object.put("ssid", ssidTV.getText().toString().trim());
    			array.put(object);
    			JSONObject sta_object = new JSONObject();
    			sta_object.put("Station", array);
    			final SendAsyncTask _task = new SendAsyncTask();
    			_task.setOnDataFinishedListener(this);
    			_task.execute(sta_object.toString());
//    			tv_send_text.setText(sta_object.toString());
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
            break;
            case R.id.cancel_btn:
                finish();
                break;
        }  
  
    }  
  
    Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {

            switch (msg.what) {
            case 1:
                String reg=".*104.*";
                String result = msg.obj.toString();
                Toast.makeText(WifiLinkActivity.this,result, Toast.LENGTH_LONG)
                        .show();
                if(result.equals("STATION:104") || result.matches(reg)){
                    Toast.makeText(WifiLinkActivity.this,getResources().getString(R.string.connect_ssid_success), Toast.LENGTH_LONG)
                            .show();
                }else{
                    Toast.makeText(WifiLinkActivity.this,getResources().getString(R.string.connect_ssid_fail), Toast.LENGTH_LONG)
                            .show();
                }
            }  
        }  
    };

    @Override
    public void onDataSuccessfully() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WifiLinkActivity.this,getResources().getString(R.string.wifi_link_success), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDataFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WifiLinkActivity.this,getResources().getString(R.string.wifi_link_fail), Toast.LENGTH_LONG).show();
            }
        });
    }
}
