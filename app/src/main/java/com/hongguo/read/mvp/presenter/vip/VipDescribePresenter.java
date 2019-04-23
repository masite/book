package com.hongguo.read.mvp.presenter.vip;

import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.CmsTopicInfo;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.vip.VipDescribeContractor;
import com.hongguo.read.mvp.model.vip.SVIPDescribeBean;
import com.hongguo.read.mvp.model.vip.SVIPShareBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.common.utils.rxjava.topic.CmsTopicDeal;

import javax.inject.Inject;

public class VipDescribePresenter extends BaseImpPresenter<VipDescribeContractor.IView> implements VipDescribeContractor.IPresenter{

	@Inject
	public VipDescribePresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
		queryHeader();
		queryShare();
		querySvipBooks();
	}

	private void queryHeader() {

		mApiService.getTopicInfo(CmsTopicInfo.SVIP_DESCRIBE).compose(CmsTopicDeal.cmsTopDeal(SVIPDescribeBean.class)).compose(RxJavaResponseDeal.create(this).commonDeal(describe->{
			mView.setSvipDesBeans(describe.svipDes);
		}));
	}

	private void queryShare() {
		mApiService.getTopicInfo(CmsTopicInfo.SVIP_SHARE).compose(CmsTopicDeal.cmsTopDeal(SVIPShareBean.class)).compose(RxJavaResponseDeal.create(this).commonDeal(shareInfo->{
			mView.setShareInfo(shareInfo);
		}));
	}

	private void querySvipBooks() {
		mApiService.queryVipBooks(Constants.BOOK_FREE_TYPE.BOOK_SVIP_LIMIT_FREE, mCurrentPage, 12).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
			mView.setSvipBooks(books.data);
		}));
	}

}