package com.hongguo.read.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.adapter.ErrorReportAdapter;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.losg.library.utils.CommonUtils;
import com.losg.library.widget.dialog.BaAnimDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by losg on 2018/4/11.
 */

public class BookErrorReport extends BaAnimDialog implements ErrorReportAdapter.ErrorReportItemClick {

    @BindView(R.id.title)
    TextView     mTitle;
    @BindView(R.id.close_dialog)
    ImageView    mCloseDialog;
    @BindView(R.id.error_report_list)
    RecyclerView mErrorReportList;
    @BindView(R.id.other_suggest)
    EditText     mOtherSuggest;
    @BindView(R.id.contact_number)
    EditText     mContactNumber;
    @BindView(R.id.submit)
    TextView     mSubmit;
    private Unbinder           mBind;
    private ErrorReportAdapter mErrorReportAdapter;
    private BookErrorSubmit    mBookErrorSubmit;
    private String             mErrorInfo;

    public BookErrorReport(Context context) {
        super(context, R.style.MessageDialog);
        mBind = ButterKnife.bind(this);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.9f);
        getWindow().setAttributes(attributes);

        initView();
    }

    @Override
    protected int initLayout() {
        return R.layout.dialog_error_report;
    }

    private void initView() {
        mErrorReportList.setLayoutManager(RecyclerLayoutUtils.createVertical(getContext()));
        mErrorReportAdapter = new ErrorReportAdapter(getContext());
        mErrorReportAdapter.setErrorReportItemClick(this);
        mErrorReportList.setAdapter(mErrorReportAdapter);
    }


    @OnClick(R.id.close_dialog)
    public void closeDialog() {
        dismiss();
    }

    @OnClick(R.id.submit)
    public void submit() {

        if (TextUtils.isEmpty(mErrorInfo)) {
            CommonUtils.toastMessage(getContext(), "请选择错误类型");
            return;
        }


        if (TextUtils.isEmpty(mContactNumber.getText().toString())) {
            CommonUtils.toastMessage(getContext(), "请填写联系方式");
            return;
        }

        boolean allSpace = true;
        for (int i = 0; i < mContactNumber.getText().toString().length() - 1; i++) {
            if (!mContactNumber.getText().toString().substring(i, i + 1).equals(" ")) {
                allSpace = false;
            }
        }

        if (allSpace) {
            mContactNumber.setText("");
            CommonUtils.toastMessage(getContext(), "请填写联系方式");
            return;
        }
        dismissWithoutAnim();
        if (mBookErrorSubmit != null) {
            mBookErrorSubmit.errorSubmit(mErrorInfo, mOtherSuggest.getText().toString(), mContactNumber.getText().toString());
        }
    }


    public void clearAll() {
        mOtherSuggest.setText("");
        mContactNumber.setText("");
    }

    @Override
    public void errorSelected(String message) {
        mErrorInfo = message;
    }

    public void setBookErrorSubmit(BookErrorSubmit bookErrorSubmit) {
        mBookErrorSubmit = bookErrorSubmit;
    }

    public interface BookErrorSubmit {
        void errorSubmit(String errorInfo, String suggest, String contactNumber);
    }

    public void onDestory() {
        mBind.unbind();
    }

}
