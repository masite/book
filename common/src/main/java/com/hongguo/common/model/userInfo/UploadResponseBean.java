package com.hongguo.common.model.userInfo;

/**
 * Created by losg on 2018/1/9.
 */

public class UploadResponseBean {


    /**
     * data : {"link":"http://i-1.hgread.com/2018/1/9/36960df1-622f-4de7-8696-f5f61b79a86f.png"}
     * code : 0
     * msg : Successful
     */

    public DataBean data;
    public int    code;
    public String msg;

    public static class DataBean {
        /**
         * link : http://i-1.hgread.com/2018/1/9/36960df1-622f-4de7-8696-f5f61b79a86f.png
         */

        public String link;
    }
}
