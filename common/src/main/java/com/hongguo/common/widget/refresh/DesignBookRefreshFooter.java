package com.hongguo.common.widget.refresh;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hongguo.common.widget.refresh.base.BaFooterView;
import com.hongguo.read.base.R;
import com.losg.library.widget.dialog.ProgressView;


/**
 * Created by losg on 2017/5/27.
 */

public class DesignBookRefreshFooter extends BaFooterView {

    private TextView     mLoadingStatus;
    private View         mFooter;
    private ProgressView mProgress;

    public DesignBookRefreshFooter(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        mFooter = View.inflate(mContext, R.layout.view_refresh_footer, null);
        mLoadingStatus = (TextView) mFooter.findViewById(R.id.loading_status);
        mProgress = (ProgressView) mFooter.findViewById(R.id.progress);
        return mFooter;
    }

    @Override
    public void loadding() {
        mProgress.setVisibility(View.VISIBLE);
        mFooter.setVisibility(View.VISIBLE);
        mLoadingStatus.setVisibility(View.VISIBLE);
        mLoadingStatus.setText("加载中");
    }

    @Override
    public void loadNormal() {
        mFooter.setVisibility(View.GONE);
        mLoadingStatus.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void loaddingError(String errorMessage) {
        mProgress.setVisibility(View.GONE);
        mFooter.setVisibility(View.VISIBLE);
        mLoadingStatus.setVisibility(View.VISIBLE);
        mLoadingStatus.setText(TextUtils.isEmpty(errorMessage) ? "加载失败,点击重试" : errorMessage);
    }

    @Override
    public void allLoaded(boolean showInfo) {
        if (showInfo) {
            mFooter.setVisibility(View.VISIBLE);
            mLoadingStatus.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
            mLoadingStatus.setText("没有更多了~");
        } else {
            loadNormal();
        }
    }
}
