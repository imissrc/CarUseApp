/**
 * 
 */
package com.caruseapp.locating.IOface;

/**
 * @author Administrator
 *
 */
public interface DataInterface {

	/**
	 * 开始计算
	 */
	void startCalc();

	/**
	 * 结束计算
	 */
	void stopCalc();

	//void event(String GNSS, byte[] IMU10);
	void eventGNSS(String GNSS);
	void eventIMU(byte[] IMU10);
	void eventGNSS(double[] GNSS);
	void eventIMU(double[] IMU10);
}
