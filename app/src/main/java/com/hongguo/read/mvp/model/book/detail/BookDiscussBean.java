package com.hongguo.read.mvp.model.book.detail;

import android.os.Parcel;
import android.os.Parcelable;

//import com.hongguo.common.base.CommonBean;

import com.hongguo.common.base.CommonBean;

import java.util.List;

/**
 * Created by losg on 2017/12/26.
 */

public class BookDiscussBean extends CommonBean {
    /**
     * pager : {"page":1,"size":30,"total":124}
     * data : [{"serial":1,"id":2710,"nickName":"麻醉","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1511517510153)/","replyTimes":0,"title":"\\u597d\\u770b","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":2,"id":2709,"nickName":"麻醉","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1511517495907)/","replyTimes":0,"title":"\\u597d\\u770b","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":3,"id":2645,"nickName":"书友1497602519","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1511348461480)/","replyTimes":0,"title":"在哪能免费看","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":4,"id":2641,"nickName":"书友1504789638","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1511333075510)/","replyTimes":0,"title":"推荐去宜搜去看流氓老师","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":5,"id":2627,"nickName":"书友1511020572","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1511185482633)/","replyTimes":0,"title":"谁有办法可以联系作者？","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":6,"id":2625,"nickName":"书友1511020572","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1511185154477)/","replyTimes":0,"title":"93章有错误","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":7,"id":2574,"nickName":"天天","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1510887887927)/","replyTimes":1,"title":"6666666","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":8,"id":2434,"nickName":"书友147258","headImgUrl":"http://i-1.hgread.com/2017/11/9/79a81b46-219d-44bb-98d8-4609489bdd79.jpg","cDate":"/Date(1510712005683)/","replyTimes":1,"title":"真好看\\ud83d\\ude0a","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":9,"id":2152,"nickName":"罪","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1510485869633)/","replyTimes":2,"title":"会员可以免费看完吗","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":10,"id":893,"nickName":"书友1510053202","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1510053254677)/","replyTimes":0,"title":"能不能换个作者？我感觉还能再多写点\\ud83d\\ude02","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":11,"id":890,"nickName":"小超","headImgUrl":"http://wx.qlogo.cn/mmopen/cg35lSsQj7m3W1lu5JpdoJMGOO1Zk6OD7cuLESA4cJsM2MJGbiaTq1E6eXG0icbMYiciaRZg5RCGjKmplHWtNuvnZMMzzDDaPAf8/0","cDate":"/Date(1510040872413)/","replyTimes":0,"title":"林叔跟林染估计有仇","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":12,"id":883,"nickName":"在人间","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509903793930)/","replyTimes":0,"title":"看得我尴尬症都犯了。。。","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":13,"id":864,"nickName":"书友1501621504","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509810208537)/","replyTimes":0,"title":"谁能借我账号看看","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":14,"id":862,"nickName":"??? ???","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509795079063)/","replyTimes":1,"title":"支持作者，打赏  188红果币","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":15,"id":852,"nickName":"梦里寻欢","headImgUrl":"http://i-1.hgread.com/2017/10/30/ad690b97-d098-45b3-bdbd-d79c776939c0.jpg","cDate":"/Date(1509716350480)/","replyTimes":0,"title":"新书叫做欲望青春期，可以在红果搜索来看哦。","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":16,"id":811,"nickName":"书友1508392309","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509513615617)/","replyTimes":2,"title":"有人借我账号来看吗","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":17,"id":804,"nickName":"书友1563187","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509425484870)/","replyTimes":0,"title":"这就结局了","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":18,"id":746,"nickName":"美女如戏铁粉","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509297810553)/","replyTimes":0,"title":"这结局不满意，仓促了些。","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":19,"id":745,"nickName":"书友860263","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509294523257)/","replyTimes":0,"title":"我还想看为轻柔报仇的","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":20,"id":744,"nickName":"书友1499962235","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509293306307)/","replyTimes":1,"title":"唉结局好仓促还希望看见吴辉和林然??","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":21,"id":743,"nickName":"xuqing","headImgUrl":"http://i-1.hgread.com/2017/10/17/29a704b5-b890-4a1e-9493-f44a12034833.png","cDate":"/Date(1509284680307)/","replyTimes":0,"title":"《欲望青春期》直接在红果阅读里面搜索就行了。","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":22,"id":742,"nickName":"xuqing","headImgUrl":"http://i-1.hgread.com/2017/10/17/29a704b5-b890-4a1e-9493-f44a12034833.png","cDate":"/Date(1509284658563)/","replyTimes":0,"title":"新书叫做《欲望青春期》，不好看尽管往我家寄刀片","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":23,"id":741,"nickName":"xuqing","headImgUrl":"http://i-1.hgread.com/2017/10/17/29a704b5-b890-4a1e-9493-f44a12034833.png","cDate":"/Date(1509284634907)/","replyTimes":0,"title":"新书现在还处于屏蔽状态，明天就能看到了","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":24,"id":739,"nickName":"美女如戏铁粉","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509261173347)/","replyTimes":0,"title":"不是要完结了吗？赶紧把它完结了吧，看着好费心。","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":25,"id":738,"nickName":"美女如戏铁粉","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509261151287)/","replyTimes":0,"title":"这种你是掉厕所里面了吗？怎么今天又没有更新","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":26,"id":729,"nickName":"书友1509166634","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509168260857)/","replyTimes":0,"title":"这尼玛什么小说，看的老子一股子气","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":27,"id":722,"nickName":"xuqing","headImgUrl":"http://i-1.hgread.com/2017/10/17/29a704b5-b890-4a1e-9493-f44a12034833.png","cDate":"/Date(1509091702683)/","replyTimes":0,"title":"有一天，我放了个屁，蹦到意大利，意大利国王闻到屁，","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":28,"id":719,"nickName":"轻柔","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509084688510)/","replyTimes":0,"title":"宝塔镇河妖","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":29,"id":716,"nickName":"书友1509072679","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509072715607)/","replyTimes":2,"title":"天王盖地虎","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""},{"serial":30,"id":711,"nickName":"书友860128","headImgUrl":"http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg","cDate":"/Date(1509033530343)/","replyTimes":0,"title":"不是那个  你的欲望有那么重吗？??      ","isGod":0,"isTop":0,"isBest":0,"isNotice":0,"isUse":1,"labels":""}]
     * code : 0
     * msg : Successful
     * time : 1514286524
     */

