package com.hongguo.read.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.search.RankBean;
import com.hongguo.read.mvp.ui.bookstore.channel.SortDetailActivity;

import java.util.List;

/**
 * Created by losg on 2018/5/29.
 */

public class SortChannelAdapter extends IosRecyclerAdapter {

    private RankBean mClickRank;
    private RankBean mRecommendRank;
    private RankBean mUpdateRank;
    private RankBean mFavorRank;
    private RankBean mRewardRank;

    public SortChannelAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.view_sort_channel_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        RankBean rank = getRank(areaPosition);
        List<RankBean.DataBean> data = rank.data;
        hoder.setText(R.id.sort_channel_name, rank.title);
        hoder.getViewById(R.id.sort_channel_name).setBackgroundResource(rank.resource);
        for (int i = 0; i < 3; i++) {
            if (i < data.size()) {
                RankBean.DataBean dataBean = data.get(i);
                ImageLoadUtils.loadUrl(getPositionImage(hoder, i), dataBean.cover);
                getPositionText(hoder, i).setText(dataBean.name);
            }
        }

        hoder.itemView.setOnClickListener(v -> {
            SortDetailActivity.toActivity(mContext, rank.type, rank.title);
        });
    }

    private TextView getPositionText(BaseHoder hoder, int index) {
        switch (index) {
            case 0:
                return hoder.getViewById(R.id.book_one_title);
            case 1:
                return hoder.getViewById(R.id.book_two_title);
            case 2:
                return hoder.getViewById(R.id.book_three_title);
        }
        return null;
    }

    private ImageView getPositionImage(BaseHoder hoder, int index) {
        switch (index) {
            case 0:
                return hoder.getViewById(R.id.book_one_cover);
            case 1:
                return hoder.getViewById(R.id.book_two_cover);
            case 2:
                return hoder.getViewById(R.id.book_three_cover);
        }
        return null;
    }

    @Override
    protected int getAreaSize() {
        return 5;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        RankBean rank = getRank(areaPosition);
        if (rank == null) return 0;
        return 1;
    }

    private RankBean getRank(int index) {
        switch (index) {
            case 0:
                return mClickRank;
            case 1:
                return mRecommendRank;
            case 2:
                return mUpdateRank;
            case 3:
                return mFavorRank;
            case 4:
                return mRewardRank;
        }
        return null;
    }


    public void setClickRank(RankBean clickRank) {
        mClickRank = clickRank;
    }

    public void setRecommendRank(RankBean recommendRank) {
        mRecommendRank = recommendRank;
    }

    public void setUpdateRank(RankBean updateRank) {
        mUpdateRank = updateRank;
    }

    public void setFavorRank(RankBean favorRank) {
        mFavorRank = favorRank;
    }

    public void setRewardRank(RankBean rewardRank) {
        mRewardRank = rewardRank;
    }
}
