package com.hongguo.read.mvp.presenter.bookstore.channel;

import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.R;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.eventbus.BookStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.TypeChannelContractor;
import com.hongguo.read.repertory.data.ClassifyRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TypeChannelPresenter extends BaseImpPresenter<TypeChannelContractor.IView> implements TypeChannelContractor.IPresenter {


    @Inject
    public TypeChannelPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        queryBookType();
    }

    private void queryBookType() {
        mView.changeLoadingStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS, 0);
        EventBus.getDefault().post(new BookStoreRefreshSuccessEvent());
        List<ClassifyRepertory.Classify> classifies = new ArrayList<>();
        classifies.addAll(ClassifyRepertory.getClassifies());
        classifies.add(new ClassifyRepertory.Classify("status-s0", "热门连载", R.mipmap.ic_classify_rmlz));
        classifies.add(new ClassifyRepertory.Classify("status-s1", "经典完结", R.mipmap.ic_classify_jdwj));
        classifies.add(new ClassifyRepertory.Classify("sort-s0", "每日更新", R.mipmap.ic_classify_mrgx));
        classifies.add(new ClassifyRepertory.Classify("sort-s1", "畅销经典", R.mipmap.ic_classify_cxjd));

        mView.setBookType(classifies);
    }
}