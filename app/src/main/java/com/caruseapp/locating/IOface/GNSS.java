package com.caruseapp.locating.IOface;

import com.caruseapp.locating.DateTime;

import java.text.DateFormat;
import java.util.Date;


public class GNSS {
    // 閿熸枻鎷峰疄閿熸枻鎷�10閿熸枻鎷稩MU閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鎺ュ尅鎷烽敓鏂ゆ嫹
    // 閿熸磥琚敓瑙ｉ儴瀹為敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鑽╅敓鏂ゆ嫹鐛犳复纰夋嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓?
    private double accuracy = 0.0; // 姘村钩閿熸枻鎷烽敓楗猴綇鎷烽敓鏂ゆ嫹浣嶉敓鏂ゆ嫹閿熸枻鎷�
    private double erro = 0.0; // 閿熸枻鎷风洿閿熸枻鎷风澘顒婃嫹閿熻娇浼欐嫹閿熸枻鎷烽敓?
    private double longitude = 0.0; // 閿熸枻鎷烽敓楗猴綇鎷烽敓鏂ゆ嫹浣嶉敓鏂ゆ嫹閿熸枻鎷�
    private double latitude = 0.0; // 绾敓楗猴綇鎷烽敓鏂ゆ嫹浣嶉敓鏂ゆ嫹閿熸枻鎷�
    private double altitude = 0.0; // 閿熸枻鎷烽敓杞块珮搴︼綇鎷烽敓鏂ゆ嫹浣嶉敓鏂ゆ嫹閿熸枻鎷�
    private double delication = 0.0; // 閿熸枻鎷峰亸閿熻锝忔嫹閿熸枻鎷峰亸閿熸枻鎷蜂负閿熸枻鎷烽敓鏂ゆ嫹鍋忛敓鏂ゆ嫹涓洪敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹浣嶉敓鏂ゆ嫹閿熸枻鎷�
    private double bearing = 0.0; // 閿熷壙璁规嫹閿熸枻鎷蜂綅閿熻锝忔嫹閿熸枻鎷峰亸閿熸枻鎷蜂负閿熸枻鎷烽敓鏂ゆ嫹鍋忛敓鏂ゆ嫹涓洪敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹浣嶉敓鏂ゆ嫹閿熸枻鎷�
    private double speed = 0.0; // 閿熷壙璁规嫹閿熷姭搴︼綇鎷烽敓鏂ゆ嫹浣嶉敓鏂ゆ嫹閿熸枻鎷锋瘡閿熸枻鎷�
    private String time = ""; // 閿熸枻鎷烽敓鏂ゆ嫹鏃堕敓鎴掞紝閿熺晫锛�2019-10-01-23-59-59-999閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹-閿熸枻鎷�-閿熸枻鎷�-鏃�-閿熸枻鎷�-閿熸枻鎷�-閿熸枻鎷烽敓鏂ゆ嫹
    private int type = 1; // 閿熸枻鎷蜂綅閿熸枻鎷烽敓鏂ゆ嫹:0 閿熸枻鎷烽敓鏉拌鎷蜂綅,1 GPS閿熸枻鎷蜂綅閿熸枻鎷�2
    // BD閿熸枻鎷蜂綅, 3
    // 閿熸枻鎷烽殭閿熻娇浼欐嫹閿�?4 閿熸枻鎷蜂緞閿熻娇?
    private int StarNumber = 0; // 閿熸娴嬪埌閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
    private double GDOP = 0.0; // 閿熻剼鐚存嫹閿熸枻鎷烽敓鏂ゆ嫹閿熶茎纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸帴鎾呮嫹閿熸枻鎷�
    private boolean gnssF = true;// 閿熸枻鎷烽敓鏂ゆ嫹鐘舵�侀敓鏂ゆ嫹蹇楅敓鏂ゆ嫹閿熸枻鎷峰True閿熸枻鎷烽敓鏂ゆ嫹閿熸嵎鎲嬫嫹get閿熸枻鎷锋椂閿熺粨琚玸et涓篎alse閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙潻鎷烽敓鏂ゆ嫹鏃堕敓鏂ゆ嫹閿熻娇鐚卹ue
    private String _gnss;
    private double[] _da_gnss;

