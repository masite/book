package com.hongguo.read.mvp.presenter.mine.mypackage;

import android.text.TextUtils;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.SellType;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.mine.mypackage.ConsumeContractor;
import com.hongguo.read.mvp.model.mine.mypackage.ConsumeBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.base.BaseView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.Observable;

public class ConsumePresenter extends BaseImpPresenter<ConsumeContractor.IView> implements ConsumeContractor.IPresenter {

    private ConsumeBean mConsumeBean;

    @Inject
    public ConsumePresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        mConsumeBean = new ConsumeBean();
        mConsumeBean.mData = new ArrayList<>();
        queryUserConsume();
    }

    @PresenterMethod
    public void queryUserConsume() {

        mApiService.queryUserConsumeHistory(SellType.DEFAULT, mCurrentPage, PAGE_SIZE).flatMap((Function<ConsumeBean.SellListBean, ObservableSource<ConsumeBean>>) sellListBean -> {

            if(mCurrentPage == 1) {
                mConsumeBean.mData.clear();
                mConsumeBean.mSize = 0;
                mConsumeBean.mTotalSize = 0;
            }

            mConsumeBean.mSize += sellListBean.data.size();
            mConsumeBean.mTotalSize = sellListBean.pager.total;

            for (ConsumeBean.SellListBean.DataBean datum : sellListBean.data) {
                datum.masterName = TextUtils.isEmpty(datum.masterName) ? datum.sellName : datum.masterName;

                if (datum.sellType == SellType.REWARD || datum.sellType == SellType.REWARD_BOOK || datum.sellType == SellType.REWARD_CHAPTER) {
                    datum.sellAmount = "打赏 " + datum.sellAmount;
                } else if (datum.sellType == SellType.REWARD_SYSTEM_RETURN) {
                    datum.sellAmount = "系统退还 " + datum.sellAmount;
                } else {
                    datum.sellAmount = "消费 " + datum.sellAmount;
                }

                int index = queryHasBookHistory(datum.masterName);

                if (index != -1) {
                    mConsumeBean.mData.get(index).mDataBeans.add(datum);
                } else {
                    ConsumeBean.DataBean dataBean = new ConsumeBean.DataBean();
                    dataBean.title = datum.masterName;
                    dataBean.mDataBeans = new ArrayList<>();
                    dataBean.mDataBeans.add(datum);
                    mConsumeBean.mData.add(dataBean);
                }
            }
            return Observable.just(mConsumeBean);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(consume -> {
            if (mCurrentPage == 1) {
                if(consume.mData.size() == 0) {
                    mView.changeLoadingStatus(BaLoadingView.LoadingStatus.RESULT_NULL, 0);
                    return;
                }
            }
            if(consume.mSize >= consume.mTotalSize){
                mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
            }
            mView.setItems(consume.mData);
        }));
    }


    @Override
    public void reLoad() {
        super.reLoad();
        queryUserConsume();
    }

    @Override
    public void loadingMore() {
        super.loadingMore();
        queryUserConsume();
    }

    @Override
    public void refresh() {
        super.refresh();
        mConsumeBean.mData.clear();
        queryUserConsume();
    }

    private int queryHasBookHistory(String bookName) {
        for (int i = 0; i < mConsumeBean.mData.size(); i++) {
            ConsumeBean.DataBean datum = mConsumeBean.mData.get(i);
            if (datum.title.equals(bookName)) return i;
        }
        return -1;
    }

}