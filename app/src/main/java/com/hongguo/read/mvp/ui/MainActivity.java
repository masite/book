package com.hongguo.read.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.base.BaseFragment;
import com.hongguo.common.utils.AppBackUtil;
import com.hongguo.common.utils.HomeFragmentManager;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.UmengAuthorHelper;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.R;
import com.hongguo.read.base.MineInfoActivity;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.dagger.component.ActivityComponent;
import com.hongguo.read.mvp.contractor.MainContractor;
import com.hongguo.read.mvp.contractor.mine.AuthorLoginContractor;
import com.hongguo.read.mvp.contractor.version.VersionContractor;
import com.hongguo.read.mvp.presenter.MainPresenter;
import com.hongguo.read.mvp.presenter.mine.AuthorLoginPresenter;
import com.hongguo.read.mvp.presenter.mine.MinePresenter;
import com.hongguo.read.mvp.presenter.version.VersionPresenter;
import com.hongguo.read.mvp.ui.bookshelf.BookShelfFragment;
import com.hongguo.read.mvp.ui.bookstore.BookStoreFragment;
import com.hongguo.read.mvp.ui.event.EventInfoDialog;
import com.hongguo.read.mvp.ui.mine.AuthorLoginActivity;
import com.hongguo.read.mvp.ui.mine.MineFragment;
import com.hongguo.read.mvp.ui.vip.VipFragment;
import com.hongguo.read.repertory.db.DBFactory;
import com.hongguo.read.repertory.share.AppRepertory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.utils.update.UpdateDialog;
import com.hongguo.read.widget.MenuContenter;
import com.hongguo.read.widget.UserLoginDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author losg
 */
public class MainActivity extends MineInfoActivity implements MainContractor.IView, VersionContractor.IView, MenuContenter.MenuAddListener, MenuContenter.MenuItemClickListener, DBFactory.UpdateDBListener, AuthorLoginContractor.IView, UmengAuthorHelper.UmAuthorResponseListener, UserLoginDialog.UserLoginListener {

    private static final String INTENT_POSITION = "intent_position";

    @Inject
    MainPresenter        mMainPresenter;
    @Inject
    VersionPresenter     mVersionPresenter;
    @Inject
    MinePresenter        mMinePresenter;
    @Inject
    AuthorLoginPresenter mLoginPresenter;

    @BindView(R.id.fragment_content)
    FrameLayout   mFragmentContent;
    @BindView(R.id.menu_content)
    MenuContenter mMenuContent;
    @BindView(R.id.menu_bg)
    LinearLayout  mMenuBgView;
    @BindView(R.id.event_image)
    ImageView     mEventImage;

    private UmengAuthorHelper mUmengAuthorHelper;
    private UserLoginDialog   mUserLoginDialog;

    private final String[] mMenuTitle    = new String[]{"书架", "书城", "会员", "我的"};
    private final int[]    mMenuResource = new int[]{R.drawable.sr_menu_bookshelf,
            R.drawable.sr_menu_bookstore,
            R.drawable.sr_menu_vip,
            R.drawable.sr_menu_mine};

