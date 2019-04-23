package com.hongguo.read.mvp.model.bookstore;

import com.hongguo.read.utils.convert.ListParma;
import com.hongguo.read.utils.convert.Parma;

import java.util.List;

/**
 * Created by losg on 2018/3/29.
 */

public class HomeBookBean {


    /**
     * data : [{"serial":1,"id":203006,"name":"那夜上错床","title":"","author":"一粒沙","cover":"http://i-1.hgread.com/2017/11/30/fbf82734-6195-4ddc-956a-4b388e31b48e.jpg","summary":"那夜喝多了，半夜醒来竟然睡在小姨子的床上，惊慌中嗅到被窝里散发出来的少女芬芳，让人沉迷，无法自拔，可被窝里却不见小姨子的身影，那夜，到底做了什么？","words":308328,"tags":",爽文,都市,情感","category":"青春"},{"serial":2,"id":203153,"name":"和美女少妇吃鸡情事","title":"和美女少妇吃鸡情事：吃鸡的正确姿势","author":"雪落飘萍","cover":"http://i-1.hgread.com/2018/2/3/f45af569-4f76-4f6b-a6a9-dc6254907538.png","summary":"在我人生陷入低谷无法自拔的时候，那个女人悄悄的出现在我生命里，改变了我的一生...\n恰逢一款叫做绝地求生的游戏在全球大卖，而我刚好是其中的佼佼者...","words":260900,"tags":",都市情感,热血","category":"青春"},{"serial":3,"id":203042,"name":"美女总裁的风流兵王","title":"美女总裁的风流兵王：纵横花都，无所不能","author":"消亡","cover":"http://i-1.hgread.com/2017/12/22/689227d0-a968-44dc-8c37-873c1ffad33b.png","summary":"曾经在西方叱咤风云的白帝回国\u2026\u2026被老头子强烈要求回国结婚，不断反抗的他，一不小心就在花丛中流连忘返。\n美女大小姐？她爹看上我了！\n清纯学生妹？一不小心沉迷于本帅比的成熟气质！\n玉女明星？抱歉，不要因为我长得帅又有内涵就爱上我。\n\n","words":550207,"tags":",爽文","category":"青春"},{"serial":4,"id":203141,"name":"你的薄情毁我情深","title":"姐姐遭绑架撕票，姐夫竟怀疑是我下手，强行娶我疯狂报复。。","author":"胖猫小咪","cover":"http://i-1.hgread.com/2018/2/4/ca855b5c-7a0f-4b0f-9215-e79cf095088f.png","summary":"在这场怨念结合的婚姻里。\n\n江淮是疯子，而许薇是傻子。\n\n他们的结合一开始就是罪恶，是血淋淋的，不被人祝福的。\n\n江淮说：许薇，看清楚了，你就是个罪人，杀死自己亲姐的刽子手，你这样的女人，一辈子都得不到幸福，一辈子，都只能活在许恬的阴影中，这是你欠她的！\n\n其实不用江淮说，许薇也知道，她有罪，她唯一的使命就是代替姐姐活在江淮的面前，做个安安静静的影子，默然陪伴，抚慰他的伤痛，然而这一切，却因为一场算计打破了平衡。\n\n许薇就是个影子，影子不需要有感觉，不需要有疼痛，她也一直是这么以为的，然而当一次次的真心被践踏成泥，心碎的声音却那么清晰。当满腔情深被薄情透支，原来也可以如蜡炬成灰。\n\n医院的天台上，许薇迎着风淋着雨，笑容凄绝，她说：江淮，你总说这是我欠许恬的，那我用这条命还她可好？","words":50012,"tags":",婚恋,虐恋","category":"言情"},{"serial":5,"id":203120,"name":"情深几许，回头太难","title":"情人患病，丈夫竟迷信谣言，逼我打胎用胚胎血救她。。","author":"墨小衿","cover":"http://i-1.hgread.com/2018/2/8/c2e5e971-e66f-466e-a44e-c45eb538d2d7.png","summary":"顾晚爱蔚容生胜过自己的命.\n可蔚容生爱的只有何雅思。\n顾晚以为只要真心付出就会得到蔚容生的回应.\n她却忘了蔚容生对她只有恨。\n","words":50943,"tags":",虐恋,婚恋","category":"青春"},{"serial":6,"id":203195,"name":"爱你情出于蓝","title":"妹妹男友娶了我，还骂我是心机女，夜夜疯狂报复我。。","author":"严如白","cover":"https://img.9kus.com/Images/00/a345dcb7e4180cc084d39ec4950bc215.jpg","summary":"结婚两年，他每次要她，都从后而入。\r\n她愤怒至极！\u201c盛又霆！你为什么每次要我都不敢看我的脸！是因为怕看见和你睡觉的人是我，不是我的妹妹吗？\u201d\r\n\u201c你想要我看着你做？只要你承受得起！\u201d\r\n他凶如野兽，她咬牙承受\u2026\u2026","words":53552,"tags":"短篇,豪门,虐恋,总裁首席,腹黑,宝宝","category":"网游情缘"}]
     * code : 0
     * msg : Successful
     * time : 1522302406
     */

    public int            code;
    public String         msg;
    public int            time;
    @ListParma(paramListName = "data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * serial : 1
         * id : 203006
         * name : 那夜上错床
         * title :
         * author : 一粒沙
         * cover : http://i-1.hgread.com/2017/11/30/fbf82734-6195-4ddc-956a-4b388e31b48e.jpg
         * summary : 那夜喝多了，半夜醒来竟然睡在小姨子的床上，惊慌中嗅到被窝里散发出来的少女芬芳，让人沉迷，无法自拔，可被窝里却不见小姨子的身影，那夜，到底做了什么？
         * words : 308328
         * tags : ,爽文,都市,情感
         * category : 青春
         */
        public String serial;
        @Parma(paramName = "bookId")
        public String id;
        @Parma(paramName = "bookName")
        public String name;
        public String title;
        @Parma(paramName = "author")
        public String author;
        @Parma(paramName = "coverPicture")
        public String cover;
        @Parma(paramName = "desc")
        public String summary;
        @Parma(paramName = "words")
        public int words;
        public String tags;
        public String category;
    }
}
