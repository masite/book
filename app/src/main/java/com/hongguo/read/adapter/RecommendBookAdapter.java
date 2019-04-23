package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.read.widget.recommenstyle.BaseRecommendStyle;
import com.hongguo.read.widget.recommenstyle.CommonRecommendManager;
import com.hongguo.read.widget.recommenstyle.RcommendItemClickListener;

/**
 * Created time 2017/12/1.
 *
 * @author losg
 */

public class RecommendBookAdapter extends IosRecyclerAdapter implements RcommendItemClickListener {

    private CommonRecommendManager    mCommonRecommendManager;
    private RecommendItemBean.ObjBean mFavorBook;
    private RecommendItemBean.ObjBean mHotRecommend;
    private RecommendItemBean.ObjBean mHotBook;
    private RecommendItemBean.ObjBean mNewBook;
    private RecommendItemBean.ObjBean mRecommend;

    public RecommendBookAdapter(Context context) {
        super(context);
        mCommonRecommendManager = new CommonRecommendManager(context);
    }

    @Override
    protected int getCellTitleResource(int areaPosition) {
        return getRecommendStyle(areaPosition).getTitleResource();
    }


    @Override
    protected void initCellTitleView(BaseHoder hoder, int areaPosition) {
        BaseRecommendStyle recommendStyle = getRecommendStyle(areaPosition);
        recommendStyle.initTitleView(hoder.itemView);
        recommendStyle.setRcommendItemClickListener(this);
    }

    @Override
    protected int getFooterResource(int areaPosition) {
        switch (areaPosition) {
            case 0:
                if (mHotRecommend == null || mHotRecommend.data.size() == 0) return 0;
                break;
            case 1:
                if (mHotBook == null || mHotBook.data.size() == 0) return 0;
                break;
            case 2:
                if (mNewBook == null || mNewBook.data.size() == 0) return 0;
                break;
            case 3:
                if (mRecommend == null || mRecommend.data.size() == 0) return 0;
                break;
            case 4:
                return 0;
        }
        return R.layout.adapter_item_row_line;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return getRecommendStyle(areaPosition).getItemResource();
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        BaseRecommendStyle recommendStyle = getRecommendStyle(areaPosition);
        recommendStyle.initItemView(hoder.itemView, index);
        recommendStyle.setRcommendItemClickListener(this);
    }

    @Override
    protected int getAreaSize() {
        return 5;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return getRecommendStyle(areaPosition).getItemCount();
    }

    @Override
    public void clicked(String bookId, int bookFrom) {
        BookDetailActivity.toActivity(mContext, bookId, bookFrom);
    }

    private BaseRecommendStyle getRecommendStyle(int areaPosition) {
        switch (areaPosition) {
            case 0:
                return mCommonRecommendManager.getStyle(mHotRecommend);
            case 1:
                return mCommonRecommendManager.getStyle(mHotBook);
            case 2:
                return mCommonRecommendManager.getStyle(mNewBook);
            case 3:
                return mCommonRecommendManager.getStyle(mRecommend);
            case 4:
                return mCommonRecommendManager.getStyle(mFavorBook);
        }
        return null;
    }


    public void setFavorBook(RecommendItemBean.ObjBean favorBook) {
        mFavorBook = favorBook;
    }

    public void setHotRecommend(RecommendItemBean.ObjBean hotRecommend) {
        mHotRecommend = hotRecommend;
    }

    public void setHotBook(RecommendItemBean.ObjBean hotBook) {
        mHotBook = hotBook;
    }

    public void setNewBook(RecommendItemBean.ObjBean newBook) {
        mNewBook = newBook;
    }

    public void setRecommend(RecommendItemBean.ObjBean recommend) {
        mRecommend = recommend;
    }
}
