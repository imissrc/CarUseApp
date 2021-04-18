package com.caruseapp.locating.IOface;


/*
 * 鏂囦欢鍚嶏細鏁版嵁鍒嗘瀽鍣�
 * 鐗堟潈锛欳opyright 2002-2018 heweisoft Tech. Co. Ltd. All Rights Reserved.
 * 鎻忚堪锛� locating.IOface
 * 淇敼鏃堕棿锛�2019/11/27
 * 淇敼鍐呭锛氭柊澧�
 * 鍒涘缓鑰咃細hewei
 */
public class DataAnalyser implements DataInterface {

    public DataAnalyser(DataCalcCallback callback) {
        final Locate singleObject = Locate.getSingleObject();
        singleObject.dc.addCallback(callback);
    }

    @Override
    public void startCalc() {
        final Locate singleObject = Locate.getSingleObject();
        singleObject.startCalc();
    }

    @Override
    public void stopCalc() {
        final Locate singleObject = Locate.getSingleObject();
        singleObject.stopCalc();
    }

   /* @Override
    public void event(String gnss, byte[] imu10) {
        final Locate singleObject = Locate.getSingleObject();
        singleObject.dc.init(gnss,imu10);
    }*/

    @Override
    public void eventGNSS(String gnss) {
        final Locate singleObject = Locate.getSingleObject();
        singleObject.dc.getGNSS(gnss);
    }

    @Override
    public void eventIMU(byte[] imu10) {
        final Locate singleObject = Locate.getSingleObject();
        singleObject.dc.getIMU(imu10);
    }

    @Override
    public void eventGNSS(double[] gnss) {
        final Locate singleObject = Locate.getSingleObject();
        singleObject.dc.getGNSS(gnss);
    }

    @Override
    public void eventIMU(double[] imu10) {
        final Locate singleObject = Locate.getSingleObject();
        singleObject.dc.getIMU(imu10);
    }
}
