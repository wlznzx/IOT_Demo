package com.example.jpushdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cn.alauncher.ioth.client.R;
import cn.jpush.android.api.JPushInterface;

public class DeviceActivity extends Activity implements View.OnClickListener{
    private final String DEVICE_URL = "http://www.alauncher.cn/IOT_Humidifier/Device";
    private final String ACTION_URL = "http://www.alauncher.cn/IOT_Humidifier/Action";
    private final String RID_URL = "http://www.alauncher.cn/IOT_Humidifier/RegistrationID";
//    private final String URL = "http://localhost:8080/IOT_Humidifier/Device";

    private TextView deviceIDTV;
    private ImageView pwrLedIV;
    private ImageView heatLedIV;
    private TextView temperatureTV;
    private TextView detecter1TV;
    private TextView detecter2TV;
    private TextView deviceStaticTV;

    private Button setTBtn;
    private Button unBindBtn ;

    private String deviceID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_layout);
        initViews();
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String device_id = extras.getString("result");
            if(device_id != null){
                SharedPreferencesUtils.setDeviceID(this,device_id);
                deviceID = device_id;
                JPushInterface.resumePush(getApplicationContext());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bindDeivce();
                    }
                }).start();
            }
        }else{

        }
        deviceID = SharedPreferencesUtils.getDeviceID(this);
        if(null != deviceID){
            deviceIDTV.setText(deviceID);
            syncDeviceInfo();
        }else{
            finish();
        }
    }

    private void bindDeivce() {
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id",deviceID);
            jsonObject.put("id",rid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String postStr = NetUtils.sendPost(RID_URL, jsonObject.toString());
    }

    private void unbindDeivce() {
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id",deviceID);
            jsonObject.put("id",rid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JPushInterface.stopPush(getApplicationContext());
    }

    private void initViews(){
        deviceIDTV = (TextView) findViewById(R.id.device_id_tv);
        pwrLedIV = (ImageView) findViewById(R.id.pwr_led_iv);
        heatLedIV = (ImageView) findViewById(R.id.heat_led_iv);
        temperatureTV = (TextView) findViewById(R.id.temperature_tv);
        detecter1TV = (TextView) findViewById(R.id.detecter1_temp_tv);
        detecter2TV = (TextView) findViewById(R.id.detecter2_temp_tv);
        deviceStaticTV = (TextView) findViewById(R.id.device_status_tv);

        setTBtn = (Button) findViewById(R.id.set_temperature_btn);
        unBindBtn = (Button) findViewById(R.id.unbind_device_btn);

        setTBtn.setOnClickListener(this);
        unBindBtn.setOnClickListener(this);
    }

    private void syncDeviceInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = NetUtils.sendGet(DEVICE_URL,"id=" + deviceID);
                Log.e("wltest","result = " + result);
                final Device dev = pauseDeviceJson(result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(dev);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Device pauseDeviceJson(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
//            int id = jsonObject.getInt("id");
            String status = jsonObject.optString("status");
            if(status.equals("200")){
                String device = jsonObject.optString("device");
                JSONObject deviceJson = new JSONObject(device);

                String device_id = deviceJson.optString("device_id");
                int temperature = deviceJson.optInt("temperature");
                int pwr_led = deviceJson.optInt("pwr_led");
                int heat_led = deviceJson.optInt("heat_led");
                int detecter1_temp = deviceJson.optInt("detecter1_temp");
                int detecter2_temp = deviceJson.optInt("detecter2_temp");
                int err_code = deviceJson.optInt("err_code");

                Device _dev = new Device(device_id,temperature,pwr_led,heat_led,detecter1_temp,detecter2_temp,err_code);

                Log.e("wltest","_dev = " + _dev.toString());
                return _dev;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean setTAction(int t){
//        String param = "{'device_id':'"+ deviceID + ",'action':'set_temperature','extra':" + t + "}";
////        String param = "{'device_id':'434357adf8','action':'set_temperature','extra':21}";
//        Log.e("wltest","param = " + param);
//        String postStr = NetUtils.sendPost(ACTION_URL, param);
//        Log.e("wltest","postStr = " + postStr);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id",deviceID);
            jsonObject.put("action","set_temperature");
            jsonObject.put("extra",t);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String postStr = NetUtils.sendPost(ACTION_URL, jsonObject.toString());
        try {
            JSONObject resultJson = new JSONObject(postStr);
            String status = resultJson.optString("status");
            if(status.equals("200")) return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateUI(Device dev){

        if(dev == null){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_error), Toast.LENGTH_LONG).show();
            return;
        }

        pwrLedIV.setImageResource(dev.getPwr_led() == 0 ? R.drawable.light_off : R.drawable.light_up);
        heatLedIV.setImageResource(dev.getHeat_led() == 0 ? R.drawable.light_off : R.drawable.light_up);
        temperatureTV.setText(String.valueOf(dev.getTemperature()) + "°C");
        detecter1TV.setText(String.valueOf(dev.getDetecter1_temp()) + "°C");
        detecter2TV.setText(String.valueOf(dev.getDetecter2_temp()) + "°C");
        deviceStaticTV.setText(String.valueOf(dev.getErr_code()));

        switch (dev.getErr_code()){
            case 0:
                deviceStaticTV.setText(R.string.normal);
                break;
            case 1:
                deviceStaticTV.setText(R.string.high_temperature);
                break;
            case 2:
                deviceStaticTV.setText(R.string.low_temperature);
                break;
            case 3:
                deviceStaticTV.setText(R.string.temperature_out_control);
                break;
            case 4:
                deviceStaticTV.setText(R.string.normal);
                break;
                default:
                    break;
        }
    }

    private void setT(){
        final EditText et = new EditText(this);
        et.setInputType( InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(this).setTitle(R.string.set_temperature)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        final String input = et.getText().toString();
                        if (input.equals("")) {
                            // Toast.makeText(getApplicationContext(), "搜索内容不能为空！" + input, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                        else {
                            final int t = Integer.valueOf(input);
                            if(t < 20 || t > 35){
                                Toast.makeText(getApplicationContext(), "Error input." + input, Toast.LENGTH_LONG).show();
                            }else{
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean isSuccess = setTAction(t);
                                        if(isSuccess){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), R.string.set_temperature_success, Toast.LENGTH_LONG).show();
                                                    temperatureTV.setText(input + "°C");
                                                }
                                            });
                                        }else{
                                            Toast.makeText(getApplicationContext(), R.string.set_temperature_fail, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }).start();
                            }
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_temperature_btn:
                setT();
                break;
            case R.id.unbind_device_btn:
                unBind();
                break;
        }
    }

    public void unBind() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this);
        builder.setTitle(R.string.unbind_device);
        builder.setMessage(R.string.sure_unbind_device);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferencesUtils.setDeviceID(DeviceActivity.this,null);
                dialog.dismiss();
                unbindDeivce();
                Intent intent = new Intent(DeviceActivity.this, LauncherActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
