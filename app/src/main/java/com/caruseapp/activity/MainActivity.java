package com.caruseapp.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.*;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.ButterKnife;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.*;
import com.amap.api.maps.model.*;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.MovingPointOverlay;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.TMC;
import com.caruseapp.R;
import com.caruseapp.Service.LocationService;
import androidx.appcompat.app.*;
import com.caruseapp.application.MainApplication;
import com.caruseapp.entityes.Criminal;
import com.caruseapp.entityes.Point;
import com.caruseapp.entityes.Task;
import com.caruseapp.utils.MapString;
import com.caruseapp.utils.OkHttpUtil;
import com.caruseapp.utils.WarnDialog;

import com.caruseapp.view.RiskChartView;
import com.google.android.material.navigation.NavigationView;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.achartengine.GraphicalView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.*;
//import org.springframework.web.client.RestTemplate;

public class MainActivity extends BaseAcitvity implements NavigationView.OnNavigationItemSelectedListener {

    private MainApplication mainApplication;
    private String TAG = "mainActivity";
    private boolean STATUS = false;
    private boolean hasNotice = false;
    private  NowBase now;
    private String Result = "";
//    private TextView latitude;
//    private TextView longitude;
    private double latitude_v;
    private double longitude_v;
    private static final int REQUEST_LOGIN = 1;
    private MapView mapView;
    private AMap aMap;
    private DriveRouteResult mDriveRouteResult;
    private LatLonPoint mStartPoint = new LatLonPoint(0,0 );
//    private LatLonPoint mStartPoint = new LatLonPoint(39.959698, 116.300278);
    private LatLonPoint mEndPoint = new LatLonPoint(39.130527, 117.176994);
    private LatLng startPoint;
    private LatLng endPoint;
    private ProgressDialog progDialog = null;
    private DrivePath drivePath;
    private List<MovingPointOverlay> smoothMarkerList;
    private List<Marker> markerList;
    private int totalCarNum = 1;
    private int curNum = 0;
    private WarnDialog warnDialog;
    private List<LatLng> mLatLngsOfPath;
    private List<Marker> throughPointMarkerList = new ArrayList<Marker>();
    private Marker startMarker;
    private Marker endMarker;
    private float mWidth = 25; //????????????
    private boolean nodeIconVisible = true;
    private List<Marker> stationMarkers = new ArrayList<Marker>();
    private boolean isColorfulline = true;
    private PolylineOptions mPolylineOptions;
    private List<TMC> tmcs;
    private List<LatLonPoint> throughPointList;
    private List<Polyline> allPolyLines = new ArrayList<Polyline>();
    private PolylineOptions mPolylineOptionscolor = null;
    private CreateUserDialog createUserDialog;
    private ArrayList<TableRow> rows = new ArrayList<>();
    private LocationService locationService;
    private LatLonPoint preLocation;
    private LatLonPoint curLocation;
    private final String amapKey = "54e709ea82fec2e7e9eff79ffd51d1fc";

    public String getAdjustLocation(String url) {
        Log.e("start adjust GPS", url);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        //????????????Call
        final Call call = okHttpClient.newCall(request);
        final String[] location = {""};

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
            try {
                //????????????
                Response response = call.execute();
                Log.e("GPS response", response.body().toString());
                JSONObject resObject = JSONObject.parseObject(response.body().toString());
                location[0] = resObject.getString("locations");
            } catch (IOException e) {
                e.printStackTrace();
                location[0] = "";
            }
//                }
//            }).start();
        Log.e("new locations: ", location[0].toString());
        return location[0];
    }

//    public String getAdjustLocation(String url) {
//            Log.e("start adjust GPS", url);
//            OkHttpClient okHttpClient = new OkHttpClient();
//            final Request request = new Request.Builder().url(url).build();
//            //????????????Call
//            final Call call = okHttpClient.newCall(request);
//            final String[] location = {""};
//
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                    Log.e("failure", e.toString());
//                }
//
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        String result = response.body().string();
//                        Log.e("result", result);
//                        JSONObject resObject = JSONObject.parseObject(result);
//                        Log.e("resObject", String.valueOf(resObject));
//                        location[0] =  resObject.getString("locations");
//                    }
//                }
//            });
//            Log.e("new locations: ", location[0]);
//            return location[0];
//    }


//    public String getAdjustLocation(String url) {
//
//        OkHttpUtil.getInstance(getBaseContext()).requestGetByAsynByOwnUrl(url, new OkHttpUtil.ReqCallBack<String>() {
//            final String[] location = {""};
//
//            @Override
//            public void onReqSuccess(String result) {
//
//                Log.e("result", result);
//                JSONObject resObject = JSONObject.parseObject(result);
//                Log.e("resObject", String.valueOf(resObject));
//                location[0] =  resObject.getString("locations");
//            }
//
//            @Override
//            public void onReqFailed(String errorMsg) {
//                Log.e(TAG, errorMsg);
//                Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//            Log.e("start adjust GPS", url);
//            OkHttpClient okHttpClient = new OkHttpClient();
//            final Request request = new Request.Builder().url(url).build();
//            //????????????Call
//            final Call call = okHttpClient.newCall(request);
//            final String[] location = {""};
//
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                    Log.e("failure", e.toString());
//                }
//
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        String result = response.body().string();
//                        Log.e("result", result);
//                        JSONObject resObject = JSONObject.parseObject(result);
//                        Log.e("resObject", String.valueOf(resObject));
//                        location[0] =  resObject.getString("locations");
//                    }
//                }
//            });
//            Log.e("new locations: ", location[0]);
//            return location[0];
//    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate(MainActivity)");
        mainApplication = (MainApplication) getApplication();

        if(!mainApplication.isLogin()) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else {
            STATUS = true;
            setContentView(R.layout.activity_main);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ActivityManager meManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            BatteryManager batManager = (BatteryManager) getSystemService(BATTERY_SERVICE);

            //bind service
            Intent intent = new Intent(this,LocationService.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);


//        sharedPreferences = this.getSharedPreferences("PadApplication", Activity.MODE_PRIVATE);
//        _dataInterface=new DataAnalyser(this);

        if(mainApplication.getUserId()==null){
            mainApplication.setUserId("130307");
        }
            ButterKnife.bind(this);
            setmStartPoint();
            setTask();
            getCarPath();
            initBase();
            System.out.println("000000000000"+mainApplication.getCarRoute());
//            setAnomalousEvents();

            try {
                init(savedInstanceState,meManager,batManager);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        Log.i("deviceId",mainApplication.getDeviceNo());
        }
    }

