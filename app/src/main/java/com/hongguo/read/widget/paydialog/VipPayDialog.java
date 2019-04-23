package com.hongguo.read.widget.paydialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.adapter.VipPayDialogAdapter;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.hongguo.read.widget.loading.LoadingView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class VipPayDialog extends Dialog implements VipPayDialogAdapter.ItemClickListener, LoadingView.LoadingViewClickListener {

    @BindView(R.id.pay_recycler)
    RecyclerView mPayRecycler;
    @BindView(R.id.text_money)
    TextView     mTextmMoney;
    @BindView(R.id.btn_confirm)
    TextView     mBtnConfirm;
    @BindView(R.id.pay_panel)
    LinearLayout mPayPanel;

    private List<RechargeBean.DataBean> mItems;
    private int                         mMoney;
    private String                      mRuleId;
    private String                      mRouleSign;
    private Context                     mContent;
    private Unbinder                    mBind;
    private VipPayDialogAdapter         mVipPayDialogAdapter;
    private VipDialogistener            mVipClickListener;
    private VipDialogFooterHelper       mVipDialogFooterHelper;
    private LoadingHelper               mLoadingHelper;

    public VipPayDialog(@NonNull Context context) {
        super(context, R.style.FullWidthDialog);
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        setContentView(R.layout.dialog_vip_pay);
        mContent = context;
        mBind = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mItems = new ArrayList<>();
        mVipDialogFooterHelper = new VipDialogFooterHelper(mContent);
        mPayRecycler.setLayoutManager(RecyclerLayoutUtils.createVertical(mContent));
        mVipPayDialogAdapter = new VipPayDialogAdapter(mContent, mItems);
        mVipPayDialogAdapter.setItemClickListener(this);
        mVipPayDialogAdapter.addFooter(mVipDialogFooterHelper.getView());
        mPayRecycler.setAdapter(mVipPayDialogAdapter);
        mLoadingHelper = new LoadingHelper(mContent, mPayPanel);
        mLoadingHelper.setLoadingViewClickListener(this);
    }

    public void setVipPayList(List<RechargeBean.DataBean> items) {
        mLoadingHelper.setStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS);
        mItems.clear();
        mItems.addAll(items);
        //默认选中第一项
        itemClick(0, 0);
        mVipPayDialogAdapter.notifyChange();
    }

    public void setPayTypeInfo(boolean weixin, boolean alipay) {
        mVipDialogFooterHelper.setPayInfo(weixin, alipay);
    }

    @OnClick(R.id.btn_confirm)
    void startPay() {
        dismiss();
        if (mVipClickListener != null)
            mVipClickListener.toPay(mMoney + "", mVipDialogFooterHelper.getPayType(), mRuleId, mRouleSign);
    }

    @Override
    public void itemClick(int area, int index) {
        RechargeBean.DataBean.RulesBean rulesBean = mItems.get(area).rules.get(index);
        mMoney = rulesBean.value ;
        mRuleId = rulesBean.id;
        mTextmMoney.setText("¥" + (rulesBean.value / 100) + "元");
        mRouleSign = rulesBean.sign;
    }

    @Override
    public void loadingClick(BaLoadingView.LoadingStatus loadingStatus) {

    }

    public interface VipDialogistener {
        void toPay(String money, String payType, String ruleId, String sign);
    }

    public void setVipDialogistener(VipDialogistener vipClickListener) {
        mVipClickListener = vipClickListener;
    }

    @Override
    public void show() {
        mLoadingHelper.setStatus(BaLoadingView.LoadingStatus.LOADING);
        mVipPayDialogAdapter.setSelectPosition(0);
        mVipPayDialogAdapter.notifyDataSetChanged();
        super.show();
    }

    public void destory() {
        mBind.unbind();
    }

    public String getPayMethod(){
        return mVipDialogFooterHelper.getPayType();
    }

}
