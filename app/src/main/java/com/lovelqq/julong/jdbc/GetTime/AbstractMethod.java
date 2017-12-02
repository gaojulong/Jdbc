package com.lovelqq.julong.jdbc.GetTime;

/**
 * Created by julong on 2017/12/2.
 */

public abstract class AbstractMethod {
    /**
     *     具体实现的任务
     */
    public abstract void getusertime();

    /**
     * 获取当前的时间
     */
    public final long getdate(){
        long getdate=0;
        getdate=System.currentTimeMillis();
        return getdate;
    }

    /**
     * 可重写，也可不重写。
     */
    public void notess(){}

    /**
     *  计算消耗的时间
     */
    public long getttime(){
        long starttime=getdate();
        getusertime();
        long endtime=getdate();
        return endtime-starttime;
    }

}
