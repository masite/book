package com.hongguo.read.mvp.model;

import java.util.List;

/**
 * Created by losg on 2018/1/4.
 */

public class CmsBannerBean {


    /**
     * pager : {"total":3,"page":1,"size":30}
     * data : [{"typeId":11,"keyId":23,"linkUrl":"/book/200729.html","lables":"","title":"桃运村支书","serial":1,"cover":"http://i-1.hgread.com/2017/11/2/6e50a77b-1bd5-4a03-97db-f0a5a1665ece.png","target":"_blank"},{"typeId":11,"keyId":21,"linkUrl":"/book/200798.html","lables":"","title":"美女如戏","serial":2,"cover":"http://i-1.hgread.com/2017/11/2/78e3f637-6690-401d-8015-d9bd61814365.png","target":"_blank"},{"typeId":11,"keyId":22,"linkUrl":"/book/200759.html","lables":"","title":"日久生情","serial":3,"cover":"http://i-1.hgread.com/2017/11/2/4a5f1b83-57f8-4839-8a9a-74164794926d.png","target":"_blank"}]
     * code : 0
     * msg : Successful
     * time : 1515031748
     */

    public PagerBean      pager;
    public int            code;
    public String         msg;
    public String         time;
    public List<DataBean> data;

    public static class PagerBean {
        /**
         * total : 3
         * page : 1
         * size : 30
         */

        public int total;
        public int page;
        public int size;
    }

    public static class DataBean {
        /**
         * typeId : 11
         * keyId : 23
         * linkUrl : /book/200729.html
         * lables :
         * title : 桃运村支书
         * serial : 1
         * cover : http://i-1.hgread.com/2017/11/2/6e50a77b-1bd5-4a03-97db-f0a5a1665ece.png
         * target : _blank
         */

        public String typeId;
        public String keyId;
        public String linkUrl;
        public String lables;
        public String title;
        public String serial;
        public String cover;
        public String target;
    }
}
