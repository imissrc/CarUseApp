package com.caruseapp.locating.cal;

public class PoseTransform {
	// ��̬ת���෽������ת˳��ZXY��yaw(z)-pitch(x)-roll(y)�� �����Ԫ��q ��СΪ 4
	final public static double[] rMatrix2Quat(double[][] dcm) {
		// ROTATIONMATRIXTOQUAT ��ת����ת��ת��Ԫ��
		double[] q = new double[4];
		q[0] = 1;// qw
		q[1] = 0;// qX
		q[2] = 0;// qY
		q[3] = 0;// qZ

		if ((dcm.length != 3) || (dcm[0].length != 3)) {
			return q;
		}

		double tr = dcm[0][0] + dcm[1][1] + dcm[2][2]; // �����������Խ�Ԫ��֮��
		double sqtrp1 = 0, sqdip1 = 0;
		double[] d = new double[3];
		if (tr > 0) {
			sqtrp1 = Math.sqrt(tr + 1.0);
			q[0] = 0.5 * sqtrp1;
			q[1] = (dcm[1][2] - dcm[2][1]) / (2.0 * sqtrp1);
			q[2] = (dcm[2][0] - dcm[0][2]) / (2.0 * sqtrp1);
			q[3] = (dcm[0][1] - dcm[1][0]) / (2.0 * sqtrp1);
		} else {
			d[0] = dcm[0][0];
			d[1] = dcm[1][1];
			d[2] = dcm[2][2];
			if ((d[1] > d[0]) && (d[1] > d[2])) {
				// max value at dcm[0][0]
				sqdip1 = Math.sqrt(d[1] - d[0] - d[2] + 1.0);
				q[2] = 0.5 * sqdip1;

				if (Math.abs(sqdip1)<1e-10) {
					sqdip1 = 0.5 / sqdip1;
				}

				q[0] = (dcm[2][0] - dcm[0][2]) * sqdip1;
				q[1] = (dcm[0][1] + dcm[1][0]) * sqdip1;
				q[3] = (dcm[1][2] + dcm[2][1]) * sqdip1;
			} else if (d[2] > d[0]) {
				// max value at dcm[1][2]
				sqdip1 = Math.sqrt(d[2] - d[0] - d[1] + 1.0);

				q[3] = 0.5 * sqdip1;

				if (sqdip1 != 0) {
					sqdip1 = 0.5 / sqdip1;
				}

				q[0] = (dcm[0][1] - dcm[1][0]) * sqdip1;
				q[1] = (dcm[2][0] + dcm[0][2]) * sqdip1;
				q[2] = (dcm[1][2] + dcm[2][1]) * sqdip1;
			} else {
				// max value at dcm[0][0]
				sqdip1 = Math.sqrt(d[0] - d[1] - d[2] + 1.0);
				q[1] = 0.5 * sqdip1;

				if (sqdip1 != 0) {
					sqdip1 = 0.5 / sqdip1;
				}

				q[0] = (dcm[1][2] - dcm[2][1]) * sqdip1;
				q[2] = (dcm[0][1] + dcm[1][0]) * sqdip1;
				q[3] = (dcm[2][0] + dcm[0][2]) * sqdip1;
			}
		}
        q=Quat.QuatNorm(q);
		return q;
	}

	final public static double[][] rQuat2Matrix(double[] qin) {
		double[][] dcm = {{1,0,0},{0,1,0},{0,0,1}};
		dcm[0][0] = qin[0]*qin[0] + qin[1]*qin[1] - qin[2]*qin[2] - qin[3]*qin[3];
		dcm[0][1] = 2*(qin[1]*qin[2] + qin[0]*qin[3]);
		dcm[0][2] = 2*(qin[1]*qin[3] - qin[0]*qin[2]);
		dcm[1][0] = 2*(qin[1]*qin[2] - qin[0]*qin[3]);
		dcm[1][1] = qin[0]*qin[0] - qin[1]*qin[1] + qin[2]*qin[2] - qin[3]*qin[3];
		dcm[1][2] = 2*(qin[2]*qin[3] + qin[0]*qin[1]);
		dcm[2][0] = 2*(qin[1]*qin[3] + qin[0]*qin[2]);
		dcm[2][1] = 2*(qin[2]*qin[3] - qin[0]*qin[1]);
		dcm[2][2] = qin[0]*qin[0] - qin[1]*qin[1] - qin[2]*qin[2] + qin[3]*qin[3];
		return dcm;
	}

