package com.hongguo.read.mvp.model.mine;

import java.util.List;

public class QuestionBean {

    /**
     * data : [{"question":"200675","answer":"豪门盛宠：扑倒高冷总裁"},{"question":"200675","answer":"豪门盛宠：扑倒高冷总裁"}]
     * code : 0
     * msg : Successful
     */
    public int            code;
    public String         msg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * question : 200675
         * answer : 豪门盛宠：扑倒高冷总裁
         */
        public String question;
        public String answer;
        public boolean isOpen = true;

    }
}