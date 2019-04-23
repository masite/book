package com.hongguo.read.mvp.ui.book.detail;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.constants.RankType;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.read.mvp.ui.reward.RewardTopActivity;


/**
 * Created by losg on 2017/12/26.
 */

public class BookDetailRewardTopHelper {

    private Context       mContext;
    private AwardRankBean mAwardRankBean;
    private String mBookId;

    public BookDetailRewardTopHelper(Context context, String bookId) {
        mContext = context;
        mBookId = bookId;
    }

    public int getTileResouce() {
        return 0;
    }

    public void initTitle(IosRecyclerAdapter.BaseHoder hoder) {
    }

    public int getCellCount() {
        if (mAwardRankBean == null) return 0;
        return mAwardRankBean.data.size() == 0 ? 0 : 1;
    }

    public int getItemResouce() {
        return R.layout.view_bookdetail_reward;
    }

    public void initBookRank(IosRecyclerAdapter.BaseHoder hoder, int position) {
        LinearLayout linearLayout = hoder.getViewById(R.id.reward_layer);
        for (int i = 0; i < 3; i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
            if(i >= mAwardRankBean.data.size()){
                textView.setVisibility(View.INVISIBLE);
            }else {
                textView.setVisibility(View.VISIBLE);
                AwardRankBean.DataBean dataBean = mAwardRankBean.data.get(i);
                textView.setText(dataBean.nickName);
            }
        }
        hoder.getViewById(R.id.reward_top_title).setOnClickListener(v->{
            RewardTopActivity.toActivity(mContext, mBookId, RankType.RANK_MONEY_REWARD);
        });
    }

    public void setAwardRankBean(AwardRankBean awardRankBean) {
        mAwardRankBean = awardRankBean;
    }
}
