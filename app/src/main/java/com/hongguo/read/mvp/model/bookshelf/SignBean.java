package com.hongguo.read.mvp.model.bookshelf;

/**
 * Created by losg on 2018/2/26.
 */

public class SignBean {

    /**
     * data : {"id":0,"gval":30,"snum":0,"gtags":"今日签到将获得30红果币","cdate":"/Date(1519626559872)/"}
     * code : 0
     * msg : Successful
     * time : 1519626559
     */

    public DataBean data;
    public int      code;
    public String   msg;
    public int      time;

    public static class DataBean {
        /**
         * id : 0
         * gval : 30
         * snum : 0
         * gtags : 今日签到将获得30红果币
         * cdate : /Date(1519626559872)/
         */

        public int    id;
        public int    gval;
        public int    snum;
        public String gtags;
        public String cdate;
    }
}
