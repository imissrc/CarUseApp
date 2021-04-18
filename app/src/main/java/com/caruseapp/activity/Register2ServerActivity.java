package com.caruseapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import com.alibaba.fastjson.JSON;
import com.caruseapp.R;
import com.caruseapp.application.MainApplication;
import com.caruseapp.utils.EditTextClearTool;
import com.caruseapp.utils.OkHttpUtil;

import java.util.HashMap;

/**
 * @Author: jojo
 * @Date: Created on 2019/5/8 17:47
 */
public class Register2ServerActivity extends BaseAcitvity {

    private static final int TYPE__REGISTER = 600;
    private TextView deviceTypeTv, deviceNoTv;
    private EditText deviceUserEt;
    private Button regBtn;
    private ImageView deviceUserClear;
    private CheckBox checkBox;
    private TelephonyManager tm;
    private String deviceType, deviceNo;

    private MainApplication mainApplication;
    private static final String TAG = "Register2ServerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg2server);
        // 去除顶部标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mainApplication = (MainApplication) getApplication();
        init();
    }

    public void init() {
        deviceTypeTv = (TextView) findViewById(R.id.tv_device_type);
        deviceNoTv = (TextView) findViewById(R.id.tv_device_no);
        regBtn = (Button) findViewById(R.id.btn_reg2server);
        deviceUserEt = (EditText) findViewById(R.id.et_device_user);
        deviceUserClear = (ImageView) findViewById(R.id.iv_device_user_Clear);
//        checkBox = (CheckBox) findViewById(R.id.cb_device_login);

        deviceType = deviceTypeTv.getText().toString();

        EditTextClearTool.addClearListener(deviceUserEt, deviceUserClear);

//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkBox.isChecked()) {
//                    check = "true";
//                } else {
//                    check = "false";
//                }
//            }
//        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> header = new HashMap<>(1);
                header.put("token",mainApplication.getToken());
                if (deviceNo == null) {
                    Toast.makeText(getBaseContext(), "获取权限失败，无法注册设备", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("deviceType", deviceType);
                    params.put("deviceNo", deviceNo);
                    params.put("userId", deviceUserEt.getText().toString());
                    OkHttpUtil.getInstance(getBaseContext()).requestAsyn("devices/register", TYPE__REGISTER, params, header, new OkHttpUtil.ReqCallBack<String>() {
                        @Override
                        public void onReqSuccess(String result) {
                            Integer code = JSON.parseObject(result).getInteger("code");
                            if (code == 200) {
                                MainApplication mainApplication = (MainApplication) getApplication();
                                mainApplication.setReg2Server(true);
                                setResult(2);
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), JSON.parseObject(result).getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onReqFailed(String errorMsg) {
                            Log.e(TAG, errorMsg);
                        }
                    });
                }
            }
        });

        deviceNo = mainApplication.getDeviceNo();
        System.out.println("=========="+deviceNo);
        if(deviceNo == null){
            tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Register2ServerActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                } else {
                    deviceNo = tm.getDeviceId();
                }
            }
        }else {
            String dn = deviceNo.substring(0, 4) + "*****" + deviceNo.substring( deviceNo.length()-5, deviceNo.length()-1);
            deviceNoTv.setText(dn);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //如果没有获取权限，那么可以提示用户去设置界面--->应用权限开启权限
                    Toast.makeText(getApplicationContext(), "获取权限失败", Toast.LENGTH_SHORT).show();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        deviceNo = tm.getDeviceId();
                        if (deviceNo != null) {
                            String dn = deviceNo.substring(0, 4) + "********" + deviceNo.substring(11, 15);
                            deviceNoTv.setText(dn);
                        }
                    }
                }
            }
        }
    }
}
