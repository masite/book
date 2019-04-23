package com.hongguo.read.repertory.share;

import android.text.TextUtils;

import com.hongguo.common.utils.SpStaticUtil;
import com.hongguo.read.mvp.model.CmsBannerBean;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.losg.library.utils.JsonUtils;

/**
 * Created time 2017/11/29.
 *
 * @author losg
 */

public class CacheRepertory {

    /**
     * 首页 loading 启动图片背景缓存
     *
     * @param imageUrl
     */
    public static void setLoadingImage(String imageUrl) {
        SpStaticUtil.putString("qidong", imageUrl);
    }

    public static String getLoadingImage() {
        return SpStaticUtil.getString("qidong", "");
    }

    public static void setBookStoreBanner(CmsBannerBean homeBanner) {
        SpStaticUtil.putString("book_store_banner", JsonUtils.toJson(homeBanner));
    }

    public static CmsBannerBean getBookStoreBanner() {
        String homeBanner = SpStaticUtil.getString("book_store_banner");
        if (TextUtils.isEmpty(homeBanner)) return null;
        return JsonUtils.fromJson(homeBanner, CmsBannerBean.class);
    }

    public static void setRecommendItem(RecommendItemBean recommendItemBean) {
        SpStaticUtil.putString("book_store_recommend", JsonUtils.toJson(recommendItemBean));
    }


    public static void setFavorItem(RecommendItemBean.ObjBean objBean) {
        SpStaticUtil.putString("book_store_favor_0_0_2", JsonUtils.toJson(objBean));
    }

    public static RecommendItemBean.ObjBean getFavor() {
        String recommend = SpStaticUtil.getString("book_store_favor_0_0_2");
        if (TextUtils.isEmpty(recommend)) return null;
        return JsonUtils.fromJson(recommend, RecommendItemBean.ObjBean.class);
    }

    public static void setHotRecommendItem(RecommendItemBean.ObjBean objBean) {
        SpStaticUtil.putString("book_store_hot_recommend_0_0_1", JsonUtils.toJson(objBean));
    }

    public static RecommendItemBean.ObjBean getHotRecommendItem() {
        String recommend = SpStaticUtil.getString("book_store_hot_recommend_0_0_1");
        if (TextUtils.isEmpty(recommend)) return null;
        return JsonUtils.fromJson(recommend, RecommendItemBean.ObjBean.class);
    }

    public static void setHotItem(RecommendItemBean.ObjBean objBean) {
        SpStaticUtil.putString("book_store_hot_0_0_1", JsonUtils.toJson(objBean));
    }

    public static RecommendItemBean.ObjBean getHotItem() {
        String recommend = SpStaticUtil.getString("book_store_hot_0_0_1");
        if (TextUtils.isEmpty(recommend)) return null;
        return JsonUtils.fromJson(recommend, RecommendItemBean.ObjBean.class);
    }

    public static void setNewBook(RecommendItemBean.ObjBean objBean) {
        SpStaticUtil.putString("book_store_new_book_0_0_1", JsonUtils.toJson(objBean));
    }

    public static RecommendItemBean.ObjBean getNewBook() {
        String recommend = SpStaticUtil.getString("book_store_new_book_0_0_1");
        if (TextUtils.isEmpty(recommend)) return null;
        return JsonUtils.fromJson(recommend, RecommendItemBean.ObjBean.class);
    }

    public static void setRecommendItem(RecommendItemBean.ObjBean objBean) {
        SpStaticUtil.putString("book_store_recommend_0_0_1", JsonUtils.toJson(objBean));
    }

    public static RecommendItemBean.ObjBean getRecommendItem() {
        String recommend = SpStaticUtil.getString("book_store_recommend_0_0_1");
        if (TextUtils.isEmpty(recommend)) return null;
        return JsonUtils.fromJson(recommend, RecommendItemBean.ObjBean.class);
    }

}
