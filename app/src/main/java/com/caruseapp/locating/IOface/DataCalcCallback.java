package com.caruseapp.locating.IOface;

/*
 * 文件名：数据计算完成回调接口
 * 版权：Copyright 2002-2018 heweisoft Tech. Co. Ltd. All Rights Reserved.
 * 描述： locating.IOface
 * 修改时间：2019/11/27
 * 修改内容：新增
 * 创建者：hewei
 */
public interface DataCalcCallback {

    /**
     * 计算数据完成
     * @param locate
     */
    void onCalcFinish(Locate locate);
}
