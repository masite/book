package com.hongguo.read.mvp.ui.reward;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongguo.common.utils.ScrollTransHelper;
import com.hongguo.common.widget.ScrollViewEx;
import com.hongguo.common.widget.recycler.GridCell;
import com.hongguo.read.R;
import com.hongguo.read.adapter.UserRewardAdapter;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.read.base.MineInfoActivity;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.constants.RankType;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.eventbus.PayCheckEvent;
import com.hongguo.read.mvp.contractor.mine.recharge.RechargeContractor;
import com.hongguo.read.mvp.contractor.reward.UserRewardContractor;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;
import com.hongguo.read.mvp.presenter.mine.MinePresenter;
import com.hongguo.read.mvp.presenter.mine.recharge.RechargePresenter;
import com.hongguo.read.mvp.presenter.reward.UserRewardPresenter;
import com.hongguo.read.mvp.ui.mine.mypackage.FeedBackActivity;
import com.hongguo.read.mvp.ui.mine.recharge.PayUtils;
import com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity;
import com.hongguo.read.repertory.data.RewardRepertory;
import com.hongguo.common.utils.MathTypeParseUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.widget.CommonImageView;
import com.hongguo.read.widget.recharge.RechargeConfirmDialog;
import com.hongguo.read.widget.recharge.RechargeRequeryDialog;
import com.hongguo.read.widget.reward.RewardDialog;
import com.losg.library.utils.DisplayUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/2/8.
 */

public class UserRewardActivity extends MineInfoActivity implements UserRewardContractor.IView, RewardDialog.RewardPayTypeClickListener, RechargeConfirmDialog.RechargeConfirmListener, RechargeRequeryDialog.RechargeRequeryListener, RechargeContractor.IView, ScrollTransHelper.ScrollTransProgressListener {

    private static final String INTENT_BOOK_COVER = "intent_book_cover";
    private static final String INTENT_BOOK_NAME  = "intent_book_name";
    private static final String INTENT_BOOK_ID    = "intent_book_id";
    private static final String INTENT_BOOK_TYPE  = "intent_book_type";

    @Inject
    UserRewardPresenter mUserRewardPresenter;
    @Inject
    MinePresenter       mMinePresenter;

    @Inject
    RechargePresenter mRechargePresenter;

    @BindView(R.id.book_cover)
    CommonImageView  mBookCover;
    @BindView(R.id.reward_list)
    RecyclerView     mRewardList;
    @BindView(R.id.cover_gradient)
    ImageView        mCoverGradient;
    @BindView(R.id.reward_money)
    TextView         mRewardMoney;
    @BindView(R.id.reward_coin)
    TextView         mRewardCoin;
    @BindView(R.id.reward_submit)
    TextView         mRewardSubmit;
    @BindView(R.id.recharge_now)
    TextView         mRechargeNow;
    @BindView(R.id.refresh_coin)
    TextView         mRefreshCoin;
    @BindView(R.id.user_left_coin)
    TextView         mUserLeftCoin;
    @BindView(R.id.content_scroll)
    ScrollViewEx     mScrollViewEx;
    @BindView(R.id.coin_info_layer)
    ConstraintLayout mCoinInfoLayer;
    @BindView(R.id.reward_layer)
    LinearLayout     mRewardLayer;
    @BindView(R.id.reward_top_title)
    LinearLayout     mRewardTopTitle;
    @BindView(R.id.trans_toolbar_layer)
    RelativeLayout   mTransToolbar;
    @BindView(R.id.tool_bg)
    ImageView        mToolBg;

    private AwardRankBean mMoneyRankBean;
    private AwardRankBean mCoinRankBean;

    private UserRewardAdapter     mUserRewardAdapter;
    private RechargeConfirmDialog mRechargeConfirmDialog;
    private RechargeRequeryDialog mRechargeRequeryDialog;

    private int               mBookType;
    private String            mBookId;
    private RewardDialog      mRewardDialog;
    private String            mPayType;
    private String            mBookName;
    private ScrollTransHelper mScrollTransHelper;
    private PayUtils mPayUtils;

