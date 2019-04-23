package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.mvp.model.book.detail.BookAuthorBean;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.mvp.model.book.detail.SimilarBookBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailAuthorHelper;
import com.hongguo.read.mvp.ui.book.detail.BookDetailDiscussHelper;
import com.hongguo.read.mvp.ui.book.detail.BookDetailRewardTopHelper;
import com.hongguo.read.mvp.ui.book.detail.BookDetailSimilarHelper;

/**
 * Created by losg on 2017/12/26.
 */

public class BookDetailAdapter extends IosRecyclerAdapter {

    /**
     * 打赏
     */
    private static final int AREA_REWAED  = 0;
    /**
     * 评论
     */
    private static final int AREA_DISCUSS = 1;
    /**
     * 相似书籍
     */
    private static final int AREA_SIMILAR = 2;
    /**
     * 授权信息
     */
    private static final int AREA_AUTHOR  = 3;


    private BookDetailDiscussHelper   mBookDetailDiscussHelper;
    private BookDetailRewardTopHelper mBookDetailRewardTopHelper;
    private BookDetailSimilarHelper   mBookDetailSimilarHelper;
    private BookDetailAuthorHelper    mBookDetailAuthorHelper;

    private int mBookType;

    public BookDetailAdapter(Context context, String bookId, int bookType) {
        super(context);
        mBookType = bookType;
        mBookDetailDiscussHelper = new BookDetailDiscussHelper(context, bookId);
        mBookDetailRewardTopHelper = new BookDetailRewardTopHelper(context,bookId);
        mBookDetailSimilarHelper = new BookDetailSimilarHelper(context);
        mBookDetailAuthorHelper = new BookDetailAuthorHelper(context,mBookType);
    }

    @Override
    protected int getCellTitleResource(int areaPosition) {
        if (areaPosition == AREA_REWAED) {
            return mBookDetailRewardTopHelper.getTileResouce();
        } else if (areaPosition == AREA_DISCUSS) {
            return mBookDetailDiscussHelper.getTileResouce();
        } else if (areaPosition == AREA_SIMILAR) {
            return mBookDetailSimilarHelper.getTileResouce();
        } else if (areaPosition == AREA_AUTHOR) {
            return mBookDetailAuthorHelper.getTileResouce();
        }
        return 0;
    }

    @Override
    protected void initCellTitleView(BaseHoder hoder, int areaPosition) {
        super.initCellTitleView(hoder, areaPosition);
        if (areaPosition == AREA_DISCUSS)
            mBookDetailDiscussHelper.initTitle(hoder);
    }

    @Override
    protected int getFooterResource(int areaPosition) {
        if (areaPosition == AREA_DISCUSS) return mBookDetailDiscussHelper.getFooterResource();
        return super.getFooterResource(areaPosition);
    }

    @Override
    protected void initFooterView(BaseHoder hoder, int areaPosition) {
        super.initFooterView(hoder, areaPosition);
        if (areaPosition == AREA_DISCUSS) mBookDetailDiscussHelper.initFooter(hoder);
    }

    @Override
    protected int getContentResource(int areaPosition) {
        if (areaPosition == AREA_REWAED)
            return mBookDetailRewardTopHelper.getItemResouce();
        if (areaPosition == AREA_DISCUSS)
            return mBookDetailDiscussHelper.getItemResouce();
        if (areaPosition == AREA_SIMILAR) {
            return mBookDetailSimilarHelper.getItemResouce();
        } else if (areaPosition == AREA_AUTHOR) {
            return mBookDetailAuthorHelper.getItemResouce();
        }
        return 0;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        if (areaPosition == AREA_REWAED) {
            mBookDetailRewardTopHelper.initBookRank(hoder, index);
        } else if (areaPosition == AREA_DISCUSS) {
            mBookDetailDiscussHelper.initBookDiscuss(hoder, index);
        } else if (areaPosition == AREA_SIMILAR) {
            mBookDetailSimilarHelper.initBookSimilar(hoder, index);
        } else if (areaPosition == AREA_AUTHOR) {
            mBookDetailAuthorHelper.initBookAuthor(hoder, index);
        }
    }

    @Override
    protected int getAreaSize() {
        return 4;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        if (areaPosition == AREA_REWAED) {
            return mBookDetailRewardTopHelper.getCellCount();
        } else if (areaPosition == AREA_DISCUSS) {
            return mBookDetailDiscussHelper.getCellCount() > 3 ? 3 : mBookDetailDiscussHelper.getCellCount();
        } else if (areaPosition == AREA_SIMILAR) {
            return mBookDetailSimilarHelper.getCellCount();
        } else if (areaPosition == AREA_AUTHOR) {
            return mBookDetailAuthorHelper.getCellCount();
        }
        return 0;
    }

    public void setBookDiscussBean(BookDiscussBean bookDiscussBean) {
        mBookDetailDiscussHelper.setBookDiscussBean(bookDiscussBean);
    }

    public void setAwardRankBean(AwardRankBean awardRankBean) {
        mBookDetailRewardTopHelper.setAwardRankBean(awardRankBean);
    }

    public void setSimilarBookBean(SimilarBookBean similarBookBean) {
        mBookDetailSimilarHelper.setSimilarBookBean(similarBookBean);
    }

    public void setAuthorBean(BookAuthorBean bookAuthorBean) {
        mBookDetailAuthorHelper.setBookAuthorBean(bookAuthorBean);
    }
}
