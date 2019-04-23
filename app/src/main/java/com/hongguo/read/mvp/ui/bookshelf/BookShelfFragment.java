package com.hongguo.read.mvp.ui.bookshelf;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hongguo.common.annotation.ViewMethod;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.common.utils.RecyclerScrollTransHelper;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.widget.recycler.GridCell;
import com.hongguo.read.R;
import com.hongguo.read.adapter.BookShelfAdapter;
import com.hongguo.read.base.FragmentEx;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.dagger.component.FragmentComponent;
import com.hongguo.read.eventbus.AddBookShelfEvent;
import com.hongguo.read.eventbus.UpdateBookShelfEvent;
import com.hongguo.read.eventbus.UpdateUserInfo;
import com.hongguo.read.mvp.contractor.bookshelf.BookShelfContractor;
import com.hongguo.read.mvp.presenter.bookshelf.BookShelfPresenter;
import com.hongguo.read.mvp.ui.MainActivity;
import com.hongguo.read.mvp.ui.book.BookReaderActivity;
import com.hongguo.read.repertory.db.DBFactory;
import com.hongguo.read.repertory.db.read.BookReader;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.utils.RecyclerLayoutUtils;
import com.hongguo.read.widget.BookShelfDeleteView;
import com.hongguo.read.widget.SignDialog;
import com.hongguo.read.widget.TransActionRelativeLayout;
import com.hongguo.read.widget.openbook.OpenBookView;
import com.losg.library.utils.DisplayUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created time 2017/11/30.
 *
 * @author losg
 */

public class BookShelfFragment extends FragmentEx implements BookShelfContractor.IView, BookShelfAdapter.BookShelfClickListener, OpenBookView.OpenBookListener, BookShelfDeleteView.BookShelfDeleteClikListener, RecyclerScrollTransHelper.ScrollTransProgressListener, DBFactory.UpdateDBListener, BookShelfHeaderHelper.BookShelfHeaderClickListener {

    @Inject
    BookShelfPresenter mBookShelfPresenter;

    @BindView(R.id.book_shelf)
    RecyclerView              mBookShelf;
    @BindView(R.id.tool_bg)
    ImageView                 mToolBg;
    @BindView(R.id.trans_toolbar_layer)
    TransActionRelativeLayout mTransActionRelativeLayout;

    //    private OpenBookManager           mOpenBookManager;
    private BookShelfAdapter          mBookShelfAdapter;
    private BookShelfDeleteView       mBookShelfDeleteView;
    private List<BookReader>          mBookShelives;
    private BookReader                mOpenBook;
    private BookShelfHeaderHelper     mBookShelfHeaderHelper;
    private RecyclerScrollTransHelper mRecyclerScrollTransHelper;
    private boolean                   mOpenImage;
    private SignDialog                mSignDialog;

    @Override
    protected int initLayout() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {
        LogUtils.log("initView");
        initBookShelf();
        mBookShelfDeleteView = new BookShelfDeleteView(mContext);
//        mOpenBookManager = new OpenBookManager(mContext);
//        mOpenBookManager.setOpenBookListener(this);
        mBookShelfPresenter.bingView(this);
        mBookShelfDeleteView.setBookShelfDeleteClikListener(this);
        mRecyclerScrollTransHelper = new RecyclerScrollTransHelper(mBookShelf);
        mRecyclerScrollTransHelper.setArmView(mTransActionRelativeLayout);
        mRecyclerScrollTransHelper.setScrollView(mBookShelfHeaderHelper.getView());
        mRecyclerScrollTransHelper.setScrollTransProgressListener(this);
        mBookShelfPresenter.loading();

        mSignDialog = new SignDialog(getActivity());

    }

    private void initBookShelf() {
        mBookShelives = new ArrayList<>();
        mBookShelfHeaderHelper = new BookShelfHeaderHelper(mContext);
        mBookShelfHeaderHelper.startWave();
        mBookShelfHeaderHelper.setBookShelfHeaderClickListener(this);

        mBookShelf.setLayoutManager(RecyclerLayoutUtils.createTitleGridManager(mContext, 3));
        mBookShelfAdapter = new BookShelfAdapter(mContext, mBookShelives);
        mBookShelfAdapter.setBookShelfClickListener(this);
        mBookShelfAdapter.addHeader(mBookShelfHeaderHelper.getView());

        mBookShelf.setAdapter(mBookShelfAdapter);
        mBookShelf.addItemDecoration(new GridCell(3, DisplayUtil.dip2px(mContext, 16), 1));
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mOpenImage) {
//            mBookShelf.smoothScrollToPosition(0);
////            mOpenBookManager.onResume(mBookShelf);
//        } else {
//            mBookShelfHeaderHelper.startWave();
//            //解决返回时书架图片不显示问题
//            RxJavaUtils.delayRun(1000, () -> {
//                mBookShelfAdapter.notifyDataSetChanged();
//            });
//        }

        mBookShelfHeaderHelper.startWave();
        //解决返回时书架图片不显示问题
        RxJavaUtils.delayRun(1000, () -> {
            mBookShelfAdapter.notifyDataSetChanged();
        });
        mBookShelfPresenter.queryUserSignInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBookShelfHeaderHelper.pauseWare();
    }


