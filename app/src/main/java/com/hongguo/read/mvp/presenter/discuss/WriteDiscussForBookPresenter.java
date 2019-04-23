package com.hongguo.read.mvp.presenter.discuss;

import android.text.TextUtils;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.utils.StringUtil;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.eventbus.WriteReplyEvent;
import com.hongguo.read.mvp.contractor.discuss.WriteDiscussForBookContractor;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class WriteDiscussForBookPresenter extends BaseImpPresenter<WriteDiscussForBookContractor.IView> implements WriteDiscussForBookContractor.IPresenter {

    @Inject
    public WriteDiscussForBookPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
    }

    @PresenterMethod
    public void sumbit(String bookId, String bookName, int bookType, String title, String content) {
        String userName = UserRepertory.getUserName();
        String userAvatar = UserRepertory.getUserAvatar();
        if (TextUtils.isEmpty(title)) {
            mView.toastMessage("请填写评论内容哦~");
            return;
        }
        mApiService.writeDiscuss(bookId, bookType, bookName, userName, userAvatar, StringUtil.String2Unicode(title), StringUtil.String2Unicode(content)).compose(RxJavaResponseDeal.create(this).widthDialog("正在提交", true
        ).commonDeal(result -> {
            EventBus.getDefault().post(new WriteReplyEvent());
            mView.toastMessage("发布成功");
            mView.finishView();
        }));
    }
}