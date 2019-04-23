package com.hongguo.common.utils.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by losg on 2016/11/30.
 */

public class RxJavaUtils {

    public static Disposable timeDown(int downTime, TimeDownListener timeDownListener) {
        Disposable subscribe = Observable.interval(0, 1, TimeUnit.SECONDS).take(downTime + 1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {

            @Override
            public void accept(Long aLong) throws Exception {
                if (timeDownListener != null) {
                    timeDownListener.down((int) (downTime - aLong));
                }
            }
        });
        return subscribe;
    }

    public static <T> ObservableTransformer<T, T> androidTranformer() {
        return o -> o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public interface TimeDownListener {
        void down(int currnetTime);
    }

    public static void backgroundDeal(BackgroundDealListener backgroundDealListener) {
        Observable.just(true).flatMap(new Function<Boolean, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Boolean aBoolean) throws Exception {
                backgroundDealListener.run();
                return Observable.just(aBoolean);
            }
        }).compose(RxJavaUtils.androidTranformer()).subscribe(aBoolean -> {
            backgroundDealListener.runSuccess();
        });
    }


    public static Disposable delayRun(int time, DelayRunUIListener delayRunUIListener) {
        return Observable.just(true).delay(time, TimeUnit.MILLISECONDS).compose(RxJavaUtils.androidTranformer()).subscribe(aBoolean -> {
            delayRunUIListener.run();
        });
    }

    public interface DelayRunUIListener {

        void run();
    }

    public static abstract class BackgroundDealListener {

        public abstract void run();

        public void runSuccess() {
        }

    }

}