    public GNSS() { }
    //
    public GNSS(String gnss) {
        this._gnss = gnss;
    }

    public GNSS(double[] gnss) {
        this._da_gnss = gnss;
    }

    //
    public void calc(int data_flag) {
        switch (data_flag) {
            case 1:
                type = 0;
                setGnssF(false);
                if (_gnss != null && !_gnss.equals("") && _gnss.length() > 0) {
                    if (_gnss.substring(0, 6).equals("$GNGGA") || _gnss.substring(0, 6).equals("$GPGGA") || _gnss.substring(0, 6).equals("$GBGGA")) {
                        calcGGA();
                    } else if (_gnss.substring(0, 6).equals("$GNRMC") || _gnss.substring(0, 6).equals("$GPRMC") || _gnss.substring(0, 6).equals("$GBRMC")) {
                        calcRMC();
                    } else if (_gnss.substring(0, 6).equals("$GNVTG") || _gnss.substring(0, 6).equals("$GPVTG") || _gnss.substring(0, 6).equals("$GBVTG")) {
                        calcVTG();
                    } else if (_gnss.substring(0, 6).equals("$GBGSA") || _gnss.substring(0, 6).equals("$GPGSA") || _gnss.substring(0, 6).equals("$GNGSA")) {
                        calcGSA();
                    }
                }
                break;
            case 2:
                type = 0;
                setGnssF(false);
                if (_da_gnss != null && _da_gnss.length > 0) {
                    long TIME = 0;
                    if ((long)_da_gnss[8] != 0){
                        TIME = (long)_da_gnss[8];
                    }else{
                        TIME = DateTime.getTime();
                    }
                    //
                    accuracy = _da_gnss[0]; // 米
                    erro = _da_gnss[1]; // 米
                    longitude = _da_gnss[2]; // 度
                    latitude = _da_gnss[3]; // 度
                    altitude = _da_gnss[4]; // 度
                    delication = -_da_gnss[5]; // 磁偏角度数，指向是真北偏东为正，-180~180，加负号转为指向逆时针旋转至真北为正
                    bearing = _da_gnss[6] > 180 ? 360 - _da_gnss[6] : -_da_gnss[6]; //度，0~360，顺时针方向，北为主方向，转换为逆时针为正-180~180
                    speed = _da_gnss[7]; // 米每秒
                    time = DateTime.format(TIME); // 毫秒时间转格式字符串时间
                    type = (int)_da_gnss[9]; // 0 不能定位；1 网络定位或估算中； 2 差分定位； 3 自动定位； 4 组合定位
                    StarNumber = (int)_da_gnss[10]; // 卫星数量
                    GDOP = _da_gnss[11]; // 几何精度因子
                    //
                    gnssF = true;
                }else {
                    setGnssF(false);
                }
                break;
        }
    }

