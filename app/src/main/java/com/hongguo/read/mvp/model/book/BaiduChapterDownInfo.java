package com.hongguo.read.mvp.model.book;

/**
 * Created by Administrator on 2017/6/2.
 */

public class BaiduChapterDownInfo {

    public String     msg;
    public int        code;
    public ResultBean result;

    public static class ResultBean {
        public ResultStateBean resultState;
        public DataBean        data;

        public static class ResultStateBean {
            public int code;
        }

        public static class DataBean {

            public String         bottomtip;
            public String         headtitle;
            public String         discription;
            public String         toptip;
            public int            iserror;
            public int            isgiftbuy;
            public PayErrorBean   payError;
            public PaySuccessBean paySuccess;

            public static class PayErrorBean {
            }

            public static class PaySuccessBean {
                public String downloadUrl;
                public String txtDownloadUrl;
                public int    type;
                public String returnmsg;
            }
        }
    }
}
