package com.hongguo.common.model;

/**
 * Created by losg on 2018/1/4.
 */

public class CmsTopicBean {


    /**
     * data : {"lables":"","typeId":26,"godId":0,"content":"<p><img alt=\"\" src=\"http://static.hgread.com/m/images/hd-one-t.png\" /> <img alt=\"\" src=\"http://static.hgread.com/m/images/hd-one-c.png\" /><\/p>\r\n\r\n<div class=\"hd-btn-one\"><a href=\"/home/distsvipinvite{$URL_QUERY}\" title=\"点击送福利\">&nbsp;<\/a><img alt=\"\" src=\"http://static.hgread.com/m/images/hd-one-b.png\" /><\/div>\r\n","likeTimes":0,"viewTimes":0,"title":"点击送福利","descript":"","userId":0,"commentTimes":0,"source":"","modelId":9,"shortTitle":"","keyId":72,"hateTimes":0,"cover":"http://i-1.hgread.com/cmsput/2017/12/1/f53e3ec7-a4a6-4621-a58e-fcb5d242e071.png","keyCode":"9yu78640k","subTitle":"","userName":"管理员","userAvatar":""}
     * code : 0
     * msg : Successful
     * time : 1515031850
     */

    public DataBean data;
    public String   code;
    public String   msg;
    public String   time;

    public static class DataBean {
        /**
         * lables :
         * typeId : 26
         * godId : 0
         * content : <p><img alt="" src="http://static.hgread.com/m/images/hd-one-t.png" /> <img alt="" src="http://static.hgread.com/m/images/hd-one-c.png" /></p>
         * <p>
         * <div class="hd-btn-one"><a href="/home/distsvipinvite{$URL_QUERY}" title="点击送福利">&nbsp;</a><img alt="" src="http://static.hgread.com/m/images/hd-one-b.png" /></div>
         * <p>
         * likeTimes : 0
         * viewTimes : 0
         * title : 点击送福利
         * descript :
         * userId : 0
         * commentTimes : 0
         * source :
         * modelId : 9
         * shortTitle :
         * keyId : 72
         * hateTimes : 0
         * cover : http://i-1.hgread.com/cmsput/2017/12/1/f53e3ec7-a4a6-4621-a58e-fcb5d242e071.png
         * keyCode : 9yu78640k
         * subTitle :
         * userName : 管理员
         * userAvatar :
         */

        public String lables;
        public String typeId;
        public String godId;
        public String content;
        public String likeTimes;
        public String viewTimes;
        public String title;
        public String descript;
        public String userId;
        public String commentTimes;
        public String source;
        public String modelId;
        public String shortTitle;
        public String keyId;
        public String hateTimes;
        public String cover;
        public String keyCode;
        public String subTitle;
        public String userName;
        public String userAvatar;
    }
}
