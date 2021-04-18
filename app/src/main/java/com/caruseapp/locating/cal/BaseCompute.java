/**
 * @version: V1.0
 * @author: xieqiong,suchaoyang
 * @className: BaseCompute
 * @packageName: Common
 * @description: 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
 * @data: 2019-5
 **/
package com.caruseapp.locating.cal;

import java.util.LinkedList;

public class BaseCompute {

    public static double rad2deg(double rad) {
        double deg;
        deg = rad / Math.PI * 180.0;
        return deg;
    }

    public static double deg2rad(double deg) {
        double rad;
        rad = deg * Math.PI / 180.0;
        return rad;
    }

    public static double _pi2pi(double angle) {
        final double PIx2 = 2 * Math.PI;
        angle = angle % PIx2;
        if (angle > Math.PI) {
            angle = angle - PIx2;
        } else if (angle < -Math.PI) {
            angle = angle + PIx2;
        }
        return angle;
    }

    public static LinkedList<?> addFixSizeLinkedList(LinkedList<Object> linkedList, Object e, int capacity){
        // 超过长度，移除第一个
        if (linkedList.size() + 1 > capacity) {
            linkedList.removeFirst();
        }
        linkedList.add(e);
        return linkedList;
    }

    public static double[] List2DoubleArray(LinkedList<?> linkedList){
        double[] da = new double[linkedList.size()];
        for (int i=0; i<linkedList.size(); i++) {
            da[i] = (Double) linkedList.get(i);
        }
        return da;
    }

    // 锟斤拷锟斤拷锟斤拷
    public static double[] vector3(double x, double y, double z) {
        double[] dResult = {0, 0, 0};
        dResult[0] = x;
        dResult[1] = y;
        dResult[2] = z;
        return dResult;
    }

    // norm(A,p)锟斤拷锟斤拷锟斤拷锟斤拷2锟斤拷锟斤拷
    public static double norm2(double[] Vector) {
        double dResult = 0;
        for (int i = 0; i < Vector.length; ++i) {
            dResult += Vector[i] * Vector[i];
        }
        return Math.sqrt(dResult);
    }

    public static double[] sqrt(double[] Vector) {
        for (int i = 0; i < Vector.length; ++i) {
            Vector[i] = Math.sqrt(Vector[i]);
        }
        return Vector;
    }

    public static double[][] sqrt(double[][] Matrix) {
        for (int i = 0; i < Matrix.length; ++i) {
            for (int j = 0; j < Matrix[0].length; ++j) {
                Matrix[i][j] = Math.sqrt(Matrix[i][j]);
            }
        }
        return Matrix;
    }

    // norm(A,p)锟斤拷锟斤拷锟斤拷锟斤拷2锟斤拷锟斤拷
    public static double min(double[] Vector) {
        double dResult = Double.POSITIVE_INFINITY;
        for (int i = 0; i < Vector.length; ++i) {
            dResult = Vector[i] > dResult ? dResult : Vector[i];
        }
        return dResult;
    }