    private androidx.appcompat.widget.Toolbar toolbar;
    private DrawerLayout drawer;
    private ImageView userImage;
    private TextView userName, userId;
    private Button logoutButton;
    private Vibrator vibrator;
    private void initBase() {

        Log.i(TAG, "initBase(MainActivity)");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        View headView = navigationView.getHeaderView(0);

        userName = headView.findViewById(R.id.user_name);
        userId = headView.findViewById(R.id.user_id);

//        logout = headView.findViewById(R.id.logout);
        logoutButton = (Button) headView.findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> header = new HashMap<>();
                header.put("token", mainApplication.getToken());
                HashMap<String, String> params = new HashMap<>();
                params.put("deviceNo", mainApplication.getDeviceNo());
                Log.i("????????????", params.get("deviceNo"));

                OkHttpUtil.getInstance(getBaseContext()).requestAsyn("/devices/logout", OkHttpUtil.TYPE_GET, params, header, new OkHttpUtil.ReqCallBack<String>() {
                    @Override

                    public void onReqSuccess(String result) {
                        if(JSON.parseObject(result).getInteger("code") == 200) {
                            Toast.makeText(getBaseContext(), JSON.parseObject(result).getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), JSON.parseObject(result).getString("message"), Toast.LENGTH_SHORT).show();

                        }
                        mainApplication.setLogin(false);
                        Intent intent = new Intent("com.gesoft.admin.loginout");
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Log.e(TAG, errorMsg);
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


//        else if(mainApplication.getUserName().equals("?????????")) {
//            userImage.setImageResource(R.mipmap.user_3);
        HashMap<String,String> header = new HashMap<>();
        header.put("token",mainApplication.getToken());
        userName.setText("?????????" + mainApplication.getUserName());
        userId.setText("???????????????" + mainApplication.getUserId());
        vibrator = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);

    }

    public void setSupportActionBar(@Nullable androidx.appcompat.widget.Toolbar toolbar) {
        this.getDelegate().setSupportActionBar(toolbar);
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //????????????MsgService??????
            locationService = ((LocationService.LocationBinder)service).getService();

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(Bundle savedInstanceState, ActivityManager meManager, BatteryManager batManager) throws InterruptedException {

        Thread.sleep(4000);
        String startPoints = mainApplication.getStartPoint();
        if(startPoints ==null){
            HashMap<String, String> params = new HashMap<>(1);

            HashMap<String, String> header = new HashMap<>(1);
            header.put("token",mainApplication.getToken());
            String url ="/tasks/getStartPoint";
            String activityTag = "get startPoint";
            final String[] location = {""};
        }
        setMap(savedInstanceState);

        topInfoBar();

//        System.out.println("9090==="+sHA1(this));



    }
    public static String sHA1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }



//    /*????????????????????? ????????????????????????*/
//    private View.OnClickListener dialogListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            String tables = mainApplication.getTableRow();
////            System.out.println("-----------------"+tables);
//            Map<String,String> row_id = MapString.getStringToMap(tables);
//            String id = row_id.get(String.valueOf(tableRow.getId()));
//            switch (v.getId()) {
//
//                case R.id.btn_save_pop:
//
//                    String description = createUserDialog.accident_des.getText().toString().trim();
//                    String state = createUserDialog.state.getText().toString().trim();
//                    createUserDialog.dismiss();
//                    TextView textView = (TextView) tableRow.getChildAt(3);
//                    String utl_comment = "/exceptions/comment";
//
//                    switch (state){
//                        case "???????????????":
//                            tableRow.setBackgroundColor(Color.parseColor("#C8628B34"));
//                            textView.setText("?????????");
//                            /**
//                             * ?????????????????????
//                             */
//                            String url = "/exceptions/changeState";
//                            HashMap<String, String> params = new HashMap<>(1);
//                            params.put("riskId", id);
//                            postInfo(url,params,"change state");
//
//
//                            params.put("comment", description);
//                            postInfo(utl_comment,params,"set comment");
//
//
//                            break;
//                        case "????????????":
//                            tableRow.setBackgroundColor(Color.parseColor("#D2834E10"));
//                            textView.setText("??????");
//                            String url_mis = "/exceptions/misdeclaration";
//                            /**
//                             * ??????????????????
//                             */
//                            HashMap<String, String> params_ = new HashMap<>(1);
//                            params_.put("riskId", id);
//                            postInfo(url_mis,params_,"mis-declaration");
//
//                            params_.put("comment", description);
//                            postInfo(utl_comment,params_,"set comment");
//
//                            break;
//                    }
//                    /**
//                     * * ????????????
//                     *  * ??????tablerow
//                     */
////                    System.out.println(description+"??????"+state);
//                    break;
//
//            case R.id.btn_exit_pop:
//                createUserDialog.dismiss();
//                break;
//        }
//        }
//    };
//    /*????????????*/
//    public void showEditDialog(View view) {
//        createUserDialog = new CreateUserDialog(this,R.style.AppTheme,tableRow, dialogListener);
//
//
//        createUserDialog.show();
//    }

//    //????????????end
    /**
     * ??????bar
     */
    private void topInfoBar(){
        ImageView run_img = findViewById(R.id.run_img);
        ImageView walk_img = findViewById(R.id.walk_img);
        ImageView lie_img = findViewById(R.id.lie_img);

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        TextView hight = findViewById(R.id.hight_text);
        TextView step = findViewById(R.id.step_text);
        TextView state = findViewById(R.id.state_text);
        TextView time1 =findViewById(R.id.time_t1);
        TextView time2 = findViewById(R.id.time_t2);
        TextView weather = findViewById(R.id.weather_type);
        TextView temp = findViewById(R.id.temp);
//        TextView longi = findViewById(R.id.longitude);
//        TextView lagi = findViewById(R.id.latitude);
        List<LatLng> latLngs = new ArrayList<LatLng>();
        Handler location_handler = new Handler();
        Runnable runnable = new Runnable(){
            @SuppressLint("SetTextI18n")
            @Override
            public void run(){
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                BitmapDescriptor mBlueTexture = BitmapDescriptorFactory
                        .fromAsset("icon_road_blue_arrow.png");
                time1.setText(year+"/"+month+"/"+day);
                time2.setText(hour+":"+minute    );
                if(locationService!=null){
                    Result = locationService.getLocation();
                    if (!(Result.isEmpty()&&Result.equals(""))) {
                        //??????:116.40 ??????:39.90
                        JSONObject jsonObj = JSON.parseObject(Result);
                        Boolean isConnection = jsonObj.getBoolean("gpsConnection");
                        String method = jsonObj.getString("method");
//                        ((TextView)findViewById(R.id.indoor)).setText(String.valueOf(isConnection)+"===="+method);
                        if(isConnection){
                            latitude_v = Double.valueOf(jsonObj.getString("latitude"));
                            longitude_v = Double.valueOf(jsonObj.getString("longitude"));


                            //upload location
                            HashMap<String,String> params = new HashMap<>();
                            params.put("deviceNo",mainApplication.getDeviceNo());
                            params.put("longitude", String.valueOf(longitude_v));
                            params.put("latitude", String.valueOf(latitude_v));
                            params.put("height",jsonObj.getString("height"));
                            if(method.equals("PDR")){
                                params.put("isIndoor",String.valueOf(true));
                            }else {
                                params.put("isIndoor",String.valueOf(false));
                            }
                            curLocation = new LatLonPoint(latitude_v,longitude_v);
                            Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(latitude_v,longitude_v)).snippet("DefaultMarker"));


                            String locationStr = Double.toString(longitude_v) + "," + Double.toString(latitude_v);

                            String adjustGpsUrl = "https://restapi.amap.com/v3/assistant/coordinate/convert?key=" + amapKey + "&locations=" + locationStr + "&coordsys=gps";
                            Log.e("adjustUrl", adjustGpsUrl);
//                            String location = getAdjustLocation(adjustGpsUrl);
//                            Log.e("location111", location);
//                            final String[] location = {""};
                            final double[] newLatitude_v = {latitude_v};
                            final double[] newLongitude_v = {longitude_v};
                            OkHttpUtil.getInstance(getBaseContext()).requestGetByAsynByOwnUrl(adjustGpsUrl, new OkHttpUtil.ReqCallBack<String>() {
                                @Override
                                public void onReqSuccess(String result) {

                                    Log.e("result", result);
                                    String[] pos = result.split(",");
                                    Log.e("pos[0]", pos[0]);
                                    Log.e("pos[1]", pos[1]);
                                    newLongitude_v[0] = Double.parseDouble(pos[0]);
                                    newLatitude_v[0] = Double.parseDouble(pos[1]);
                                    latLngs.add(new LatLng(newLatitude_v[0],newLongitude_v[0]));
                                }

                                @Override
                                public void onReqFailed(String errorMsg) {
                                    Log.e(TAG, errorMsg);
                                    Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                                    latLngs.add(new LatLng(newLatitude_v[0],newLongitude_v[0]));
                                }
                            });
//                            String[] pos = location.split(",");

//                            Log.e("pos[0]", pos[0]);
//                            Log.e("pos[1]", pos[1]);
//                            newLongitude_v[0] = Double.parseDouble(pos[0]);
//                            newLatitude_v[0] = Double.parseDouble(pos[1]);

//                            latLngs.add(new LatLng(newLatitude_v[0],newLongitude_v[0]));
//                            Log.e("GPS lat1111",latLngs.get(0).toString());
//                            Log.e("GPS lng2222",latLngs.get(latLngs.size() - 1).toString());
//                            LatLngBounds bounds = new LatLngBounds(latLngs.get(0), latLngs.get(latLngs.size() - 1));
//                            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                            aMap.clear();
                            aMap.addPolyline(new PolylineOptions().
                                    addAll(latLngs).width(10).setUseTexture(true).setCustomTexture(mBlueTexture));
//                            preLocation = new LatLonPoint(latitude_v,longitude_v);
                            if(latLngs.size()>2){

                                SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
// ?????????????????????
                                smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.icon_car));

                                LatLng drivePoint = latLngs.get(latLngs.size() - 2);
                                Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(latLngs, drivePoint);
                                latLngs.set(pair.first, drivePoint);
                                List<LatLng> subList = latLngs.subList(pair.first, latLngs.size());

// ??????????????????????????????
                                smoothMarker.setPoints(subList);
// ????????????????????????
                                smoothMarker.setTotalDuration(1);
                                // ????????????
                                smoothMarker.startSmoothMove();
                            }


                            HashMap<String,String> header = new HashMap<>();
                            header.put("token",mainApplication.getToken());
                            String url = "devices/upload2";

                            postInfo(url,params,"upload location value");

                        }
//
//                        getWeatherFromHeAPI(
//                                nf.format(longitude_v),
//                                nf.format(latitude_v)
//                        );
                        if (now != null ) {
                            weather.setText(now.getCond_txt());
                            temp.setText(now.getTmp()+"??");
                        }


                        hight.setText(jsonObj.getString("height")+"M");
                        step.setText(jsonObj.getString("stepCounter")+"???");
                        String sta = jsonObj.getString("state");
                        switch (sta){
                            case "walk":
                                state.setText("???");
                                walk_img.setImageDrawable((getResources().getDrawable(R.drawable.walk_a)));
                                lie_img.setImageDrawable((getResources().getDrawable(R.drawable.tang_ina)));
                                run_img.setImageDrawable((getResources().getDrawable(R.drawable.run_ina)));
                                break;
                            case "run":
                                state.setText("???");
                                run_img.setImageDrawable((getResources().getDrawable(R.drawable.run_a)));
                                walk_img.setImageDrawable((getResources().getDrawable(R.drawable.walk_ina)));
                                lie_img.setImageDrawable((getResources().getDrawable(R.drawable.tang_ina)));
                                break;
                            case "lie":
                                state.setText("???");
                                lie_img.setImageDrawable(getResources().getDrawable(R.drawable.tang_a));
                                run_img.setImageDrawable(getResources().getDrawable(R.drawable.run_ina));
                                walk_img.setImageDrawable(getResources().getDrawable(R.drawable.walk_ina));
                                break;
                            case "other":
                                state.setText("??????");
                                run_img.setImageDrawable((getResources().getDrawable(R.drawable.run_ina)));
                                walk_img.setImageDrawable((getResources().getDrawable(R.drawable.walk_ina)));
                                lie_img.setImageDrawable((getResources().getDrawable(R.drawable.tang_ina)));
                                break;
                        }

                    }
                }
                else {
                    Log.d(TAG,"?????????");
                }

//                System.out.println("===result"+Result);

