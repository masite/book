package com.hongguo.read.mvp.model.mine.mypackage;

import com.hongguo.common.base.CommonBean;

import java.util.List;

public class ConsumeBean{

    public int mSize;
    public int mTotalSize;

    public List<DataBean> mData;

    public static class DataBean {
        public String                      title;
        public List<SellListBean.DataBean> mDataBeans;
    }

    public static class SellListBean extends CommonBean {
        public String         msg;
        public List<DataBean> data;

        public static class DataBean {
            /**
             * userId : 207443
             * sellType : 16
             * sellCover : http://www.sweetread.net//UploadFile/bookphoto/3097/logo.jpg
             * sellNo : 1491011785T200240368001
             * masterName : 红颜祸水
             * masterId : 200240
             * sellName : 第94章 爱到恨不得你死！
             * sellAmount : 11
             * keyId : 368001
             * createDate : 2017-04-01 09:56:24
             */

            public String userId;
            public int sellType;
            public String sellCover;
            public String sellNo;
            public String masterName;
            public String masterId;
            public String sellName;
            public String sellAmount;
            public String keyId;
            public String createDate;
        }
    }
}