package com.caruseapp.locating;

import com.caruseapp.locating.IOface.DataCalcCallback;
import com.caruseapp.locating.IOface.GNSS;
import com.caruseapp.locating.IOface.IMU10;
import com.caruseapp.locating.IOface.Locate;
import com.caruseapp.locating.cal.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public class DataCal implements Runnable {
    private static final double G = 9.80655;
    private static final int k = 5; // data length of check step ,must be odd and less than IMUDataLength
    private static final int IMUDataLength = 50;
    private static final int FilterLength = 5;
    private static final int FloorSteps = 15;
    // 娴ｅ秶鐤�
    double longitude = 116.3834686111111 * Math.PI / 180;
    double latitude = 39.90012208333333 * Math.PI / 180;
    double height = 44.0; // 濞撮攱瀚堟妯哄閿涘苯宕熸担宥忕窗缁拷
    int pdrState = 0;// 0鏉╂劕濮╅悩鑸碉拷浣规弓閻儻绱�1閸嬫粣绱�2鐠у府绱�3鐠烘埊绱�4閸楋拷
    double pitch; // 娣囶垯璇濈憴鎺炵礉閸楁洑缍呴敍姘
    double roll; // 濠婃俺娴嗙憴鎺炵礉閸楁洑缍呴敍姘
    double yaw; // 閸嬪繗鍩呯憴鎺炵礉閸楁洑缍呴敍姘
    String time = DateTime.get(); // 閺冨爼妫块敍灞筋洤閿涳拷2019-10-01-23-59-59-999閿涘苯宓嗛敍姘嬀-閺堬拷-閺冿拷-閺冿拷-閸掞拷-缁夛拷-濮ｎ偆
    // 缁変焦婀侀幋鎰喅
    private int millis = 1;// 鏉╂稓鈻兼导鎴犳耿閺冨爼妫块梹鍖＄礄濮ｎ偆顫楅敍锟�
    private boolean runFlag = true;// 闁拷閸戝搫鐣炬担宥嗙垼韫囷拷
    private int type = 1; // 鐎规矮缍呯猾璇茬��:0 閹垱锟窖呮祲鐎电懓鐣炬担锟�,1 閼奉亜濮╃紒鍕値鐎规矮缍�
    private int gnssType = 1;// GNSS鐎规矮缍呯猾璇茬��:0 娑撳秷鍏樼�规矮缍�,1 GPS鐎规矮缍呴敍锟�, 2 DGPS鐎规矮缍�,3锛屽ǎ宄版値鐎规矮缍呴敍锟�4 瀹割喖鍨庣�规矮缍�
    private boolean gnssInitLocateDone = false; //init locate have geted at runing by gnss
    // 閸旂娀锟界喎瀹崇拋鈽呯礄閸栧懎鎯堥柌宥呭閸掑棝鍣洪敍灞藉闁喎瀹抽崡鏇氱秴閿涙氨鑳�/娴滃本顐奸弬鍦潡閿涳拷
    private double accX; // X鏉炴潙锟斤拷,娑撳鎮�
    private double accY; // Y鏉炴潙锟斤拷,娑撳鎮�
    private double accZ; // Z鏉炴潙锟斤拷,娑撳鎮�
    private double aX; // X鏉炲鍤庨崝鐘伙拷鐔峰閸婏拷
    private double aY; // Y鏉炲鍤庨崝鐘伙拷鐔峰閸婏拷
    private double aZ; // Z鏉炲鍤庨崝鐘伙拷鐔峰閸婏拷
    private double aXinv; // 閸︽壆鎮婇崸鎰垼缁绗匵鏉炲鍤庨崝鐘伙拷鐔峰閸婏拷
    private double aYinv; // 閸︽壆鎮婇崸鎰垼缁绗匶鏉炲鍤庨崝鐘伙拷鐔峰閸婏拷
    private double aZinv; // 閸︽壆鎮婇崸鎰垼缁绗匷鏉炲鍤庨崝鐘伙拷鐔峰閸婏拷
    private double gX = 0; // X鏉炴挳鍣搁崝娑樺闁喎瀹抽崐锟�
    private double gY = 0; // Y鏉炴挳鍣搁崝娑樺闁喎瀹抽崐锟�
    private double gZ = 9.8; // Z鏉炴挳鍣搁崝娑樺闁喎瀹抽崐锟�
    private double gyroX;
    private double gyroY;
    private double gyroZ;
    private double magX;
    private double magY;
    private double magZ;
    private double hpa = 0.0;
    private double hpaH = 0.0;
    //
    private double imuTime = 0.0;
    private double lastGnssTime = 0.0;
    private double lastGyroTime = 0;
    private double lastGnssL = 0.0;
    private double lastGnssB = 0.0;
    private double lastGnssH = 0.0;
    private double gnssL = 116.3834686111111 * Math.PI / 180.0; // 閸掓繂顫愮紒蹇撳閿涘苯銇夌�瑰妫稉顓炵妇缂佸繐瀹抽敍灞藉礋娴ｅ稄绱版惔锟�
    private double gnssB = 39.90012208333333 * Math.PI / 180.0; // 閸掓繂顫愮痪顒�瀹抽敍灞姐亯鐎瑰妫稉顓炵妇缁绢剙瀹抽敍灞藉礋娴ｅ稄绱版惔锟�
    private double gnssH = 44.0;
    private double delication = 0.0 * Math.PI / 180.0; // 閸嬪繗鍩呯憴鎺炵礉閸楁洑缍呴敍姘
    private double gnssSpeed = 0;
    private double gnssBearing = 0;
    private double length = 0.0;
    private double lastgpH = 44.0;
    private double lastgnssH = 44.0;
    private double lastimuTime = 0.0;
    private double gnssAccuracy = 0.0;
    private double gnssError = 0.0;
    private double pdrAccuracy = 0.0;
    private double lastPdrStepLength;
    private double lasthpaH = 0;
    private double lasthpaHTime = 0;
    private double pdrAngle = 0;
    private double lastpdrAngle = 0;
    private double pdrStepLength = 0.5;
    private double pdrSpeed = 0.0;
    private double lastpdrpeaktime;
    private double pdrpeaktime;
    private double lastpdrvalleytime;
    private double pdrvalleytime;
    private double pdrAngletime;
    private double accyaw = 0.0;
    private double lastaccyaw = 0.0;
    private double gyroyaw = 0.0;
    private double lastgyroyaw = 0.0;
    private double magyaw = 0.0;
    private double lastmagyaw = 0.0;
    private List<DataCalcCallback> _callbackList;
    private int IMUCounter = 1;
    private int ID = 0;
    private int IDlasthalfK = 0; // main index of loncating data
    private int halfK = k / 2;
    private int flag = 0;// 濞夈垹鍢插▔銏ｈ兒閺嶅洤绻�
    private int data_flag = 0;// 濞夈垹鍢插▔銏ｈ兒閺嶅洤绻�
    private boolean initFlag = false;
    private int State = 0;
    private int pdrStepCounter = 0;
    private int Floor = 0;
    private double FloorHeight = height;
    private double lastFloorHeight = height;
    private int pdrFloor = Floor;
    private double pdrFloorHeight = FloorHeight;
    private double lastPdrFloorHeight = lastFloorHeight;
    private int upAxis = 0;//鍚戜笂鐨勮酱
    private String gnssTime = "";
    private String lastgTime = DateTime.get();
    private String lastsTime = DateTime.get();
    private String _gnss;
    private String _lastGnss;
    private double[] _da_gnss;
    private double[] _da_lastGnss;
    private double[] _da_imu10;
    private double[] _da_lastImu10;
    private byte[] _imu10;
    private byte[] _lastImu10;
    private double[][] R = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    private double[] PoseA = new double[3];
    private double[] poseangel = {0.0, 0.0, 0.0};
    private double[] lastposeangel = {0.0, 0.0, 0.0};
    private double[] pdrGyroAngle = new double[3];
    private double[] lastpdrGyroAngle = new double[3];
    private double[][] ArrayAcc = new double[3][IMUDataLength];
    private double[][] ArrayAccL = new double[3][IMUDataLength];
    private double[][] ArrayAccLinv = new double[3][IMUDataLength];
    private double[][] ArrayPose = new double[3][IMUDataLength];
    private double[][] ArrayMaginv = new double[3][IMUDataLength];
    private double[][] ArrayAccG = new double[3][IMUDataLength];
    private double[][] ArrayGyro = new double[3][IMUDataLength];
    private double[][] ArrayMag = new double[3][IMUDataLength];
    private double[] ArrayimuTime = new double[IMUDataLength];
    private double[] Arrayhpa = new double[IMUDataLength];
    private double[] ArrayhpaH = new double[IMUDataLength];
    private LinkedList<?> ArrayPdrHeight = new LinkedList<Double>();
    private double[] PoseQuat = {1, 0, 0, 0};
    private double[] gyroQuat = {1, 0, 0, 0};
    private double[] lastpdrQuat = {1, 0, 0, 0};
    private double[] pdrQuat = {1, 0, 0, 0};
    private double[] gnssgyroQuat = {1, 0, 0, 0};
    private double[] lastpdrAccL = new double[3];
    private double[] pdrAccL = new double[3];
    private double[] pdrLBH = new double[3];
    private double[] lastXYZ = {0.0, 0.0, 0.0};
    private double[][] lastPcov = {{0.0, 0.0}, {0.0, 0.0}};
    private double[] pdrpeakAccL = new double[k];
    private double[] pdrvalleyAccL = new double[k];
    private double[] accUchkData = new double[k];

    //

    public DataCal() {
        _callbackList = new ArrayList<>();
    }

    public DataCal(DataCalcCallback callback) {
        _callbackList = new ArrayList<>();
        this.addCallback(callback);
    }

    // overwrite run method
    @Override
    public void run() {
        try {
            System.out.println("Try Start DataCal");
            cal();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * public void init(String gnss, byte[] imu) {
     * this._gnss = gnss;
     * this._imu10 = imu;
     * }
     */

    public void getGNSS(String gnss) {
        this._gnss = gnss;
    }

    public void getIMU(byte[] imu) {
        this._imu10 = imu;
    }

    public void getGNSS(double[] gnss) {
        this._da_gnss = gnss;
    }

    public void getIMU(double[] imu) {
        this._da_imu10 = imu;
        //System.out.println("_da_imu10: "+_da_imu10[2]);
    }
    /**
     * add callback method
     *
     * @param callback
     */
    public void addCallback(DataCalcCallback callback) {
        if (!_callbackList.contains(callback)) {
            _callbackList.add(callback);
        }
    }

    /**
     * remove callback method
     *
     * @param callback
     */
    public void removeCallback(DataCalcCallback callback) {
        _callbackList.remove(callback);
    }

    /**
     * clear callback method
     */
    public void clearCallback() {
        _callbackList.clear();
    }

    /**
     * note calculate finished
     *
     * @param locate
     */
    public void notifyResult(Locate locate) {
        for (DataCalcCallback calcCallback : _callbackList) {
            calcCallback.onCalcFinish(locate);
        }
    }

    public int getFloor() {
        return Floor;
    }

    public void setFloor(int Floor) {
        this.Floor = Floor;
    }

    // initial
    private void initial() {
        int rt = 100;
        for (int i = 0; i < rt; i++) {
            if( null == _gnss){
                _gnss = "";
            }
            if( null == _lastGnss){
                _lastGnss = "";
            }
            calGNSS();
            calIMU();
            _lastGnss = _gnss;
            if (null != _imu10) {
                _lastImu10 = _imu10.clone();
            } else {
                _lastImu10 = null;
            }
            if (null != _da_gnss) {
                _da_lastGnss = _da_gnss.clone();
            } else {
                _da_lastGnss = null;
            }
            if (null != _da_imu10) {
                _da_lastImu10 = _da_imu10.clone();
            } else {
                _da_lastImu10 = null;
            }
            lastpdrQuat = PoseQuat.clone();
            gyroQuat = PoseQuat.clone();
            lastGyroTime = imuTime;
            lastpdrpeaktime = imuTime;
            lastpdrvalleytime = imuTime;
            pdrAngletime = imuTime;
            pdrAngle = yaw;
            lastpdrAngle = Math.abs(gnssBearing) < 1e-4 ? yaw : gnssBearing;
            lasthpaH = hpaH;
            lasthpaHTime = imuTime;
            lastimuTime = imuTime;
            State = 1;
            pdrState = State;
            pdrLBH[0] = gnssL;
            pdrLBH[1] = gnssB;
            pdrLBH[2] = gnssH;
            lastGnssL = gnssL;
            lastGnssB = gnssB;
            lastGnssTime = imuTime;
            //System.out.println("lastgpH : "+lastgpH);
            //System.out.println("hpaH : "+hpaH);
            if (hpaH > 0.0) {
                if ((Math.abs(44330.0 - lastgpH) < 5000 || Math.abs(lastgpH - hpaH) > 10)
                        && Math.abs(44330.0 - hpaH) > 1e-10) { //
                    rt++;
                    lastgpH = hpaH;
                } else {
                    lastgnssH = gnssH;
                    gnssH = hpaH;
                    lastgpH = hpaH;
                }
            } else {
                lastgpH = hpaH;
                lastgnssH = gnssH;
            }
            System.out.println("initing : i < rt !");
        }
        if(Math.abs(delication)<1e-10 ){
            delication = 0;
            TSAGeoMag TSAGM=new TSAGeoMag();
            String[] strNow = time.split("-");
            Integer year = Integer.parseInt(strNow[0]);
            Integer month = Integer.parseInt(strNow[1]);
            Integer day = Integer.parseInt(strNow[2]);
            double _da_time = DateTime.getYear(year, month, day);
            delication = -TSAGM.getDeclination(latitude/Math.PI*180,longitude/Math.PI*180,_da_time,height)*Math.PI/180.0;//转为逆时针为正
        }
        lastXYZ = PositionTransform.wgs84toXyz(BaseCompute.vector3(gnssL, gnssB, gnssH));
        lastPcov[0][0] = gnssAccuracy * gnssAccuracy;
        lastPcov[1][1] = gnssAccuracy * gnssAccuracy;
        lastPdrStepLength = pdrStepLength;
        if (gnssType != 0) {
            gnssgyroQuat = gyroQuat.clone();
        } else {
            Floor = 1;
            pdrFloor = Floor;
            FloorHeight = height;
            lastFloorHeight = FloorHeight;
            pdrFloorHeight = height;
            lastPdrFloorHeight = pdrFloorHeight;
        }
        //System.out.println("gyroyaw : "+PoseTransform.rQuat2Angle(gyroQuat[0], gyroQuat[1], gyroQuat[2], gyroQuat[3])[2]/Math.PI*180);
        //System.out.println("yaw     : "+BaseCompute.rad2deg(yaw));
        //System.out.println("poseyaw : "+PoseTransform.rQuat2Angle(PoseQuat[0], PoseQuat[1], PoseQuat[2], PoseQuat[3])[2]/Math.PI*180);
        System.out.println("init done!");
        notifyResult(Locate.getSingleObject());
    }

    // calculate
    public void cal() {
        // TODO 閼奉亜濮╅悽鐔稿灇閻ㄥ嫭鏌熷▔鏇炵摠閺嶏拷,鐎规矮缍呯憴锝囩暬閺傝纭�
        if (initFlag == false) {
            initial();
            initFlag = true;
        }
        int i = 0;
        while (runFlag) {
            //System.out.println(" running !");
            i++;
            // try sleep
            while (true) {
                boolean sleepFlag1,sleepFlag2;
                boolean breakFlag;
                if(_gnss==null){
                    _gnss="";
                }
                if(_lastGnss==null){
                    _lastGnss="";
                }
                if ((null == _imu10 && null == _lastImu10) && ("" == _gnss && "" == _lastGnss)) {
                    sleepFlag1 = true;
                } else if ((null != _imu10 && null != _lastImu10 && Arrays.equals(_imu10, _lastImu10))
                        && ("" != _gnss && "" != _lastGnss && _gnss.equals(_lastGnss))) {
                    sleepFlag1 = true;
                } else {
                    sleepFlag1 = false;
                }
                if ((null == _da_imu10 && null == _da_lastImu10) && (null == _da_gnss && null == _da_lastGnss)) {
                    sleepFlag2 = true;
                } else if ((null != _da_imu10 && null != _da_lastImu10 && Arrays.equals(_da_imu10, _da_lastImu10))
                        && (null != _da_gnss && null != _da_lastGnss && Arrays.equals(_da_gnss,_da_lastGnss))) {
                    sleepFlag2 = true;
                } else {
                    sleepFlag2 = false;
                }
                if(sleepFlag1==false) {
                    data_flag = 1;
                }else if(sleepFlag2==false){
                    data_flag = 2;
                }
                if (sleepFlag1 && sleepFlag2) {
                    //System.out.print("cal: haven't got new data of sensors, ");
                    breakFlag = false;
                    try {
                        Thread.sleep(millis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        ;
                        //System.out.println("trying sleep!");
                    }
                } else {
                    //System.out.println("cal: have got new data of sensors, break sleeping to calculate!");
                    breakFlag = true;
                }
                if (breakFlag) break;
            }
            // calculate locate
            synchronized (this) {
                try {
                    calGNSS();
                    calIMU();
                    _lastGnss = _gnss;
                    if (null != _imu10) {
                        _lastImu10 = _imu10.clone();
                    } else {
                        _lastImu10 = null;
                    }
                    if (null != _da_gnss) {
                        _da_lastGnss = _da_gnss.clone();
                    } else {
                        _da_lastGnss = null;
                    }
                    if (null != _da_imu10) {
                        _da_lastImu10 = _da_imu10.clone();
                    } else {
                        _da_lastImu10 = null;
                    }
                    /*
                     * System.out.println("gnss:  " + _gnss);
                     * System.out.println(_imu10);
                     */
                    // 鑷姩瀹氫綅,瀛樺湪鍗槦瀹氫綅淇″彿鏃�
                    if (gnssType != 0 && gnssType != 1 && type == 1) {
                        longitude = gnssL;
                        latitude = gnssB;
                        height = gnssH;
                    }
                    // 棰勮绠楄繍鍔ㄧ姸鎬�
                    calState();
                    // IMU杩愬姩鐘舵�佽В绠楀拰PDR鎯�у畾浣�
                    calPDR();
                    if (gnssType == 0 || gnssType == 1) {
                        if (pdrState == 2 || pdrState == 3) {
                            longitude = pdrLBH[0];
                            latitude = pdrLBH[1];
                        }
                        height = hpaH > 0.0 ? lastgnssH + hpaH - lastgpH : lastgnssH;
                        FloorHeight = height;
                        if (FloorHeight - lastFloorHeight > 3.5) {
                            Floor += 1;
                        } else if (FloorHeight - lastFloorHeight < -3.5) {
                            Floor -= 1;
                        }
                        lastFloorHeight = FloorHeight;
                        if (pdrState != 0 && pdrState !=1) {
                            Floor = pdrFloor;
                        }
                    }else{
                        Floor = 0;
                        pdrFloor = 0;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //System.out.println("cal: location have been calculated!");
                }
            }
            if (i >= 25) {
                notifyResult(Locate.getSingleObject());
                // System.out.println("cTime" + i + "   " + DateTime.get());
                i = 0;
            }
        }
    }

    // 鏉╂劕濮╅悩鑸碉拷浣筋吀缁狅拷
    private void calState() {
        State = 0;
        accUchkData = BaseCompute.selectKtn(ArrayAccLinv, ID, 2, k);// 閸︽壆鎮婇崸鎰垼缁绗呴張锟介弬鐧烘稉鐛勬潪鏉戝闁喎瀹虫惔蹇撳灙
        if (k >= 3 && k % 2 == 1) {
            if (Math.abs(accUchkData[halfK]) < 0.05) {
                State = 1;
            } else if ((Math.abs(accUchkData[halfK]) > 0.2)
                    && (Math.abs(accUchkData[halfK]) < 1.4)) {
                State = 2;
            } else if ((Math.abs(accUchkData[halfK]) > 1.4)
                    && (Math.abs(accUchkData[halfK]) < 9.9)) {
                State = 3;
            } else {
                State = 0;
            }
            // 濮ｅ繑顐奸棁锟界憰浣哥暰閺冭埖澧界悰宀勭彯鎼达箑褰夐崠鏍у灲閺傤叀骞忛崣鏍у锤閻樿埖锟戒降锟斤拷
            if (imuTime - lasthpaHTime < 750) {
                if (hpaH < lasthpaH - 0.75 && hpaH > lasthpaH - 1.6
                        && Math.abs(accUchkData[halfK]) > 2.0 && Math.abs(accUchkData[halfK]) < 6.0) {
                    State = 4;
                    pdrState = State;
                    // System.out.println("   liedown !  ");
                }
            } else {
                lasthpaH = hpaH;
                lasthpaHTime = imuTime;
            }

            if (imuTime - lastimuTime > 2000) {
                if (State == 0 || State == 1) {
                    if (pdrState == 4 || pdrState == 3 || pdrState == 2) {
                        pdrState = 0;
                        pdrSpeed = 0;
                        pdrStepLength = 0.0;
                        pdrAngle = yaw;
                        lastimuTime = imuTime;
                    }
                }
            }
        }
    }

    // 濮濄儴顢戦幆顖氼嚤鐠侊紕鐣�
    private void calPDR() {
        // TODO 閼奉亜濮╅悽鐔稿灇閻ㄥ嫭鏌熷▔鏇炵摠閺嶏拷
        if (k >= 3 && (State == 2 || State == 3) && pdrState != 4) {
            flag = 0;
            if (accUchkData[halfK - 2] < accUchkData[halfK - 1] && accUchkData[halfK - 1] < accUchkData[halfK]
                    && accUchkData[halfK] > accUchkData[halfK + 1] && accUchkData[halfK + 1] > accUchkData[halfK + 2]
                    && accUchkData[halfK] > 0) {
                pdrpeaktime = imuTime;
                // 濡拷濞村鍩屽▔銏犲槻
                if ((pdrpeaktime - lastpdrpeaktime) / 1000.0 > 0.20) {
                    flag = 1;
                    pdrpeakAccL = BaseCompute.getColElementByColId(ArrayAccLinv, ID, halfK);
                }
            }
            if (accUchkData[0] > accUchkData[1]
                    && accUchkData[1] > accUchkData[2]
                    && accUchkData[2] < accUchkData[3]
                    && accUchkData[3] < accUchkData[4] && accUchkData[2] < 0) {
                pdrvalleytime = imuTime;
                // 濡拷濞村鍩屽▔銏ｈ兒
                if ((pdrvalleytime - lastpdrvalleytime) / 1000.0 > 0.20) {
                    flag = -1;
                    pdrvalleyAccL = BaseCompute.getColElementByColId(ArrayAccLinv, ID, halfK);
                }
            }
            if (flag == 1) {
                double T = (0.5 * (pdrpeaktime - lastpdrpeaktime) + 0.5 * (pdrvalleytime - lastpdrvalleytime)) / 1000.0;// 濮ｎ偆顫�
                if (T > 0.15) {
                    double H = (BaseCompute.norm2(pdrpeakAccL) - (-BaseCompute.norm2(pdrvalleyAccL)));
                    double temppdrStepLength = 0.0;
                    if (T > 2.5) {
                        T = 1.5;
                    }
                    temppdrStepLength = 0.37 - 0.155 * T + 0.1638 * 3 * H;//
                    pdrStepLength = 0.5 * Math.max(temppdrStepLength, 0.0) + 0.5 * pdrStepLength;// 缁拷
                    if (pdrStepLength > 2.5) {
                        pdrStepLength = 0.5;
                    }
                    pdrSpeed = 0.5 * pdrStepLength / T + 0.5 * pdrSpeed;
                    if (length < 10000) {
                        length = length + pdrStepLength;
                    } else {
                        length = pdrStepLength;
                        pdrStepCounter = 1;
                    }
                    if (pdrStepCounter < 20000) {
                        pdrStepCounter = pdrStepCounter + 1;
                    } else {
                        pdrStepCounter = 1;
                        length = pdrStepLength;
                    }
                    if ("00-00-00".equals(DateTime.getClock().substring(0, 8))) {
                        pdrStepCounter = 0;
                        length = 0.0;
                    }
                    if("0-00".equals(DateTime.getClock().substring(4, 8)) || Math.abs(delication)<1e-10){
                        delication = 0;
                        TSAGeoMag TSAGM=new TSAGeoMag();
                        String[] strNow = time.split("-");
                        Integer year = Integer.parseInt(strNow[0]);
                        Integer month = Integer.parseInt(strNow[1]);
                        Integer day = Integer.parseInt(strNow[2]);
                        double _da_time = DateTime.getYear(year, month, day);
                        delication = -TSAGM.getDeclination(latitude/Math.PI*180,longitude/Math.PI*180,_da_time,height)*Math.PI/180.0;//转为逆时针为正
                    }
                    //
                    pdrQuat = gyroQuat.clone();
                    pdrGyroAngle = PoseTransform.rQuat2Angle(pdrQuat[0], pdrQuat[1], pdrQuat[2], pdrQuat[3]);
                    pdrAccL = BaseCompute.getColElementByColId(ArrayAccLinv, ID, halfK);
                    poseangel = BaseCompute.getColElementByColId(ArrayPose, ID, halfK);
                    double tempyaw = 0.0;
                    aXinv = ArrayAccLinv[0][IDlasthalfK];
                    aYinv = ArrayAccLinv[1][IDlasthalfK];
                    aZinv = ArrayAccLinv[2][IDlasthalfK];
                    if (aZinv > 0.1 && Math.abs(aXinv) > 0.1 && Math.abs(aYinv) > 0.1) {
                        tempyaw = 0.5 * (Math.atan2(-pdrAccL[0], pdrAccL[1]) + Math.atan2(-lastpdrAccL[0], lastpdrAccL[1]));
                    } else {
                        tempyaw = lastpdrAngle + (poseangel[2] - lastposeangel[2]);
                    }
                    // cal yaw
                    double tempaccyaw = tempyaw;
                    double tempgyroyaw = pdrGyroAngle[upAxis] - lastpdrGyroAngle[upAxis];//TODO
                    double tempmagyaw = Math.atan2(-ArrayMaginv[0][IDlasthalfK], ArrayMaginv[1][IDlasthalfK]);
                    //transform
                    accyaw = BaseCompute._pi2pi(tempaccyaw);
                    gyroyaw = BaseCompute._pi2pi(tempgyroyaw);
                    magyaw = BaseCompute._pi2pi(tempmagyaw - delication);//
                    double deltagyroyaw = gyroyaw - lastgyroyaw;
                    double deltamagyaw = magyaw - lastmagyaw;
                    pdrAngle = 0.9 * (lastpdrAngle + (0.9 * deltamagyaw + 0.1 * deltagyroyaw)) + 0.1 * accyaw;// 閼割亜鎮滅憴锟�
                    double pdrAngel = pdrAngle;
                    double rn = Math.random();
                    if (upAxis == 2) {
                        pdrAngle = yaw;
                    } else if (upAxis == 1) {
                        if (rn > 0.4) {
                            pdrAngle = yaw;
                        }
                    } else if (upAxis == 0) {
                        if (ArrayAccL[2][ID] > 0.5) {
                            pdrAngle = yaw - Math.PI / 2.0;
                        } else if (ArrayAccL[2][ID] < -0.5) {
                            pdrAngle = yaw + Math.PI / 2.0;
                        }
                    }
                    if (Math.random() < 0.4 && upAxis == 1) {
                        if (imuTime - pdrAngletime < 120000.0) {//璁＄畻鍒濆pdr杩愬姩鏂瑰悜瑙� less than 2.0 min use gyro to cal pdrAngel
                            double[] temp1gyroBearing = pdrGyroAngle.clone();
                            double[] temp2gyroBearing = PoseTransform.rQuat2Angle(gnssgyroQuat[0], gnssgyroQuat[1], gnssgyroQuat[2], gnssgyroQuat[3]);
                            double deltagyroBearing = temp1gyroBearing[upAxis] - temp2gyroBearing[upAxis];
                            if (Math.abs(gnssBearing) < 1e-4) {
                                double[] gnssXYZ = PositionTransform.wgs84toXyz(BaseCompute.vector3(gnssL, gnssB, gnssH));
                                double[] lastGnssXYZ = PositionTransform.wgs84toXyz(BaseCompute.vector3(lastGnssL, lastGnssB, lastGnssH));
                                gnssBearing = Math.atan2(-(gnssXYZ[0] - lastGnssXYZ[0]), gnssXYZ[1] - lastGnssXYZ[1]);
                            }
                            pdrAngle = gnssBearing + deltagyroBearing;
                        }
                    }
                    pdrAngle = BaseCompute._pi2pi(pdrAngle);
                    System.out.println("moving");
                    System.out.println("pdrAngel : " + pdrAngel / Math.PI * 180);
                    System.out.println("Yaw      : " + yaw / Math.PI * 180);
                    System.out.println("pdrAngle : " + pdrAngle / Math.PI * 180);
                    System.out.println("height   : " + height);
                    System.out.println(" ");
                    double[] XY0 = new double[3];
                    XY0[0] = pdrStepLength * Math.sin(-pdrAngle);
                    XY0[1] = pdrStepLength * Math.cos(-pdrAngle);
                    XY0[2] = 0;
                    pdrAccuracy = 0.5 * Math.abs(pdrStepLength - lastPdrStepLength);
                    double[] XYZ = PositionTransform.wgs84toXyz(BaseCompute.vector3(longitude, latitude, height));
                    if (gnssType == 1) {
                        //gnss瀹氫綅璐ㄩ噺宸椂浣跨敤Kalman婊ゆ尝杩涜缁勫悎瀹氫綅
                        double[][] Pcov = new double[2][2];
                        double[] hatXYZ = XYZ.clone();
                        hatXYZ[0] = 1 * lastXYZ[0] + 1 * XY0[0];
                        hatXYZ[1] = 1 * lastXYZ[1] + 1 * XY0[1];
                        Pcov[0][0] = 1 * lastPcov[0][0] * 1 + gnssAccuracy * gnssAccuracy;
                        Pcov[1][1] = 1 * lastPcov[1][1] * 1 + gnssAccuracy * gnssAccuracy;
                        double[][] Kalman = new double[2][2];
                        Kalman[0][0] = Pcov[0][0] * 1 * 1.0 / (1 * Pcov[0][0] * 1 + pdrAccuracy * pdrAccuracy);
                        Kalman[1][1] = Pcov[1][1] * 1 * 1.0 / (1 * Pcov[1][1] * 1 + pdrAccuracy * pdrAccuracy);
                        XYZ[0] = hatXYZ[0] + Kalman[0][0] * (XYZ[0] - 1 * hatXYZ[0]);
                        XYZ[1] = hatXYZ[1] + Kalman[1][1] * (XYZ[1] - 1 * hatXYZ[1]);
                        Pcov[0][0] = (1 - Kalman[0][0] * 1) * Pcov[0][0];
                        Pcov[1][1] = (1 - Kalman[1][1] * 1) * Pcov[1][1];
                        lastPcov = Pcov.clone();
                    } else {
                        XYZ = PositionTransform.enutoXyz(XY0, XYZ, BaseCompute.vector3(longitude, latitude, height));
                    }
                    lastXYZ = XYZ.clone();
                    pdrLBH = PositionTransform.xyztoWgs84(XYZ);
                    lastPdrStepLength = pdrStepLength;
                    lastpdrAngle = pdrAngle;
                    lastposeangel = poseangel;
                    lastaccyaw = accyaw;
                    lastgyroyaw = gyroyaw;
                    lastmagyaw = magyaw;
                    lastpdrAccL = pdrAccL.clone();
                    lastpdrGyroAngle = pdrGyroAngle.clone();
                    lastpdrQuat = pdrQuat.clone();
                    lastpdrpeaktime = pdrpeaktime;
                    lastpdrvalleytime = pdrvalleytime;
                    pdrState = State;
                    /*
                     * System.out.println("time:  " + time);
                     * System.out.println("pdrpeakAccL:  " + pdrpeakAccL[2]);
                     * System.out.println("pdrvalleyAccL:  " +pdrvalleyAccL[2]);
                     * System.out.println("pdrStepLength:  "+ pdrStepLength);
                     * System.out.println("pdrAngle:  " +lastpdrAngle / Math.PI * 180);
                     */
                    ArrayPdrHeight = BaseCompute.addFixSizeLinkedList((LinkedList<Object>) ArrayPdrHeight, height, FloorSteps);
                    if (ArrayPdrHeight.size() == FloorSteps) {
                        pdrFloorHeight = height;
                        double doubleStdPdrHeight = 2 * BaseCompute.std(BaseCompute.List2DoubleArray(ArrayPdrHeight));
                        if ((doubleStdPdrHeight > 2.5 && doubleStdPdrHeight < 5.0) && (pdrFloorHeight - lastPdrFloorHeight > 2.5 && pdrFloorHeight - lastPdrFloorHeight < 5.0)) {
                            pdrFloor += 1;
                            lastPdrFloorHeight = pdrFloorHeight;
                        } else if ((doubleStdPdrHeight > 2.5 && doubleStdPdrHeight < 5.0) && (pdrFloorHeight - lastPdrFloorHeight < -2.5 && pdrFloorHeight - lastPdrFloorHeight > -5.0)) {
                            pdrFloor -= 1;
                            lastPdrFloorHeight = pdrFloorHeight;
                        }
                    }

                }
            }
        }
    }

    // 鐠侊紕鐣绘慨鎸庯拷浣筋潡
    private double[] getPoseAngle() {
        // TODO 閼奉亜濮╅悽鐔稿灇閻ㄥ嫭鏌熷▔鏇炵摠閺嶏拷
        PoseA = PoseTransform.rQuat2Angle(PoseQuat[0], PoseQuat[1], PoseQuat[2], PoseQuat[3]);
        pitch = PoseA[0];
        roll = PoseA[1];
        yaw = PoseA[2];
        yaw = BaseCompute._pi2pi(yaw + delication);
        PoseA[2] = yaw;
        PoseQuat = PoseTransform.rAngle2Quat(BaseCompute.vector3(pitch, roll, yaw));
        return PoseA;
    }

    // 鐠侊紕鐣绘慨鎸庯拷浣告磽閸忓啯鏆�
    private double[] getPoseQuat() {
        // TODO 閼奉亜濮╅悽鐔稿灇閻ㄥ嫭鏌熷▔鏇炵摠閺嶏拷
        double[] tpdrPoseQuat = PoseTransform.rMatrix2Quat(R);
        PoseQuat = Quat.QuatNorm(tpdrPoseQuat);
        return PoseQuat;
    }

    // 鐠侊紕鐣婚崷鎵梿閸ф劖鐖ｆ稉濠璏U閿涘牊鍨ㄧ拋鎯ь槵閿涘协閹焦妫嗘潪顒傜叐闂冿拷
    private double[][] getMagPoseMatrix(double gravityX, double gravityY, double gravityZ,
                                        double geomagneticX, double geomagneticY, double geomagneticZ) {
        // TODO: move this to native code for efficiency
        double Ax = gravityX;
        double Ay = gravityY;
        double Az = gravityZ;
        double Ex = geomagneticX;
        double Ey = geomagneticY;
        double Ez = geomagneticZ;
        double Hx = Ey * Az - Ez * Ay;
        double Hy = Ez * Ax - Ex * Az;
        double Hz = Ex * Ay - Ey * Ax;
        double normH = Math.sqrt(Hx * Hx + Hy * Hy + Hz * Hz);
        double[][] r = new double[3][3];
        r[0][0] = 1;
        r[1][1] = 1;
        r[2][2] = 1;
        if (normH < 0.1) {
            // 婵寧锟戒胶鐓╅梼闈涙躬閼奉亞鏁遍拃鎴掔秼閵嗕線娴傞柌宥呭缁屾椽妫块妴浣稿繁婢舵牜顥嗛崷鍝勫叡閹佃埇锟戒焦鍨ㄩ崷鎵梿閺嬩胶鍋ｆ稉瀣￥濞夋洝袙缁犳绱漬ormH閸忕鐎烽崐鐓庣安婢堆傜艾100
            return BaseCompute.matProduct(r, this.R);
        }
        double invH = 1.0 / normH;
        Hx *= invH;
        Hy *= invH;
        Hz *= invH;
        double invA = 1.0 / Math.sqrt(Ax * Ax + Ay * Ay + Az * Az);
        Ax *= invA;
        Ay *= invA;
        Az *= invA;
        double Mx = Ay * Hz - Az * Hy;
        double My = Az * Hx - Ax * Hz;
        double Mz = Ax * Hy - Ay * Hx;
        //
        r[0][0] = Hx;
        r[0][1] = Hy;
        r[0][2] = Hz;
        r[1][0] = Mx;
        r[1][1] = My;
        r[1][2] = Mz;
        r[2][0] = Ax;
        r[2][1] = Ay;
        r[2][2] = Az;
        // return r;//yxz
        return BaseCompute.matTranspose(r); // ZXY
    }

    private void calIMU() {
        // 閼惧嘲褰囨导鐘冲妳閸ｃ劍鏆熼幑锟�
        IMU10 imu = null;//new IMU10();
        if (data_flag==1) {
            imu = new IMU10(_imu10);
        }else if(data_flag==2){
            imu = new IMU10(_da_imu10);
        }else {
            return;
        }
        boolean tfi = imu.getIMUf();
        imu.calc(data_flag);
        //hpaH = hpaH;
        // time = DateTime.get();
        if (tfi == true) {
            // data proccess
            final double g = G * Math.pow(10, -3); // 濮ｎ偊鍣搁崝娑樺闁喎瀹冲В鏂垮
            // 鏉烇拷锛岄崝鐘伙拷鐔峰缁拷/娴滃本顐奸弬鍦潡
            accX = imu.getAccX() * g;
            accY = imu.getAccY() * g;
            accZ = imu.getAccZ() * g;
			/*
			accX = imu.getAccX() * g +2*(Math.random()-0.5)*2.0;
			accY = imu.getAccY() * g +2*(Math.random()-0.5)*2.0;
			accZ = imu.getAccZ() * g +2*(Math.random()-0.5)*2.0;
			*/
            //
            final double alpha = 0.8;// 娑擄拷闂冩湹缍嗛柅姘姢濞夈垻閮撮弫锟�
            gX = alpha * gX + (1 - alpha) * accX;
            gY = alpha * gY + (1 - alpha) * accY;
            gZ = alpha * gZ + (1 - alpha) * accZ;

            //
            aX = accX - gX;
            aY = accY - gY;
            aZ = accZ - gZ;

            // 闂勶拷閾昏桨鍗庣憴鎺楋拷鐔峰閿涘苯瀹虫潪顒�濮惔锟�
            final double gd = 1 * Math.PI / 180.0;
            gyroX = imu.getGyroX() * gd;
            gyroY = imu.getGyroY() * gd;
            gyroZ = imu.getGyroZ() * gd;

            // 绾句礁濮忕拋锟�
            final double md = 0.1;// 濮ｎ偊鐝弬顖濇祮瀵邦喚澹掗弬顖涘娑旀ɑ鏆�
            magX = imu.getMagX() * md;
            magY = imu.getMagY() * md;
            magZ = imu.getMagZ() * md;

            // 濮樻柨甯�-妤傛ê瀹崇拋锟�
            final double pd = 0.001;// 閸楁洑缍呮潪顒佸床閿涙艾浜曞鏉戝煂閻ф儳绗楅弬顖氬幢
            hpa = imu.getHpaH() * pd;
            hpaH = 44330.0 * (1 - Math.pow(hpa / 1013.25, 1.0 / 5.255));// 濮樻柨甯�-妤傛ê瀹抽敍瀵�bar-hpa-meter
            //44300,5.256,44330,44306,5.255
            // imu.setIMUf(false);
            // Date curtime = new Date();
            imuTime = DateTime.getTime();
            // time = DateTime.format(imuTime);

            // data record
            IMUCounter = IMUCounter + 1;
            IMUCounter = IMUCounter != IMUDataLength ? ((IMUCounter + IMUDataLength) % IMUDataLength) : IMUDataLength;
            ID = IMUCounter - 1;
            IDlasthalfK = BaseCompute.getIndex(IMUDataLength, ID, halfK);
            double[][] arrayAcc = new double[3][IMUDataLength];
            arrayAcc[0][ID] = accX;
            arrayAcc[1][ID] = accY;
            arrayAcc[2][ID] = accZ;
            double[][] arrayAccL = new double[3][IMUDataLength];
            arrayAccL[0][ID] = aX;
            arrayAccL[1][ID] = aY;
            arrayAccL[2][ID] = aZ;
            double[][] arrayAccG = new double[3][IMUDataLength];
            arrayAccG[0][ID] = gX;
            arrayAccG[1][ID] = gY;
            arrayAccG[2][ID] = gZ;
            double[][] arrayGyro = new double[3][IMUDataLength];
            arrayGyro[0][ID] = gyroX;
            arrayGyro[1][ID] = gyroY;
            arrayGyro[2][ID] = gyroZ;
            double[][] arrayMag = new double[3][IMUDataLength];
            arrayMag[0][ID] = magX;
            arrayMag[1][ID] = magY;
            arrayMag[2][ID] = magZ;
            double[] arrayhpa = new double[IMUDataLength];
            arrayhpa[ID] = hpa;
            double[] arrayhpaH = new double[IMUDataLength];
            arrayhpaH[ID] = hpaH;
            double[] arrayimuTime = new double[IMUDataLength];
            arrayimuTime[ID] = imuTime;
            // 閸у洤锟界厧閽╁鎴犵崶閸欙絾鎶ゅ▔锟�
            int id = ID + 1;
            double sax = 0, say = 0, saz = 0;
            double salx = 0, saly = 0, salz = 0;
            double sagx = 0, sagy = 0, sagz = 0;
            double sgx = 0, sgy = 0, sgz = 0;
            double smx = 0, smy = 0, smz = 0;
            double smp = 0, smh = 0;
            for (int i = 0; i < FilterLength; i++) {
                id--;
                if (id == -1) {
                    id = IMUDataLength - 1;
                }
                sax += arrayAcc[0][id];
                say += arrayAcc[1][id];
                saz += arrayAcc[2][id];

                salx += arrayAccL[0][id];
                saly += arrayAccL[1][id];
                salz += arrayAccL[2][id];

                sagx += arrayAccG[0][id];
                sagy += arrayAccG[1][id];
                sagz += arrayAccG[2][id];

                sgx += arrayGyro[0][id];
                sgy += arrayGyro[1][id];
                sgz += arrayGyro[2][id];

                smx += arrayMag[0][id];
                smy += arrayMag[1][id];
                smz += arrayMag[2][id];

                smp += arrayhpa[id];
                smh += arrayhpaH[id];
            }
            double tempFL = FilterLength;
            ArrayAcc[0][ID] = sax / tempFL;
            ArrayAcc[1][ID] = say / tempFL;
            ArrayAcc[2][ID] = saz / tempFL;
            ArrayAccL[0][ID] = salx / tempFL;
            ArrayAccL[1][ID] = saly / tempFL;
            ArrayAccL[2][ID] = salz / tempFL;
            ArrayAccG[0][ID] = sagx / tempFL;
            ArrayAccG[1][ID] = sagy / tempFL;
            ArrayAccG[2][ID] = sagz / tempFL;
            ArrayGyro[0][ID] = arrayGyro[0][id];//sgx / tempFL;
            ArrayGyro[1][ID] = arrayGyro[1][id];//sgy / tempFL;
            ArrayGyro[2][ID] = arrayGyro[2][id];//sgz / tempFL;
            ArrayMag[0][ID] = smx / tempFL;
            ArrayMag[1][ID] = smy / tempFL;
            ArrayMag[2][ID] = smz / tempFL;
            Arrayhpa[ID] = smp / tempFL;
            //hpaH = 44330.0 * (1 - Math.pow(Arrayhpa[ID] / 1013.25, 1.0 / 5.255));
            ArrayhpaH[ID] = smh / tempFL;//hpaH;
            ArrayimuTime[ID] = arrayimuTime[ID];

            // 濮瑰倽娴囨担鎾虫勾閻炲棗娼楅弽鍥╅兇娑撳绻嶉崝銊ュ闁喎瀹�
            R = getMagPoseMatrix(ArrayAccG[0][ID], ArrayAccG[1][ID], ArrayAccG[2][ID],
                    ArrayMag[0][ID], ArrayMag[1][ID], ArrayMag[2][ID]);
            //PoseQuat=getPoseQuat();
            //PoseA=getPoseAngle();
            /*
            System.out.println("\n");
			System.out.println("R0:" + R[0][0]+","+R[0][1]+","+R[0][2]);
			System.out.println("R1:" + R[1][0]+","+R[1][1]+","+R[1][2]);
			System.out.println("R2:" + R[2][0]+","+R[2][1]+","+R[2][2]);
            System.out.println("PoseQuat:" + PoseQuat[0]+","+PoseQuat[1]+","+PoseQuat[2]+","+PoseQuat[3]);
            System.out.println("PoseA:" + PoseA[0]+","+PoseA[1]+","+PoseA[2]);
            */
            PoseA = PoseTransform.rMatrix2Angle(R);
            pitch = PoseA[0];
            roll = PoseA[1];
            yaw = PoseA[2];
            yaw = BaseCompute._pi2pi(yaw + delication);
            PoseA[2] = yaw;
            //System.out.println("yaw     : "+BaseCompute.rad2deg(yaw));
            //System.out.println("PoseA:" + PoseA[0]/Math.PI*180+","+PoseA[1]/Math.PI*180+","+PoseA[2]/Math.PI*180);
            PoseQuat = PoseTransform.rAngle2Quat(PoseA);
            ArrayPose[0][ID] = pitch;
            ArrayPose[1][ID] = roll;
            ArrayPose[2][ID] = yaw;
            double[] accinv = AxisRotation.RFT2ENU(BaseCompute.getColElementByColId(ArrayAccL, ID), PoseA);
            ArrayAccLinv[0][ID] = accinv[0];
            ArrayAccLinv[1][ID] = accinv[1];
            ArrayAccLinv[2][ID] = accinv[2];
            double[] maginv = AxisRotation.RFT2ENU(BaseCompute.getColElementByColId(ArrayMag, ID), PoseA);
            ArrayMaginv[0][ID] = maginv[0];
            ArrayMaginv[1][ID] = maginv[1];
            ArrayMaginv[2][ID] = maginv[2];

            // data computation
            double gxAbsDiff = Math.abs(ArrayAccG[0][IDlasthalfK] - G);
            double gyAbsDiff = Math.abs(ArrayAccG[1][IDlasthalfK] - G);
            double gzAbsDiff = Math.abs(ArrayAccG[2][IDlasthalfK] - G);
            if (gxAbsDiff < gyAbsDiff && gxAbsDiff < gzAbsDiff) {
                upAxis = 0;
            } else if (gyAbsDiff < gxAbsDiff && gyAbsDiff < gzAbsDiff) {
                upAxis = 1;
            } else if (gzAbsDiff < gxAbsDiff && gzAbsDiff < gyAbsDiff) {
                upAxis = 2;
            } else {
                upAxis = (int) Math.random() * 3;
            }
            /*
            System.out.println("accx: " + Double.toString(accX));
            System.out.println("accy: " + Double.toString(accY));
            System.out.println("accz: " + Double.toString(accZ));
            System.out.println("gyrx: " + Double.toString(gyroX));
            System.out.println("gyry: " + Double.toString(gyroY));
            System.out.println("gyrz: " + Double.toString(gyroZ));
            System.out.println("magx: " + Double.toString(magX));
            System.out.println("magy: " + Double.toString(magY));
            System.out.println("magz: " + Double.toString(magZ));
            System.out.println("hpaH: " + Double.toString(hpaH));
            */
            /*
			System.out.println("Gx: " + Arrays.toString(ArrayAccG[0]));
			System.out.println("Gy: " + Arrays.toString(ArrayAccG[1]));
			System.out.println("Gz: " + Arrays.toString(ArrayAccG[2]));
			System.out.println("iLx: " + Arrays.toString(ArrayAccLinv[0]));
			System.out.println("iLy: " + Arrays.toString(ArrayAccLinv[1]));
			System.out.println("iLz: " + Arrays.toString(ArrayAccLinv[2]));
			System.out.println("hH: " + Arrays.toString(ArrayhpaH));
            */

            // 闂勶拷閾昏桨鍗庣粔顖氬瀻
            double wx = ArrayGyro[0][IDlasthalfK];
            double wy = ArrayGyro[1][IDlasthalfK];
            double wz = ArrayGyro[2][IDlasthalfK];
            double q0 = gyroQuat[0];
            double q1 = gyroQuat[1];
            double q2 = gyroQuat[2];
            double q3 = gyroQuat[3];
            // 娴ｈ法鏁�1闂冨爼绶抽弽鐓庣氨婵夋梹纭堕弴瀛樻煀閸ユ稑鍘撻弫鏉啃幀渚婄礉鏉堟挸鍤弴瀛樻煀閻ㄥ嫬娲撻崗鍐╂殶婵寧锟斤拷
            double[] vec = {-wx * q1 - wy * q2 - wz * q3,
                    wx * q0 - wy * q3 + wz * q2,
                    wx * q3 + wy * q0 - wz * q1,
                    -wx * q2 + wy * q1 + wz * q0};
            double deltaTime = 0;
            deltaTime = Math.abs((ArrayimuTime[IDlasthalfK] - lastGyroTime) / 1000.0);// 濮ｎ偆顫楁潪顒傤潡
            double[] temp = BaseCompute.vectorProductNum(vec, deltaTime * 0.5);
            double[] midAttitude = BaseCompute.addVector(gyroQuat.clone(), temp);
            gyroQuat = Quat.QuatNorm(midAttitude);// 閺嶅洤鍣敍鍫濈秺娑擄拷閿涘瀵查崶娑樺帗閺侊拷
            //System.out.println("difTime : "+deltaTime);
            //System.out.println("gyroyaw : "+PoseTransform.rQuat2Angle(gyroQuat[0], gyroQuat[1], gyroQuat[2], gyroQuat[3])[2]/Math.PI*180);
            //System.out.println("yaw     : "+BaseCompute.rad2deg(yaw));
            //System.out.println("poseyaw : "+PoseTransform.rQuat2Angle(PoseQuat[0], PoseQuat[1], PoseQuat[2], PoseQuat[3])[2]/Math.PI*180);
            //
            lastGyroTime = ArrayimuTime[IDlasthalfK];
        }
    }

    //
    private void calGNSS() {

        GNSS gnss = null;//= new GNSS();
        if (data_flag == 1) {
            gnss = new GNSS(_gnss);
        }else if(data_flag==2){
            gnss = new GNSS(_da_gnss);
        }else {
            return;
        }
        boolean tfg = gnss.getGnssF();
        gnss.calc(data_flag);
        gnssType = gnss.getType();
        if (gnssType != 0 && tfg == true) {
            boolean L = false, B = false, H = false;
            double temp_gnssL = gnss.getLongitude() * Math.PI / 180.0;
            if (Math.abs(temp_gnssL) > 1e-10) {
                gnssL = temp_gnssL;
                L = true;
            }
            double temp_gnssB = gnss.getLatitude() * Math.PI / 180.0;
            if (Math.abs(temp_gnssB) > 1e-10) {
                gnssB = temp_gnssB;
                B = true;
            }
            double temp_gnssH = gnss.getAltitude();
            if (Math.abs(temp_gnssH) > 1e-10) {
                gnssH = temp_gnssH;
                H = true;
            }
            if ((L && B) || H == true && gnssInitLocateDone == false) {
                gnssInitLocateDone = true;
            }
            if (Math.abs(delication) < 1e-10) {
                delication = gnss.getDelication() * Math.PI / 180.0;
            }
            gnssAccuracy = gnss.getAccuracy();
            gnssError = gnss.getErro();
            lastgnssH = gnssH;
            lastgpH = hpaH;
            gnssBearing = gnss.getBearing() * Math.PI / 180.0;
            gnssSpeed = gnss.getSpeed();
            // gnss.setGnssF(false);
            gnssgyroQuat = gyroQuat.clone();
            pdrAngletime = imuTime;
        }

        String TempTime = gnss.getTime();
        if (!TempTime.equals("")) {
            gnssTime = TempTime;
            lastgTime = gnssTime;
            lastsTime = DateTime.get();
        } else if (TempTime.equals("")) {
            gnssTime = DateTime.calTime(lastgTime, lastsTime, DateTime.get());
        }
        if (imuTime - lastGnssTime > 2500) {
            lastGnssTime = imuTime;
            lastGnssL = gnssL;
            lastGnssB = gnssB;
            lastGnssH = gnssH;
        }
        time = gnssTime;
    }

    // get/set method
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getHeight() {
        if (Math.abs(44330 - height) < 500) { return 44.0; }
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getGnssAccuracy() {
        return gnssAccuracy;
    }

    public double getGnssError() {
        return gnssError;
    }

    public double getpdrAccuracy() {
        return pdrAccuracy;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public double getYaw() {
        return yaw;
    }

    public String getTime() {
        return time;
    }

    public int getGnssType() {
        return gnssType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getGnssInitLocateIsDone() {
        return gnssInitLocateDone;
    }

    public void setGnssInitLocateIsDone(boolean gnssInitLocateDone) {
        this.gnssInitLocateDone = gnssInitLocateDone;
    }

    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getStepLength() {
        return pdrStepLength;
    }

    public int getStepCounter() {
        return pdrStepCounter;
    }

    public void setStepCounter(int StepCounter) {
        pdrStepCounter = StepCounter;
    }

    public int getPdrState() {
        return pdrState;
    }

    public double getPdrAngle() {
        return pdrAngle;
    }

    public double getGnssAngle() {
        return gnssBearing;
    }

    public double getPdrSpeed() {
        return pdrSpeed;
    }

    public double getGnssSpeed() {
        return gnssSpeed;
    }

    public double getDelication() {
        return delication;
    }

    public void setDelication(double delication) {
        if (Math.abs(this.delication) < 1e-10) {
            this.delication = delication;
        }
    }

}
