package com.hongguo.read.mvp.presenter.loading;

import android.Manifest;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.common.utils.rxjava.topic.CmsTopicDeal;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.CmsTopicInfo;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.eventbus.BackgroundLoginEvent;
import com.hongguo.read.eventbus.BaiDuTimeOut;
import com.hongguo.read.mvp.contractor.loading.LoadingContractor;
import com.hongguo.read.mvp.model.book.detail.BookDetailBean;
import com.hongguo.read.mvp.model.book.detail.baidu.BaiduDetailBean;
import com.hongguo.read.mvp.model.loading.BaiDuOut;
import com.hongguo.read.mvp.model.loading.BookPattern;
import com.hongguo.read.mvp.model.loading.LoadingBean;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.repertory.share.CacheRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.BookNumberUtils;
import com.hongguo.read.utils.MetaDataUtils;
import com.hongguo.read.utils.baidu.BaiduShuChengUrls;
import com.hongguo.read.utils.convert.BeanConvert;
import com.losg.library.utils.JsonUtils;
import com.losg.library.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author losg
 */
public class LoadingPresenter extends BaseImpPresenter<LoadingContractor.IView> implements LoadingContractor.IPresenter {

    private static final int DELAY_TIME = 3;

    private enum LoadingStatus {
        //加载成功
        LOADING_SUCCESS,
        //加载失败
        LOADING_FAILURE,
        //加载中
        LOADING_LOADING
    }

    /**
     * 单推包相关
     */
    private String  mSingleBookId = "";
    private int     mSingleType   = 0;
    private boolean mSingleFlag   = false;
    private BookDetailBean mBookDetailBean;
    //单推包书籍信息
    private LoadingStatus mBookInfoStatus = LoadingStatus.LOADING_LOADING;
    //是否登录成功的状态， 即是否获取到了userid +token  ，只有它为success时，才允许跳转阅读页
    private LoadingStatus mLoginStatus    = LoadingStatus.LOADING_LOADING;

    @Inject
    @ContextLife(ContextLife.Life.Application)
    Context mContext;

