package com.hongguo.read.mvp.ui.topic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.hongguo.read.R;
import com.hongguo.read.adapter.TopicAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.topic.TopicContractor;
import com.hongguo.read.mvp.model.topic.TopicBean;
import com.hongguo.read.mvp.presenter.topic.TopicPresenter;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/13.
 */

public class TopicActivity extends ActivityEx  implements TopicContractor.IView, LoadingView.LoadingViewClickListener {

	@Inject
    TopicPresenter mTopicPresenter;

    private static final String INTENT_TOPIC_TYPE = "topic_type";
    @BindView(R.id.topic_list)
    RecyclerView mTopicList;

    private int mTopicType = Constants.TOPIC_TYPE.TOPIC_GIRLS;

    private List<TopicBean.ZtlistBean> mItems;
    private TopicAdapter mTopicAdapter;

    public static void toActivity(Context context, int topicType) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(INTENT_TOPIC_TYPE, topicType);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_topic;
    }

    
	@Override
	protected void inject(ActivityComponent activityComponent) {
		activityComponent.inject(this);
	}

	@Override
    protected void initView()  {
        setTitle("精选专题");

        mTopicType = getIntent().getIntExtra(INTENT_TOPIC_TYPE, Constants.TOPIC_TYPE.TOPIC_GIRLS);
        mItems = new ArrayList<>();
        mTopicAdapter = new TopicAdapter(mContext, mItems);
        mTopicList.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mTopicList.setAdapter(mTopicAdapter);

        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper,mTopicList, 0);

        mTopicPresenter.bingView(this);
        mTopicPresenter.loading();
        mTopicPresenter.queryTopic(mTopicType);
    }

    @ViewMethod
    public void setTopic(List<TopicBean.ZtlistBean> topic) {
        mTopicAdapter.setTopicBeans(topic);
        mTopicAdapter.notifyChange();
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mTopicPresenter.queryTopic(mTopicType);
    }
}
