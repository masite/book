package com.hongguo.read.mvp.model.loading;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.hongguo.common.utils.SpStaticUtil;
import com.losg.library.utils.JsonUtils;

/**
 * Created by losg on 2017/6/16.
 */

public class BookPattern {

    private static BookPattern sBookPattern;

    public static BookPattern getInstance() {
        if (sBookPattern == null) {
            String pattern = SpStaticUtil.getString("pattern");
            if (TextUtils.isEmpty(pattern)) {
                sBookPattern = new BookPattern();
                sBookPattern.data = new Data();
                sBookPattern.data.versionCode = "1.3.21";
                sBookPattern.data.patternContent = new Data.PatternContent();
                sBookPattern.data.patternContent.baiduPattern = new Data.PatternContent.BaiduPattern();
                sBookPattern.data.patternContent.baiduPattern.baiduFreePage = new Data.PatternContent.BaiduPattern.BaiduFreePage();
                sBookPattern.data.patternContent.baiduPattern.baiduFreePage.url = "http://megatron.platform.zongheng.com/limitfree?p10=7.4.0.37";
                sBookPattern.data.patternContent.baiduPattern.baiduFreePage.split = "[\\s\\S]+?(<section class=\"All-Title\">|</body>)";
                sBookPattern.data.patternContent.baiduPattern.baiduFreePage.title = "<span>([\\s\\S]+?)</span>";
                sBookPattern.data.patternContent.baiduPattern.baiduFreePage.filter = ".*?(?=特价|章免费|促销)";
                sBookPattern.data.patternContent.baiduPattern.baiduFreePage.bookImage = "<img[\\s\\S]*?data-original=\"(.*?)\"";
                sBookPattern.data.patternContent.baiduPattern.baiduFreePage.bookItem = "<(?:div|img) class=\"[\\s\\S]*?_clickBook\" data-saprop=\"[\\s\\S]+?book_id=(\\d+)\"[\\s\\S]+?data-bookname=\"([^\"]*?)\"[^>]*>";
                sBookPattern.data.patternContent.baiduPattern.baiduFreePage.bookFirstDescribe = "<div class=\"nr\">([\\s\\S]*?)</div>[^<]*?<div class=\"Can_list\">[^<]*?" +
                        "<dl class=\"N\">([\\s\\S]*?)</dl>[^<]*?" +
                        "<ul class=\"u_list\">[^<]*?" +
                        "<li>([\\s\\S]*?)</li>[^<]*?" +
                        "<li class=\"color_1\">([\\s\\S]*?)</li>";
                sBookPattern.data.patternContent.baiduPattern.baiduFreePage.bookOtherDescribe = "<div class=\"Brief\">([\\s\\S]*?)</div>";
            } else {
                sBookPattern = JsonUtils.fromJson(pattern, BookPattern.class);
            }
        }
        return sBookPattern;
    }

    //清除旧版本
    private static void clearHistory(){
        SpStaticUtil.putString("pattern", "");
    }

    public String getFreeUrl(){
        return data.patternContent.baiduPattern.baiduFreePage.url;
    }

    public void update() {
        if (data == null) {
            return;
        }
        BookPattern instance = BookPattern.getInstance();
        if (!TextUtils.isEmpty(data.versionCode) && data.versionCode.compareTo(instance.data.versionCode) > 0) {
            SpStaticUtil.putString("pattern", new Gson().toJson(this));
        }
        sBookPattern = null;
    }

    public Data data;

    public static class Data {

        public String         versionCode;
        public PatternContent patternContent;

        public static class PatternContent {

            public BaiduPattern baiduPattern;

            public static class BaiduPattern {

                public BaiduFreePage baiduFreePage;

                public static class BaiduFreePage {

                    public String url;
                    public String banner;               //banner 切割
                    public String split;                //文本切割
                    public String title;                //书的大标题
                    public String filter;               //书本过滤
                    public String bookItem;             //获取每本书 名称和id
                    public String bookImage;            //获取书的图片
                    public String bookFirstDescribe;    //获取第一本书的描述
                    public String bookOtherDescribe;    //获取其他书的描述信息

                }
            }
        }
    }

}
