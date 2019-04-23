package com.hongguo.read.mvp.model.mine;

import java.util.List;

public class SkinBean {

    public List<SkinlistBean> skinlist;

    public static class SkinlistBean {
        /**
         * name : 涓冨蹇箰
         * skinimage : http://x1.hgread.com/skin/qixi/icon1_on.png
         * vipstatus : 0
         * isneedbuy : false
         * sign : 鏍囪瘑
         * skinurl : http://x1.hgread.com/skin/skin1.skin
         * selected:
         */

        public String  name;
        public String  skinimage;
        public int     vipstatus;
        public String  skinurl;
        public boolean enable;
        public String  showMsg;
        public boolean fileExist;
    }
}