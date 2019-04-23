package com.hongguo.read.mvp.ui.mine.mypackage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.router.AppRouter;
import com.hongguo.common.utils.EditWatcher;
import com.hongguo.read.R;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.mine.mypackage.FeedBackContractor;
import com.hongguo.read.mvp.presenter.mine.mypackage.FeedBackPresenter;
import com.hongguo.read.widget.AutoLinefeedLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by losg on 2018/1/7.
 */

@Route(path = AppRouter.USER_FEEDBACK)
public class FeedBackActivity extends ActivityEx implements FeedBackContractor.IView, AutoLinefeedLayout.LabelClickListener {

    @Inject
    FeedBackPresenter  mFeedBackPresenter;
    @BindView(R.id.feedback_label)
    AutoLinefeedLayout mFeedbackLabel;
    @BindView(R.id.question_content)
    EditText           mQuestionContent;
    @BindView(R.id.text_number)
    TextView           mTextNumber;
    @BindView(R.id.label_contact)
    TextView           mLabelContact;
    @BindView(R.id.contact_number)
    EditText           mContactNumber;
    @BindView(R.id.submit)
    TextView           mSubmit;


    public static void toActivity(Context context) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {

        setTitle("意见反馈");
        mFeedbackLabel.setLabelClickListener(this);
        mFeedBackPresenter.bingView(this);
        mFeedBackPresenter.loading();
        initEdit();
    }

    private void initEdit() {
        mQuestionContent.addTextChangedListener(new EditWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mTextNumber.setText(mQuestionContent.getText().toString().length() + " /200 字");
            }
        });
    }

    @ViewMethod
    public void setFeedBackLabels(String[] labels) {
        mFeedbackLabel.setLables(labels);
    }

    @OnClick(R.id.submit)
    public void submit() {
        mFeedBackPresenter.submitFeedBack(mContactNumber.getText().toString(), mQuestionContent.getText().toString());
    }

    @Override
    public void labelClick(View view, String labelName) {
        view.setSelected(!view.isSelected());
        if (view.isSelected()) {
            mQuestionContent.setText(mQuestionContent.getText().toString() + " " +labelName);
        } else {
            mQuestionContent.setText(mQuestionContent.getText().toString().replace(" "+labelName, ""));
            mQuestionContent.setText(mQuestionContent.getText().toString().replace(labelName, ""));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem onLine = menu.add(0, 0, 0, "在线客服");
        onLine.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 0){
            onlineService();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onlineService() {
        openQQ();
    }

    private void openQQ() {
        if (checkApkExist(this, "com.tencent.mobileqq")){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=1373073715&version=1")));
        }else{
            toastMessage("本机未安装QQ应用");
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
