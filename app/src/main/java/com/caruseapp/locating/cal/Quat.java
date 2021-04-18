package com.caruseapp.locating.cal;

public class Quat {

	public static double[] QuatNorm(double[] q) {
		// TODO nomalize quatation
		double tempq=Math.sqrt(q[0]*q[0]+ q[1]*q[1]+ q[2]*q[2]+ q[3]*q[3]);
		if(tempq<1e-10) {
			q[0] = 1;
			q[1] = 0;
			q[2] = 0;
			q[3] = 0;
		} else{
			q[0] = q[0] / tempq;
			q[1] = q[1] / tempq;
			q[2] = q[2] / tempq;
			q[3] = q[3] / tempq;
		}
		return q;
	}

}