    // norm(A,p)锟斤拷锟斤拷锟斤拷锟斤拷2锟斤拷锟斤拷
    public static double max(double[] Vector) {
        double dResult = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < Vector.length; ++i) {
            dResult = Vector[i] < dResult ? dResult : Vector[i];
        }
        return dResult;
    }

    public static int getIndex(int dataLength, int curentID, int n){
        int num = curentID + 1, index = curentID;
        for (int i = 0; i < n; i++) {
            num--;
            if (num == -1) {
                num = dataLength - 1;
            }
            index = num;
        }
        return index;
    }

    //杩斿洖n-k鍒楃储寮�
    public static int selectKni(double[][] vec, int k, int n) {
        int num = k + 1, index = k;
        for (int i = 0; i < n; i++) {
            num--;
            if (num == -1) {
                num = vec[0].length - 1;
            }
            index = num;
        }
        return index;
    }

    public static int getKni(double[] vec, int k, int n) { //n must be >= 0
        int num = k + 1, index = k;
        for (int i = 0; i < n; i++) {
            num--;
            if (num == -1) {
                num = vec.length - 1;
            }
            index = num;
        }
        return index;
    }

    //锟斤拷循锟斤拷锟斤拷锟斤拷锟叫碉拷指锟斤拷锟斤拷锟斤拷位锟斤拷K锟斤拷始锟斤拷锟斤拷取n锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
    public static double[] selectKn(double[] vec, int k, int n) { //n must be >0
        double[] newVec = new double[n];
        int num = k + 1;
        for (int i = 0; i < n; i++) {
            num--;
            if (num == -1) {
                num = vec.length - 1;
            }
            newVec[i] = vec[num];
        }
        return newVec;
    }

    //锟斤拷锟斤拷循锟斤拷double[t][]锟叫碉拷指锟斤拷锟斤拷锟斤拷位锟矫ｏ拷K锟斤拷始锟斤拷锟斤拷取n锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
    public static double[] selectKtn(double[][] vec2, int k, int t, int n) {
        double[] newVec = new double[n];
        int num = k + 1;
        for (int i = 0; i < n; ++i) {
            num--;
            if (num == -1) {
                num = vec2[t].length - 1;
            }
            newVec[i] = vec2[t][num];
        }
        return newVec;
    }

    // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟脚ｏ拷取一锟斤拷锟斤拷锟斤拷
    public static double[] getColElementByColId(double[][] mat, int id) {
        if (id >= mat[0].length || id < 0)
            return null;
        double[] outPut = new double[mat.length];
        for (int i = 0; i < mat.length; ++i) {
            outPut[i] = mat[i][id];
        }
        return outPut;
    }

    public static double[] getColElementByColId(double[][] mat, int id, int k) {
        int idx = id;
        for (int i = 0; i < k; i++) {
            idx--;
            if (idx < 0) {
                idx = mat[0].length - 1;
            }
        }
        double[] outPut = new double[mat.length];
        for (int i = 0; i < mat.length; ++i) {
            outPut[i] = mat[i][idx];
        }
        return outPut;
    }

    //锟斤拷锟斤拷锟斤拷锟�
    public static double sum(double[] Vector) {
        double dResult = 0;
        for (int i = 0; i < Vector.length; ++i) {
            dResult += Vector[i];
        }

        return dResult;
    }

    //锟斤拷锟斤拷锟斤拷锟街�
    public static double mean(double[] Vector) {
        double dResult = 0;
        for (int i = 0; i < Vector.length; ++i) {
            dResult += Vector[i];
        }
        return dResult / (double)Vector.length;
    }

    //锟斤拷锟斤拷锟襟方诧拷
    public static double var(double[] Vector) {
        double dResult = 0;
        double av = mean(Vector);
        for (int i = 0; i < Vector.length; ++i) {
            dResult += (Vector[i] - av) * (Vector[i] - av);
        }
        return dResult / (double)Vector.length;
    }

    //standard difference
    public static double std(double[] Vector) {
        double dResult = 0;
        double av = mean(Vector);
        for (int i = 0; i < Vector.length; ++i) {
            dResult += (Vector[i] - av) * (Vector[i] - av);
        }
        return Math.sqrt(dResult / (double)Vector.length);
    }

    // 锟斤拷锟斤拷锟斤拷锟斤拷锟窖癸拷锟揭伙拷锟皆拷锟�
    public static double[] pushBackVec(double[] vec, double num) {
        for (int i = 0; i < (vec.length - 1); i++) {
            vec[i] = vec[i + 1];
        }
        vec[vec.length - 1] = num;
        return vec;
    }

    // 锟斤拷mat压锟斤拷一锟斤拷锟斤拷锟斤拷 1:锟斤拷锟斤拷压锟斤拷2锟斤拷锟斤拷锟斤拷压锟斤拷
    public static double[][] pushBackMat(double[][] mat, double[] vec, int pushType) {
        if (pushType == 1) {
            if (mat[0].length != vec.length) {
                return null;
            }
            for (int i = 0; i < (mat.length - 1); i++) {
                mat[i] = mat[i + 1];
            }
            mat[vec.length - 1] = vec;
        } else if (pushType == 2) {
            if (mat.length != vec.length) {
                return null;
            }
            for (int i = 0; i < (mat[0].length - 1); i++) {
                for (int j = 0; j < vec.length; j++) {
                    mat[j][i] = mat[j][i + 1];
                }
            }
            for (int i = 0; i < vec.length; i++) {
                mat[i][vec.length - 1] = vec[i];
            }
        }
        return mat;

    }

    // 锟斤拷维 锟斤拷锟斤拷 ,锟斤拷锟斤拷压锟斤拷锟轿拷锟斤拷锟�
    public static double[][][] pushBackThreeMat(double[][][] mat, double[][] vec) {
        for (int i = 0; i < mat.length - 1; i++) {
            mat[i] = mat[i + 1];
        }
        mat[mat.length - 1] = vec;
        return mat;
    }

    // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫斤拷
    public static double subspace(double[] vectorA, double[] vectorB) {
        double lengthProduct = Math.abs(norm2(vectorA) * norm2(vectorB));
        if (Math.abs(lengthProduct) < 1e-16)
            return 0;
        else
            return Math.acos(dotProduct(vectorA, vectorB) / (double)lengthProduct);
    }

    // 锟斤拷锟斤拷锟斤拷锟�
    public static double dotProduct(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length)
            return 0;
        int iLength = vectorA.length;
        double dot = 0;
        for (int i = 0; i < iLength; ++i) {
            dot += vectorA[i] * vectorB[i];
        }
        return dot;
    }

    //


    // 锟斤拷锟斤拷锟斤拷锟� A+B
    public static double[] addVector(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            return new double[1];
        } else {
            int iLength = vectorA.length;
            double[] vectorResult = new double[iLength];
            for (int i = 0; i < iLength; ++i) {
                vectorResult[i] = vectorA[i] + vectorB[i];
            }
            return vectorResult;

        }
    }

    // 锟斤拷锟斤拷锟斤拷锟� (A+B)/2
    public static double[] meanVectorAB(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            return null;
        } else {
            int iLength = vectorA.length;
            double[] vectorResult = new double[iLength];
            for (int i = 0; i < iLength; ++i) {
                vectorResult[i] = (vectorA[i] + vectorB[i]) / 2.0;
            }
            return vectorResult;

        }
    }

    // 锟斤拷锟斤拷锟斤拷 锟斤拷锟斤拷锟斤拷锟�
    public static double[] addNumVector(double[] vector, double num) {
        int iLength = vector.length;
        double[] dRet = new double[iLength];
        for (int i = 0; i < iLength; ++i) {
            dRet[i] = vector[i] + num;
        }
        return dRet;
    }

    // 锟斤拷锟斤拷锟斤拷锟紸-B
    public static double[] subVector(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            return new double[1];
        } else {
            int iLength = vectorA.length;
            double[] vectorResult = new double[iLength];
            for (int i = 0; i < iLength; ++i) {
                vectorResult[i] = vectorA[i] - vectorB[i];
            }
            return vectorResult;

        }
    }

    //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
    public static double[] matProduct(double[][] matA, double[] vecB) {
        if (matA[0].length != vecB.length)
            return null;
        int irow = matA.length;
        int icol = vecB.length;
        double[] matRet = new double[irow];
        for (int i = 0; i < irow; ++i) {
            for (int j = 0; j < icol; ++j) {
                matRet[i] += matA[i][j] * vecB[j];
            }
        }
        return matRet;
    }

    // 锟斤拷锟斤拷锟斤拷锟�
    public static double[][] matProduct(double[][] matA, double[][] matB) {
        if (matA[0].length != matB.length)
            return new double[1][1];
        int irow = matA.length;
        int icol = matB[0].length;
        int icolA = matA[0].length;
        double[][] matRet = new double[irow][icol];
        for (int i = 0; i < irow; ++i) {
            for (int j = 0; j < icol; ++j) {
                for (int k = 0; k < icolA; ++k) {
                    matRet[i][j] += matA[i][k] * matB[k][j];
                }
            }
        }
        return matRet;
    }

    // 锟斤拷锟斤拷映锟斤拷锟�
    public static double[][] matAddNum(double[][] mat, double num) {
        int irow = mat.length;
        int icol = mat[0].length;
        for (int i = 0; i < irow; i++) {
            for (int j = 0; j < icol; ++j) {
                mat[i][j] += num;
            }
        }
        return mat;
    }

    // 锟斤拷锟斤拷锟斤拷锟�
    public static double[][] matAdd(double[][] matA, double[][] matB) {
        int irowA = matA.length;
        int icolA = matA[0].length;
        int irowB = matB.length;
        int icolB = matB[0].length;
        if (irowA != irowB || icolA != icolB) {
            return null;
        }
        double[][] matRet = new double[irowA][icolA];
        for (int i = 0; i < irowA; ++i) {
            for (int j = 0; j < icolA; ++j) {
                matRet[i][j] = matA[i][j] + matB[i][j];
            }
        }
        return matRet;
    }

    public static double[][] diag(double[] vector) {
        double[][] Matrix = new double[vector.length][vector.length];
        for (int i = 0; i < vector.length; i++) {
            Matrix[i][i] = vector[i];
        }
        return Matrix;
    }

    //求矩阵元素处的余子式
    public static double[][] getConfactor(double[][] data, int h, int v) {
        int H = data.length;
        int V = data[0].length;
        double[][] newdata = new double[H - 1][V - 1];
        for (int i = 0; i < newdata.length; i++) {
            if (i < h - 1) {
                for (int j = 0; j < newdata[i].length; j++) {
                    if (j < v - 1) {
                        newdata[i][j] = data[i][j];
                    } else {
                        newdata[i][j] = data[i][j + 1];
                    }
                }
            } else {
                for (int j = 0; j < newdata[i].length; j++) {
                    if (j < v - 1) {
                        newdata[i][j] = data[i + 1][j];
                    } else {
                        newdata[i][j] = data[i + 1][j + 1];
                    }
                }
            }
        }
//      for(int i=0; i<newdata.length; i ++)
//          for(int j=0; j<newdata[i].length; j++) {
//              System.out.println(newdata[i][j]);
//          }
//      }
        return newdata;
    }

    /*
     * 计算行列式的值
     */
    public static double det(double[][] data) {
        /*
         * 二维矩阵计算
         */
        if (data.length == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }
        /*
         * 二维以上的矩阵计算
         */
        double result = 0;
        int num = data.length;
        double[] nums = new double[num];
        for (int i = 0; i < data.length; i++) {
            if (i % 2 == 0) {
                nums[i] = data[0][i] * det(getConfactor(data, 1, i + 1));
            } else {
                nums[i] = -data[0][i] * det(getConfactor(data, 1, i + 1));
            }
        }
        for (int i = 0; i < data.length; i++) {
            result += nums[i];
        }
//      System.out.println(result);
        return result;
    }


    // 锟斤拷锟斤拷转锟斤拷
    public static double[][] matTranspose(double[][] inputMat) {
        double[][] outMat = new double[3][3];
        for (int i = 0; i < inputMat.length; i++) {
            for (int j = 0; j < inputMat[0].length; j++) {
                outMat[i][j] = inputMat[j][i];
            }
        }
        return outMat;
    }

    //矩阵求逆
    public static double[][] matReverse(double[][] data) {
        double[][] newdata = new double[data.length][data[0].length];
        double A = det(data);
//      System.out.println(A);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if ((i + j) % 2 == 0) {
                    newdata[i][j] = det(getConfactor(data, i + 1, j + 1)) / A;
                } else {
                    newdata[i][j] = -det(getConfactor(data, i + 1, j + 1)) / A;
                }
            }
        }
        newdata = matTranspose(newdata);
        //
        return newdata;
    }

    // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷值
    public static double[][] matEigValue(double[][] inputMat) {
        //undefined
        return inputMat;
    }

    // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
    public static double[][] matEigVector(double[][] inputMat) {
        //undefined
        return inputMat;
    }

    // 锟斤拷锟斤拷锟酵撅拷锟斤拷锟斤拷锟�
    public static double[][] numProductMat(double num, double[][] mat) {
        int irow = mat.length;
        int icol = mat[0].length;
        for (int i = 0; i < irow; ++i) {
            for (int j = 0; j < icol; ++j) {
                mat[i][j] *= num;
            }
        }
        return mat;
    }

    // 锟斤拷锟斤拷锟酵筹拷锟斤拷锟斤拷锟� A*num
    public static double[] vectorProductNum(double[] vector, double num) {
        int iLength = vector.length;
        double[] vectorRet = new double[iLength];
        for (int i = 0; i < iLength; ++i) {
            vectorRet[i] = vector[i] * num;
        }
        return vectorRet;
    }

    // 锟斤拷锟斤拷锟斤拷锟�
    public static double[] pointDiv(double[] vectorA, double[] vectorB) {
        int iLength = vectorA.length;
        if (iLength != vectorB.length)
            return new double[1];
        double[] vectorRet = new double[iLength];
        for (int i = 0; i < iLength; ++i) {
            if (Math.abs(vectorB[i]) < 0.000001)
                vectorRet[i] = 0;
            else
                vectorRet[i] = vectorA[i] / vectorB[i];
        }
        return vectorRet;
    }

    //锟叫讹拷0锟斤拷锟斤拷
    public static boolean vecIsZero(double[] vectorA) {
        boolean bResult = true;
        int iLength = vectorA.length;
        for (int i = 0; i < iLength; ++i) {
            if (Math.abs(vectorA[i]) > 1e-15) {
                bResult = false;
                break;
            }
        }
        return bResult;
    }

}
