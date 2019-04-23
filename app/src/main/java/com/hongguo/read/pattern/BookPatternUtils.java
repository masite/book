package com.hongguo.read.pattern;

import android.text.TextUtils;

import com.hongguo.read.mvp.model.bookstore.channel.FreeChannelBean;
import com.hongguo.read.mvp.model.loading.BookPattern;
import com.hongguo.read.utils.convert.ListParma;
import com.hongguo.read.utils.convert.Parma;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created time 2017/12/5.
 * 获取百度书城 页面对应的抓取
 *
 * @author losg
 */

public class BookPatternUtils {


    /**
     * 解析百度书城打赏榜
     * http://megatron.platform.zongheng.com/v7/top/detail?rankid=4
     *
     * @return
     */
    public static BaiduRankBook patternBaiduRank(String rank) {
        BaiduRankBook baiduRankBook = new BaiduRankBook();
        baiduRankBook.data = new ArrayList<>();
        String rankPattern = "<section class=\"book-list\">[\\s\\S]*?data-bookid=\"([^\"]*)\"[\\S\\s]*?<img[\\s\\S]*?data-original=\"([^\"]*)\">[\\s\\S]*?<div class=\"name\">[\\s\\S]*?<dt>([^<]*)</dt>[\\s\\S]*?<div class=\"nr\">([^<]*)</div>[\\s\\S]*?<dl class=\"N\">([^<]*)</dl>[\\s\\S]*?<ul class=\"u_list\">[^<]*<li>([^<]*)</li>[^<]*<li[^>]*>([^<]*)</li>";
        Pattern pattern = Pattern.compile(rankPattern);
        Matcher matcher = pattern.matcher(rank);
        while (matcher.find()) {
            BaiduRankBook.Data baiduRankBookData = new BaiduRankBook.Data();
            baiduRankBookData.bookId = matcher.group(1);
            baiduRankBookData.bookImage = matcher.group(2);
            baiduRankBookData.bookName = matcher.group(3);
            baiduRankBookData.bookDescribe = matcher.group(4);
            baiduRankBookData.auther = matcher.group(5);
            baiduRankBookData.words = matcher.group(6);
            baiduRankBookData.bookType = matcher.group(7);
            baiduRankBook.data.add(baiduRankBookData);
            if (baiduRankBook.data.size() >= 20) {
                break;
            }
        }
        return baiduRankBook;
    }


    /**
     * 获取百度免费 书籍信息
     *
     * @param response
     * @return
     */
    public static FreeChannelBean dealFreeBaidu(String response) {

        FreeChannelBean baiduFree = new FreeChannelBean();
        baiduFree.baidufrees = new ArrayList<>();

        Pattern pattern = Pattern.compile(BookPattern.getInstance().data.patternContent.baiduPattern.baiduFreePage.split);
        Matcher matcher = pattern.matcher(response);
        int size = 0;
        while (matcher.find()) {
            if (size++ == 0) {
                continue;
            }
            FreeChannelBean.BaiduFree freeBean = new FreeChannelBean.BaiduFree();
            freeBean.baiduFreeItems = new ArrayList<>();

            String area = matcher.group();
            //获取标题
            Pattern titlePattern = Pattern.compile(BookPattern.getInstance().data.patternContent.baiduPattern.baiduFreePage.title);
            Matcher titleMatcher = titlePattern.matcher(area);
            if (titleMatcher.find()) {
                freeBean.title = titleMatcher.group(1);
            }

            Pattern filterPattern = Pattern.compile(BookPattern.getInstance().data.patternContent.baiduPattern.baiduFreePage.filter);
            Matcher filterMatcher = filterPattern.matcher(freeBean.title);

            if (TextUtils.isEmpty(freeBean.title) || filterMatcher.find()) {
                continue;
            }

            //书的列表
            String contentPattern = BookPattern.getInstance().data.patternContent.baiduPattern.baiduFreePage.bookItem;
            Pattern itemPattern = Pattern.compile(contentPattern);
            Matcher itemMatcher = itemPattern.matcher(area);
            while (itemMatcher.find()) {
                FreeChannelBean.BaiduFree.BaiduFreeItem item = new FreeChannelBean.BaiduFree.BaiduFreeItem();
                item.bookId = itemMatcher.group(1);
                item.name = URLDecoder.decode(itemMatcher.group(2)).replaceAll("\n", "");
                freeBean.baiduFreeItems.add(item);
            }

            //获取图片或者详细信息
            String imagePattern = BookPattern.getInstance().data.patternContent.baiduPattern.baiduFreePage.bookImage;
            Pattern imgPattern = Pattern.compile(imagePattern);
            Matcher imgMatcher = imgPattern.matcher(area);
            int position = 0;
            while (imgMatcher.find()) {
                FreeChannelBean.BaiduFree.BaiduFreeItem baiduFreeItem = freeBean.baiduFreeItems.get(position++);

                if (baiduFreeItem != null) {
                    baiduFreeItem.image = imgMatcher.group(1);
                }
            }

            //获取描述信息
            String firstDescirbe = BookPattern.getInstance().data.patternContent.baiduPattern.baiduFreePage.bookFirstDescribe;
            Pattern firstPattern = Pattern.compile(firstDescirbe);
            Matcher firstMatcher = firstPattern.matcher(area);
            if (firstMatcher.find() && freeBean.baiduFreeItems.size() > 0) {
                FreeChannelBean.BaiduFree.BaiduFreeItem baiduFreeItem = freeBean.baiduFreeItems.get(0);
                if (baiduFreeItem != null) {
                    baiduFreeItem.describe = firstMatcher.group(1);
                    baiduFreeItem.auther = firstMatcher.group(2);
                    baiduFreeItem.words = firstMatcher.group(3);
                    baiduFreeItem.type = firstMatcher.group(4);
                    if (freeBean.baiduFreeItems.size() % 3 != 0) {
                        freeBean.baiduFreeItems.remove(1);
                    }
                }
            }

            //获取描述信息
            String otherDescirbe = BookPattern.getInstance().data.patternContent.baiduPattern.baiduFreePage.bookOtherDescribe;
            Pattern otherPattern = Pattern.compile(otherDescirbe);
            Matcher otherMatcher = otherPattern.matcher(area);
            position = 1;
            while (otherMatcher.find()) {
                FreeChannelBean.BaiduFree.BaiduFreeItem baiduFreeItem = freeBean.baiduFreeItems.get(position++);
                if (baiduFreeItem != null) {
                    baiduFreeItem.describe = otherMatcher.group(1).replace("\n", "");
                }
            }
            baiduFree.baidufrees.add(freeBean);
        }

        return baiduFree;
    }


    public static class BaiduRankBook {
        @ListParma(paramListName = "data")
        public List<Data> data;

        public static class Data {
            @Parma(paramName = "coverPicture")
            public String bookImage;
            @Parma(paramName = "bookName")
            public String bookName;
            @Parma(paramName = "desc")
            public String bookDescribe;
            @Parma(paramName = "bookId")
            public String bookId;
            @Parma(paramName = "categoryName")
            public String bookType;
            @Parma(paramName = "author")
            public String auther;
            @Parma(paramName = "wordsText")
            public String words;
            @Parma(paramName = "bookfrom")
            public int bookFrom = 1;
        }
    }
}