                location_handler.postDelayed(this, 1000);
            }
        };
        location_handler.post(runnable);
    }



    /**  /////////////////////////////////////////////////////////////////////////
     *Map
     ///////////////////////////////////////////////////////////////////////// **/
    private void  setMap(Bundle savedInstanceState){
        mapView = findViewById(R.id.map_view);

        mapView.onCreate(savedInstanceState);
        mainApplication = (MainApplication) getApplication();
        Log.i(TAG, "init(MapActivity)");

        if (aMap == null) {
            aMap = mapView.getMap();
        }
//        aMap.getUiSettings().setZoomControlsEnabled(false);
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        aMap.setMapTextZIndex(2);
//        setUpMap();

        aMap.clear();// ?????????????????????????????????

    }


    private AMapLocationClientOption mLocationOption;
    private void setUpMap() {
        /**
         * ????????????amap?????????
         */
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);// ???????????????????????????
        uiSettings.setZoomControlsEnabled(true);// ??????????????????????????????
        uiSettings.setScaleControlsEnabled(true);// ???????????????????????????
        uiSettings.setRotateGesturesEnabled(true);// ??????????????????????????????
        uiSettings.setTiltGesturesEnabled(true);// ??????????????????????????????
        uiSettings.setMyLocationButtonEnabled(false);// ????????????????????????????????????

        /** ??????????????????????????????
         *
         */
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// ????????????????????????????????????
        aMap.setMyLocationEnabled(true);// ?????????true??????????????????????????????????????????false??????????????????????????????????????????????????????false


    }

    private double distance;
    /**
     * ??????????????????
     *
     *
     */
    public void drawLines(LatLonPoint curLocation) {

        if (preLocation==null ) {
            return;
        }
        System.out.println(preLocation.toString()+"9090909");
        PolylineOptions options = new PolylineOptions();
        //????????????????????????
        options.add(new LatLng(preLocation.getLatitude(), preLocation.getLongitude()));
        //??????????????????
        options.add(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()));
        options.width(10).geodesic(true).color(Color.GREEN);
        aMap.addPolyline(options);
        //???????????????
        distance = AMapUtils.calculateLineDistance(new LatLng(preLocation.getLatitude(),
                preLocation.getLongitude()), new LatLng(curLocation.getLatitude(),
                curLocation.getLongitude()));

    }

    /**
     * ?????????????????????
     */
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    /**
     * ?????????????????????
     */
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    /**
     * ?????????????????????
     */
    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

