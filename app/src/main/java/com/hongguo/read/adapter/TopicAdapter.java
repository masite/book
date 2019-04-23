package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.topic.TopicBean;
import com.hongguo.read.mvp.ui.topic.TopicDetailActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2018/1/13.
 */

public class TopicAdapter extends IosRecyclerAdapter {

    private List<TopicBean.ZtlistBean> mTopicBeans;

    public TopicAdapter(Context context, List<TopicBean.ZtlistBean> topicBeans) {
        super(context);
        mTopicBeans = topicBeans;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_topic;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        TopicBean.ZtlistBean ztlistBean = mTopicBeans.get(index);
        ImageLoadUtils.loadingBannel(hoder.getViewById(R.id.topic_image), ztlistBean.image);
        hoder.setText(R.id.topic_describe, ztlistBean.des);
        hoder.itemView.setOnClickListener(v->{
            TopicDetailActivity.toActivity(mContext, ztlistBean.zhuanti.get(0).title,  ztlistBean.zhuanti.get(0).keyword);
        });

    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mTopicBeans.size();
    }

    public void setTopicBeans(List<TopicBean.ZtlistBean> topicBeans) {
        mTopicBeans = topicBeans;
    }
}
