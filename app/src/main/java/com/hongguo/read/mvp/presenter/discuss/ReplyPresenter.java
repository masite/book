package com.hongguo.read.mvp.presenter.discuss;

import android.text.TextUtils;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.base.CommonBean;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.eventbus.WriteReplyEvent;
import com.hongguo.read.mvp.contractor.discuss.ReplyContractor;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.base.BaseView;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

public class ReplyPresenter extends BaseImpPresenter<ReplyContractor.IView> implements ReplyContractor.IPresenter {

    private String          mBookid;
    private String          mDiscussId;
    private BookDiscussBean mBookDiscussBean;

    @Inject
    public ReplyPresenter(@ApiLife ApiService apiService) {
        super(apiService);
        mBookDiscussBean = new BookDiscussBean();
        mBookDiscussBean.data = new ArrayList<>();
    }

    @Override
    public void loading() {
    }

    @PresenterMethod
    public void queryReply(String bookid, String discussId) {
        mBookid = bookid;
        mDiscussId = discussId;
        mApiService.discussReply(bookid, discussId, mCurrentPage, CommonBean.PAGE_SIZE).compose(RxJavaResponseDeal.create(this).loadingTag(0).commonDeal(reply -> {
            if (mCurrentPage == 1) {
                mBookDiscussBean.data.clear();
            }
            mBookDiscussBean.data.addAll(reply.data);
            if (mCurrentPage == 1 && reply.data.size() == 0) {
                mView.changeLoadingStatus(BaLoadingView.LoadingStatus.RESULT_NULL, 0);
            }
            if (!reply.hasMore()) {
                mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
            }
            mView.setBookDiscussInfo(mBookDiscussBean);
        }));
    }

    @Override
    public void refresh() {
        super.refresh();
        queryReply(mBookid, mDiscussId);
    }

    @Override
    public void loadingMore() {
        super.loadingMore();
        queryReply(mBookid, mDiscussId);
    }

    @Override
    public void reLoad() {
        super.reLoad();
        queryReply(mBookid, mDiscussId);
    }

    @PresenterMethod
    public void reply(String bookid, String discussId, String content) {
        if (TextUtils.isEmpty(content)) {
            mView.toastMessage("评论内容不能未空");
            return;
        }
        mApiService.writeDiscussForPerson(bookid, discussId, UserRepertory.getUserName(), UserRepertory.getUserAvatar(), content, content).compose(RxJavaResponseDeal.create(this).widthDialog("正在提交").commonDeal(response -> {
            if (response.code == 0) {
                mView.toastMessage("评论成功");
                EventBus.getDefault().post(new WriteReplyEvent());
                mView.repaySuccess();
                refresh();
            } else {
                mView.toastMessage("出现未知错误,评论失败");
            }
        }));
    }


}