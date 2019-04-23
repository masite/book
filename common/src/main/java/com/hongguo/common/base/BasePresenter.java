package com.hongguo.common.base;

import com.losg.library.base.BaseView;

import io.reactivex.disposables.Disposable;
import io.reactivex.Observable;

/**
 * Created by losg on 2016/10/28.
 */

public interface BasePresenter {

    void bingView(BaseView view);

    BaseView getView();

    void loading();

    Observable<?> relogin();

    void unBindView();

    void addSubscriptions(Disposable subscription);

    void permissionSuccess();

    void permissionFailure();

    /*********针对刷新*************/
    void refresh();

    void loadingMore();

    void reLoad();
}
