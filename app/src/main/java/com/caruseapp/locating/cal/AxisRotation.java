package com.caruseapp.locating.cal;

import java.util.Arrays;

public class AxisRotation {
    // //东北天坐标转右前上坐标，yaw-pitch-roll，ZXY
	public static double[] ENU2RFT(double[] enuPosition, double[] eularAngle)
	{
		double xPitch = eularAngle[0];
		double yRoll = eularAngle[1];
		double zYaw = eularAngle[2];

		double[][] a = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
		a[0][0] = Math.cos(yRoll)*Math.cos(zYaw)- Math.sin(xPitch)*Math.sin(yRoll)*Math.sin(zYaw);
		a[0][1] = Math.cos(yRoll)*Math.sin(zYaw)+ Math.cos(zYaw)*Math.sin(xPitch)*Math.sin(yRoll);
		a[0][2] = -Math.cos(xPitch)*Math.sin(yRoll);
		a[1][0] = -Math.cos(xPitch)*Math.sin(zYaw);
		a[1][1] = Math.cos(xPitch)*Math.cos(zYaw);
		a[1][2] = Math.sin(xPitch);
		a[2][0] = Math.cos(zYaw)*Math.sin(yRoll)+Math.cos(yRoll)*Math.sin(xPitch)*Math.sin(zYaw);
		a[2][1] = Math.sin(yRoll)*Math.sin(zYaw)-Math.cos(yRoll)*Math.cos(zYaw)*Math.sin(xPitch);
		a[2][2] = Math.cos(xPitch)*Math.cos(yRoll);

		double[] rftPosition = {0, 0, 0};
		double[][] matB = {{enuPosition[0],0,0}, {enuPosition[1],0,0}, {enuPosition[2],0,0}};
		double[][] matProduct = BaseCompute.matProduct(a, matB);
		

		rftPosition[0] = matProduct[0][0];
		rftPosition[1] = matProduct[1][0];
		rftPosition[2] = matProduct[2][0];
		return rftPosition;
	}

	public static double[] RFT2ENU(double[] rftPosition, double[] eularAngle)
	{
		double xPitch = eularAngle[0];
		double yRoll = eularAngle[1];
		double zYaw = eularAngle[2];
		double[][] a = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

		a[0][0] = Math.cos(yRoll)*Math.cos(zYaw)-Math.sin(xPitch)*Math.sin(yRoll)*Math.sin(zYaw);
		a[1][0] = Math.cos(yRoll)*Math.sin(zYaw)+Math.cos(zYaw)*Math.sin(xPitch)*Math.sin(yRoll);
		a[2][0] = -Math.cos(xPitch)*Math.sin(yRoll);
		a[0][1] = -Math.cos(xPitch)*Math.sin(zYaw);
		a[1][1] = Math.cos(xPitch)*Math.cos(zYaw);
		a[2][1] = Math.sin(xPitch);
		a[0][2] = Math.cos(zYaw)*Math.sin(yRoll)+Math.cos(yRoll)*Math.sin(xPitch)*Math.sin(zYaw);
		a[1][2] = Math.sin(yRoll)*Math.sin(zYaw)-Math.cos(yRoll)*Math.cos(zYaw)*Math.sin(xPitch);
		a[2][2] = Math.cos(xPitch)*Math.cos(yRoll);

		double[] enuPosition = {0, 0, 0};
		double[][] matB = {{rftPosition[0]}, {rftPosition[1]}, {rftPosition[2]}};
		double[][] matProduct = BaseCompute.matProduct(a, matB);
		enuPosition[0] = matProduct[0][0];
		enuPosition[1] = matProduct[1][0];
		enuPosition[2] = matProduct[2][0];
		return enuPosition;

	}
	
}
