package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.vip.SVIPDescribeBean;
import com.hongguo.read.mvp.model.vip.VipBean;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.read.mvp.ui.vip.VipDescribeActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.VipThreeItem;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created time 2017/12/7.
 *
 * @author losg
 */

public class SVIPDescribeAdapter extends IosRecyclerAdapter {

    private List<VipBean.DataBean>             mItems;
    private List<SVIPDescribeBean.SvipDesBean> mSvipDesBeans;
    private DecimalFormat                      mDecimalFormat;


    public SVIPDescribeAdapter(Context context, List<VipBean.DataBean> items) {
        super(context);
        mItems = items;
        mDecimalFormat = new DecimalFormat("#.#");
    }

    @Override
    protected int getCellTitleResource(int areaPosition) {
        if (areaPosition == 0) {
            if (mSvipDesBeans == null) {
                return 0;
            } else {
                return R.layout.adapter_vip_describe_title_title;
            }
        } else {
            return R.layout.adapter_svip_describe_book_title;
        }
    }

    @Override
    protected void initCellTitleView(BaseHoder hoder, int areaPosition) {
        super.initCellTitleView(hoder, areaPosition);

        if (areaPosition == 0) {
            hoder.getViewById(R.id.share_vip).setOnClickListener(v -> {
                ((VipDescribeActivity) mContext).share();
            });
        }

        if (areaPosition == 1) {
            hoder.getViewById(R.id.more).setOnClickListener(v -> {
                MainActivity.toActivity(mContext, 2);
            });
        }
    }

    @Override
    protected int getFooterResource(int areaPosition) {
        if (areaPosition == 0) {
            if (mSvipDesBeans == null) return 0;
            return R.layout.adapter_vip_describe_title_footer;
        }
        return 0;
    }

    @Override
    protected void initFooterView(BaseHoder hoder, int areaPosition) {
        super.initFooterView(hoder, areaPosition);
        if (areaPosition == 0) {
            hoder.getViewById(R.id.open_vip).setOnClickListener(v -> {
                ((VipDescribeActivity) mContext).openSVip();
            });
        }
    }

    @Override
    protected int getContentResource(int areaPosition) {
        if (areaPosition == 0) {
            return R.layout.adapter_svip_describe_title_item;
        }
        return R.layout.adapter_super_vip_store_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        if (areaPosition == 0) {
            initHeader(hoder, index);
            return;
        }
        initBookItem(hoder, index);
    }

    private void initHeader(BaseHoder hoder, int index) {
        SVIPDescribeBean.SvipDesBean svipDesBean = mSvipDesBeans.get(index);
        hoder.setText(R.id.number, index + 1 + "");
        hoder.setText(R.id.title, svipDesBean.title);
        hoder.setText(R.id.describe, svipDesBean.des);
    }

    private void initBookItem(BaseHoder hoder, int index) {
        int position = index * 3;

        VipThreeItem vipThreeItem = hoder.getViewById(R.id.vip_three);
        vipThreeItem.setLoadImage(ImageLoadUtils::loadUrl);

        vipThreeItem.setImageUrl(0, mItems.get(position).cover);
        String price = mDecimalFormat.format(mItems.get(position).words / 20000f);
        vipThreeItem.setName(0, mItems.get(position).name, price);

        vipThreeItem.setItemClickListener(position1 -> {
            StatisticsUtils.collect("SVIP专区", mItems.get(position + position1).name);
        });

        if (position + 1 >= mItems.size()) {
            vipThreeItem.setPositionVisiable(1, false);
        } else {
            vipThreeItem.setPositionVisiable(1, true);
            vipThreeItem.setImageUrl(1, mItems.get(position + 1).cover);
            price = mDecimalFormat.format(mItems.get(position + 1).words / 20000f);
            vipThreeItem.setName(1, mItems.get(position + 1).name, price);
        }
        if (position + 2 >= mItems.size()) {
            vipThreeItem.setPositionVisiable(2, false);
        } else {
            vipThreeItem.setPositionVisiable(2, true);
            vipThreeItem.setImageUrl(2, mItems.get(position + 2).cover);
            price = mDecimalFormat.format(mItems.get(position + 2).words / 20000f);
            vipThreeItem.setName(2, mItems.get(position + 2).name, price);
        }

        vipThreeItem.setItemClickListener(position12 -> {
            VipBean.DataBean dataBean = mItems.get(position + position12);
            BookDetailActivity.toActivity(mContext, dataBean.id, dataBean.bookType);
        });
    }


    @Override
    protected int getAreaSize() {
        return 2;
    }

    public void setSvipDesBeans(List<SVIPDescribeBean.SvipDesBean> svipDesBeans) {
        mSvipDesBeans = svipDesBeans;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        if (areaPosition == 0) {
            if (mSvipDesBeans == null) {
                return 0;
            } else {
                return mSvipDesBeans.size();
            }
        }
        return mItems.size() > 0 ? mItems.size() / 3 : 0;
    }
}