    public String         msg;
    public int            time;
    public List<DataBean> data;

    public static class DataBean implements Parcelable {
        /**
         * serial : 1
         * id : 2710
         * nickName : 麻醉
         * headImgUrl : http://i-1.hgread.com/2017/8/30/7a7f4ca1-9aea-453c-b4fe-69be84b6d5ef.jpg
         * cDate : /Date(1511517510153)/
         * replyTimes : 0
         * title : \u597d\u770b
         * isGod : 0
         * isTop : 0
         * isBest : 0
         * isNotice : 0
         * isUse : 1
         * labels :
         */

        public String serial;
        public String id;
        public String nickName;
        public String headImgUrl;
        public String cDate;
        public String content;
        public int    replyTimes;
        public String title;
        public int    isGod;
        public int    isTop;
        public int    isBest;
        public int    isVip;
        public int    isSVip;
        public int    isNotice;
        public int    isUse;
        public String labels;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.serial);
            dest.writeString(this.id);
            dest.writeString(this.nickName);
            dest.writeString(this.headImgUrl);
            dest.writeString(this.cDate);
            dest.writeInt(this.replyTimes);
            dest.writeString(this.title);
            dest.writeInt(this.isGod);
            dest.writeInt(this.isTop);
            dest.writeInt(this.isBest);
            dest.writeInt(this.isVip);
            dest.writeInt(this.isSVip);
            dest.writeInt(this.isNotice);
            dest.writeInt(this.isUse);
            dest.writeString(this.labels);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.serial = in.readString();
            this.id = in.readString();
            this.nickName = in.readString();
            this.headImgUrl = in.readString();
            this.cDate = in.readString();
            this.replyTimes = in.readInt();
            this.title = in.readString();
            this.isGod = in.readInt();
            this.isTop = in.readInt();
            this.isBest = in.readInt();
            this.isVip = in.readInt();
            this.isSVip = in.readInt();
            this.isNotice = in.readInt();
            this.isUse = in.readInt();
            this.labels = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}




