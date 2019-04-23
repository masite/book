package com.hongguo.read.mvp.model.bookstore;

import com.hongguo.common.base.CommonBean;
import com.hongguo.read.utils.convert.ListParma;
import com.hongguo.read.utils.convert.Parma;

import java.util.List;

/**
 * Created time 2017/12/6.
 *
 * @author losg
 */

public class BookStoreBookBean extends CommonBean{

    /**
     * data : [{"bookId":200594,"bookName":"我在火葬场上夜班的那几年","author":"冰河","coverPicture":"http://www.youyuezw.com/files/article/image/0/404/404s.jpg","desc":"我叫李冰河，为了赚钱，我做起了火葬场的夜班司机。原本我以为这只是一个普通的工作，直到我有一天遇到了一具美丽的女尸，从此生活不再宁静\u2026\u2026"},{"bookId":200528,"bookName":"我在殡仪馆工作的那些事儿","author":"再见萧郎","coverPicture":"http://www.youyuezw.com/files/article/image/0/288/288s.jpg","desc":"我叫凌余，跟大胖一起在殡仪馆工作，有一天殡仪馆里来了一具身穿香奈儿的漂亮女尸，我把这套香奈儿偷了回去，送给了女朋友，接着，一连串的邪门事情发生了\u2026\u2026接连的死亡，一步步逼近我跟大胖，我们又该如何逃脱死神的追逐？\n\nPS：读者交流QQ群：573956074（欢迎大家的加入）"},{"bookId":200549,"bookName":"我的新郎是阎王","author":"秀儿","coverPicture":"http://www.youyuezw.com/files/article/image/0/64/64s.jpg","desc":"跟男友回老家结婚，没想到洞房夜他竟然让我和六个壮汉同房，当晚我还被男鬼破了身\u2026\u2026\n\n秀儿继《我的新郎是猛鬼》《我的奇妙男友》《我的吸血鬼恋人》之后2016年又一全新力作，不一样的刺激，不一样的奇妙，欢迎大家阅读，同时欢迎大家来到秀儿的小窝QQ群：587518352，群内有福利，惊喜大大滴！"},{"bookId":200531,"bookName":"最强兵王之谁与争锋","author":"兵锋","coverPicture":"http://www.youyuezw.com/files/article/image/0/333/333s.jpg","desc":"佣兵界流传着一句话，世界很大，阳光能照耀到的地方，法律说了算。阳光照耀不到的地方，血徒便是真理。\n然而当血徒回到都市的时候才发现，原来女人才是世界上最恐怖的暴徒。\n美女总裁赖在床上不走，清纯校花逼婚，极品萝莉强抱，还有温柔御姐来勾引，这生活何时是个头啊\u2026\u2026"},{"bookId":200555,"bookName":"错嫁阴夫","author":"盛桃","coverPicture":"http://www.youyuezw.com/files/article/image/0/83/83s.jpg","desc":"阴差阳错之下，我替得急性肠胃炎动不了的姐姐和姐夫走一下婚礼仪式，没想到竟误惹了一霸道鬼夫，从此被他夜夜纠缠\u2026\u2026"},{"bookId":200588,"bookName":"狼血兵王","author":"丁少白","coverPicture":"http://www.youyuezw.com/files/article/image/0/331/331s.jpg","desc":"美女，总裁，校花，御姐？\n别过来，你昨晚对我做了什么？\n我可是兵王萧晨，你们怎么可以这样对我，再脱衣服的话别怪我不客气了\u2026\u2026"},{"bookId":200534,"bookName":"造化之城","author":"邪君","coverPicture":"http://www.youyuezw.com/files/article/image/0/334/334s.jpg","desc":"陆天羽重生了，醒来后发现自己身上多了一座城。\n这座城中，保留着荒古时代无数强者的传承。\n你是天帝传人？巧了，我从造化之城里得到了天帝的传承，不服来较量一下？\n龙族至高秘技失传了？没事，造化之城里有，不过想要的话得拿龙女来换哦\u2026\u2026"},{"bookId":200561,"bookName":"和漂亮女鬼同居的日子","author":"鸿老九","coverPicture":"http://www.youyuezw.com/files/article/image/0/246/246s.jpg","desc":"漂亮女友有一天突然失踪，当我回到家开门，竟发现女友浑身湿漉漉地站在我面前，之后却发现她居然是\u2026\u2026"},{"bookId":200567,"bookName":"人间禁区","author":"北方先生","coverPicture":"http://www.youyuezw.com/files/article/image/0/302/302s.jpg","desc":"老板娘初次见面就要约我去开房，然而有人说老板娘在一个月前就死了，那么要和我开房的是谁\u2026\u2026"},{"bookId":200807,"bookName":" 娶个女鬼做老婆","author":"老王住七楼","coverPicture":"http://i-1.hgread.com/2017/5/17/647f094b-0286-4039-abf1-f1f69f7c4f92.jpg","desc":"自从烧了那个女尸之后，我每晚都被逼着，做哪些事..."}]
     * code : 0
     * msg : Successful
     */

    public String         msg;
    @ListParma(paramListName = "data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * bookId : 200594
         * bookName : 我在火葬场上夜班的那几年
         * author : 冰河
         * coverPicture : http://www.youyuezw.com/files/article/image/0/404/404s.jpg
         * desc : 我叫李冰河，为了赚钱，我做起了火葬场的夜班司机。原本我以为这只是一个普通的工作，直到我有一天遇到了一具美丽的女尸，从此生活不再宁静……
         */

        @Parma(paramName = "author")
        public String author;
        @Parma(paramName = "bookId")
        public String bookId;
        @Parma(paramName = "bookName")
        public String bookName;
        public int    bookType;
        public int    categoryId;
        public String categoryName;
        @Parma(paramName = "coverPicture")
        public String coverPicture;
        @Parma(paramName = "desc")
        public String desc;
        public String labels;
    }
}
