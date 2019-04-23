package com.hongguo.common.utils.rxjava;

import android.text.TextUtils;

import com.hongguo.common.base.BasePresenter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created time 2017/11/30.
 *
 * @author losg
 */

public class SubscriberImp<T> implements Observer<T> {

    private BasePresenter mBasePresenter;

    public SubscriberImp() {
    }

    public SubscriberImp(BasePresenter basePresenter) {
        mBasePresenter = basePresenter;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mBasePresenter != null) {
            mBasePresenter.addSubscriptions(d);
        }
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        //javax2 不允许 oNext为空，但是在有些场景下，我们需要onNext未空的情况,默认继续调用onNext(null)
        if (!TextUtils.isEmpty(e.toString()) && e.toString().contains("onNext called with null")) {
            onNext(null);
        }
    }

    @Override
    public void onComplete() {

    }
}