	final public static double[] rMatrix2Angle(double[][] RotationMatrix) {
		double[] RotationAngle={Math.asin(RotationMatrix[1][2]),Math.atan2(-RotationMatrix[0][2],RotationMatrix[2][2]),Math.atan2(-RotationMatrix[1][0],RotationMatrix[1][1])};                       // {X-Pitch,Y-Roll,Z-Yaw}
		return RotationAngle;
	}

	final public static double[][] rAngle2Matrix(double[] RotationAngle) {
		double ThetaX=RotationAngle[0];
		double GamaY=RotationAngle[1];
		double PsiZ=RotationAngle[2];
		double[][] PostureMatrix = {{Math.cos(GamaY)*Math.cos(PsiZ)-Math.sin(ThetaX)*Math.sin(GamaY)*Math.sin(PsiZ),Math.cos(GamaY)*Math.sin(PsiZ)+Math.cos(PsiZ)*Math.sin(ThetaX)*Math.sin(GamaY),-Math.cos(ThetaX)*Math.sin(GamaY)},
				{-Math.cos(ThetaX)*Math.sin(PsiZ),Math.cos(ThetaX)*Math.cos(PsiZ),Math.sin(ThetaX)},
				{Math.cos(PsiZ)*Math.sin(GamaY)+Math.cos(GamaY)*Math.sin(ThetaX)*Math.sin(PsiZ),Math.sin(GamaY)*Math.sin(PsiZ)-Math.cos(GamaY)*Math.cos(PsiZ)*Math.sin(ThetaX),Math.cos(ThetaX)*Math.cos(GamaY)}};
		return PostureMatrix;
	}

	final public static double[] rQuat2Angle(double rQw, double rQx,
			double rQy, double rQz) {
		//��Ԫ����̬תŷ������̬ ,'ZXY'
		double[] rA = new double[3];//pitch-roll-yaw
		double[] qin = new double[4];
		qin[0]=rQw;
		qin[1]=rQx;
		qin[2]=rQy;
		qin[3]=rQz;
		rA[0] = Math.asin(2.0*(qin[2]*qin[3] + qin[0]*qin[1]));
		rA[0] = Double.isNaN(rA[0])?0:rA[0]; // //////////////////////////////////////////
		rA[1] = Math.atan2( -2.0*(qin[1]*qin[3] - qin[0]*qin[2]) , qin[0]*qin[0] - qin[1]*qin[1] - qin[2]*qin[2] + qin[3]*qin[3] );//(y,x)
		rA[2] = Math.atan2( -2.0*(qin[1]*qin[2] - qin[0]*qin[3]) , qin[0]*qin[0] - qin[1]*qin[1] + qin[2]*qin[2] - qin[3]*qin[3] );//(y/x)
        
		return rA;
	}

	public static double[] rAngle2Quat(double[] rotateAngle)
	{
		//�ǶȞ黡��
		double[] cang = {0, 0, 0};
		double[] sang = {0, 0, 0};
		for(int i = 0; i < 3; ++i)
		{
			cang[i] = Math.cos( rotateAngle[i]/2.0 );
			sang[i] = Math.sin( rotateAngle[i]/2.0 );
		}

		double[] quatMat = {1, 0, 0, 0};
		quatMat[0] =cang[0]*cang[1]*cang[2] - sang[0]*sang[1]*sang[2];
		quatMat[1] = sang[0]*cang[1]*cang[2] - cang[0]*sang[1]*sang[2];
		quatMat[2] = cang[0]*sang[1]*cang[2] + sang[0]*cang[1]*sang[2];
		quatMat[3] = sang[0]*sang[1]*cang[2] + cang[0]*cang[1]*sang[2];

		return quatMat;
	}

}
