package com.hongguo.common.utils.rxjava;

import android.util.Log;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.read.base.BuildConfig;
import com.losg.library.base.BaseView;
import com.losg.library.widget.dialog.ProgressDialog;
import com.losg.library.widget.loading.BaLoadingView;

import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;


/**
 * Created by losg on 2017/7/26.
 */

public class RxJavaResponseDeal implements ProgressDialog.DialogForceCloseListener {

    private BasePresenter mBasePresenter;

    //使用loading 加载
    private boolean mWithLoading = false;

    //是否有对话框
    private boolean mWithDialog = false;

    //recycler 形式
    private boolean mWithRefresh = false;

    //dialog退出无动画，防止退出动画异常
    private boolean mCloseWithoutAnim = false;

    //View对应的tag
    private int mTag;

    private Disposable mSubscribe;

    public static RxJavaResponseDeal create(BasePresenter basePresenter) {
        return new RxJavaResponseDeal(basePresenter);
    }

    private RxJavaResponseDeal(BasePresenter basePresenter) {
        mBasePresenter = basePresenter;
        mWithLoading = presenterIsFirstLoading(basePresenter);
    }

    public RxJavaResponseDeal withLoading(boolean withLoading) {
        mWithLoading = withLoading;
        return this;
    }

    public RxJavaResponseDeal widthDialog(String dialogMessage) {
        return widthDialog(dialogMessage, false);
    }

    public RxJavaResponseDeal widthDialog(String dialogMessage, boolean closeWithoutAnim) {
        mWithDialog = true;
        mCloseWithoutAnim = closeWithoutAnim;
        mWithLoading = false;
        mBasePresenter.getView().showWaitDialog(dialogMessage, this);
        return this;
    }

    public RxJavaResponseDeal loadingTag(int tag) {
        mTag = tag;
        return this;
    }

    public RxJavaResponseDeal withRefresh(boolean withRefresh) {
        mWithRefresh = withRefresh;
        return this;
    }

    public <T> ObservableTransformer<T, Boolean> commonDeal(ResponseListener<T> response) {
        return commonDeal(response, true);
    }

    /**
     * 信息预处理
     */
    public <T> ObservableTransformer<T, Boolean> commonDeal(ResponseListener<T> response, boolean autoRefresh) {
        if (mBasePresenter.getView() == null) {
            Log.e("losg_log", "view can not null");
            return null;
        }
        //使用loading 加载 修改loading 状态
        if (mWithLoading) {
            mBasePresenter.getView().changeLoadingStatus(BaLoadingView.LoadingStatus.LOADING, mTag);
        }

        return o -> o.compose((ObservableTransformer<T, Boolean>) upstream -> {
            upstream.compose(RxJavaUtils.androidTranformer()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {

                @Override
                public void onSubscribe(Disposable d) {
                    mBasePresenter.addSubscriptions(d);
                }

                @Override
                public void onNext(T t) {
                    dealResponse(t, response, autoRefresh);
                }

                @Override
                public void onError(Throwable e) {
                    dealError(e, response);
                }

                @Override
                public void onComplete() {

                }
            });
            return Observable.just(true);
        });
    }

    private <T> void dealResponse(T t, ResponseListener<T> response, boolean autoRefresh) {
        //关闭等待对话框
        if (mWithDialog) {
            if (mCloseWithoutAnim) {
                mBasePresenter.getView().dismissWaitDialogWithoutAnim();
            } else {
                mBasePresenter.getView().dismissWaitDialog();
            }
        }
        //解析出错，服务器返回数据有问题
        if (t == null) {
            if (mWithLoading) {
                mBasePresenter.getView().changeLoadingStatus(BaLoadingView.LoadingStatus.SERVER_ERROR, mTag);
            } else {
                if (mWithRefresh) {
                    mBasePresenter.getView().refreshStatus(BaseView.RefreshStatus.Failure, BaseView.ErrorStatus.ERROR_SERVICE);
                }
            }
            return;
        }


        //加载成功,处理其它操作
        if (mWithLoading)
            mBasePresenter.getView().changeLoadingStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS, mTag);

        //修改不是首次
        setFirstLoading(mBasePresenter, false);

