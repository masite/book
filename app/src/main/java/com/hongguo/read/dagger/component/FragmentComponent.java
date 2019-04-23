package com.hongguo.read.dagger.component;

import com.hongguo.read.mvp.ui.bookstore.channel.SortChannelFragment;
import com.hongguo.common.dagger.scope.ContextLife;
import com.hongguo.common.dagger.scope.FragmentScope;
import com.hongguo.read.mvp.ui.book.detail.BookReaderChapterFragment;
import com.hongguo.read.mvp.ui.vip.NormalVipStoreFragment;
import com.hongguo.read.mvp.ui.vip.SuperVipStoreFragment;
import com.hongguo.read.mvp.ui.mine.MineFragment;
import com.hongguo.read.mvp.ui.vip.VipFragment;
import com.hongguo.read.mvp.ui.bookshelf.BookShelfFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.TypeChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.FreeChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.BoysChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.GirlsChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.channel.RecommendChannelFragment;
import com.hongguo.read.mvp.ui.bookstore.BookStoreFragment;

import android.content.Context;

import com.hongguo.read.dagger.module.FragmentModule;


import dagger.Component;

/**
 * Created by losg on 2016/11/1.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    @ContextLife(value = ContextLife.Life.Application)
    Context getApplication();
	void inject(BookStoreFragment mBookStoreFragment);
	void inject(RecommendChannelFragment mRecommendChannelFragment);
	void inject(GirlsChannelFragment mGirlsChannelFragment);
	void inject(BoysChannelFragment mBoysChannelFragment);
	void inject(FreeChannelFragment mFreeChannelFragment);
	void inject(TypeChannelFragment mTypeChannelFragment);
	void inject(BookShelfFragment mBookShelfFragment);
	void inject(VipFragment mVipFragment);
	void inject(MineFragment mMineFragment);
	void inject(SuperVipStoreFragment mSuperVipStoreFragment);
	void inject(NormalVipStoreFragment mNormalVipStoreFragment);
	void inject(BookReaderChapterFragment mBookReaderChapterFragment);
	void inject(SortChannelFragment mSortChannelFragment);
}