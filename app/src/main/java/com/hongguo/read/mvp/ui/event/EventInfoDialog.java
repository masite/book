package com.hongguo.read.mvp.ui.event;

import android.content.Context;
import android.widget.ImageView;

import com.hongguo.common.utils.webview.AppWebView;
import com.hongguo.read.R;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.losg.library.widget.dialog.BaAnimDialog;


/**
 * Created time 2017/11/28.
 *
 * @author losg
 */

public class EventInfoDialog extends BaAnimDialog {

    private String mEventImage = "";

    private ImageView mEventImageView;
    private String    mDirctUrl;

    public EventInfoDialog(Context context) {
        super(context, R.style.FullWidthDialog);
        initView();
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_event;
    }

    private void initView() {
        mEventImageView = (ImageView) findViewById(R.id.event_image);
        findViewById(R.id.close).setOnClickListener(v -> {
            dismiss();
        });
        mEventImageView.setOnClickListener(v -> {
            if(UserRepertory.currentUserIsNew()){
                UserRepertory.setUserIsNew(false);
                AppRepertory.setVipEventClick(true);
                MainActivity.toActivity(getContext(), 2);
                dismiss();
                return;
            }
            StatisticsUtils.event(mDirctUrl);
            AppWebView.toActivity(mDirctUrl);
            dismiss();
        });
    }

    public void setDirctUrl(String dirctUrl) {
        mDirctUrl = dirctUrl;
    }

    public void setEventImage(String eventImage) {
        mEventImage = eventImage;
    }

    @Override
    public void show() {
        if(UserRepertory.currentUserIsNew()){
            ImageLoadUtils.loadResourceDefault(mEventImageView, R.mipmap.ic_svip_event);
        }else{
            ImageLoadUtils.loadUrlDefault(mEventImageView, mEventImage);
        }
        superShow();
    }

    private void superShow() {
        super.show();
    }
}
