/**
 *
 */
package com.caruseapp.locating.IOface;


import com.caruseapp.locating.DataCal;

/**
 * @author Administrator
 *
 */
public class Locate {
    private static Locate SingleObjectLocate;
    static DataCal dc;//protected
    private Thread _calcThread;//static
    private static final long millis = 1;
    private static boolean _stop;//
    //
    public String time;// 鏃堕敓鎴掞紝閿熸枻鎷�-閿熸枻鎷�-閿熸枻鎷�-鏃�-閿熸枻鎷�-閿熸枻鎷�-閿熸枻鎷烽敓鏂ゆ嫹
    public String state;// 閿熷壙璁规嫹鐘舵��:閿熺鈽呮嫹閿熸澃鈽呮嫹閿熸枻鎷�
    public String method;//positioning method : GNSS,IMU
    //
    public double longitude;// 閿熸枻鎷烽敓楗猴綇鎷烽敓鏂ゆ嫹
    public double latitude;// 绾敓楗猴綇鎷烽敓鏂ゆ嫹
    public double height;// 閿熺搴︼綇鎷烽敓鏂ゆ嫹
    public int stepCounter;// 璧拌繃鐨勬鏁�
    public double lentgh;// 璧拌繃鐨勮窛绂�
    public double speed;// 绉诲姩閫熷害
    public double angle;// 鑸悜瑙�

    //

    private Locate() {
        // 绉佹湁鍖栨瀯閫犳柟娉�
    }

    public void startCalc() {
        try {
            if (null == dc) {
                dc = new DataCal();
            }
            dc.setRunFlag(true);
            _calcThread = new Thread(dc);
            _stop = false;
            _calcThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(Locate.class.getSimpleName() + " try to start thread _calcThread!");
        }
    }

    public void stopCalc() {
        if (_calcThread != null) {
            dc.setRunFlag(false);
            _stop = true;
            _calcThread.interrupt();
        }
    }

    public String getLongitude() {
        double longitude = 0;
        longitude = dc.getLongitude() / Math.PI * 180;
        this.longitude = longitude;
        String Lon = "0.0";
        if (dc.getGnssInitLocateIsDone() == true){
            Lon = Double.toString(longitude);
        }
        return Lon;
    }

    public static void setLongitude(double longitude) {
        dc.setLongitude(longitude * Math.PI / 180.0);
    }

    public String getLatitude() {
        double latitude = 0;
        latitude = dc.getLatitude() / Math.PI * 180;
        this.latitude = latitude;
        String Lat = "0.0";
        if (dc.getGnssInitLocateIsDone() == true) {
            Lat = Double.toString(latitude);
        }
        return Lat;
    }

    public static void setLatitude(double latitude) {
        dc.setLatitude(latitude * Math.PI / 180.0);
    }

    public String getHeight() {
        double height = 0;
        height = dc.getHeight();
        this.height = height;
        String H = "0"+String.valueOf(height);
        if (dc.getGnssInitLocateIsDone() == true) {
            H = String.valueOf(height);
        }
        return H;
    }

    public void setHeight(double height) {
        dc.setHeight(height);
    }

    public static double getPitch() {
        double pitch = 0;
        pitch = dc.getPitch() / Math.PI * 180;
        return pitch;
    }

    public double getRoll() {
        double roll = 0;
        roll = dc.getRoll() / Math.PI * 180;
        return roll;
    }

    public double getYaw() {
        double yaw = 0;
        yaw = dc.getYaw() / Math.PI * 180;
        return yaw;
    }

    public String getTime() {
        String time = "";
        time = dc.getTime();
        this.time = time;
        return time;
    }

    public double getDelication() {
        double delication = 5.0;
        delication = dc.getDelication() / Math.PI * 180;
        return delication;
    }

    public static void setDelication(double Delication) {
        dc.setDelication(Delication * Math.PI / 180.0); // 閿熸枻鎷锋湭閿熸枻鎷峰彇閿熸枻鎷烽敓鏂ゆ嫹鍋忛敓鏂ゆ嫹鏃堕敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熻В閮ㄩ敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鍙ㄩ敓杞夸紮鎷烽敓鏂ゆ嫹閿燂拷
    }