    public static void toActivity(Context context, String bookId, String bookName, int bookType, String coverUrl) {
        Intent intent = new Intent(context, UserRewardActivity.class);
        intent.putExtra(INTENT_BOOK_COVER, coverUrl);
        intent.putExtra(INTENT_BOOK_ID, bookId);
        intent.putExtra(INTENT_BOOK_TYPE, bookType);
        intent.putExtra(INTENT_BOOK_NAME, bookName);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_user_reward;
    }


    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {

        getSupportActionBar().hide();
        mToolLayer.setVisibility(View.GONE);

        mBookType = getIntent().getIntExtra(INTENT_BOOK_TYPE, 0);
        mBookId = getIntent().getStringExtra(INTENT_BOOK_ID);
        mBookName = getIntent().getStringExtra(INTENT_BOOK_NAME);
        mRewardMoney.setSelected(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mRewardList.setLayoutManager(gridLayoutManager);
        mRewardList.addItemDecoration(new GridCell(3, DisplayUtil.dip2px(mContext, 8), 0));
        mUserRewardAdapter = new UserRewardAdapter(mContext, RewardRepertory.getMoneyRewardList());
        mUserRewardAdapter.setMoney(true);
        mRewardList.setAdapter(mUserRewardAdapter);

        initCover();

        mRewardDialog = new RewardDialog(mContext);
        mRewardDialog.setRewardPayTypeClickListener(this);

        mUserRewardPresenter.bingView(this);
        mUserRewardPresenter.loading();
        mUserRewardPresenter.queryMoneyTop(mBookId);
        mUserRewardPresenter.queryCoinTop(mBookId);
        mMinePresenter.bingView(this);
        mMinePresenter.loading();

        mRechargePresenter.bingView(this);
        mRechargePresenter.loading();
        mRechargePresenter.setRechargeType(Constants.STATISTICS_PAY_TYPE.REWARD_RECHARGE);

        mRechargeConfirmDialog = new RechargeConfirmDialog(mContext);
        mRechargeConfirmDialog.setRechargeConfirmListener(this);
        mRechargeRequeryDialog = new RechargeRequeryDialog(mContext);
        mRechargeRequeryDialog.setRechargeRequeryListener(this);

        mScrollTransHelper = new ScrollTransHelper(mScrollViewEx);
        mScrollTransHelper.setArmView(mTransToolbar);
        mScrollTransHelper.setScrollView(mCoverGradient);
        mScrollTransHelper.setScrollTransProgressListener(this);

    }

    private void initCover() {
        String bookCover = getIntent().getStringExtra(INTENT_BOOK_COVER);
        ImageLoadUtils.loadUrl(mBookCover, bookCover);
        ImageLoadUtils.loadUrlWithGradient(mCoverGradient, bookCover);
    }

    @ViewMethod
    public void setCoinReward(AwardRankBean awardRankBean) {
        mCoinRankBean = awardRankBean;
        if (mRewardCoin.isSelected()) {
            initRank(awardRankBean, RankType.RANK_COIN_REWARD);
        }
    }

    @ViewMethod
    public void setMoneyReward(AwardRankBean awardRankBean) {
        mMoneyRankBean = awardRankBean;
        if (mRewardMoney.isSelected()) {
            initRank(awardRankBean, RankType.RANK_MONEY_REWARD);
        }
    }

    @OnClick(R.id.reward_money)
    public void rewardMoney() {
        if (mRewardMoney.isSelected()) return;
        mUserRewardAdapter.setSelectedPosition(0);
        mRewardMoney.setSelected(true);
        mRewardCoin.setSelected(false);
        mUserRewardAdapter.setMoney(true);
        mUserRewardAdapter.setRewardLists(RewardRepertory.getMoneyRewardList());
        mUserRewardAdapter.notifyChange();
        mCoinInfoLayer.setVisibility(View.GONE);
        initRank(mMoneyRankBean, RankType.RANK_MONEY_REWARD);
    }

    @OnClick(R.id.reward_coin)
    public void rewardCoin() {
        if (mRewardCoin.isSelected()) return;
        mUserRewardAdapter.setSelectedPosition(0);
        mRewardMoney.setSelected(false);
        mRewardCoin.setSelected(true);
        mUserRewardAdapter.setMoney(false);
        mUserRewardAdapter.setRewardLists(RewardRepertory.getCoinRewardList());
        mUserRewardAdapter.notifyChange();
        mCoinInfoLayer.setVisibility(View.VISIBLE);
        initRank(mCoinRankBean, RankType.RANK_COIN_REWARD);
    }

    @Override
    public void setCoins(int currentCoin, int giveCoin) {
        super.setCoins(currentCoin, giveCoin);
        mUserLeftCoin.setText(currentCoin + " 红果币");
    }

    @OnClick(R.id.reward_submit)
    public void onViewClicked() {
        mRewardDialog.setMoney(mUserRewardAdapter.getCurrentRewardLists().coin);
        if (mRewardCoin.isSelected()) {
            mRewardDialog.setRewardType(RewardDialog.REWARD_COIN);
        } else {
            mRewardDialog.setRewardType(RewardDialog.REWARD_MONEY);
        }
        mRewardDialog.show();
    }

    @OnClick(R.id.recharge_now)
    public void rechargeNow() {
        RechargeActivity.toActivity(mContext);
    }

    @OnClick(R.id.refresh_coin)
    public void refreshCoin() {
        mMinePresenter.queryUserCoin();
    }

    private void initRank(AwardRankBean awardRankBean, String rankType) {
        for (int i = 0; i < 3; i++) {
            TextView textView = (TextView) mRewardLayer.getChildAt(i);
            if (awardRankBean == null) {
                textView.setText("---");
                continue;
            }
            if (i >= awardRankBean.data.size()) {
                textView.setText("---");
            } else {
                AwardRankBean.DataBean dataBean = awardRankBean.data.get(i);
                textView.setText(dataBean.nickName);
            }
        }
        mRewardTopTitle.setOnClickListener(v -> {
            RewardTopActivity.toActivity(mContext, mBookId, rankType);
        });
    }

    @Override
    public void moneyReward(String money, String payMethod) {
        StatisticsUtils.rewardRechargeClick(MathTypeParseUtils.string2Int(money) * 100);
        mPayType = payMethod;
        mRechargePresenter.setPayType(payMethod);
        mRechargePresenter.setPayMoney(MathTypeParseUtils.string2Int(money) * 100);
        mUserRewardPresenter.rewardMoney(mBookId, mBookType, mBookName, money, payMethod);
    }

    @Override
    public void coinReward(String coin) {
        mUserRewardPresenter.rewardCoin(mBookId, mBookType, mBookName, coin);
    }

    /**
     * 支付订单页回调支付状态
     *
     * @param payCheckEvent
     */
    @Subscribe
    public void onEvent(PayCheckEvent payCheckEvent) {
        if (!payCheckEvent.checkSelf(this.getClass())) return;
        dismissWaitDialog();
        if (TextUtils.isEmpty(payCheckEvent.mError)) {
            openPayDuration();
        } else {
            toastMessage(payCheckEvent.mError);
        }
    }

    /**
     * 打开支付后让用户手动触发是否支付完成
     */
    private void openPayDuration() {
        RxJavaUtils.delayRun(500, () -> {
            mRechargeConfirmDialog.show();
        });
    }

    /**
     * 用户点击 确认支付完成回调，查询订单状态
     */
    @Override
    public void rechargeConfirmSuccess() {
        mRechargePresenter.queryOrderStatus(true);
    }

    /**
     * 用户点击 尚未支付(防止用户误点,请求一次)
     */
    @Override
    public void rechargeCancel() {
        mRechargePresenter.queryOrderStatus(false);
    }

    /**
     * 支付成功，刷新红果币
     */
    @ViewMethod
    public void rechargeSuccess() {
        mUserRewardPresenter.queryMoneyTop(mBookId);
    }

    /**
     * 当没有查询到订单支付成功时，让用户手动去刷新订单信息
     */
    @ViewMethod
    public void showRechargeStausInfo() {
        mRechargeRequeryDialog.show();
    }

    /**
     * 重新请求订单状态
     */
    @Override
    public void requeryOrder() {
        mRechargePresenter.queryOrderStatus(true);
    }

    /**
     * 意见反馈
     */
    @Override
    public void postRequestion() {
        FeedBackActivity.toActivity(mContext);
    }

    @Override
    public void setRechargeList(List<RechargeBean.DataBean> rechargeList) {

    }

    @Override
    public void startToPay(String directUrl) {
        if(mPayUtils != null){
            mPayUtils.destroy();
        }
        mPayUtils = PayUtils.startToPay(mContext, directUrl, mPayType, UserRewardActivity.class);
    }

    @Override
    public void setPayOrder(String order) {
        mRechargePresenter.setPayOrder(order);
    }

    @ViewMethod
    public void rewardCoinSuccess() {
        mMinePresenter.queryUserCoin();
    }

    @Override
    public void progressChange(int percent) {
        mToolBg.setAlpha(percent / 100f);
    }

    @OnClick(R.id.view_back)
    void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPayUtils != null){
            mPayUtils.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPayUtils != null && mPayUtils.onBackPress()) {
            return;
        }
    }
}
