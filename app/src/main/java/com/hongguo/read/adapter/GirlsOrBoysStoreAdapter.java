package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.read.mvp.ui.booktype.BookTypeWithoutSelectActivity;
import com.hongguo.read.widget.recommenstyle.BaseRecommendStyle;
import com.hongguo.read.widget.recommenstyle.CommonRecommendManager;
import com.hongguo.read.widget.recommenstyle.RcommendItemClickListener;

/**
 * Created time 2017/12/1.
 *
 * @author losg
 */

public class GirlsOrBoysStoreAdapter extends IosRecyclerAdapter implements RcommendItemClickListener {

    private CommonRecommendManager    mCommonRecommendManager;
    private RecommendItemBean.ObjBean mRecommendBook;
    private RecommendItemBean.ObjBean mNewBook;
    private RecommendItemBean.ObjBean mFinishBook;
    private RecommendItemBean.ObjBean mAppendOne;
    private RecommendItemBean.ObjBean mAppendTwo;

    public GirlsOrBoysStoreAdapter(Context context) {
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
        if (areaPosition == 3 || areaPosition == 4) {
            return R.layout.adapter_more_item;
        }
        return R.layout.adapter_item_row_line;
    }

    @Override
    protected void initFooterView(BaseHoder hoder, int areaPosition) {
        super.initFooterView(hoder, areaPosition);
        if (areaPosition == 3 || areaPosition == 4) {
            hoder.getViewById(R.id.more).setOnClickListener(v -> {
                RecommendItemBean.ObjBean item = getItem(areaPosition);
                BookTypeWithoutSelectActivity.toActivity(mContext, item.dataname, item.systemMore, item.status, item.sort);
            });
        }
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
        //畅销精选 + 新书推荐 + 经典完本 + 浪漫 + 古代言情
        return 5;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        int itemCount = getRecommendStyle(areaPosition).getItemCount();
        if (itemCount > 4) return 4;
        return itemCount;
    }

    public void setRecommendBook(RecommendItemBean.ObjBean recommendBook) {
        mRecommendBook = recommendBook;
    }

    public void setNewBook(RecommendItemBean.ObjBean newBook) {
        mNewBook = newBook;
    }

    public void setFinishBook(RecommendItemBean.ObjBean finishBook) {
        mFinishBook = finishBook;
    }

    public void setAppendOneItem(RecommendItemBean.ObjBean appendOne) {
        mAppendOne = appendOne;
    }

    public void setAppendTwoItem(RecommendItemBean.ObjBean appendTwo) {
        mAppendTwo = appendTwo;
    }

    @Override
    public void clicked(String bookId, int bookFrom) {
        BookDetailActivity.toActivity(mContext, bookId, bookFrom);
    }

    private RecommendItemBean.ObjBean getItem(int position) {
        switch (position) {
            case 0:
                return mRecommendBook;
            case 1:
                return mNewBook;
            case 2:
                return mFinishBook;
            case 3:
                return mAppendOne;
            default:
                return mAppendTwo;
        }
    }

    private BaseRecommendStyle getRecommendStyle(int position) {
        return mCommonRecommendManager.getStyle(getItem(position));
    }
}