    public static int getType() {
        int type = 1;
        type = dc.getType();
        return type;// 閿熸枻鎷蜂綅閿熸枻鎷烽敓閰碉綇鎷�0閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯繙閿熻娇浼欐嫹閿燂拷1
        // 閿熺殕璁规嫹閿熸枻鎷烽殭閿熻娇锟�
    }

    public static void setType(int Type) {
        dc.setType(Type);// 閿熸枻鎷蜂綅閿熸枻鎷烽敓閰碉綇鎷�0閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯繙閿熻娇浼欐嫹閿燂拷1
        // 閿熺殕璁规嫹閿熸枻鎷烽殭閿熻娇锟�
    }

    public static void setRunFlag(boolean RunFlag) {
        dc.setRunFlag(RunFlag);
    }

    public double getLength() {
        double length = 0;
        length = dc.getLength();// 閿熸枻鎷蜂綅閿熸枻鎷烽敓閰碉綇鎷�0閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯繙閿熻娇浼欐嫹閿燂拷1
        // 閿熺殕璁规嫹閿熸枻鎷烽殭閿熻娇锟�
        this.lentgh = length;
        return length;
    }

    public double getStepLength() {
        double stepLength = 0;
        stepLength = dc.getStepLength();
        int t = dc.getPdrState();
        if (t != 2 && t != 3){
            stepLength = 0.0;
        }
        return stepLength;
    }

    public static void setLength(double Length) {
        dc.setLength(Length);// 閿熸枻鎷蜂綅閿熸枻鎷烽敓閰碉綇鎷�0閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯繙閿熻娇浼欐嫹閿燂拷1
        // 閿熺殕璁规嫹閿熸枻鎷烽殭閿熻娇锟�
    }

    public static void setStepCounter(int Length) {
        dc.setStepCounter(Length);//
    }

    public int getStepCounter() {
        int pdrStepCounter = 0;
        pdrStepCounter = dc.getStepCounter();
        this.stepCounter = pdrStepCounter;
        return pdrStepCounter;
    }

    public String getPdrState() {
        // TODO 閿熺殕璁规嫹閿熸枻鎷烽敓缂寸殑鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
        int t = dc.getPdrState();
        String str;
        if (t == 2) {
            str = "walk";
        } else if (t == 3) {
            str = "run";
        } else if (t == 4) {
            str = "lie";
        } else {
            str = "-";
        }
        state = str;
        return str;
    }

    public double getAngle() {
        // TODO 閿熺殕璁规嫹閿熸枻鎷烽敓缂寸殑鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
        double angle = 0.0;
        int t = dc.getPdrState();
        if (dc.getType() == 0) {
            angle = dc.getPdrAngle();
            if (t != 2 && t != 3 && t!=4){
                angle = dc.getYaw();
            }
        } else {
            if (dc.getGnssType() != 0) {
                if (dc.getGnssType() == 1){
                    angle = dc.getPdrAngle();
                    if (t != 2 && t != 3 && t!=4){
                        angle = dc.getYaw();
                    }
                }else{
                    angle = dc.getGnssAngle();
                }
            } else {
                angle = dc.getPdrAngle();
                if (t != 2 && t != 3 && t!=4){
                    angle = dc.getYaw();
                }
            }
        }
        angle = (angle / Math.PI * 180.0) % 360.0;
        if (angle > 180.0) {
            angle = angle - 360.0;
        } else if (angle < -180.0) {
            angle = angle + 360.0;
        }// N axis 0d， anticlockwise +: -180d~180d
        angle = angle > 0 ? 360 - angle : 0 - angle;//N axis 0d，clockwise + ：0d~360d
        this.angle = angle;
        return angle;
    }

    public double getSpeed() {
        double speed = 0.0;
        int t = dc.getPdrState();
        if (dc.getType() == 0) {
            speed = dc.getPdrSpeed();
            if (t != 2 && t != 3 && t!=4){
                speed = 0.0;
            }
        } else {
            if (dc.getGnssType() != 0) {
                speed = dc.getGnssSpeed();
            } else {
                speed = dc.getPdrSpeed();
                if (t != 2 && t != 3 && t!=4){
                    speed = 0.0;
                }
            }
        }
        this.speed = speed;
        return speed;
    }

