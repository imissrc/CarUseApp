/**
 * demo.test
 */
package com.caruseapp.locating.demo;


import com.caruseapp.locating.IOface.Locate;

/**
 * @author zhaoyang.su 742248041@qq.com
 */
public class test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Use down two lines code to start Locate at outer:
		Locate locate = Locate.getSingleObject();// Locate is single object class
		locate.startCalc();//start
		//use reloaded method directly start at inner:   Locate locate = Locate.getSingleObject("InnerStart");//
		String string = "";
		int i = 0;
		while (true) {
			i++;
			/*if (i == 10) {
				locate.stopCalc();//stop locate
				System.out.println("stop run at : " + "i" + " times loop!");
			}
			if (i==50) {
					locate.startCalc();//restart
					System.out.println("restart run at : " + i + " times loop!");
			}
			if (i==100) {
				locate.stopCalc();//stop
				System.out.println("stop run and exit at : " + i + " times loop!");
				return;//exit
		    }*/
			string = locate.getPose();//
			System.out.println(string);
			// Json String:
			// {"time":"2019-11-27-10-24-06-004","longitude":"-4.797343705389636","latitude":"62.86146787024766","height":"27.19989202058521","state":"��"}
			//System.out.println("yaw: " + (locate.getYaw()<=0?0-locate.getYaw():360-locate.getYaw()));//
			/*
			 * System.out.println("time: " + locate.getTime());//
			 * System.out.println("longitude: " + locate.getLongitude());//
			 * System.out.println("latitude: " + locate.getLatitude());//
			 * System.out.println("height: " + locate.getHeight());//
			 * System.out.println("pitch: " + locate.getPitch());//
			 * System.out.println("roll: " + locate.getRoll());//
			 * System.out.println("yaw: " + locate.getYaw());//
			 * System.out.println("move state: " + locate.getPdrState());״̬
			 * System.out.println("Length: " + locate.getLength());//
			 * * System.out.println("Length: " + locate.getStepCounter());//
			 * System.out.println("Bearing: " + locate.getAngle());//
			 */
			System.out.println("");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
