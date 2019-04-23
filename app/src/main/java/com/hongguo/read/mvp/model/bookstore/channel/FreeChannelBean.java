package com.hongguo.read.mvp.model.bookstore.channel;

import java.util.List;

public class FreeChannelBean {

    public List<BaiduFree> baidufrees;
    public BaiduBanner     mBannerFress;

    public static class BaiduFree {
        public String              title;
        public List<BaiduFreeItem> baiduFreeItems;

        public static class BaiduFreeItem {
            public String name;
            public String bookId;
            public String image;
            public String describe;
            public String auther;
            public String type;
            public String words;
        }

    }

    public static class BaiduBanner {
        public String bannerUrl;
        public String bookId;
        public String image;
    }
}