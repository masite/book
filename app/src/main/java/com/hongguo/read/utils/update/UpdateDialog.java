package com.hongguo.read.utils.update;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.losg.library.widget.dialog.BaAnimDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateDialog extends BaAnimDialog {

    @BindView(R.id.btn_sure)
    TextView     mBtnSure;
    @BindView(R.id.describe)
    TextView     mDescribe;
    @BindView(R.id.cancel)
    TextView     mCancel;
    @BindView(R.id.content_view)
    LinearLayout mContentView;
    @BindView(R.id.title)
    TextView     mTitle;

    private UpdateDialogClickListener mUpdateDialogClickListener;
    private CancelUpdateDialogClickListener mCancelUpdateDialogClickListener;


    public UpdateDialog(Context context) {
        super(context, R.style.MessageDialog);
        setCancelable(false);
        initView();
    }

    public void showDescribe(String describe) {
        if (TextUtils.isEmpty(describe)) {
            return;
        }
        mDescribe.setText(describe);
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void setForce(){
        mCancel.setTextColor(0xff999999);
        mCancel.setEnabled(false);
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_update;
    }

    private void initView() {
        ButterKnife.bind(this);
        View view = findViewById(R.id.content_view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels * 3 / 4;
        view.setLayoutParams(layoutParams);

        mBtnSure.getLayoutParams().width = getContext().getResources().getDisplayMetrics().widthPixels / 4;
        mCancel.getLayoutParams().width = getContext().getResources().getDisplayMetrics().widthPixels / 4;
    }

    @OnClick(R.id.btn_sure)
    void startDown() {
        if(mUpdateDialogClickListener != null){
            mUpdateDialogClickListener.click();
        }
        dismiss();
    }

    @OnClick(R.id.cancel)
    void cancelDialog() {
        dismiss();
        setOnDismissListener(dialog -> {
            if(mCancelUpdateDialogClickListener != null){
                mCancelUpdateDialogClickListener.cancelClick();
            }
        });
    }

    public void sureBtnText(String sureText){
        mBtnSure.setText(sureText);
    }

    public void setBtnCancel(String cancel){
        mCancel.setText(cancel);
    }

    public void setCancelClickListner(CancelUpdateDialogClickListener cancelUpdateDialogClickListener) {
        mCancelUpdateDialogClickListener = cancelUpdateDialogClickListener;
    }

    public void setUpdateDialogClickListener(UpdateDialogClickListener updateDialogClickListener) {
        mUpdateDialogClickListener = updateDialogClickListener;
    }

    public interface UpdateDialogClickListener{
        void click();
    }

    public interface CancelUpdateDialogClickListener{
        void cancelClick();
    }

}