    public void calcGGA() {
        // this._gnss = gnss;
        //System.out.println(_gnss);
        // TODO GNSS数据解析
        //System.out.println("   解析   gnss");
        String[] source = this._gnss.split(",", 15);
        if (source != null && (source[0].equals("$GNGGA") || source[0].equals("$GPGGA") || source[0].equals("$GBGGA")) && source.length == 15) {

            // 定位类型
            if (source[6].equals("0")) {
                type = 0;
            } else if (source[6].equals("6")) {
                type = 1;
            } else if (source[6].equals("2")) {
                type = 2;
            } else if (source[6].equals("1")) {
                type = 3;
            }

            // 纬度
            if (source[3].length() > 0 && source[2].length() > 2) {
                String s1 = "0";
                if (source[3].equals("N")) {
                    s1 = "+1";
                } else if (source[3].equals("S")) {
                    s1 = "-1";
                }
                int lenb = source[2].length();
                String s2 = source[2].substring(0, 2);
                String s3 = source[2].substring(2, lenb);

                latitude = Double.parseDouble(s1)
                        * (Double.parseDouble(s2) + Double.parseDouble(s3) / 60.0);
            }


            // 经度
            if (source[5].length() > 0 && source[4].length() > 3) {
                String s1 = "0";
                if (source[5].equals("E")) {
                    s1 = "+1";
                } else if (source[5].equals("W")) {
                    s1 = "-1";
                }
                int lenl = source[4].length();
                String s2 = source[4].substring(0, 3);
                String s3 = source[4].substring(3, lenl);
                longitude = Double.parseDouble(s1)
                        * (Double.parseDouble(s2) + Double.parseDouble(s3) / 60.0);
            }

            //星数
            if (source[7].length() > 0) {
                StarNumber = Integer.parseInt(source[7]);
            }

            //水平精度因子
            if (source[8].length() > 0) {
                accuracy = Double.parseDouble(source[8]);
            }


            //高度
            if (source[9].length() > 0) {
                altitude = Double.parseDouble(source[9]);
            }
/*
            // 日期-时间
            if (source[1].length() >= 6) {
                String str = DateTime.get();
                time = str.substring(0, 10) + "-"
                        + source[1].substring(0, 2) + "-"
                        + source[1].substring(2, 4) + "-"
                        + source[1].substring(4, 6) + "-"
                        + source[1].substring(7, 10);
            }
*/
            gnssF = true;
        }
    }

    public void calcRMC() {
        // this._gnss = gnss;
        //System.out.println(_gnss);
        // TODO GNSS数据解析
        //System.out.println("   解析   gnss");
        String[] source = this._gnss.split(",", 13);
        if (source != null && (source[0].equals("$GNRMC") || source[0].equals("$GPRMC") || source[0].equals("$GBRMC")) && source.length == 13) {

            // 定位类型
            if (source[2].equals("V")/* || source[12].substring(1) == "N" */) {
                type = 0;
            } else if (source[2].equals("A")
                    && source[12].substring(1).equals("E")) {
                type = 1;
            } else if (source[2].equals("A")
                    && source[12].substring(1).equals("D")) {
                type = 2;
            } else if (source[2].equals("A")
                    && source[12].substring(1).equals("A")) {
                type = 3;
            }

            // 纬度
            if (source[4].length() > 0 && source[3].length() > 2) {
                String s1 = "0";
                if (source[4].equals("N")) {
                    s1 = "+1";
                } else if (source[4].equals("S")) {
                    s1 = "-1";
                }
                int lenb = source[3].length();
                String s2 = source[3].substring(0, 2);
                String s3 = source[3].substring(2, lenb);

                latitude = Double.parseDouble(s1)
                        * (Double.parseDouble(s2) + Double.parseDouble(s3) / 60.0);
            }

            // 经度
            if (source[6].length() > 0 && source[5].length() > 3) {
                String s1 = "0";
                if (source[6].equals("E")) {
                    s1 = "+1";
                } else if (source[6].equals("W")) {
                    s1 = "-1";
                }
                int lenl = source[3].length();
                String s2 = source[5].substring(0, 3);
                String s3 = source[5].substring(3, lenl);
                longitude = Double.parseDouble(s1)
                        * (Double.parseDouble(s2) + Double.parseDouble(s3) / 60.0);
            }

            // 速度
            if (source[7].length() > 0) {
                speed = Double.parseDouble(source[7]) * 1852.0 / 3600.0;
            }

            // 方位角
            if (source[8].length() > 0) {
                double x = Double.parseDouble(source[8]);
                bearing = x > 180 ? 360 - x : -x;
            }

            // 磁偏角
            if (source[10].length() > 0 && source[11].length() > 0) {
                String sx = "0";
                if (source[11].equals("E")) {
                    sx = "-";
                } else if (source[11].equals("W")) {
                    sx = "+";
                }
                delication = Double.parseDouble(sx + source[10]);
            }

            // 日期-时间
            if (source[9].length() == 6 && source[1].length() >= 6) {
                String s = "000";
                int lent = source[1].length();
                if (lent == 8) {
                    s = source[1].substring(7, 8) + "00";
                } else if (lent == 9) {
                    s = source[1].substring(7, 9) + "0";
                } else if (lent == 10) {
                    s = source[1].substring(7, 10);
                }
                time = "20" + source[9].substring(4, 6) + "-"
                        + source[9].substring(2, 4) + "-"
                        + source[9].substring(0, 2) + "-"
                        + source[1].substring(0, 2) + "-"
                        + source[1].substring(2, 4) + "-"
                        + source[1].substring(4, 6) + "-"
                        + s;
                time = DateTime.format(DateTime.transform(time) + 2.88 * 1e7);//UTC时间转北京时间
                //System.out.println("tmie8: "+time);
            }
            gnssF = true;
        }
    }

