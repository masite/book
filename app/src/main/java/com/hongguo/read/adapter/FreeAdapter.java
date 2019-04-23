package com.hongguo.read.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.read.R;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.mvp.model.bookstore.channel.FreeChannelBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.CommonThreeItem;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;


/**
 * Created by losg on 2017/6/15.
 */

public class FreeAdapter extends IosRecyclerAdapter {

    private FreeChannelBean mBaiduFree;

    public FreeAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getCellTitleResource(int areaPosition) {
        if (TextUtils.isEmpty(mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.get(0).describe)) {
            return R.layout.adapter_free_title;
        } else {
            return R.layout.adapter_part_free_title;
        }
    }

    @Override
    protected void initCellTitleView(BaseHoder hoder, int areaPosition) {
        if (TextUtils.isEmpty(mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.get(0).describe)) {
            FreeChannelBean.BaiduFree baiduFree = mBaiduFree.baidufrees.get(areaPosition);
            ((TextView) hoder.itemView).setText(baiduFree.title);
        } else {
            hoder.setText(R.id.free_title, mBaiduFree.baidufrees.get(areaPosition).title);
            FreeChannelBean.BaiduFree.BaiduFreeItem baiduFreeItem = mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.get(0);
            ImageLoadUtils.loadUrl((ImageView) hoder.getViewById(R.id.book_image),baiduFreeItem.image);
            hoder.setText(R.id.book_name, baiduFreeItem.name);
            hoder.setText(R.id.book_descibe, baiduFreeItem.describe);
            hoder.setText(R.id.book_auther, baiduFreeItem.auther);
            hoder.setText(R.id.word_size, baiduFreeItem.words);
            hoder.setText(R.id.book_type, baiduFreeItem.type);
            hoder.getViewById(R.id.first_book).setOnClickListener(v -> {
                BookDetailActivity.toActivity(mContext, baiduFreeItem.bookId, Constants.BOOK_FROM.FROM_BAIDU);
                StatisticsUtils.collect("活动专区",mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.get(0).name);
            });
        }
    }

    @Override
    protected int getContentResource(int areaPosition) {
        if (TextUtils.isEmpty(mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.get(0).describe)) {
            return R.layout.adapter_free_item;
        } else {
            return R.layout.adapter_free_item;
        }
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        if (!TextUtils.isEmpty(mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.get(0).describe)) {
            initPartFree(hoder, areaPosition, index);
            return;
        }
        FreeChannelBean.BaiduFree baiduFree = mBaiduFree.baidufrees.get(areaPosition);
        int position = index * 3;
        CommonThreeItem freeItem = hoder.getViewById(R.id.common_three_item);
        freeItem.setName(0, baiduFree.baiduFreeItems.get(position).name);
        freeItem.setName(1, baiduFree.baiduFreeItems.get(position + 1).name);
        freeItem.setName(2, baiduFree.baiduFreeItems.get(position + 2).name);
        freeItem.setLoadImage((imageView, url) -> ImageLoadUtils.loadUrl(imageView,url));
        freeItem.setImageUrl(0, baiduFree.baiduFreeItems.get(position).image);
        freeItem.setImageUrl(1, baiduFree.baiduFreeItems.get(position + 1).image);
        freeItem.setImageUrl(2, baiduFree.baiduFreeItems.get(position + 2).image);
        freeItem.setItemClickListener(position1 -> {
            FreeChannelBean.BaiduFree.BaiduFreeItem baiduFreeItem = baiduFree.baiduFreeItems.get(position + position1);
            BookDetailActivity.toActivity(mContext, baiduFreeItem.bookId, Constants.BOOK_FROM.FROM_BAIDU);
            StatisticsUtils.collect("活动专区",baiduFree.baiduFreeItems.get(index * 3 + position1).name);
        });
    }

    private void initPartFree(BaseHoder hoder, int areaPosition, int index) {
        FreeChannelBean.BaiduFree.BaiduFreeItem baiduFreeItem = mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.get(index + 1);
        hoder.setText(R.id.book_name, baiduFreeItem.name);
        hoder.setText(R.id.book_descibe, baiduFreeItem.describe);
        hoder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailActivity.toActivity(mContext, baiduFreeItem.bookId, Constants.BOOK_FROM.FROM_BAIDU);
                StatisticsUtils.collect("活动专区",baiduFreeItem.name);
            }
        });
    }

    @Override
    protected int getFooterResource(int areaPosition) {
        return R.layout.view_empty_footer;
    }

    @Override
    protected void initFooterView(BaseHoder hoder, int areaPosition) {
        super.initFooterView(hoder, areaPosition);
    }

    @Override
    protected int getAreaSize() {
        if (mBaiduFree == null) {
            return 0;
        }
        return mBaiduFree.baidufrees.size();
    }

    @Override
    protected int getCellCount(int areaPosition) {
        if (TextUtils.isEmpty(mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.get(0).describe)) {
            return mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.size() / 3;
        } else {
            return mBaiduFree.baidufrees.get(areaPosition).baiduFreeItems.size() - 1;
        }
    }

    public void setBaiduFree(FreeChannelBean baiduFree) {
        mBaiduFree = baiduFree;
    }
}
