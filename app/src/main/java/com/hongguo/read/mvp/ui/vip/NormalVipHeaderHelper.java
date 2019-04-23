package com.hongguo.read.mvp.ui.vip;

import android.content.Context;

import com.hongguo.read.R;
import com.hongguo.read.base.BaseViewHelper;
import com.hongguo.read.mvp.model.vip.VipBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.CommonThreeItem;

import java.util.List;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/10.
 */

public class NormalVipHeaderHelper extends BaseViewHelper {

    @BindView(R.id.common_three)
    CommonThreeItem mCommonThree;

    public NormalVipHeaderHelper(Context context) {
        super(context);
    }

    @Override
    protected int initLayout() {
        return R.layout.view_normal_vip_store_header;
    }

    public void setTopBooks(List<VipBean.DataBean> books) {
        mCommonThree.setLoadImage((imageView, url) -> ImageLoadUtils.loadUrl(imageView, url));
        mCommonThree.setLines(1);
        for (int i = 0; i < 3; i++) {
            VipBean.DataBean dataBean = books.get(i);
            mCommonThree.setImageUrl(i, dataBean.cover);
            mCommonThree.setName(i, dataBean.name);
        }

        mCommonThree.setItemClickListener(position -> {
            VipBean.DataBean dataBean = books.get(position);
            StatisticsUtils.vip(dataBean.name);
            BookDetailActivity.toActivity(mContext, dataBean.id, dataBean.bookType);
        });
    }

}