        if (autoRefresh)
            mBasePresenter.getView().refreshStatus(BaseView.RefreshStatus.REFRESH_SUCCESS, null);
        if (response != null) {
            response.success(t);
        }
    }

    private <T> void dealError(Throwable e, ResponseListener<T> response) {

        //关闭弹窗
        if (mWithDialog) {
            mBasePresenter.getView().dismissWaitDialog();
        }

        //json 解析失败
        if (e instanceof com.google.gson.JsonSyntaxException) {
            Log.e("losg_log", "json error");
            if (BuildConfig.DEBUG)
                mBasePresenter.getView().toastError(BaseView.ErrorStatus.ERROR_DATA);
            if (response instanceof ResponseWithErrorListener) {
                ((ResponseWithErrorListener) response).failure(e);
            }
            //修改刷新 问题
            if (mWithRefresh) {
                mBasePresenter.getView().refreshStatus(BaseView.RefreshStatus.Failure, BaseView.ErrorStatus.ERROR_SERVICE);
                return;
            }
            //使用loading则显示错误信息
            if (mWithLoading)
                mBasePresenter.getView().changeLoadingStatus(BaLoadingView.LoadingStatus.SERVER_ERROR, mTag);
            else if (BuildConfig.DEBUG)
                mBasePresenter.getView().toastError(BaseView.ErrorStatus.ERROR_SERVICE);
            return;
        }


        //网络连接失败
        if (e instanceof ConnectException || e instanceof UnknownHostException) {
            Log.e("losg_log", "ConnectException");
            if (response instanceof ResponseWithErrorListener) {
                ((ResponseWithErrorListener) response).netError();
            }
            //dialog提醒
            if (mWithDialog) {
                mBasePresenter.getView().toastError(BaseView.ErrorStatus.ERROT_NET_ERROR);
                return;
            }
            //列表加载
            if (mWithRefresh) {
                mBasePresenter.getView().refreshStatus(BaseView.RefreshStatus.Failure, BaseView.ErrorStatus.ERROT_NET_ERROR);
                return;
            }
            //首次加载
            if (mWithLoading)
                mBasePresenter.getView().changeLoadingStatus(BaLoadingView.LoadingStatus.NET_ERROR, mTag);
            else
                mBasePresenter.getView().toastError(BaseView.ErrorStatus.ERROT_NET_ERROR);

            //连接服务器超时
        } else if (e instanceof SocketTimeoutException) {
            Log.e("losg_log", "SocketTimeoutException");
            if (response instanceof ResponseWithErrorListener) {
                ((ResponseWithErrorListener) response).netError();
            }
            if (mWithDialog) {
                mBasePresenter.getView().toastError(BaseView.ErrorStatus.ERROR_CONNECT_TIME_OUT);
                return;
            }
            if (mWithRefresh) {
                mBasePresenter.getView().refreshStatus(BaseView.RefreshStatus.Failure, BaseView.ErrorStatus.ERROR_CONNECT_TIME_OUT);
                return;
            }
            if (mWithLoading)
                mBasePresenter.getView().changeLoadingStatus(BaLoadingView.LoadingStatus.CONNECT_TIMEOUT, mTag);
            else
                mBasePresenter.getView().toastError(BaseView.ErrorStatus.ERROR_CONNECT_TIME_OUT);

        } else if (e instanceof HttpException) {
            if (response instanceof ResponseWithErrorListener) {
                ((ResponseWithErrorListener) response).netError();
            }
            if (mWithDialog) {
                mBasePresenter.getView().toastError(BaseView.ErrorStatus.ERROR_SERVICE);
                return;
            }
            if (mWithRefresh) {
                mBasePresenter.getView().refreshStatus(BaseView.RefreshStatus.Failure, BaseView.ErrorStatus.ERROR_SERVICE);
                return;
            }
            if (mWithLoading)
                mBasePresenter.getView().changeLoadingStatus(BaLoadingView.LoadingStatus.SERVER_ERROR, mTag);
            else
                mBasePresenter.getView().toastError(BaseView.ErrorStatus.ERROR_SERVICE);
        } else {
            if (response instanceof ResponseWithErrorListener) {
                ((ResponseWithErrorListener) response).failure(e);
            }
        }
    }

    @Override
    public void forceClose() {
        mSubscribe.dispose();
    }

    public interface ResponseListener<T> {
        void success(T t);
    }

    public abstract static class ResponseWithErrorListener<T> implements ResponseListener<T> {
        public abstract void failure(Throwable e);

        public void netError() {
        }
    }

    private boolean presenterIsFirstLoading(BasePresenter basePresenter) {
        try {
            Field mFirstLoading = basePresenter.getClass().getField("mFirstLoading");
            mFirstLoading.setAccessible(true);
            return (boolean) mFirstLoading.get(basePresenter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setFirstLoading(BasePresenter basePresenter, boolean first) {
        try {
            Field mFirstLoading = basePresenter.getClass().getField("mFirstLoading");
            mFirstLoading.setAccessible(true);
            mFirstLoading.setBoolean(basePresenter, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
