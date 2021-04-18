package com.caruseapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caruseapp.R;
import com.caruseapp.application.MainApplication;
import com.caruseapp.utils.EditTextClearTool;
import com.caruseapp.utils.OkHttpUtil;



import java.util.HashMap;

/**
 * @Author: jojo
 * @Date: Created on 2019/11/7 10:42
 */
public class LoginActivity extends BaseAcitvity {
    private static final int TYPE_LOG_REGISTER = 600;
    private static final int TYPE_JUDGE = 666;
    private TextView registerTv;
    private Button loginBtn;
    private EditText userNameEt, pwdEt, prisonerIdEt;
    private ImageView userNameClear, pwdClear, idClear;
    private CheckBox rememberPwd;
    private boolean isRememberPwd;
    private MainApplication mainApplication;
    private String deviceNo;
    private TelephonyManager tm;
    private boolean logout;
    private static final String TAG = "LoginActivity";
    private TextView changeIpTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 去除顶部标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        mainApplication = (MainApplication) getApplication();
        Intent intent = getIntent();
        logout = intent.getBooleanExtra("logout", false);
//        String androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceNo = mainApplication.getDeviceNo();
        if(deviceNo == null){
            String ANDROID_ID = Settings.System.getString(this.getContentResolver(), Settings.System.ANDROID_ID);
            mainApplication.setDeviceNo(ANDROID_ID);
            Log.i("deviceId",ANDROID_ID);

        }
//        mainApplication.setDeviceNo(androidId);
        System.out.println("deviceId"+deviceNo);

        init();
    }


    public void init() {
        userNameEt = (EditText) findViewById(R.id.et_userName);
        pwdEt = (EditText) findViewById(R.id.et_password);
        userNameClear = (ImageView) findViewById(R.id.iv_unameClear);
        pwdClear = (ImageView) findViewById(R.id.iv_pwdClear);
        loginBtn = (Button) findViewById(R.id.btn_login);
        registerTv = (TextView) findViewById(R.id.tv_register);
        rememberPwd = (CheckBox) findViewById(R.id.cb_checkbox);
        changeIpTv = findViewById(R.id.tv_change_ip);


        EditTextClearTool.addClearListener(userNameEt,userNameClear);
        EditTextClearTool.addClearListener(pwdEt,pwdClear);

        if(mainApplication.getUserName() != null) {
            userNameEt.setText(mainApplication.getUserId());
            if(mainApplication.isRememberPwd()) {
                rememberPwd.setChecked(true);
                isRememberPwd = true;
                pwdEt.setText(mainApplication.getPassword());
            }
        }

        deviceNo = mainApplication.getDeviceNo();
        if(deviceNo == null){
            tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                } else {
                    deviceNo = tm.getDeviceId();
                    mainApplication.setDeviceNo(deviceNo);
                }
            }
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRequest();
//                initWebSocket();
            }
        });

        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        rememberPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isRememberPwd = true;
                } else {
                    isRememberPwd = false;
                }
            }
        });

        changeIpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initServerIpDialog(v);
            }
        });
    }

    public void initServerIpDialog(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//        final EditText etServerIp = new EditText(LoginActivity.this);
        // 加载布局
        View serverIpView = View.inflate(this, R.layout.login_pop_ip, null);
        builder.setView(serverIpView);
        AlertDialog dialog = builder.create();
        dialog.show();
        final EditText etServerIp = serverIpView.findViewById(R.id.et_server_ip);
        Button btnOk = serverIpView.findViewById(R.id.btn_ok);
        Button btnCancel = serverIpView.findViewById(R.id.btn_cancle);

//
//        serverIpDialog.setView(etServerIp);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String serverIp = etServerIp.getText().toString().trim();
                Log.i("serverIp为", serverIp);
                if (!serverIp.equals("")) {
                    mainApplication.setServerAddress(serverIp);
                    // 关闭对话框
                    dialog.dismiss();
                } else {
                    Toast.makeText(getBaseContext(), "用户名或密码不能为空。", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 关闭对话框
                dialog.dismiss();
            }
        });

