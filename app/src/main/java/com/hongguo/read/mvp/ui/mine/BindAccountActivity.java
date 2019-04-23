package com.hongguo.read.mvp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

import com.hongguo.common.utils.UmengAuthorHelper;
import com.hongguo.read.R;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.BindAccountContractor;
import com.hongguo.read.mvp.presenter.mine.BindAccountPresenter;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by losg on 2018/1/9.
 */

public class BindAccountActivity extends ActivityEx implements BindAccountContractor.IView, LoadingView.LoadingViewClickListener, UmengAuthorHelper.UmAuthorResponseListener {

    @Inject
    BindAccountPresenter mBindAccountPresenter;

    @BindView(R.id.content_rooter)
    ConstraintLayout mConstraintLayout;

    @BindView(R.id.bind_qq)
    TextView mBindQq;
    @BindView(R.id.bind_weixin)
    TextView mBindWeixin;

    private UmengAuthorHelper mUmengAuthorHelper;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, BindAccountActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_bind_account;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setTitle("账号绑定");
        LoadingHelper loadingHelper = new LoadingHelper(mContext);
        loadingHelper.setLoadingViewClickListener(this);
        bindLoadingView(loadingHelper, mConstraintLayout, 0);
        mBindAccountPresenter.bingView(this);
        mBindAccountPresenter.loading();

        mUmengAuthorHelper = new UmengAuthorHelper(this);
        mUmengAuthorHelper.setUmAuhorResponseListener(this);
    }

    @ViewMethod
    public void setQQBind(boolean qqBind){
        mBindQq.setText(qqBind ? "已绑定" : "绑定");
        mBindQq.setEnabled(!qqBind);
    }

    @ViewMethod
    public void setWeiXinBind(boolean weixin){
        mBindWeixin.setText(weixin ? "已绑定" : "绑定");
        mBindWeixin.setEnabled(!weixin);
    }


    @OnClick(R.id.bind_qq)
    public void onMBindQqClicked() {
        mUmengAuthorHelper.author(UmengAuthorHelper.AUTHOR_QQ);
    }

    @OnClick(R.id.bind_weixin)
    public void onMBindWeixinClicked() {
        mUmengAuthorHelper.author(UmengAuthorHelper.AUTHOR_WEIXIN);
    }

    @OnLongClick(R.id.bind_qq_layer)
    public boolean unBindQQ() {
        mBindAccountPresenter.unbindQQ();
        return true;
    }

    @OnLongClick(R.id.bind_weixin_layer)
    public boolean unBindWeinXin() {
        mBindAccountPresenter.unbindWeiXin();
        return true;
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {
        mBindAccountPresenter.loading();
    }

    /********************************友盟授权登录相关回调 start **********************************/
    @Override
    public void authorStart() {
        showWaitDialog(true, "授权绑定", null);
    }

    @Override
    public void authorComplete(int authorType, UmengAuthorHelper.AuthorResult authorResult) {
        dismissWaitDialogWithoutAnim();
        mBindAccountPresenter.authorLogin(authorType, authorResult);
    }

    @Override
    public void authorError() {
        dismissWaitDialogWithoutAnim();
        toastMessage("授权失败");
    }

    @Override
    public void authorCancel() {
        dismissWaitDialogWithoutAnim();
        toastMessage("授权取消");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUmengAuthorHelper.onActivityResult(requestCode, resultCode, data);
    }

    /********************************友盟授权登录相关回调 end **********************************/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUmengAuthorHelper.destory();
    }
}