    public void calcVTG() {
        // TODO GNSS数据解析
        String[] source = this._gnss.split(",", 8);
        if (source != null && (source[0].equals("$GNVTG") || source[0].equals("$GPVTG") || source[0].equals("$GBVTG")) && source.length == 10) {
            //定位模式
            if (source[9].substring(1) == "N") {
                type = 0;
            } else if (source[9].substring(1).equals("E")) {
                type = 1;
            } else if (source[9].substring(1).equals("D")) {
                type = 2;
            } else if (source[9].substring(1).equals("A")) {
                type = 3;
            }

            // 速度
            if (source[7].length() > 0) {
                speed = Double.parseDouble(source[7]) * 1000.0 / 3600.0;
            } else if (source[5].length() > 0) {
                speed = Double.parseDouble(source[5]) * 1852.0 / 3600.0;
            }

            // 方位角
            double bearing1 = 0.0;
            double bearing2 = 0.0;
            if (source[1].length() > 0) {
                double x = Double.parseDouble(source[1]);
                bearing1 = x > 180 ? 360 - x : -x;
            }
            if (source[3].length() > 0) {
                bearing2 = Double.parseDouble(source[3]) * 1852.0 / 3600.0;
            }
            bearing = bearing1;
            // 磁偏角
            if (source[1].length() > 0 && source[3].length() > 0) {
                delication = bearing2 - bearing1;
            }

            gnssF = true;
        }
    }

    public void calcGSA() {
        // TODO GNSS数据解析
        String[] source = this._gnss.split(",", 8);
        if (source != null && (source[0].equals("$GNGSA") || source[0].equals("$GPGSA") || source[0].equals("$GBGSA")) && source.length == 18) {
            //定位模式
            if (source[2].substring(1) == "0") {
                type = 0;
            } else if (source[2].substring(1).equals("2")) {
                type = 3;
            } else if (source[2].substring(1).equals("1")) {
                type = 2;
            } else {
                type = 1;
            }

            // 精度因子
            if (source[16].length() > 0) {
                accuracy = Double.parseDouble(source[16]);//水平hdop
            }
            if (source[17].length() > 0) {
                erro = Double.parseDouble(source[5]);//垂直vdop
            }

            gnssF = true;
        }
    }

    //
    public double getAccuracy() {

        return accuracy;
    }

    public double getErro() {

        return erro;
    }

    public double getLongitude() {
        // longitude = Math.random() * 360 - 180;
        return longitude;
    }

    public double getLatitude() {
        // latitude = Math.random() * 180 - 90;
        return latitude;
    }

    public double getAltitude() {
        // altitude = Math.random() * 20 + 20;
        return altitude;
    }

    public double getDelication() {

        return delication;
    }

    public double getBearing() {

        return bearing;
    }

    public double getSpeed() {

        return speed;
    }

    public String getTime() {
        // DateTime.get();
        return time;
    }

    public int getType() {
        //System.out.println("GnssType: "+type);
        return type;
    }

    public boolean getGnssF() {

        return gnssF;
    }

    public void setGnssF(boolean gnssF) {

        this.gnssF = gnssF; //
    }

    public int getStarNumber() {

        return StarNumber;
    }

    public double getGDOP() {

        return GDOP;
    }

}