    @Inject
    public LoadingPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    /**
     * 相关方法
     * {@link com.hongguo.read.base.ActivityEx#checkAllPermission(String...)}
     */
    @Override
    public void loading() {
        //查询权限
        mView.checkAllPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //网络不可用
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            return;
        }
        //查询 百度商城正则信息
        queryPattern();
        queryBaiduTimeOut();
    }

    /**
     * 调用来源
     * {@link ActivityEx#permissionSuccess()}
     * <p>
     * 相关方法
     * {@link com.hongguo.read.mvp.ui.loading.LoadingActivity#toLogin}
     */
    @Override
    public void permissionSuccess() {
        //登录操作
        mView.toLogin();
        //查询背景图
        queryImage();
        mSingleFlag = isSingleBook();
        //单推包，查询书籍信息
        if (mSingleFlag) {
            queryBookInfo();
            return;
        }
        //不是单推包，直接延时跳转
        delayJump();
    }

    /**
     * 调用来源
     * {@link ActivityEx#permissionFailure()}
     * <p>
     * 相关方法
     * {@link ActivityEx#finishView()}
     */
    @Override
    public void permissionFailure() {
        mView.finishView();
        RxJavaUtils.delayRun(300, () -> Process.killProcess(Process.myPid()));
    }


    /**
     * 调用来源
     * {@link com.hongguo.read.mvp.ui.loading.LoadingActivity#onEvent(BackgroundLoginEvent)}
     */
    @PresenterMethod
    public void loginSuccess() {
        mLoginStatus = LoadingStatus.LOADING_SUCCESS;
        jumpActivity();
    }

    @PresenterMethod
    public void chooseEnter() {

    }

    /**
     * 查询启动页的信息
     * 先展示缓存的图片，后网络加载最新图片
     */
    private void queryImage() {
        String loadingImageCache = CacheRepertory.getLoadingImage();
        dealSplashResponse(loadingImageCache);
        mApiService.queryLoadingImage(Constants.Request.LOADING_HOST).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<LoadingBean.LoadingImageBean>() {

            @Override
            public void onNext(LoadingBean.LoadingImageBean loadingImageBean) {
                dealSplashResponse(JsonUtils.toJson(loadingImageBean));
            }
        });
    }

    /**
     * 启动页背景结果处理
     * <p>
     * 相关方法
     * {@link com.hongguo.read.mvp.ui.loading.LoadingActivity#setLoadingImageUrl}
     *
     * @param loadingImageCache
     */
    private void dealSplashResponse(String loadingImageCache) {
        if (TextUtils.isEmpty(loadingImageCache)) {
            return;
        }
        LoadingBean.LoadingImageBean loadingImageBean = JsonUtils.fromJson(loadingImageCache, LoadingBean.LoadingImageBean.class);
        mView.setLoadingImageUrl(loadingImageBean.pic);
        //缓存当前结果
        CacheRepertory.setLoadingImage(loadingImageCache);
    }

    /**
     * 获取熊猫看书html内容的正则表达式
     */
    private void queryPattern() {
        mApiService.queryBaiduFreePattern().compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<BookPattern>() {

            @Override
            public void onNext(BookPattern bookPattern) {
                super.onNext(bookPattern);
                bookPattern.update();
            }
        });
    }


    /**
     * 获取书本信息
     */
    private void queryBookInfo() {
        //百度书籍查询百度数据,自己书籍查询自己的书籍
        if (mSingleType == Constants.BOOK_FROM.FROM_BAIDU) {
            queryBookInfoFromBaiDu();
        } else {
            queryBookInfoFromSelf();
        }
    }

    private void queryBookInfoFromBaiDu() {
        mApiService.queryBaiduDetail(BaiduShuChengUrls.getBaiDuBookDetailUrl(mSingleBookId)).compose(RxJavaResponseDeal.create(this).withLoading(true).loadingTag(1).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<BaiduDetailBean>() {
            @Override
            public void failure(Throwable e) {
                mBookInfoStatus = LoadingStatus.LOADING_FAILURE;
                jumpActivity();
            }

            @Override
            public void netError() {
                super.netError();
                failure(null);
            }

            @Override
            public void success(BaiduDetailBean baiduBean) {
                dealCommonInfoConvert(baiduBean);
            }
        }));
    }

    private void queryBookInfoFromSelf() {
        mApiService.queryBookDetail(mSingleBookId).compose(RxJavaResponseDeal.create(this).withLoading(true).loadingTag(1).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<BookDetailBean>() {

            @Override
            public void failure(Throwable e) {
                mBookInfoStatus = LoadingStatus.LOADING_FAILURE;
                jumpActivity();
            }

            @Override
            public void netError() {
                super.netError();
                mBookInfoStatus = LoadingStatus.LOADING_FAILURE;
                jumpActivity();
            }

            @Override
            public void success(BookDetailBean bookDetail) {
                bookDetail.data.readersStr = BookNumberUtils.formartUserReads(bookDetail.data.readers);
                bookDetail.data.wordsNumber = BookNumberUtils.formartWords(bookDetail.data.words);
                bookDetail.data.statusStr = bookDetail.data.status == 0 ? "状态：连载" : "状态：完结";
                commonDealBookInfo(bookDetail);
            }
        }));
    }

    private void dealCommonInfoConvert(BaiduDetailBean baiduBean) {
        BookDetailBean bookDetailBean = new BookDetailBean();
        BeanConvert.convertBean(baiduBean, bookDetailBean);

        if (bookDetailBean.data.wordsNumber.contains(".")) {
            String[] split = bookDetailBean.data.wordsNumber.split("\\.");
            bookDetailBean.data.wordsNumber = (split[0] + "." + split[1].charAt(0) + "万字");
            //百度书籍没有阅读人数，用字数充
            bookDetailBean.data.readersStr = BookNumberUtils.formartUserReads(Integer.parseInt(split[0]) * 753);
        } else {
            bookDetailBean.data.readersStr = BookNumberUtils.formartUserReads(Integer.parseInt(bookDetailBean.data.wordsNumber) * 753);
            bookDetailBean.data.wordsNumber = bookDetailBean.data.wordsNumber + "万字";
        }
        bookDetailBean.data.statusStr = bookDetailBean.data.status == 0 ? "状态：连载" : "状态：完结";

        commonDealBookInfo(bookDetailBean);
    }

    private void commonDealBookInfo(BookDetailBean bookDetailBean) {
        mBookDetailBean = bookDetailBean;
        mBookInfoStatus = LoadingStatus.LOADING_SUCCESS;
        jumpActivity();
    }

    /**
     * 相关方法
     * {@link com.hongguo.read.mvp.ui.loading.LoadingActivity#toBookReader}
     */
    private void jumpActivity() {
        //只有登录了，才能进入app
        if (mLoginStatus != LoadingStatus.LOADING_SUCCESS) {
            return;
        }

        //是单推包(书本信息没加载完成)
        if (mSingleFlag && mBookInfoStatus == LoadingStatus.LOADING_LOADING) {
            return;
        }

        if (mSingleFlag && mBookDetailBean != null) {
            mView.toBookReader(mSingleBookId, mSingleType, mBookDetailBean.data.bookName, mBookDetailBean.data.coverPicture, mBookDetailBean.data.author);
            AppRepertory.setIsFirstLoading(false);
        } else {
            delayJump();
        }
    }

    /**
     * 处理跳转
     * <p>
     * 相关方法
     * {@link com.hongguo.read.mvp.ui.loading.LoadingActivity#setCurrentTime}
     */
    public void delayJump() {
        mView.setDelayVisiable();
        Disposable disposable = RxJavaUtils.timeDown(DELAY_TIME, currentTime -> {
            if (currentTime == 0) {
                mView.toMain();
                AppRepertory.setIsFirstLoading(false);
            } else {
                mView.setCurrentTime(currentTime);
            }
        });
        addSubscriptions(disposable);
    }


    /**
     * 是否是单推包
     */
    private boolean isSingleBook() {
        //从包数据中  获取 单推包ID
        String singleID = MetaDataUtils.getMeta(mContext, "SINGLE_BOOK_ID");
        singleID = singleID.substring(1, singleID.length());

        //从包数据中  获取 单推包数据的类型，百度的还是其他的
        String singleType = MetaDataUtils.getMeta(mContext, "SINGLE_BOOK_TYPE");
        if (!TextUtils.isEmpty(singleID) && "0".equals(singleID)) {
            singleID = "";
        }

        //如果不为空，则表明此包为单推包的包，需要加载单推包数据
        if (!TextUtils.isEmpty(singleID) && AppRepertory.issFirstLoading()) {
            mSingleBookId = singleID;
            mSingleType = Integer.parseInt(singleType);
            return true;
        }

        //非单推包,不进行查询操作
        if (TextUtils.isEmpty(mSingleBookId)) {
            AppRepertory.setIsFirstLoading(false);
        }

        return false;
    }

    private void queryBaiduTimeOut() {
        if (AppRepertory.getBaiduTimeout()) return;

        mApiService.getTopicInfo(CmsTopicInfo.BAIDU_OUT).compose(CmsTopicDeal.cmsTopDeal(BaiDuOut.class)).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<BaiDuOut>() {
            @Override
            public void onNext(BaiDuOut baiDuOut) {
                super.onNext(baiDuOut);
                if (baiDuOut.baiduOut) {
                    EventBus.getDefault().post(new BaiDuTimeOut());
                    AppRepertory.setBaiduTimeout();
                }
            }
        });

    }
}