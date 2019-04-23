package com.hongguo.read.widget.recommenstyle;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.topic.TopicBean;
import com.hongguo.read.mvp.ui.topic.TopicActivity;
import com.hongguo.read.mvp.ui.topic.TopicDetailActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;

/**
 * Created time 2017/12/6.
 *
 * @author losg
 */

public class RecommendTopicManager extends BaseRecommendStyle {

    private TopicBean mTopicBean;

    public RecommendTopicManager(Context context) {
        super(context);
    }

    @Override
    public int getTitleResource() {
        if (mTopicBean == null || mTopicBean.ztlist.size() == 0) return 0;
        return R.layout.view_recommend_common_title;
    }

    @Override
    public void initTitleView(View titleView) {
        TextView title = (TextView) titleView.findViewById(R.id.title);
        title.setText("精选专题");
        titleView.findViewById(R.id.more).setVisibility(View.VISIBLE);
        titleView.findViewById(R.id.more).setOnClickListener(v->{
            TopicActivity.toActivity(mContext, mTopicBean.topicType);
        });
    }

    @Override
    public int getItemResource() {
        return R.layout.view_topic;
    }

    @Override
    public void initItemView(View itemView, int position) {
        ImageLoadUtils.loadingBannel((ImageView) ((LinearLayout) itemView).getChildAt(0), mTopicBean.ztlist.get(0).image);
        ImageLoadUtils.loadingBannel((ImageView) ((LinearLayout) itemView).getChildAt(1), mTopicBean.ztlist.get(1).image);

        ((LinearLayout) itemView).getChildAt(0).setOnClickListener(v->{
            TopicDetailActivity.toActivity(mContext, mTopicBean.ztlist.get(0).zhuanti.get(0).title,  mTopicBean.ztlist.get(0).zhuanti.get(0).keyword);
        });

        ((LinearLayout) itemView).getChildAt(1).setOnClickListener(v->{
            TopicDetailActivity.toActivity(mContext, mTopicBean.ztlist.get(1).zhuanti.get(0).title,  mTopicBean.ztlist.get(1).zhuanti.get(0).keyword);
        });

    }

    @Override
    public int getItemCount() {
        if (mTopicBean == null) return 0;
        return 1;
    }

    public void setTopicBean(TopicBean topicBean) {
        mTopicBean = topicBean;
    }
}