//        serverIpDialog.setNegativeButton("关闭", null);
        // 设置内容

        // 创建
    }


//    public void initServerIpDialog(View v) {
//        final AlertDialog.Builder serverIpDialog = new AlertDialog.Builder(LoginActivity.this);
//        final EditText etServerIp = new EditText(LoginActivity.this);
//        serverIpDialog.setTitle("请输入服务器Ip");
//
//        serverIpDialog.setView(etServerIp);
//        serverIpDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String serverIp = etServerIp.getText().toString().trim();
//                if (!serverIp.equals("")) {
//                    mainApplication.setServerAddress(serverIp);
//                }
//            }
//        });
//        serverIpDialog.setNegativeButton("关闭", null);
//        serverIpDialog.show();
//    }
//    public JWebSocketClient client;
//    private void initWebSocket(){
//        URI uri = URI.create("ws://10.28.209.130:8089/websocket/pad");
////        URI uri = URI.create("ws://echo.websocket.org");
//        client = new JWebSocketClient(uri) {
//
//            @Override
//            public void onMessage(String message) {
//                //message就是接收到的消息
//                Log.e("JWebSClientService", message);
//            }
//            @Override
//            public void onOpen(ServerHandshake handshakedata) {
//                super.onOpen(handshakedata);
//                Log.e("JWebSocketClientService", "websocket连接成功");
//            }
//        };
//        connect();
//    }
//    /**
//     * 连接websocket
//     */
//
//    private void connect() {
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
//                    client.connectBlocking();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //如果没有获取权限，那么可以提示用户去设置界面--->应用权限开启权限
                    Toast.makeText(getApplicationContext(), "获取权限失败，请去设置界面--->应用权限开启权限", Toast.LENGTH_SHORT).show();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        deviceNo = tm.getDeviceId();
                        if (deviceNo != null) {
                            mainApplication.setDeviceNo(deviceNo);
                        }
                    }
                }
            }
        }
    }
    private void loginRequest() {
        HashMap<String,String> header = new HashMap<>();
        String ANDROID_ID = Settings.System.getString(this.getContentResolver(), Settings.System.ANDROID_ID);

        HashMap<String, String> params = new HashMap<>(3);
        params.put("userId", userNameEt.getText().toString());
        params.put("deviceType", "一体化终端");
        params.put("deviceNo", ANDROID_ID);
        Log.i("deviceId",ANDROID_ID);
        mainApplication.setDeviceNo(ANDROID_ID);
//                    params.put("prisonerId", prisonerIdEt.getText().toString());
        params.put("password", pwdEt.getText().toString());
        OkHttpUtil.getInstance(getBaseContext()).requestAsyn("users/login", TYPE_LOG_REGISTER, params,header, new OkHttpUtil.ReqCallBack<String>() {
            @Override

            public void onReqSuccess(String result) {
                if(JSON.parseObject(result).getInteger("code") == 200) {
                    JSONObject jsonObj = JSON.parseObject(result).getJSONObject("data");
                    String token = jsonObj.getString("token");
                    String userName = jsonObj.getString("userName");
//                                String prisonerId = jsonObj.getString("prisonerId");
                    String userId = jsonObj.getString("userId");
                    String idCard = jsonObj.getString("idCard");

                    mainApplication.setLogin(true);
                    mainApplication.setLoginUserInfo(userName, token, userId, idCard);
                    System.out.println("token===="+mainApplication.getToken());

                    if(isRememberPwd) {
                        mainApplication.setPassword(pwdEt.getText().toString());
                        mainApplication.setRememberPwd(true);
                    } else {
                        mainApplication.setRememberPwd(false);
                    }
                    if(logout){
                        setResult(2);
                        finish();
                    }else {
                        setResult(1);
                        finish();
                    }

                } else {
                    Toast.makeText(getBaseContext(), JSON.parseObject(result).getString("message"), Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onReqFailed(String errorMsg) {
                Log.e(TAG, errorMsg);
                Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
//        Intent intent = new Intent();
//        intent.setClass(LoginActivity.this, MainActivity.class);
//        startActivity(intent);

    }
//    private void loginRequest() {
//        HashMap<String, String> pp = new HashMap<>(1);
//        Log.i(TAG, mainApplication.getDeviceNo());
//        pp.put("deviceNo", mainApplication.getDeviceNo());
//        HashMap<String, String> header = new HashMap<>(1);
//        header.put("token",mainApplication.getToken());
//        OkHttpUtil.getInstance(getBaseContext()).requestAsyn("devices/judgeDeviceNo", TYPE_JUDGE, pp,header,new OkHttpUtil.ReqCallBack<String>() {
//            @Override
//            public void onReqSuccess(String result) {
//                Log.i(TAG, result);
//                if(result.equals("true")){
//                    if(!mainApplication.isReg2Server()){
//                        mainApplication.setReg2Server(true);
//                    }
//                    HashMap<String, String> params = new HashMap<>(3);
//                    params.put("userId", userNameEt.getText().toString());
////                    params.put("prisonerId", prisonerIdEt.getText().toString());
//                    params.put("password", pwdEt.getText().toString());
//
//                    OkHttpUtil.getInstance(getBaseContext()).requestAsyn("users/login", TYPE_LOG_REGISTER, params,header, new OkHttpUtil.ReqCallBack<String>() {
//                        @Override
//                        public void onReqSuccess(String result) {
//                            if(JSON.parseObject(result).getInteger("code") == 200) {
//                                JSONObject jsonObj = JSON.parseObject(result).getJSONObject("data");
//                                String token = jsonObj.getString("loginToken");
//                                String userName = jsonObj.getString("userName");
////                                String prisonerId = jsonObj.getString("prisonerId");
//                                String userId = jsonObj.getString("userId");
//                                String idCard = jsonObj.getString("idCard");
//
//                                mainApplication.setLogin(true);
//                                mainApplication.setLoginUserInfo(userName, token, userId, idCard);
//                                System.out.println("token===="+mainApplication.getToken());
//                                if(isRememberPwd) {
//                                    mainApplication.setPassword(pwdEt.getText().toString());
//                                    mainApplication.setRememberPwd(true);
//                                } else {
//                                    mainApplication.setRememberPwd(false);
//                                }
//                                if(logout){
//                                    setResult(2);
//                                    finish();
//                                }else {
//                                    setResult(1);
//                                    finish();
//                                }
//
//                            } else {
//                                Toast.makeText(getBaseContext(), JSON.parseObject(result).getString("message"), Toast.LENGTH_SHORT).show();
//                            }
//                            Intent intent = new Intent();
//                            intent.setClass(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onReqFailed(String errorMsg) {
//                            Log.e(TAG, errorMsg);
//                            Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }else {
//                    mainApplication.setReg2Server(false);
//                    Intent intent = new Intent();
//                    intent.setClass(LoginActivity.this, Register2ServerActivity.class);
//                    startActivityForResult(intent,2);
//                }
//            }
//
//            @Override
//            public void onReqFailed(String errorMsg) {
//                Log.e(TAG, errorMsg);
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult(MainActivity)");
        if(resultCode == 1) {
            userNameEt.setText(data.getStringExtra("userName"));
//            prisonerIdEt.setText(data.getStringExtra("prisonerId"));
            pwdEt.setText(data.getStringExtra("password"));
        }else if(resultCode == 2){

        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed(LoginActivity)");
        if(logout) {
            setResult(4);
            finish();
        }else {
            setResult(3);
            finish();
        }
    }
}

