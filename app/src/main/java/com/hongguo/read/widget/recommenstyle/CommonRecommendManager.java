package com.hongguo.read.widget.recommenstyle;

import android.content.Context;

import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;

/**
 * Created by losg
 */

public class CommonRecommendManager {

    private RecommendStyleOneManager   mRecommendStyleOneManager;
    private RecommendStyleTwoManager   mRecommendStyleTwoManager;
    private RecommendStyleThreeManager mRecommendStyleThreeManager;
    private RecommendStyleFourManager  mRecommendStyleFourManager;
    private RecommendStyleFiveManager  mRecommendStyleFiveManager;
    private Context                    mContext;

    public CommonRecommendManager(Context context) {
        mContext = context;
        mRecommendStyleOneManager = new RecommendStyleOneManager(context);
        mRecommendStyleTwoManager = new RecommendStyleTwoManager(context);
        mRecommendStyleThreeManager = new RecommendStyleThreeManager(context);
        mRecommendStyleFourManager = new RecommendStyleFourManager(context);
        mRecommendStyleFiveManager = new RecommendStyleFiveManager(context);
    }

    public BaseRecommendStyle getStyle(RecommendItemBean.ObjBean objBean) {
        if (objBean == null) return new BaseRecommendStyle(mContext);
        BaseRecommendStyle baseRecommendStyle;
        if (objBean.showtype == 1) {
            if (objBean.data.size() < 4) {
                baseRecommendStyle = mRecommendStyleThreeManager;
            } else {
                baseRecommendStyle = mRecommendStyleOneManager;
            }
        } else if (objBean.showtype == 2) {
            if (objBean.data.size() < 6) {
                baseRecommendStyle = mRecommendStyleThreeManager;
            } else {
                baseRecommendStyle = mRecommendStyleTwoManager;
            }
        } else if (objBean.showtype == 3) {
            baseRecommendStyle = mRecommendStyleThreeManager;
        } else if (objBean.showtype == 4) {
            if (objBean.data.size() < 3) {
                baseRecommendStyle = mRecommendStyleThreeManager;
            } else {
                baseRecommendStyle = mRecommendStyleFourManager;
            }
        } else {
            if (objBean.data.size() < 3) {
                baseRecommendStyle = mRecommendStyleThreeManager;
            } else {
                baseRecommendStyle = mRecommendStyleFiveManager;
            }
        }
        baseRecommendStyle.setBookStoreItem(objBean);
        return baseRecommendStyle;
    }
}
