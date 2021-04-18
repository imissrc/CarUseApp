package com.caruseapp.locating.cal;

import java.util.Arrays;

public class PositionTransform {
	//浣嶇疆杞崲绫绘柟娉�
	public static double[] wgs84toXyz(double[] wgs84Position) {
		double a = 6378137;
		double f = 1 / 298.257223563;
		double b = a * (1 - f);
		double e = Math.sqrt(a * a - b * b) / a;
		double n = a
				/ Math.sqrt(1 - e * e
						* Math.sin(wgs84Position[1])
						* Math.sin(wgs84Position[1]));
		double[] xyzPosition = { 0, 0, 0 };
		xyzPosition[0] = (n + wgs84Position[2])
				* Math.cos(wgs84Position[0])
				* Math.cos(wgs84Position[1]);
		xyzPosition[1] = (n + wgs84Position[2])
				* Math.sin(wgs84Position[0])
				* Math.cos(wgs84Position[1]);
		xyzPosition[2] = (n * (1 - (e * e)) + wgs84Position[2])
				* Math.sin(wgs84Position[1]);
		return xyzPosition;
	}

	public static double[] xyztoWgs84(double[] xyzPosition) {
		double a = 6378137;
		double f = 1 / 298.257223563;
		double b = a * (1 - f);
		double ee = (a * a - b * b) / Math.pow(a, 2);
		double ee2 = (a * a - b * b) / Math.pow(b, 2);
		double[] dposition = { xyzPosition[0], xyzPosition[1] };

		double theta = Math.atan2(xyzPosition[2]*a, BaseCompute.norm2(dposition)*b);
		double[] wgs84Position = { 0, 0, 0 };
		wgs84Position[0] = Math.atan2(xyzPosition[1], xyzPosition[0]);
		wgs84Position[1] = Math.atan2(
				xyzPosition[2] + ee2 * b * Math.pow(Math.sin(theta), 3),
				BaseCompute.norm2(dposition) - ee * a
						* Math.pow(Math.cos(theta), 3));
		wgs84Position[2] = BaseCompute.norm2(dposition)
				/ Math.cos(wgs84Position[1]) - a
				/ (Math.sqrt(1 - ee * Math.pow(Math.sin(wgs84Position[1]), 2)));
		return wgs84Position;
	}

	//
	public static double[] enutoXyz(double[] enuPosition, double[] enuCenter, double[] center)
	{
		double lambda = center[0];
		double gama = lambda + 90*Math.PI/180;
		double phi = center[1];
		double a = 6378137;  //wgs84妞悆闀垮害锛岀背
		double f = 1/298.257223563;  //妞渾鎵佺巼
		double b = a*(1-f);
		double alpha = 0;
		double[] xyzPosition = {0, 0, 0};
		double[] wgs84Position = {center[0], center[1], 0};
		double[] centerProjection = wgs84toXyz(wgs84Position);

		if (!BaseCompute.vecIsZero(enuCenter))
		{
			double centerNorm2 = BaseCompute.norm2(centerProjection);
			double[] subspaceA = BaseCompute.vectorProductNum(centerProjection, 1/centerNorm2);
			double[] pointDivVector = {Math.pow(a, 2), Math.pow(a, 2), Math.pow(b, 2)};
			double[] centerTemp = BaseCompute.pointDiv(BaseCompute.vectorProductNum(centerProjection, 2), pointDivVector);
			double centerTempNorm =  BaseCompute.norm2(centerTemp);
			double[] subspaceB = BaseCompute.vectorProductNum(centerTemp, 1/centerTempNorm);
			alpha= Math.signum(center[1])*Math.abs(BaseCompute.subspace(subspaceA, subspaceB));
		}
		double beta = alpha + phi;
		double theta = 90*Math.PI/180.0 - beta;
		double[][] coefficientA= {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
		coefficientA[0][0] = Math.cos(gama);
		coefficientA[1][0] = Math.sin(gama);
		coefficientA[2][0] = 0;
		coefficientA[0][1] = -Math.cos(theta)*Math.sin(gama);
		coefficientA[1][1] = Math.cos(theta)*Math.cos(gama);
		coefficientA[2][1] = Math.sin(theta);
		coefficientA[0][2] = Math.sin(theta)*Math.sin(gama);
		coefficientA[1][2] = -Math.sin(theta)*Math.cos(gama);
		coefficientA[2][2] = Math.cos(theta);
		double[] matProduct = BaseCompute.matProduct(coefficientA, enuPosition);
		xyzPosition=BaseCompute.addVector(matProduct, enuCenter);
		return xyzPosition;
	}

	public static double[] xyztoEnu(double[] xyzPosition,  double[] enuCenter, double[] center)
	{

		double lambda = center[0];
		double gama = lambda + 90*Math.PI/180.0;
		double phi = center[1];
		double a = 6378137;
		double f = 1/298.257223563;
		double b = a*(1-f);
		double[] enuPosition = {0, 0, 0};
		double[] wgs84Position = {center[0], center[1], 0};
		double[] centerProjection = wgs84toXyz(wgs84Position);

		double alpha = 0;
		if(!BaseCompute.vecIsZero(enuCenter))
		{
			double centerNorm2 = BaseCompute.norm2(centerProjection);
			double[] subspaceA = BaseCompute.vectorProductNum(centerProjection, 1/centerNorm2);
			double[] pointDivVector = {Math.pow(a, 2), Math.pow(a, 2), Math.pow(b, 2)};
			double[] centerTemp = BaseCompute.pointDiv(BaseCompute.vectorProductNum(centerProjection, 2), pointDivVector);
			double centerTempNorm =  BaseCompute.norm2(centerTemp);
			double[] subspaceB = BaseCompute.vectorProductNum(centerTemp, 1/centerTempNorm);
			alpha = Math.signum(center[1])*Math.abs(BaseCompute.subspace(subspaceA, subspaceB));
		}
		double beta = alpha + phi;
		double theta = 90*Math.PI/180.0 - beta;
		double[][] coefficientA= {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
		coefficientA[0][0] = Math.cos(gama);
		coefficientA[0][1] = Math.sin(gama);
		coefficientA[0][2] = 0;
		coefficientA[1][0] = -Math.cos(theta)*Math.sin(gama);
		coefficientA[1][1] = Math.cos(theta)*Math.cos(gama);
		coefficientA[1][2] = Math.sin(theta);
		coefficientA[2][0] = Math.sin(theta)*Math.sin(gama);
		coefficientA[2][1] = -Math.sin(theta)*Math.cos(gama);
		coefficientA[2][2] = Math.cos(theta);
		double[] subVector = BaseCompute.subVector(xyzPosition, enuCenter);
		double[] matProduct = BaseCompute.matProduct(coefficientA, subVector);
		enuPosition=matProduct;
		return enuPosition;
	}

}
