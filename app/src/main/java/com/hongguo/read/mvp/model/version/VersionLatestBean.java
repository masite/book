package com.hongguo.read.mvp.model.version;


public class VersionLatestBean {



    /**
     * data : {"versionName":"1.0.0","versionUrl":"http://x2.yiwan.com/yiwan/ytbaihuo.apk?ver=1.0.0","versionDesc":"红果小说"}
     * code : 0
     * msg : Successful
     */

    public DataBean data;
    public int      code;
    public String   msg;
    /**
     * data : {"serial":119,"title":"红果阅读1.5.6正式版","desc":"致歉：\r\n近期部分书籍出现章节错乱问题，严重影响了用户的阅读体验，对此我们深感抱歉，特紧急发布优化版本，给您带来的不便敬请谅解。","link":"http://x1.hgread.com/guanwang/hgyd.apk","safelink":"http://a.app.qq.com/o/simple.jsp?pkgname=com.hongguo.read","date":"/Date(1507801366000)/","ver":"1.5.6","uver":"1.5.5","utag":1}
     */


    public static class DataBean {
        /**
         * serial : 119
         * title : 红果阅读1.5.6正式版
         * desc : 致歉：
         近期部分书籍出现章节错乱问题，严重影响了用户的阅读体验，对此我们深感抱歉，特紧急发布优化版本，给您带来的不便敬请谅解。
         * link : http://x1.hgread.com/guanwang/hgyd.apk
         * safelink : http://a.app.qq.com/o/simple.jsp?pkgname=com.hongguo.read
         * date : /Date(1507801366000)/
         * ver : 1.5.6
         * uver : 1.5.5
         * utag : 1   0>强制更新，1>静默更新
         */

        public int     serial;
        public String  title;
        public String  desc;
        public String  link;
        public String  safelink;
        public String  date;
        public String  ver;
        public String  uver;
        public int     utag;
    }
}
