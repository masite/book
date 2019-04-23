package com.hongguo.read.dagger.component;

import com.hongguo.read.mvp.ui.bookstore.channel.SortDetailActivity;
import com.hongguo.read.mvp.ui.book.BookEndRecommendActivity;
import android.content.Context;

import com.hongguo.common.dagger.scope.ActivityScope;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.read.dagger.module.ActivityModule;

import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.mvp.ui.book.BookReaderActivity;
import com.hongguo.read.mvp.ui.book.chapter.ChapterActivity;
import com.hongguo.read.mvp.ui.book.chapter.DownChapterActivity;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.read.mvp.ui.booktype.BookTypeDetailActivity;
import com.hongguo.read.mvp.ui.booktype.BookTypeWithoutSelectActivity;
import com.hongguo.read.mvp.ui.discuss.AllDiscussActivity;
import com.hongguo.read.mvp.ui.discuss.ReplyActivity;
import com.hongguo.read.mvp.ui.discuss.WriteDiscussForBookActivity;
import com.hongguo.read.mvp.ui.loading.LoadingActivity;
import com.hongguo.read.mvp.ui.mine.AuthorLoginActivity;
import com.hongguo.read.mvp.ui.mine.BindAccountActivity;
import com.hongguo.read.mvp.ui.mine.MyPackageActivity;
import com.hongguo.read.mvp.ui.mine.QuestionActivity;
import com.hongguo.read.mvp.ui.mine.RewardActivity;
import com.hongguo.read.mvp.ui.mine.SettingActivity;
import com.hongguo.read.mvp.ui.mine.SkinActivity;
import com.hongguo.read.mvp.ui.mine.center.UserCenterActivity;
import com.hongguo.read.mvp.ui.mine.mypackage.ConsumeActivity;
import com.hongguo.read.mvp.ui.mine.mypackage.FeedBackActivity;
import com.hongguo.read.mvp.ui.mine.mypackage.RechargeHistoryActivity;
import com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity;
import com.hongguo.read.mvp.ui.reward.RewardTopActivity;
import com.hongguo.read.mvp.ui.reward.UserRewardActivity;
import com.hongguo.read.mvp.ui.search.SearchActivity;
import com.hongguo.read.mvp.ui.topic.TopicActivity;
import com.hongguo.read.mvp.ui.topic.TopicDetailActivity;
import com.hongguo.read.mvp.ui.vip.VipDescribeActivity;

import dagger.Component;

/**
 * Created by losg on 2016/10/28.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {

    @ContextLife(value = ContextLife.Life.Activity)
    Context getActivityContext();

    void inject(LoadingActivity mLoadingActivity);

    void inject(MainActivity mMainActivity);

    void inject(BookDetailActivity mBookDetailActivity);

    void inject(AllDiscussActivity mAllDiscussActivity);

    void inject(WriteDiscussForBookActivity mWriteDiscussForBookActivity);

    void inject(MyPackageActivity mMyPackageActivity);

    void inject(RechargeHistoryActivity mRechargeHistoryActivity);

    void inject(ConsumeActivity mConsumeActivity);

    void inject(RechargeActivity mRechargeActivity);

    void inject(RewardActivity mRewardActivity);

    void inject(QuestionActivity mQuestionActivity);

    void inject(FeedBackActivity mFeedBackActivity);

    void inject(SettingActivity mSettingActivity);

    void inject(SkinActivity mSkinActivity);

    void inject(UserCenterActivity mUserCenterActivity);

    void inject(BindAccountActivity mBindAccountActivity);

    void inject(AuthorLoginActivity mLoginActivity);

    void inject(SearchActivity mSearchActivity);

    void inject(TopicActivity mTopicActivity);

    void inject(TopicDetailActivity mTopicDetailActivity);

    void inject(BookTypeDetailActivity mBookTypeDetailActivity);

    void inject(BookTypeWithoutSelectActivity mBookTypeWithoutSelectActivity);

    void inject(ReplyActivity mReplyActivity);

    void inject(RewardTopActivity mRewardTopActivity);

    void inject(ChapterActivity mChapterActivity);

    void inject(DownChapterActivity mDownChapterActivity);

    void inject(BookReaderActivity mBookReaderActivity);
	void inject(VipDescribeActivity mVipDescribeActivity);
	void inject(UserRewardActivity mUserRewardActivity);
	void inject(BookEndRecommendActivity mBookEndRecommendActivity);
	void inject(SortDetailActivity mSortDetailActivity);
}