    private HomeFragmentManager mHomeFragmentManager;
    private AppBackUtil         mAppBackUtil;
    private EventInfoDialog     mEventInfoDialog;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void toActivity(Context context, int position) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(INTENT_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initView() {

        getSupportActionBar().hide();
        initGlobalPararms();

        dynamicAddView(mMenuBgView, "background", R.drawable.ic_menu_bg);
        mMenuContent.setMenuAddListener(this);
        mMenuContent.showMenus(mMenuTitle, mMenuResource);
        mMenuContent.setMenuItemClickListener(this);

        mToolLayer.setVisibility(View.GONE);

        mHomeFragmentManager = new HomeFragmentManager(getSupportFragmentManager());
        mHomeFragmentManager.addFragments(BookShelfFragment.class, BookStoreFragment.class, VipFragment.class, MineFragment.class);
        mMenuContent.setMenuSelected(1);

        mAppBackUtil = new AppBackUtil(this);
        mEventInfoDialog = new EventInfoDialog(mContext);

        mMinePresenter.bingView(this);
        mMinePresenter.loading();

        mVersionPresenter.bingView(this);
        mVersionPresenter.loading();

        mMainPresenter.bingView(this);
        mMainPresenter.loading();

        mLoginPresenter.bingView(this);

        mUmengAuthorHelper = new UmengAuthorHelper(this);
        mUmengAuthorHelper.setUmAuhorResponseListener(this);

        mUserLoginDialog = new UserLoginDialog(mContext);
        mUserLoginDialog.setUserLoginListener(this);

        delyShowLogin();
    }

    private void delyShowLogin() {
        if(AppRepertory.getLoginShow()){
            RxJavaUtils.delayRun(1000, ()->{
                mUserLoginDialog.show();
            });
        }
    }

    /**
     * 初始化全局数据
     */
    private void initGlobalPararms() {
        DBFactory.getInstance().loadDb(FileManager.getUserDbPath(UserRepertory.getUserID()), this);
    }

    /**
     * menu 添加监听
     *
     * @param menuIcon
     * @param menuText
     * @param position
     */
    @Override
    public void menuAdd(ImageView menuIcon, TextView menuText, int position) {
        dynamicAddView(menuIcon, "src", mMenuResource[position]);
        dynamicAddView(menuText, "textColor", R.color.cr_menu);
    }

    @Override
    public void menuClick(int position) {
        StatisticsUtils.function(mMenuTitle[position]);
        mHomeFragmentManager.showTab(R.id.fragment_content, position);
    }


    @Override
    public void onBackPressed() {
        if (mHomeFragmentManager != null && mHomeFragmentManager.getFragments().size() > 2) {
            BaseFragment baseFragment = mHomeFragmentManager.getFragments().get(2);
            if (baseFragment.backPress()) {
                return;
            }
        }
        mAppBackUtil.onBackPress();
    }

    @Override
    public void showAppUpdateDialog(String title, String message, boolean force, UpdateDialog.UpdateDialogClickListener updateDialogClickListener) {
        UpdateDialog updateDialog = new UpdateDialog(mContext);
        updateDialog.setTitle(title);
        if (force)
            updateDialog.setForce();
        updateDialog.showDescribe(message);
        updateDialog.setUpdateDialogClickListener(updateDialogClickListener);
        updateDialog.show();
    }

    @Override
    public void needUpdate(boolean needUpdate) {

    }

    @OnClick(R.id.event_image)
    public void openEvenDialog() {
        mEventInfoDialog.show();
    }

    @ViewMethod
    public void setEventInfo(String littleImage, String bigImage, String direct, boolean hasEvent, boolean showEventDialog) {
        if (!hasEvent) {
            mEventImage.setVisibility(View.GONE);
            return;
        }

        mEventImage.setVisibility(View.VISIBLE);
        ImageLoadUtils.loadUrlDefault(mEventImage, littleImage);
        mEventInfoDialog.setEventImage(bigImage);
        mEventInfoDialog.setDirctUrl(direct);

        if (showEventDialog) {
            mEventInfoDialog.show();
        }
    }

    @ViewMethod
    public void checkVersion() {
        mVersionPresenter.versionCheck(false, false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra(INTENT_POSITION, -1);
        if (index != -1) {
            mMenuContent.setMenuSelected(index);
        }
    }


    /********************数据库升级************************/
    @Override
    public void startUpdate() {
        showWaitDialog(true, "数据升级中", null);
    }

    @Override
    public void updateFinish() {
        dismissWaitDialog();
    }


    /****************登录相关*****************************/


    @Override
    public void authorStart() {
        showWaitDialog(true, "授权中", null);
    }

    @Override
    public void authorComplete(int authorType, UmengAuthorHelper.AuthorResult authorResult) {
        dismissWaitDialogWithoutAnim();
        mLoginPresenter.loginByAuthor(authorType, authorResult);
    }

    @Override
    public void authorError() {
        dismissWaitDialogWithoutAnim();
        toastMessage("授权失败");
    }

    @Override
    public void authorCancel() {
        dismissWaitDialogWithoutAnim();
        toastMessage("授权取消");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUmengAuthorHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUmengAuthorHelper.destory();
    }

    @Override
    public void loginPassword() {
        AuthorLoginActivity.toActivity(mContext);
    }

    @Override
    public void loginWeiXin() {
        mUmengAuthorHelper.author(UmengAuthorHelper.AUTHOR_WEIXIN);
    }
}