    @ViewMethod
    public void initShelfHeader(String bookCover, String bookName, String readProcess) {
        mBookShelfHeaderHelper.initShelfHeader(bookCover, bookName, readProcess);
    }

    @Override
    public void setShelfHeaderBookIdInfo(String bookId, int bookType) {
        mBookShelfHeaderHelper.setBookIdInfo(bookId, bookType);
    }

    @ViewMethod
    public void setSignInfo(String signMessage, boolean sign) {
        mBookShelfHeaderHelper.setSignInfo(signMessage, sign);
    }

    @Override
    public void showSignSuccessInfo(int gval, String signNumber) {
        mSignDialog.setGiveCoin(gval + "");
        mSignDialog.setSignNumber(signNumber);
        mSignDialog.show();
    }

    @ViewMethod
    public void setBookShelfBooks(List<BookReader> books) {
        mBookShelives.clear();
        mBookShelives.addAll(books);
        mBookShelfAdapter.notifyChange();
    }

    /**
     * 用户修改更新书架的内容
     * {@link com.hongguo.read.mvp.presenter.mine.AuthorLoginPresenter}
     * {@link com.hongguo.read.mvp.presenter.mine.center.UserCenterPresenter}
     * {@link com.hongguo.read.mvp.ui.vip.VipFragment}
     *
     * @param updateUserInfo
     */
    @Subscribe
    public void onEvent(UpdateUserInfo updateUserInfo) {
        DBFactory.getInstance().loadDb(FileManager.getUserDbPath(UserRepertory.getUserID()), this);
        mBookShelfPresenter.loading();
    }

    @Subscribe
    public void onEvent(UpdateBookShelfEvent updateBookShelfEvent) {
        mBookShelfPresenter.loading();
    }

    /**
     * 关闭动画
     *
     * @param show
     */
    @Override
    public void showChange(boolean show) {
        if (mBookShelfHeaderHelper == null) return;
        if (show) {
            mBookShelfHeaderHelper.startWave();
            mBookShelfPresenter.queryUserSignInfo();
        } else {
            mBookShelfHeaderHelper.pauseWare();
        }
    }

    /**
     * *************adapter start **********************
     */
    @Override
    public void bookClick(ImageView imageView, BookReader bookShelf) {
        mOpenImage = true;

        //关闭动画，防止卡顿
        mBookShelfHeaderHelper.pauseWare();

//        mOpenBookManager.openBook(imageView);
        mOpenBook = bookShelf;

        openFinish();
    }

    @Override
    public void addBookClick() {
        MainActivity.toActivity(mContext, 1);
    }

    @Override
    public void enterEditMode() {
        mBookShelfDeleteView.showDelete(mBookShelf);
    }

    /**************adapter end ****************/

    @Subscribe
    public void onEvent(AddBookShelfEvent addBookShelfEvent) {
        mBookShelfPresenter.addBookShelf(addBookShelfEvent.mBookShelf);
    }

    /**
     * 书籍打开动画回掉
     */
    @Override
    public void openFinish() {
        BookReaderActivity.toActivity(mContext, mOpenBook.bookId, mOpenBook.bookType, mOpenBook.bookName, mOpenBook.coverPicture, mOpenBook.bookAuthor);
        RxJavaUtils.delayRun(300, () -> {
            mBookShelfPresenter.addBookShelf(mOpenBook);
        });

    }

    @Override
    public void closeFinish() {
        mBookShelfHeaderHelper.startWave();
        mOpenImage = false;
    }

    /**
     * 删除书架点击
     */
    @Override
    public void deleteClick() {
        mBookShelfPresenter.deleteBookShelf();
        mBookShelfAdapter.setCommendMode();
    }

    @Override
    public boolean backPress() {
        if (mBookShelfAdapter == null) return false;
        if (mBookShelfAdapter.isInEditMode()) {
            mBookShelfAdapter.setCommendMode();
            mBookShelfPresenter.clearBookShelfSelect();
            return true;
        }
        return false;
    }

    @Override
    public void progressChange(int percent) {
        mToolBg.setAlpha(percent / 100f);
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

    @Override
    public void headerSignClick() {
        mBookShelfPresenter.toSign();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBookShelfHeaderHelper.destory();
    }
}
