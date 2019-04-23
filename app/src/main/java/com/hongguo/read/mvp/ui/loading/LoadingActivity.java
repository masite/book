package com.hongguo.read.mvp.ui.loading;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.R;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.eventbus.BackgroundLoginEvent;
import com.hongguo.read.mvp.contractor.loading.LoadingContractor;
import com.hongguo.read.mvp.presenter.loading.LoadingPresenter;
import com.hongguo.read.mvp.presenter.login.LoginPresenter;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.mvp.ui.book.BookReaderActivity;
import com.hongguo.read.repertory.db.DBFactory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.utils.update.ApkDownService;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created time 2017/11/30.
 *
 * @author losg
 *         默认加载页,主要任务
 *         1、权限的获取
 *         2、登录操作
 *         3、活动splash
 *         4、单推包操作
 */

public class LoadingActivity extends ActivityEx implements LoadingContractor.IView, DBFactory.UpdateDBListener {

    @Inject
    LoadingPresenter mLoadingPresenter;
    @Inject
    LoginPresenter   mLoginPresenter;

    @BindView(R.id.loading_image)
    ImageView    mLoadingImage;
    @BindView(R.id.time_down_layer)
    LinearLayout mTimeDownLayer;
    @BindView(R.id.time_down)
    TextView     mTimeDown;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, LoadingActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_stay);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_loading;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {
        //解决每次点击logo重启的问题
        if (!isTaskRoot()) {
            finish();
            return;
        }

        hiddenToolBar();

        //统计开始
        StatisticsUtils.start();

        //开启下载app 服务线程
        ApkDownService.startSelfService(mContext);
    }

    @Override
    protected void initData() {
        super.initData();
        mLoadingPresenter.bingView(this);
        mLoadingPresenter.loading();
    }


    /**
     * 权限请求成功后登录操作
     * {@link LoadingPresenter#permissionSuccess}
     */
    @ViewMethod
    public void toLogin() {
        mLoginPresenter.login();
    }

    /**
     * 登录后回调
     * {@link LoginPresenter#dealLoginResult}
     *
     * @param backgroundLoginEvent
     */
    @Subscribe
    public void onEvent(BackgroundLoginEvent backgroundLoginEvent) {
        mLoadingPresenter.loginSuccess();
    }


    /**
     * 设置背景图片
     * {@link LoadingPresenter#queryImage}
     *
     * @param url
     */
    @ViewMethod
    public void setLoadingImageUrl(String url) {
        ImageLoadUtils.loadLoadingPage(mLoadingImage, url);
    }

    /**
     * 设置倒计时可见
     * {@link LoadingPresenter#delayJump}
     */
    @ViewMethod
    public void setDelayVisiable() {
        mTimeDownLayer.setVisibility(View.VISIBLE);
    }

    /**
     * 倒计时时间
     * {@link LoadingPresenter#delayJump}
     *
     * @param time
     */
    @ViewMethod
    public void setCurrentTime(int time) {
        mTimeDown.setText(String.valueOf(time));
    }

    /**
     * 跳转到首页
     * {@link LoadingPresenter#delayJump}
     */
    @ViewMethod
    public void toMain() {
        onTimeDownClick();
    }

    /**
     * 跳转到网页
     * {@link LoadingPresenter#chooseEnter}
     *
     * @param title
     * @param url
     */
    @ViewMethod
    public void toWebActivity(String title, String url) {

    }

    /**
     * 跳转到阅读页
     * {@link LoadingPresenter#jumpActivity}
     *
     */
    @ViewMethod
    public void toBookReader(String bookid, int bookFrom, String bookName, String bookCover, String author) {
        DBFactory.getInstance().loadDb(FileManager.getUserDbPath(UserRepertory.getUserID()), this);
        Intent intents[] = new Intent[2];
        intents[0] = new Intent(mContext, MainActivity.class);
        intents[1] = BookReaderActivity.getBookIntent(mContext, bookid, bookFrom, bookName, bookCover, -1, -1, author);
        mContext.startActivities(intents);
        finish();
    }

    /**
     *  数据库更新相关
     */
    @Override
    public void startUpdate() {

    }

    @Override
    public void updateFinish() {

    }


    @OnClick(R.id.loading_image)
    void onLoadImageClick() {
        mLoadingPresenter.chooseEnter();
    }

    @OnClick(R.id.time_down_layer)
    public void onTimeDownClick() {
        MainActivity.toActivity(mContext);
        finish();
    }

}
