package com.hongguo.read.mvp.model.search;

import com.hongguo.common.base.CommonBean;
import com.hongguo.read.constants.Constants;

import java.util.List;

public class SearchBean extends CommonBean {

    /**
     * data : [{"bookId":200594,"bookName":"我在火葬场上夜班的那几年","author":"冰河","coverPicture":"http://www.youyuezw.com/files/article/image/0/404/404s.jpg","desc":"我叫李冰河，为了赚钱，我做起了火葬场的夜班司机。原本我以为这只是一个普通的工作，直到我有一天遇到了一具美丽的女尸，从此生活不再宁静\u2026\u2026","categoryId":275,"categoryName":"婚恋情缘","chapters":71,"words":168111,"lastUpdateChapterId":14835201,"lastUpdateChapterName":"第七十一章 诡异的纸人","lastUpdateTime":"2017-04-06 09:10:41","readers":36707},{"bookId":200528,"bookName":"我在殡仪馆工作的那些事儿","author":"再见萧郎","coverPicture":"http://www.youyuezw.com/files/article/image/0/288/288s.jpg","desc":"我叫凌余，跟大胖一起在殡仪馆工作，有一天殡仪馆里来了一具身穿香奈儿的漂亮女尸，我把这套香奈儿偷了回去，送给了女朋友，接着，一连串的邪门事情发生了\u2026\u2026接连的死亡，一步步逼近我跟大胖，我们又该如何逃脱死神的追逐？\r\n\r\nPS：读者交流QQ群：573956074（欢迎大家的加入）","categoryId":275,"categoryName":"婚恋情缘","chapters":216,"words":646412,"lastUpdateChapterId":13477001,"lastUpdateChapterName":"第215章 这就是那个饿鬼头目","lastUpdateTime":"2017-03-29 22:54:21","readers":17848}]
     * code : 0
     * msg : Successful
     */
    public String         msg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * bookId : 200594
         * bookName : 我在火葬场上夜班的那几年
         * author : 冰河
         * coverPicture : http://www.youyuezw.com/files/article/image/0/404/404s.jpg
         * desc : 我叫李冰河，为了赚钱，我做起了火葬场的夜班司机。原本我以为这只是一个普通的工作，直到我有一天遇到了一具美丽的女尸，从此生活不再宁静……
         * categoryId : 275
         * categoryName : 婚恋情缘
         * chapters : 71
         * words : 168111
         * lastUpdateChapterId : 14835201
         * lastUpdateChapterName : 第七十一章 诡异的纸人
         * lastUpdateTime : 2017-04-06 09:10:41
         * readers : 36707
         */
        public String formerName;
        public String bookId;
        public String bookName;
        public String author;
        public String coverPicture;
        public String desc;
        public String categoryId;
        public String categoryName;
        public String chapters;
        public String words;
        public int bookFrom = Constants.BOOK_FROM.FROM_SLEF;
        public String lastUpdateChapterId;
        public String lastUpdateChapterName;
        public String lastUpdateTime;
        public String readers;
    }

}