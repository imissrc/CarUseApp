package com.caruseapp.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.caruseapp.entityes.Criminal;
import com.caruseapp.entityes.Event;
import com.caruseapp.entityes.Task;
import com.caruseapp.utils.MapString;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author: jojo
 * @Date: Created on 2019/4/20 15:53
 */
public class MainApplication extends Application {

    private String userName;
    private String userId;
    private String idCard;
    private String password;
    private String token;
    private String prisonerId;
    private boolean isLogin;
    private boolean isRememberPwd;
    private boolean isReg2Server;
    private String deviceNo;
    private String braceletNo;
    private String prisonerInfo;
    private Criminal criminalInfo;
    ArrayList<Event> events_c;
    private Map<String,String> tableRow;
    private Task task_;
    private String respounse;
    private String startPoint;
    private String carRoute;
    private String briefRoute;

    ArrayList<Event> old_events;
    private boolean event_dialog_flag;
    private SharedPreferences sharedPreferences;
    private String totalRisk;
    private String videoRisk;
    private String environmentRisk;
    private String serverIp;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = this.getSharedPreferences("PadApplication", Activity.MODE_APPEND);

        if(!sharedPreferences.contains("defaultLogin")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("defaultLogin", false);
            editor.commit();
        }
    }

    public String getStartPoint() {
        return sharedPreferences.getString("startPoint", null);
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("startPoint", startPoint);
        editor.commit();
    }

    public String getCarRoute() {
        return sharedPreferences.getString("startPoint", null);
    }

    public void setCarRoute(String carRoute) {
        this.carRoute = carRoute;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("carRoute", carRoute);
        editor.commit();
    }

    public String getBriefRoute() {
        return sharedPreferences.getString("briefRoute", null);
    }

    public void setBriefRoute(String briefRoute) {
        this.briefRoute = briefRoute;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("briefRoute", briefRoute);
        editor.commit();
    }

    public  String getTableRow() {
        return sharedPreferences.getString("table_row", null);
    }


    public void setTableRow(Map<String, String> tableRow) {

        this.tableRow = tableRow;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("table_row", MapString.getMapToString(tableRow));
        editor.commit();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean("defaultLogin", false);
    }

    public void setLogin(boolean login) {
        isLogin = login;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("defaultLogin", login);
        editor.commit();
    }

    public boolean isEvent_dialog_flag() {
        return sharedPreferences.getBoolean("event_dialog_flag", true);
    }

    public void setEvent_dialog_flag(boolean flag) {
        this.event_dialog_flag = flag;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("event_dialog_flag", flag);
        editor.commit();
    }

    public boolean isReg2Server() {
        return sharedPreferences.getBoolean("reg2Server", false);
    }

    public void setReg2Server(boolean reg2Server) {
        isReg2Server = reg2Server;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("reg2Server", reg2Server);
        editor.commit();
    }

    public String getOld_events() {
        return sharedPreferences.getString("old_events", null);
    }

    public void setOld_events(ArrayList<Event> old_events) {
        this.old_events = old_events;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("old_events",event2string(this.old_events));
        editor.commit();
    }
    private String event2string(ArrayList<Event> eventl){
        String events = "";
        for(int i =0;i<eventl.size();i++){
            events=events+"%"+eventl.get(i).toString()+"%";
        }
        return events;
    }
    public void setUserName(String userName) {
        this.userName = userName;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", userName);
        editor.commit();
    }

    public void setPassword(String password) {
        this.password = password;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.commit();
    }

    public void setToken(String token) {
        this.token = token;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public void setRememberPwd(boolean rememberPwd) {
        isRememberPwd = rememberPwd;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isRememberPwd", rememberPwd);
        editor.commit();
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", null);
    }

    public String getPassword() {
        return sharedPreferences.getString("password", null);
    }

    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    public boolean isRememberPwd() {
        return sharedPreferences.getBoolean("isRememberPwd", false);
    }

    public void setLoginUserInfo(String userName, String token, String userId, String idCard) {
        this.userName = userName;
//        this.prisonerId = prisonerId;
        this.token = token;
        this.userId = userId;
        this.idCard = idCard;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", userName);
        editor.putString("userId", userId);
        editor.putString("idCard", idCard);
//        editor.putString("prisonerId", prisonerId);
        editor.putString("token", token);
        editor.commit();
    }

    public String getPrisonerId() {
        return sharedPreferences.getString("prisonerId", null);
//        return this.prisonerId;
    }

    public void setPrisonerId(String prisonerId) {
        this.prisonerId = prisonerId;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("prisonerId", prisonerId);
        editor.commit();
    }

    public String getDeviceNo() {
//        return this.deviceNo;
        return sharedPreferences.getString("deviceNo", null);
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deviceNo", deviceNo);
        editor.commit();
    }

    public String getUserId() {
//        return this.userId;
        return sharedPreferences.getString("userId", null);
    }

    public void setUserId(String userId) {
        this.userId = userId;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.commit();
    }

    public String getServerIp() {
        Log.i("mainApplication", sharedPreferences.getString("serverAddress", ""));
        return sharedPreferences.getString("serverAddress", "");
    }

    public void setServerAddress(String serverIp) {
        this.serverIp = serverIp;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("serverAddress", serverIp);
        editor.commit();
    }

    public String getIdCard() {
        return sharedPreferences.getString("idCard", null);
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idCard", idCard);
        editor.commit();
    }

    public String getBraceletNo() {
        return sharedPreferences.getString("bracelet", null);
    }

    public void setBraceletNo(String braceletNo) {
        this.braceletNo = braceletNo;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("bracelet", braceletNo);
        editor.commit();
    }

    public String getPrisonerInfo() {
        return sharedPreferences.getString("prisonerInfo", null);
    }

    public void setPrisonerInfo(String prisonerInfo) {
        this.prisonerInfo = prisonerInfo;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("prisonerInfo", prisonerInfo);
        editor.commit();
    }

    public String getEvent_c() {
        return sharedPreferences.getString("event_c", null);
    }

    public void setEvents(ArrayList<Event> events_c) {
        this.events_c = events_c;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String events = "";
        for(int i =0;i<events_c.size();i++){
            events=events+"%"+events_c.get(i).toString()+"%";
        }
        editor.putString("event_c",events);
        editor.commit();
    }
    public String getTotalRisk(){
        return  sharedPreferences.getString("totalRisk", null);
    }

    public void setTotalRisk(String totalRisk) {
        this.totalRisk = totalRisk;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("totalRisk", totalRisk);
        editor.commit();
    }

    public String getVideoRisk() {
        return sharedPreferences.getString("videoRisk", null);
    }

    public void setVideoRisk(String videoRisk) {
        this.videoRisk = videoRisk;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("videoRisk", totalRisk);
        editor.commit();    }

    public String getEnvironmentRisk() {
        return sharedPreferences.getString("environmentRisk", null);
    }

    public void setEnvironmentRisk(String environmentRisk) {
        this.environmentRisk = environmentRisk;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("environmentRisk", totalRisk);
        editor.commit();
    }

    public String getTask_() {
        return String.valueOf(sharedPreferences.getString("Task", null));
    }

    public void setTask_(Task task_) {
        this.task_ = task_;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Task", task_.toString());
        editor.commit();
    }

    public String getCriminalInfo() {
        return sharedPreferences.getString("criminalInfo", null);
    }

    public void setCriminalInfo(Criminal criminalInfo) {
        this.criminalInfo = criminalInfo;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("criminalInfo", criminalInfo.toString());
        editor.commit();
    }
}