    public String getMethod() {
        // TODO 定位方法：GNSS，IMU
        int t = dc.getType();
        int t2 = dc.getGnssType();
        String str = "-";
        if (t == 0) {
            str = "PDR";
        } else if (t == 1 && (t2 != 0 && t2 != 1)) {
            str = "GNSS";
        } else if (t == 1 && (t2 == 0 || t2 == 1)) {
            str = "PDR";
        }
        method = str;
        return str;
    }

    public String getFloor() {
        return String.valueOf(dc.getFloor());
    }

    public void setFloor(int floor) {
        dc.setFloor(floor);
    }

    public double getGnssAccuracy() {
        return dc.getGnssAccuracy();
    }

    public double getGnssError() {
        return dc.getGnssError();
    }

    public double getInsAccuracy() { return dc.getpdrAccuracy(); }

    // get single object
    public static synchronized Locate getSingleObject() {
        if (SingleObjectLocate == null) { // 閿熷彨鏂潻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓瑙掑嚖鎷蜂负閿熸枻鎷�
            SingleObjectLocate = new Locate();
            dc = new DataCal();
            dc.setRunFlag(true);
            new Thread(dc);
            _stop = true;
        }
        return SingleObjectLocate;
    }

    // reloaded method
    public synchronized Locate getSingleObject(String str) {
        if (str.equals("InnerStart")) {
            if (SingleObjectLocate == null) {
                SingleObjectLocate = new Locate();
                if (null == dc) {
                    dc = new DataCal();
                    dc.setRunFlag(true);
                    new Thread(dc);
                    _calcThread = new Thread(dc);
                    _stop = false;
                    _calcThread.start();
                }
            }
            return SingleObjectLocate;
        } else {
            if (SingleObjectLocate == null) {
                SingleObjectLocate = getSingleObject();
            }
            return SingleObjectLocate;
        }
    }

    public String getPose() {
        // 閿熸枻鎷烽敓鏂ゆ嫹JSon閿熸枻鎷峰紡閿熻鍑ゆ嫹閿熸枻鎷�
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // TODO 鑷姩鐢熸垚鐨� catch 鍧�
            e.printStackTrace();
        }
        String Pose = "";
        String Time = getTime();// 鏃堕敓鎴掞紝閿熸枻鎷�-閿熸枻鎷�-閿熸枻鎷�-鏃�-閿熸枻鎷�-閿熸枻鎷�-閿熸枻鎷烽敓鏂ゆ嫹
        String Longitude =getLongitude();// 閿熸枻鎷烽敓楗猴綇鎷烽敓鏂ゆ嫹
        String Latitude = getLatitude();// 绾敓楗猴綇鎷烽敓鏂ゆ嫹
        String Height = getHeight();// 閿熺搴︼綇鎷烽敓鏂ゆ嫹
        String Floor = getFloor();//floors
        String State = getPdrState();// 閿熷壙璁规嫹鐘舵��:閿熺鈽呮嫹閿熸澃鈽呮嫹閿熸枻鎷�
        String Length = String.valueOf(getLength());// 绱璧拌繃鐨勮窛绂�
        String StepCounter = String.valueOf(getStepCounter());// 绱璧拌繃鐨勬鏁�
        String StepLength = Double.toString(getStepLength());
        String Speed = Double.toString(getSpeed());
        String Yaw = Double.toString(getYaw() > 0 ? 360 - getYaw() : 0 - getYaw());
        String Angle = Double.toString(getAngle());
        String Method = getMethod();//定位方法：GNSS，INS
        Pose = "{"
                + "\"time\":\"" + Time
                + "\",\"longitude\":\"" + Longitude
                + "\",\"latitude\":\"" + Latitude
                + "\",\"height\":\"" + Height
                + "\",\"floor\":\"" + Floor
                + "\",\"state\":\"" + State
                + "\",\"length\":\"" + Length
                + "\",\"stepCounter\":\"" + StepCounter
                + "\",\"stepLength\":\"" + StepLength
                + "\",\"speed\":\"" + Speed
                //+ "\",\"yaw\":\"" + Yaw
                + "\",\"angle\":\"" + Angle
                + "\",\"method\":\"" + Method
                + "\"}";
        return Pose;
    }

}
