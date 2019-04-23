package com.hongguo.read.mvp.model.bookstore;

import java.util.List;

/**
 * Created time 2017/12/1.
 *
 * @author losg
 */

public class RecommendItemBean {

    public List<ObjBean> obj;

    public static class ObjBean {
        /**
         * showtype : 5
         * dataname : 主编力荐
         * data : [{"bookId":202631,"bookName":"婚外燃情","formerName":null,"author":"金三叔","coverPicture":"http://i-1.hgread.com/2017/9/15/5202806e-59d2-4491-a72f-76c172331d44.jpg","desc":"因妻子的背叛，他一蹶不振，在舞厅里偶遇一位神秘女人，让他事业有成，从此飞黄腾达，在美女如云的都市里，开始了他美妙的人生\u2026\u2026","categoryId":334,"categoryName":"网游情缘","chapters":809,"words":1715697,"lastUpdateChapterId":45726201,"lastUpdateChapterName":"第810章 郊游（2）","lastUpdateTime":"1970-01-18 18:43:58","readers":18643,"bookfrom":0},{"bookId":201866,"bookName":"寸寸销魂","formerName":null,"author":"左冷右暖","coverPicture":"http://i-1.hgread.com/2017/7/4/c1e84e71-25de-4ac7-ad15-b5160240546f.jpg","desc":"我因为在课堂上看片被老师抓住了，结果却意外的发现老师的秘密\u2026\u2026","categoryId":340,"categoryName":"青春","chapters":369,"words":757038,"lastUpdateChapterId":50223601,"lastUpdateChapterName":" 第三百六十九章针锋相对","lastUpdateTime":"2017-10-30 05:35:59","readers":125210,"bookfrom":0},{"bookId":202859,"bookName":"妻色撩人","formerName":null,"author":"江念忆","coverPicture":"http://static.yqread.com/cover/4440/1497480087.jpg","desc":"我恨他，我与他站在对立两面，我所有的技巧全都师承于他。 　　我一步步走上报复他的道路，用他身上所学报复他。 　　可我爱他，我毁了他，我也毁了我。 ","categoryId":334,"categoryName":"网游情缘","chapters":139,"words":316264,"lastUpdateChapterId":50171401,"lastUpdateChapterName":"第139章 你这是在威胁我？","lastUpdateTime":"2017-10-27 14:43:40","readers":157,"bookfrom":0},{"bookId":202868,"bookName":"谁拿错爱共此生","formerName":null,"author":"锦夏温婉T","coverPicture":"http://static.yqread.com/cover/4448/1496717021.jpg","desc":"展开刘沐瑶的日记扉页，每个字里行间都被点滴泪水晕染。 她写到： 我的丈夫每晚都会躺在枕边轻唤另一个女人的名字狠狠\u2018抱\u2019我，对此我却甘之如饴. 并非我犯贱喜欢如此，仅是因为我失去了有关过去的记忆。 而他就如同传销者一样，在我脑中植入性的输入：\u201c你的名字叫刘沐瑶，你爱的人是我肖铭泽。\u201d 如果我知道拿回记忆是与幸福背道而驰，我宁愿不要那个真相，然而\u2026\u2026","categoryId":334,"categoryName":"网游情缘","chapters":275,"words":835850,"lastUpdateChapterId":50169401,"lastUpdateChapterName":"第275章 是你填补了我的不完美【3】","lastUpdateTime":"2017-10-27 14:42:52","readers":118,"bookfrom":0}]
         */

        public int    showtype;
        public String dataname;
        public String systemMore = "";
        public String sort;
        public String status;
        public List<DataBean> data;

        public static class DataBean {
            /**
             * bookId : 202631
             * bookName : 婚外燃情
             * formerName : null
             * author : 金三叔
             * coverPicture : http://i-1.hgread.com/2017/9/15/5202806e-59d2-4491-a72f-76c172331d44.jpg
             * desc : 因妻子的背叛，他一蹶不振，在舞厅里偶遇一位神秘女人，让他事业有成，从此飞黄腾达，在美女如云的都市里，开始了他美妙的人生……
             * categoryId : 334
             * categoryName : 网游情缘
             * chapters : 809
             * words : 1715697
             * lastUpdateChapterId : 45726201
             * lastUpdateChapterName : 第810章 郊游（2）
             * lastUpdateTime : 1970-01-18 18:43:58
             * readers : 18643
             * bookfrom : 0
             */

            public String bookId;
            public String bookName;
            public Object formerName;
            public String author;
            public String coverPicture;
            public String desc;
            public int    categoryId;
            public String categoryName;
            public int    chapters;
            public int    words;
            public int    lastUpdateChapterId;
            public String lastUpdateChapterName;
            public String lastUpdateTime;
            public int    readers;
            public int    bookfrom;
            public String wordsText;
        }
    }

}
