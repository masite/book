package com.hongguo.common.model.userInfo;

/**
 * Created by Administrator on 2017/9/27.
 */

public class UploadUrlPathBean {

    /**
     * data : {"userId":207443,"uploadUrl":"http://m.hgread.com/oput/uploader?userId=207443&upToken=8a3ed94396b494dc0162b543"}
     * code : 0
     * msg : Successful
     */

    public DataBean data;
    public int      code;
    public String   msg;

    public static class DataBean {
        /**
         * userId : 207443
         * uploadUrl : http://m.hgread.com/oput/uploader?userId=207443&upToken=8a3ed94396b494dc0162b543
         */

        public int    userId;
        public String uploadUrl;
    }
}
