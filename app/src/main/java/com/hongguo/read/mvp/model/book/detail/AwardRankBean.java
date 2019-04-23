package com.hongguo.read.mvp.model.book.detail;

import com.hongguo.common.base.CommonBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class AwardRankBean extends CommonBean{

    /**
     * data : [{"serialNo":1,"userId":829898,"nickName":"在家里889900","sumAmount":1000},{"serialNo":2,"userId":203486,"nickName":"朱振铭","sumAmount":400},{"serialNo":3,"userId":204230,"nickName":"zzmqq","sumAmount":201},{"serialNo":4,"userId":816722,"nickName":"随缘","sumAmount":200},{"serialNo":5,"userId":834764,"nickName":"GY2017","sumAmount":200},{"serialNo":6,"userId":857618,"nickName":"yanzi2255","sumAmount":200},{"serialNo":7,"userId":868202,"nickName":"zll1589977","sumAmount":200}]
     * code : 0
     * msg : Successful
     */
    public String         msg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * serialNo : 1
         * userId : 829898
         * nickName : 在家里889900
         * sumAmount : 1000
         */

        public int    serialNo;
        public String userId;
        public String nickName;
        public int sumAmount;
        public int    type;
    }
}
