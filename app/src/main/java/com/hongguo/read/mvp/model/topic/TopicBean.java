package com.hongguo.read.mvp.model.topic;

import java.util.List;

/**
 * Created time 2017/12/1.
 * 专题
 *
 * @author losg
 */

public class TopicBean {


    public List<ZtlistBean> ztlist;
    public int topicType;

    public static class ZtlistBean {
        /**
         * zhuanti : [{"keyword":"特种兵","title":"特种兵王"}]
         * image : http://x1.hgread.com/ztlist/image1/1.png
         * littleImage : http://x1.hgread.com/ztlist/image1/l1.png
         * des : 他，是特种兵中的特种兵；而她，则是女人中的女人，他们相遇，相知，相恋到相爱，最后，她才发现：原来自己深深爱着的人，居然是自己的杀父仇人。这一切，到底是阴谋还是真实，她，该怎么样去面对？
         */

        public String            image;
        public String            littleImage;
        public String            des;
        public List<ZhuantiBean> zhuanti;

        public static class ZhuantiBean {
            /**
             * keyword : 特种兵
             * title : 特种兵王
             */

            public String keyword;
            public String title;
        }
    }
}