//    private void setMap(Bundle savedInstanceState){
//        mapView = findViewById(R.id.map_view);
//        mapView.onCreate(savedInstanceState);
//        mainApplication = (MainApplication) getApplication();
//        Log.i(TAG, "init(MapActivity)");
//
//        if (aMap == null) {
//            aMap = mapView.getMap();
//        }
//        dissmissProgressDialog();
//        aMap.clear();// ?????????????????????????????????
//        //????????????marker????????????
//        setNodeIconVisibility(false);
//        // ????????????????????????????????????????????????true
//        setIsColorfulline(true);
//        removeFromMap();
//        addToMap();
//        zoomToSpan();
//        plotLine(aMap);
//        markerList = new ArrayList<>();
//        smoothMarkerList = new ArrayList<>();
//        for (int i = 0; i < totalCarNum; i++) {
//            markerList.add(aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_car)).anchor(0.5f, 0.5f)));
//            smoothMarkerList.add(new MovingPointOverlay(aMap, markerList.get(markerList.size() - 1)));
//        }
//
////        LatLng drivePoint = mLatLngsOfPath.get(0);
////        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(mLatLngsOfPath, drivePoint);
////        mLatLngsOfPath.set(pair.first, drivePoint);
////        for (int i = 0; i  < smoothMarkerList.size(); i++){
////            smoothMarkerList.get(i).setPoints(mLatLngsOfPath.subList(pair.first + i*4, mLatLngsOfPath.size()));
////            smoothMarkerList.get(i).setTotalDuration(1000 - i*10);
////        }
////        for (MovingPointOverlay sm : smoothMarkerList){
////            sm.startSmoothMove();
////        }
////        // ??????  ????????????InfoWindow ?????????
////        aMap.setInfoWindowAdapter(infoWindowAdapter);
////        // ?????? infowindow
////        markerList.get(0).showInfoWindow();
////        String cName = getCriminal().getName();
////        String uName = mainApplication.getUserName();
////        String carNo = "";
////        if(!getTask().equals(null)){
////            carNo = getTask().getCarNo();
////        }
////
////        // ???????????????????????????  ?????? ??????????????????  ?????? ???
////        String finalCName = cName;
////        String finalCarNo = carNo;
////        smoothMarkerList.get(0).setMoveListener(new MovingPointOverlay.MoveListener() {
////            @SuppressLint("SetTextI18n")
////            @Override
////            public void move(double v) {
////                runOnUiThread(() -> {
////                    if(title != null){
////
////                        title.setText( "????????????????????? " + (int) v + "???");
////                        prisoner_icon.setImageResource(R.mipmap.prisoner_6);
////                        prisoner_name.setText(cName);
////                        police_name.setText(uName);
////                        car_no.setText(finalCarNo);
////
////
////                    }
////                });
////            }
////        });
//
//    }
//    private Polyline polyline;
//    private void plotLine( AMap aMap){
//        List<LatLng> latLngs = new ArrayList<>();
//        latLngs.add(new LatLng(39.999391,116.135972));
//        latLngs.add(new LatLng(39.898323,116.057694));
////                    carRoute.add(new LatLng(39.900430,116.265061));
////                    carRoute.add(new LatLng(39.955192,118.140092));
////        List<Point> list = JSON.parseArray(mainApplication.getCarRoute(), Point.class);
////        for(Point p  : list){
////            latLngs.add(new LatLng( p.getLatitude(),p.getLongitude()));
////        }
//
//        polyline =aMap.addPolyline(new PolylineOptions().
//                addAll(latLngs).setUseTexture(true).setCustomTexture(
//                        BitmapDescriptorFactory.fromResource(R.drawable.amap_route_color_texture_1_arrow)) //setCustomTextureList(bitmapDescriptors)
//                .useGradient(true)
//                .width(10));
//
//    }
////    private void setMap(Bundle savedInstanceState){
////        mapView = findViewById(R.id.map_view);
////        mapView.onCreate(savedInstanceState);
////        mainApplication = (MainApplication) getApplication();
////        Log.i(TAG, "init(MapActivity)");
////
////        if (aMap == null) {
////            aMap = mapView.getMap();
////        }
////        RouteSearch routeSearch = new RouteSearch(this);
////        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
////            @Override
////            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
////
////            }
////
////            @Override
////            public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
////                dissmissProgressDialog();
////                aMap.clear();// ?????????????????????????????????
////                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
////                    if (result != null && result.getPaths() != null) {
////                        if (result.getPaths().size() > 0) {
////                            mDriveRouteResult = result;
////                            drivePath = mDriveRouteResult.getPaths().get(0);
////                            //????????????marker????????????
////                            setNodeIconVisibility(false);
////                            // ????????????????????????????????????????????????true
////                            setIsColorfulline(true);
////                            removeFromMap();
////                            addToMap();
////                            zoomToSpan();
////                            markerList = new ArrayList<>();
////                            smoothMarkerList = new ArrayList<>();
////                            for (int i = 0; i < totalCarNum; i++) {
////                                markerList.add(aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_car)).anchor(0.5f, 0.5f)));
////                                smoothMarkerList.add(new MovingPointOverlay(aMap, markerList.get(markerList.size() - 1)));
////                            }
////                            LatLng drivePoint = mLatLngsOfPath.get(0);
////                            Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(mLatLngsOfPath, drivePoint);
////                            mLatLngsOfPath.set(pair.first, drivePoint);
////                            for (int i = 0; i  < smoothMarkerList.size(); i++){
////                                smoothMarkerList.get(i).setPoints(mLatLngsOfPath.subList(pair.first + i*4, mLatLngsOfPath.size()));
////                                smoothMarkerList.get(i).setTotalDuration(1000 - i*10);
////                            }
////                            for (MovingPointOverlay sm : smoothMarkerList){
////                                sm.startSmoothMove();
////                            }
////                            // ??????  ????????????InfoWindow ?????????
////                            aMap.setInfoWindowAdapter(infoWindowAdapter);
////                            // ?????? infowindow
////                            markerList.get(0).showInfoWindow();
////                            String cName = getCriminal().getName();
////                            String uName = mainApplication.getUserName();
////                            String carNo = "";
////                            if(!getTask().equals(null)){
////                                carNo = getTask().getCarNo();
////                            }
////                            // ???????????????????????????  ?????? ??????????????????  ?????? ???
////                            String finalCName = cName;
////                            String finalCarNo = carNo;
////                            smoothMarkerList.get(0).setMoveListener(new MovingPointOverlay.MoveListener() {
////                                @SuppressLint("SetTextI18n")
////                                @Override
////                                public void move(double v) {
////                                    runOnUiThread(() -> {
////                                        if(title != null){
////
////                                            title.setText( "????????????????????? " + (int) v + "???");
////                                            prisoner_icon.setImageResource(R.mipmap.prisoner_6);
////                                            prisoner_name.setText(cName);
////                                            police_name.setText(uName);
////                                            car_no.setText(finalCarNo);
////
////
////                                        }
////                                    });
////                                }
////                            });
////                        } else if (result.getPaths() == null) {
////                            Toast.makeText(getApplicationContext(), R.string.no_result, Toast.LENGTH_LONG).show();
////                        }
////                    } else {
////                        Toast.makeText(getApplicationContext(), R.string.no_result,Toast.LENGTH_LONG).show();
////                    }
////                } else {
////                    Toast.makeText(getApplicationContext(), "errorCode???" + errorCode, Toast.LENGTH_LONG).show();
////                }
////            }
////
////            @Override
////            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
////
////            }
////
////            @Override
////            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
////
////            }
////        });
////        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
////        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, 2, null, null, "");
////        routeSearch.calculateDriveRouteAsyn(query);
////    }
//    private void showMessage(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
//    private void showDialog() {
//        if(warnDialog == null) {
//            warnDialog = WarnDialog.showDialog(this, "");
//        }
//        warnDialog.show();
//    }
//    private void destroyDialog() {
//        if(warnDialog != null) {
//            warnDialog.dismiss();
//        }
//    }
//
//    /**
//     *  ??????????????????????????????????????????
//     *  ?????????????????????????????????????????????????????????getInfoWindow(Marker)?????????
//     *  ???????????????????????????????????????????????????????????????getInfoContents(Marker)?????????
//     */
//    private TextView title, prisoner_name, police_name, car_no;
//    private ImageView prisoner_icon;
//    private AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter(){
//        // ?????????Marker???InfoWindow ??????
//        // ????????????????????????null??????????????????????????????????????????????????????????????????getInfoContents(Marker)????????????
//        @Override
//        public View getInfoWindow(Marker marker) {
//            View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window,null);
//            render(infoWindow);
//            return infoWindow;
//        }
//        // ?????????????????????getInfoWindow(Marker)??????null ??????????????????
//        // ????????????view ?????????????????????????????????????????????null ????????????????????????
//        @Override
//        public View getInfoContents(Marker marker) {
//            return null;
//        }
//    };
//    @SuppressLint("SetTextI18n")
//    private void render(View view) {
//        //????????????????????????Infow?????????????????????view??????????????????
//        title = view.findViewById(R.id.title);
//        prisoner_icon = view.findViewById(R.id.escort_image);
//        prisoner_name = view.findViewById(R.id.prisoner_name);
//        police_name = view.findViewById(R.id.police_name);
//        car_no = view.findViewById(R.id.car_no);
//        title.setText("????????????????????? " + " " + "???");
//        prisoner_icon.setImageResource(R.mipmap.escort_dog);
//        prisoner_name.setText("");
//        police_name.setText("");
//        car_no.setText("");
//    }
//
//    public void showPreCarInfo(View view){
//        if(aMap!=null && markerList.size()!= 0){
//            curNum = (curNum + totalCarNum+1)%totalCarNum;
//            markerList.get(curNum).showInfoWindow();
//        }
//    }
//
//    public void showNextCarInfo(View view){
//        if(aMap!=null && markerList.size()!= 0){
//            curNum = (curNum + totalCarNum-1)%totalCarNum;
//            markerList.get(curNum).showInfoWindow();
//        }
//    }
//
//    /**
//     * ???????????????
//     */
//    private void dissmissProgressDialog() {
//        if (progDialog != null) {
//            progDialog.dismiss();
//        }
//    }
//
//    private void removeFromMap() {
//        try {
//            if (startMarker != null) {
//                startMarker.remove();
//            }
//            if (endMarker != null) {
//                endMarker.remove();
//            }
//            for (Marker marker : stationMarkers) {
//                marker.remove();
//            }
//            for (Polyline line : allPolyLines) {
//                line.remove();
//            }
//            if (this.throughPointMarkerList != null
//                    && this.throughPointMarkerList.size() > 0) {
//                for (Marker marker : this.throughPointMarkerList) {
//                    marker.remove();
//                }
//                this.throughPointMarkerList.clear();
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void zoomToSpan() {
//        if (mStartPoint != null) {
//            if (aMap == null){
//                return;
//            }
//            try {
//                LatLngBounds bounds = getLatLngBounds();
//                aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private LatLngBounds getLatLngBounds() {
//        LatLngBounds.Builder b = LatLngBounds.builder();
//        b.include(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()));
//        b.include(new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()));
//        return b.build();
//    }
//
//    private void addStationMarker(MarkerOptions options) {
//        if(options == null) {
//            return;
//        }
//        Marker marker = aMap.addMarker(options);
//        if(marker != null) {
//            stationMarkers.add(marker);
//        }
//    }
//
//    /**
//     * ???????????????????????????????????????
//     * @param visible true????????????????????????false???????????????
//     * @since V2.3.1
//     */
//    private void setNodeIconVisibility(boolean visible) {
//        try {
//            nodeIconVisible = visible;
//            if (stationMarkers != null && stationMarkers.size() > 0) {
//                for (Marker stationMarker : stationMarkers) {
//                    stationMarker.setVisible(visible);
//                }
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setIsColorfulline(boolean iscolorfulline) {
//        this.isColorfulline = iscolorfulline;
//    }
//
//    /**
//     * ?????????????????????????????????????????????
//     */
//    private void addToMap() {
//        initPolylineOptions();
//        try {
//            if (aMap == null) {
//                return;
//            }
//            if (mWidth == 0 || drivePath == null) {
//                return;
//            }
//            mLatLngsOfPath = new ArrayList<LatLng>();
//            tmcs = new ArrayList<TMC>();
//            List<DriveStep> drivePaths = drivePath.getSteps();
//            startPoint = convertToLatLng(mStartPoint);
//            endPoint = convertToLatLng(mEndPoint);
//            mPolylineOptions.add(startPoint);
//            for (DriveStep step : drivePaths) {
//                List<LatLonPoint> latlonPoints = step.getPolyline();
//                List<TMC> tmclist = step.getTMCs();
//                tmcs.addAll(tmclist);
//                addDrivingStationMarkers(step, convertToLatLng(latlonPoints.get(0)));
//                for (LatLonPoint latlonpoint : latlonPoints) {
//                    mPolylineOptions.add(convertToLatLng(latlonpoint));
//                    mLatLngsOfPath.add(convertToLatLng(latlonpoint));
//                }
//            }
//            mPolylineOptions.add(endPoint);
//            if (startMarker != null) {
//                startMarker.remove();
//                startMarker = null;
//            }
//            if (endMarker != null) {
//                endMarker.remove();
//                endMarker = null;
//            }
//            addStartAndEndMarker();
//            addThroughPointMarker();
//            if (isColorfulline && tmcs.size()>0 ) {
//                colorWayUpdate(tmcs);
//            }else {
//                addPolyLine(mPolylineOptions);
//            }
//
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * ??????????????????????????????????????????????????????
//     * @param tmcSection is
//     */
//    private void colorWayUpdate(List<TMC> tmcSection) {
//        if (aMap == null) {
//            return;
//        }
//        if (tmcSection == null || tmcSection.size() <= 0) {
//            return;
//        }
//        TMC segmentTrafficStatus;
//        addPolyLine(new PolylineOptions().add(startPoint,
//                convertToLatLng(tmcSection.get(0).getPolyline().get(0)))
//                .setDottedLine(true));
//        String status = "";
//        for (int i = 0; i < tmcSection.size(); i++) {
//            segmentTrafficStatus = tmcSection.get(i);
//            List<LatLonPoint> mployline = segmentTrafficStatus.getPolyline();
//            if (status.equals(segmentTrafficStatus.getStatus())) {
//                for (int j = 1; j < mployline.size(); j++) {
//                    //?????????????????????????????????????????????????????????????????????
//                    mPolylineOptionscolor.add(convertToLatLng(mployline.get(j)));
//                }
//            }else {
//                if (mPolylineOptionscolor != null) {
//                    addPolyLine(mPolylineOptionscolor.color(getColor(status)));
//                }
//                mPolylineOptionscolor = null;
//                mPolylineOptionscolor = new PolylineOptions().width(mWidth);
//                status = segmentTrafficStatus.getStatus();
//                for (LatLonPoint latLonPoint : mployline) {
//                    mPolylineOptionscolor.add(convertToLatLng(latLonPoint));
//                }
//            }
//            if (i == tmcSection.size()-1 && mPolylineOptionscolor != null) {
//                addPolyLine(mPolylineOptionscolor.color(getColor(status)));
//                addPolyLine(new PolylineOptions().add(
//                        convertToLatLng(mployline.get(mployline.size()-1)), endPoint)
//                        .setDottedLine(true));
//            }
//        }
//    }
//
//    private int getColor(String status) {
//        switch (status) {
//            case "??????":
//                return Color.GREEN;
//            case "??????":
//                return Color.YELLOW;
//            case "??????":
//                return Color.RED;
//            case "????????????":
//                return Color.parseColor("#990033");
//            default:
//                return Color.parseColor("#537edc");
//        }
//    }
//
//    private void addPolyLine(PolylineOptions options) {
//        if(options == null) {
//            return;
//        }
//        Polyline polyline = aMap.addPolyline(options);
//        if(polyline != null) {
//            allPolyLines.add(polyline);
//        }
//    }
//
//    private void addThroughPointMarker() {
//        if (this.throughPointList != null && this.throughPointList.size() > 0) {
//            LatLonPoint latLonPoint;
//            for (LatLonPoint lonPoint : this.throughPointList) {
//                latLonPoint = lonPoint;
//                if (latLonPoint != null) {
//                    boolean throughPointMarkerVisible = true;
//                    throughPointMarkerList.add(aMap
//                            .addMarker((new MarkerOptions())
//                                    .position(
//                                            new LatLng(latLonPoint
//                                                    .getLatitude(), latLonPoint
//                                                    .getLongitude()))
//                                    .visible(throughPointMarkerVisible)
//                                    .icon(getThroughPointBitDes())
//                                    .title("\u9014\u7ECF\u70B9")));
//                }
//            }
//        }
//    }
//
//    private BitmapDescriptor getThroughPointBitDes() {
//        return BitmapDescriptorFactory.fromResource(R.mipmap.amap_through);
//    }
//
//    private void addStartAndEndMarker() {
//        startMarker = aMap.addMarker((new MarkerOptions())
//                .position(startPoint).icon(getStartBitmapDescriptor())
//                .title("\u8D77\u70B9"));
//        endMarker = aMap.addMarker((new MarkerOptions()).position(endPoint)
//                .icon(getEndBitmapDescriptor()).title("\u7EC8\u70B9"));
//    }
//
//    /**
//     * ?????????Marker????????????????????????????????????????????????????????????????????????????????????????????????
//     * @return ?????????Marker?????????
//     * @since V2.1.0
//     */
//    private BitmapDescriptor getStartBitmapDescriptor() {
//        return BitmapDescriptorFactory.fromResource(R.mipmap.amap_start);
//    }
//    private BitmapDescriptor getEndBitmapDescriptor() {
//        return BitmapDescriptorFactory.fromResource(R.mipmap.amap_end);
//    }
//    private BitmapDescriptor getDriveBitmapDescriptor() {
//        return BitmapDescriptorFactory.fromResource(R.mipmap.amap_car);
//    }
//
//    private void addDrivingStationMarkers(DriveStep driveStep, LatLng latLng) {
//        addStationMarker(new MarkerOptions()
//                .position(latLng)
//                .title("\u65B9\u5411:" + driveStep.getAction()
//                        + "\n\u9053\u8DEF:" + driveStep.getRoad())
//                .snippet(driveStep.getInstruction()).visible(nodeIconVisible)
//                .anchor(0.5f, 0.5f).icon(getDriveBitmapDescriptor()));
//    }
//
//    private static LatLng convertToLatLng(LatLonPoint latLonPoint) {
//        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
//    }
//
//    private void initPolylineOptions() {
//        mPolylineOptions = null;
//        mPolylineOptions = new PolylineOptions();
//        mPolylineOptions.color(Color.parseColor("#537edc")).width(18f);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.i(TAG, "onActivityResult(Map)");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.i(TAG, "onStart(Map)");
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.i(TAG, "onDestroy(Map)");
//        if(smoothMarkerList.size() > 0) {
//            for (MovingPointOverlay movingPointOverlay : smoothMarkerList) {
//                if (movingPointOverlay != null) {
//                    movingPointOverlay.setMoveListener(null);
//                    movingPointOverlay.destroy();
//                }
//            }
//        }
//        try{
//            mapView.onDestroy();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.i(TAG, "onPause(Map)");
//
//        try{
//            mapView.onPause();
//         } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.i(TAG, "onResume(Map)");
//        try{
//            mapView.onResume();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.i(TAG, "onRestart(Map)");
//
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        try{
//            mapView.onSaveInstanceState(outState);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }


    /**
     * ????????????
     * @param url ???????????????
     * @param params ????????????
     * @param activityTag ??????????????????
     */
    private void postInfo(String url, HashMap<String, String> params,String activityTag) {
        HashMap<String, String> header = new HashMap<>(1);
        header.put("token",mainApplication.getToken());

        OkHttpUtil.getInstance(getBaseContext()).requestAsyn(url, OkHttpUtil.TYPE_POST_FORM, params, header,new OkHttpUtil.ReqCallBack<String>() {
            @Override
            public void onReqSuccess(String result) {
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                Log.i(TAG, activityTag+result+currentDateTimeString);
            }

            @Override
            public void onReqFailed(String errorMsg) {
//                Toast.makeText(getApplicationContext(),activityTag+"??????", Toast.LENGTH_LONG).show();
//
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                Log.e(TAG, activityTag+errorMsg+currentDateTimeString);

            }
        });
    }

    private void setmStartPoint() {


        HashMap<String, String> params = new HashMap<>(1);

        HashMap<String, String> header = new HashMap<>(1);
        header.put("token",mainApplication.getToken());
        String url ="/tasks/getStartPoint";
        String activityTag = "get startPoint";
        final String[] location = {""};
//        OkHttpUtil.getInstance(getBaseContext()).requestAsyn(url, OkHttpUtil.TYPE_GET, params,header, new OkHttpUtil.ReqCallBack<String>() {
//            @Override
//            public void onReqSuccess(String result) {
//                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
//
//                if(!result.isEmpty()){
//
//                    Log.i(TAG,activityTag+ result + currentDateTimeString);
////
//                    JSONObject object = JSON.parseObject(result);
//                    location[0] = object.getString("data");
//                    location[0] = location[0].substring(1, location[0].length()-1);
//                    mainApplication.setStartPoint(location[0]);
//
//                }
//            }
//
//            @Override
//            public void onReqFailed(String errorMsg)
//            {
//                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
//                Log.e(TAG, activityTag+errorMsg+currentDateTimeString);
//            }
//        });

    }
    private List<LatLng> getRoughPath() {
        List<LatLng> briefPoint = new ArrayList<>();

        HashMap<String, String> params = new HashMap<>(1);

        HashMap<String, String> header = new HashMap<>(1);
        header.put("token",mainApplication.getToken());
        String url ="/tasks/getRouteBrief";
        String activityTag = "get startPoint";

//        OkHttpUtil.getInstance(getBaseContext()).requestAsyn(url, OkHttpUtil.TYPE_GET, params,header, new OkHttpUtil.ReqCallBack<String>() {
//            @Override
//            public void onReqSuccess(String result) {
//                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
//
//                if(!result.isEmpty()){
//
//                    Log.i(TAG,activityTag+ result + currentDateTimeString);
////
//                    List<Point> list = JSON.parseArray(result, Point.class);
//                    for(Point p  : list){
//                        briefPoint.add(new LatLng( p.getLatitude(),p.getLongitude()));
//                    }
//                }
//            }
//
//            @Override
//            public void onReqFailed(String errorMsg)
//            {
//                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
//                Log.e(TAG, activityTag+errorMsg+currentDateTimeString);
//            }
//        });
        System.out.println("909090"+briefPoint.toString());
        return briefPoint;

    }
    List<LatLng> carRoute = new ArrayList<>();
    private void getCarPath() {


        HashMap<String, String> params = new HashMap<>(1);

        HashMap<String, String> header = new HashMap<>(1);
        header.put("token",mainApplication.getToken());
        String url ="/tasks/getCarRouteByPhone";
        String activityTag = "get car Route";

//        OkHttpUtil.getInstance(getBaseContext()).requestAsyn(url, OkHttpUtil.TYPE_GET, params,header, new OkHttpUtil.ReqCallBack<String>() {
//            @Override
//            public void onReqSuccess(String result) {
//                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
//
//                if(!result.isEmpty()){
//
//                    Log.i(TAG,activityTag+ result + currentDateTimeString);
//                    JSONObject object = JSON.parseObject(result);
//
//                    String str = object.getString("data");
//
//                    mainApplication.setCarRoute(str);
////
//
////                    carRoute.add(new LatLng(39.999391,116.135972));
////                    carRoute.add(new LatLng(39.898323,116.057694));
////                    carRoute.add(new LatLng(39.900430,116.265061));
////                    carRoute.add(new LatLng(39.955192,118.140092));
//                }
//            }
//
//            @Override
//            public void onReqFailed(String errorMsg)
//            {
//                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
//                Log.e(TAG, activityTag+errorMsg+currentDateTimeString);
//            }
//        });
    }

    private Task getTask(){
        Task task_ = new Task();
        task_ = task_.String2Task(mainApplication.getTask_()) ;
        return task_;
    }
    private void setTask(){
        Task task_ = new Task();
        HashMap<String, String> params = new HashMap<>(1);

        params.put("userName",mainApplication.getUserName());
        HashMap<String, String> header = new HashMap<>(1);
        header.put("token",mainApplication.getToken());
        String url = "tasks/deviceGetTasks";
        String activityTag = "get user task from service";
//        OkHttpUtil.getInstance(getBaseContext()).requestAsyn(url, OkHttpUtil.TYPE_GET, params,header, new OkHttpUtil.ReqCallBack<String>() {
//            @Override
////            "taskNo":"BJ012548","level":"1","carNo":null,"detail":"??????????????????????????????",
////            "createAt":"2020-10-02 11:25:00.0","startEscortTime":"2020-10-02 03:25:13.0",
////            "endEscortTime":"2020-10-02 10:25:17.0","startingPoint":"?????????","endingPoint":"?????????","escortRoute":"1"}
//            public void onReqSuccess(String result) {
//                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
////                System.out.println(result+"task");
//                if(!result.isEmpty()){
//                    Log.i(TAG,activityTag+ result + currentDateTimeString);
//                    JSONObject jsonObj = JSON.parseObject(result);
//                    Criminal p = new Criminal();
//                    p = p.String2Criminal(mainApplication.getCriminalInfo());
//
//                    task_.setUserName(mainApplication.getUserName());
//                    task_.setTaskNo(jsonObj.getString("taskNo"));
//                    task_.setPrisonerName(p.getName());
//
//                    task_.setCarNo(jsonObj.getString("carNo"));
//                    task_.setLevel(jsonObj.getString("level"));
//                    task_.setDetail(jsonObj.getString("detail"));
//
//                    mainApplication.setTask_(task_);
//                }
//            }
//
//            @Override
//            public void onReqFailed(String errorMsg)
//            {
//                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
//                Log.e(TAG, activityTag+errorMsg+currentDateTimeString);
//            }
//        });

    }

    /** //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     *  ?????? ??????
     * @param longitude_ is
     * @param latitude_ is
     */
    private void getWeatherFromHeAPI(String longitude_, String latitude_){
        /*  ????????????????????????username???key  */
        HeConfig.init("HE2002151946381825", "1aa9d204d8e5431896914ec51bb2582d");
        HeConfig.switchToFreeServerNode();
        HeWeather.getWeatherNow(MainActivity.this,longitude_+","+latitude_,
                Lang.CHINESE_SIMPLIFIED , Unit.METRIC ,
                new HeWeather.OnResultWeatherNowBeanListener() {
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "Weather Now onError: ", e);
                    }

                    @Override
                    public void onSuccess(Now dataObject) {
                        /*  ???????????????????????????json??????  */
                        //??????????????????status??????????????????status???????????????????????????status?????????????????????status?????????Code???????????????
                        if ( Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus()) ){
                            //??????????????????
                            Log.i(TAG, "//weather//"+"get data successful");
                            /* ??????now???????????????????????? , ????????????SDK???????????????  */
                            now = dataObject.getNow();

                        } else {
                            //???????????????????????????????????????
                            String status = dataObject.getStatus();
                            Code code = Code.toEnum(status);
                            Log.e(TAG, "//weather//"+"failed code: " + code);
                        }
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}

