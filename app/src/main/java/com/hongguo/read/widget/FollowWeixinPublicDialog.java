package com.hongguo.read.widget;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.hongguo.read.R;
import com.losg.library.widget.dialog.BaAnimDialog;

/**
 * Created by losg on 2018/2/11.
 */

public class FollowWeixinPublicDialog extends BaAnimDialog implements View.OnClickListener {


    private ClipboardManager mClipboardManager;

    public FollowWeixinPublicDialog(Context context) {
        super(context, R.style.MessageDialog);
        initView();
    }

    private void initView() {
        mClipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        findViewById(R.id.jump_weixin).setOnClickListener(this);
        findViewById(R.id.close_dialog).setOnClickListener(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_follow_weixin;
    }

    @Override
    public void show() {
        mClipboardManager.setText("红果小说");
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = getContext().getResources().getDisplayMetrics().widthPixels * 4 / 5;
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_weixin:
                JumpToWeixin();
                break;
            case R.id.close_dialog:
                dismiss();
                break;
        }
    }

    private void JumpToWeixin() {
        try {
            Intent LaunchIntent = getContext().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            getContext().startActivity(LaunchIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dismiss();
    }
}
