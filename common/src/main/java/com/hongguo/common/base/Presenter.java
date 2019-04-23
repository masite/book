package com.hongguo.common.base;

import android.text.TextUtils;

import com.losg.library.base.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.Observable;

/**
 * Created by losg on 2016/10/29.
 */

public abstract class Presenter<T extends BaseViewEx> implements BasePresenter {

    protected static final int PAGE_SIZE    = 20;
    protected              int mCurrentPage = 1;

    protected T                   mView;
    protected CompositeDisposable mSubscriptions;

    public boolean mFirstLoading = true;

    public Presenter() {
        mSubscriptions = new CompositeDisposable();
    }

    @Override
    public void bingView(BaseView view) {
        mView = (T) view;
        mView.setPresener(this);
    }

    @Override
    public BaseView getView() {
        return mView;
    }

    @Override
    public Observable<?> relogin() {

        return null;
    }

    public void addSubscriptions(Disposable subscription) {
        mSubscriptions.add(subscription);
    }

    protected boolean textEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    @Override
    public void unBindView() {
        mSubscriptions.clear();
    }

    @Override
    public void permissionSuccess() {

    }

    @Override
    public void permissionFailure() {

    }

    @Override
    public void refresh() {
        mCurrentPage = 1;
    }

    @Override
    public void loadingMore() {
        mCurrentPage++;
    }

    @Override
    public void reLoad() {
    }

}
