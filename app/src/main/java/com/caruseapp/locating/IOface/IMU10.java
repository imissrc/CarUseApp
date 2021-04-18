package com.caruseapp.locating.IOface;

public class IMU10 {
    // ?????10??IMU???????????
    // ?????????????????????????????????????????
    // ???????????????????????????��????/???��???
    private double accE = 0; // ??????,???
    private double accX = 0; // X???,???
    private double accY = 0; // Y???,???
    private double accZ = 0; // Z???,???
    // ?????????????��??????/??
    private double gyroE = 0;
    private double gyroX = 0;
    private double gyroY = 0;
    private double gyroZ = 0;
    // ???????????????��????????=0.01?????
    private double magE = 0; // ????
    private double magX = 0;
    private double magY = 0;
    private double magZ = 0;
    // ???-?????????��???????????
    private double hpaE = 0; // ????
    private double hpaH = 0;
    //
    private boolean IMUf = true;
    //
    private byte[] _imu10;
    private double[] _da_imu10;

    public IMU10(){}

    public IMU10(byte[] imu10) {
        // TODO ?????????????????
        this._imu10 = imu10;
    }
    public IMU10(double[] imu10) {
        // TODO ?????????????????
        this._da_imu10 = imu10;
    }

    public void calc(int data_flag) {
        // IMU数据解析
        switch(data_flag) {
            case 1:
                if (_imu10 != null && _imu10.length == 23 && _imu10[0] == (byte) 0xAA
                        && _imu10[1] == (byte) 0x33 && _imu10[22] == (byte) 0xFF) {

                    // 加速度计：重力加速度比力，mG
                    accX = 0.8 * (double) ((_imu10[2] << 8) | (_imu10[3] & 0x00FF));
                    accY = -0.8 * (double) ((_imu10[4] << 8) | (_imu10[5] & 0x00FF));
                    accZ = -0.8 * (double) ((_imu10[6] << 8) | (_imu10[7] & 0x00FF));

                    // 陀螺仪：角速度,度/秒
                    gyroX = 0.02 * (double) ((_imu10[8] << 8) | (_imu10[9] & 0x00FF));
                    gyroY = -0.02 * (double) ((_imu10[10] << 8) | (_imu10[11] & 0x00FF));
                    gyroZ = -0.02 * (double) ((_imu10[12] << 8) | (_imu10[13] & 0x00FF));

                    // 磁力计：磁感应强度,mgauss
                    magX = 0.1 * (double) ((_imu10[14] << 8) | (_imu10[15] & 0x00FF));
                    magY = -0.1 * (double) ((_imu10[16] << 8) | (_imu10[17] & 0x00FF));
                    magZ = -0.1 * (double) ((_imu10[18] << 8) | (_imu10[19] & 0x00FF));

                    // 气压高度计：大气压强微巴：uBar
                    hpaH = 40 * (double) ((_imu10[20] << 8) | (_imu10[21] & 0x00FF));

                    //
                    IMUf = true;
                } else {
                    setIMUf(false);
                }
                break;
            case 2:
                if (_da_imu10 != null && _da_imu10.length == 10){
                    accX = _da_imu10[0] * Math.pow(10, 3) / 9.80655;
                    accY = _da_imu10[1] * Math.pow(10, 3) / 9.80655;
                    accZ = _da_imu10[2] * Math.pow(10, 3) / 9.80655;
                    //
                    gyroX = _da_imu10[3] / Math.PI * 180.0;
                    gyroY = _da_imu10[4] / Math.PI * 180.0;
                    gyroZ = _da_imu10[5] / Math.PI * 180.0;
                    //
                    magX = _da_imu10[6] * 10.0;
                    magY = _da_imu10[7] * 10.0;
                    magZ = _da_imu10[8] * 10.0;
                    //
                    hpaH = _da_imu10[9] * Math.pow(10, 3);
                    //
                    IMUf = true;
                    //11-03 17:38:24.149 27635-27732/com.example.demo I/System.out:
                    // imu10data:
                    // 0.12083754688501358 -0.2267199456691742 9.455238342285156 -0.04948008432984352 -0.09224064648151398
                    // 0.025503622367978096 8.399999618530273 11.880000114440918 25.31999969482422 959.7899780273438
                    //System.out.print("imu10data: ");
                    //for(double a:_da_imu10) { System.out.print(a+" "); }
                    //System.out.print("\n");
                    //System.out.println(_da_imu10[0] + " " + _da_imu10[1] + " " + _da_imu10[2]);
                }else{
                    setIMUf(false);
                }
                break;
        }
    }

    public double getAccE() {
        return accE;
    }

    public double getAccX() {
        //accX = ((Math.random()>0.5?1:-1)*Math.random()+0.0)* Math.pow(10, 3);
        return accX;
    }

    public double getAccY() {
        //accY = ((Math.random()>0.5?1:-1)*Math.random()*Math.sqrt((1-Math.pow(accX/1000.0,2)))+0.0)*Math.pow(10, 3);
        return accY;
    }

    public double getAccZ() {
        //accZ = ((Math.random()>0.5?1:-1)*Math.sqrt(1-Math.pow(accX/1000.0,2)-Math.pow(accY/1000.0,2))+0.0)*Math.pow(10, 3);
        return accZ;
    }

    //
    public double getGyroE() {
        return gyroE;
    }

    public double getGyroX() {
        //gyroX = (Math.random()-0.5)*2*90;
        return gyroX;
    }

    public double getGyroY() {
        //gyroY = (Math.random()-0.5)*2*180;
        return gyroY;
    }

    public double getGyroZ() {
        //gyroZ = (Math.random()-0.5)*2*180;
        return gyroZ;
    }

    //
    public double getMagE() {
        return magE;
    }

    public double getMagX() {
        //magX = (Math.random()>0.5?1:-1)*Math.random()*600;
        return magX;
    }

    public double getMagY() {
        //magY = (Math.random()>0.5?1:-1)*Math.random()*Math.sqrt(1-Math.pow(magX/600.0,2))*600;
        return magY;
    }

    public double getMagZ() {
        //magZ = (Math.random()>0.5?1:-1) * Math.sqrt(1-Math.pow(magX/600.0,2)-Math.pow(magY/600.0,2)) * 600;
        return magZ;
    }

    //
    public double getHpaE() {
        return hpaE;
    }

    public double getHpaH() {
        //hpaH = ((Math.random()-0.5)*2*0.25-5 + 1013.25)*1000;
        return hpaH;
    }

    //
    public boolean getIMUf() {
        return IMUf;
    }

    public void setIMUf(boolean iMUf) {
        IMUf = iMUf;
    }

}
