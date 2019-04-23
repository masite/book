package com.hongguo.read.mvp.ui.vip;

import android.content.Context;

import com.hongguo.common.utils.webview.AppWebView;
import com.hongguo.read.R;
import com.hongguo.read.base.BaseViewHelper;
import com.hongguo.read.mvp.model.CmsBannerBean;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.EventBannerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/10.
 */

public class SuperVipHeaderHelper extends BaseViewHelper {

    @BindView(R.id.event_banner)
    EventBannerView mEventBanner;

    public SuperVipHeaderHelper(Context context) {
        super(context);
    }

    @Override
    protected int initLayout() {
        return R.layout.view_super_vip_header;
    }

    public void setBannerUrls(List<CmsBannerBean.DataBean> banners) {
        mEventBanner.setImageLoader(ImageLoadUtils::loadingBannel);
        List<String> imageUrls = new ArrayList<>();
        for (CmsBannerBean.DataBean banner : banners) {
            imageUrls.add(banner.cover);
        }
        mEventBanner.setLoadUrls(imageUrls);
        mEventBanner.setOnBannerItemClickListener(position -> {
            CmsBannerBean.DataBean dataBean = banners.get(position);
            AppWebView.toActivity(dataBean.linkUrl);
        });
    }
}
