package com.hongguo.read.mvp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hongguo.read.R;
import com.hongguo.read.adapter.QuestionAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.QuestionContractor;
import com.hongguo.read.mvp.model.mine.QuestionBean;
import com.hongguo.read.mvp.presenter.mine.QuestionPresenter;
import com.hongguo.read.mvp.ui.mine.mypackage.FeedBackActivity;
import com.hongguo.common.router.AppRouter;
import com.hongguo.read.utils.RecyclerLayoutUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by losg on 2018/1/7.
 */
@Route(path = AppRouter.VIEW_REQUEST)
public class QuestionActivity extends ActivityEx  implements QuestionContractor.IView {

	@Inject
    QuestionPresenter mQuestionPresenter;

    @BindView(R.id.request_list)
    RecyclerView mRequestList;
    @BindView(R.id.feedback)
    TextView     mFeedback;
    private List<QuestionBean.DataBean> mItems;
    private QuestionAdapter mAdapter;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, QuestionActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_question;
    }

	@Override
	protected void inject(ActivityComponent activityComponent) {
		activityComponent.inject(this);
	}

	@Override
    protected void initView()  {
        setTitle("常见问题");
        mRequestList.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mItems = new ArrayList<>();
        mAdapter = new QuestionAdapter(mContext, mItems);
        mRequestList.setAdapter(mAdapter);
        mQuestionPresenter.bingView(this);
        mQuestionPresenter.loading();
    }

    @ViewMethod
    public void setRequestList(List<QuestionBean.DataBean> items){
        mItems.clear();
        mItems.addAll(items);
        mAdapter.notifyChange();
    }


    @OnLongClick(R.id.copy_weixin)
    boolean copyWeinXin(){
        //TODO 微信公众号弹窗
        return true;
    }

    @OnClick(R.id.feedback)
    void feedback(){
        FeedBackActivity.toActivity(mContext);
    }

}
