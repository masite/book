package com.hongguo.read.mvp.ui.book.detail;

import android.content.Context;

import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.book.detail.SimilarBookBean;
import com.hongguo.read.widget.CommonThreeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by losg on 2017/12/26.
 */

public class BookDetailSimilarHelper {

    private Context                        mContext;
    private SimilarBookBean                mSimilarBookBean;
    private List<SimilarBookBean.DataBean> mChooseSimilar;


    public BookDetailSimilarHelper(Context context) {
        mContext = context;
    }

    public int getTileResouce() {
        return 0;
    }

    public void initTitle(IosRecyclerAdapter.BaseHoder hoder) {
    }

    public int getCellCount() {
        if (mSimilarBookBean == null) return 0;
        return mSimilarBookBean.data.size() == 0 ? 0 : 1;
    }

    public int getItemResouce() {
        return R.layout.view_book_detail_similar;
    }

    public void initBookSimilar(IosRecyclerAdapter.BaseHoder hoder, int position) {
        CommonThreeItem freeThireItem = hoder.getViewById(R.id.free_items);
        freeThireItem.setLoadImage(ImageLoadUtils::loadUrl);
        if (mChooseSimilar == null)
            mChooseSimilar = radomItem();
        freeThireItem.setLines(1);
        for (int i = 0; i < 3; i++) {
            if (i >= mChooseSimilar.size()) {
                freeThireItem.setPositionVisiable(i, false);
            } else {
                freeThireItem.setPositionVisiable(i, true);
                freeThireItem.setName(i, mChooseSimilar.get(i).bookName);
                freeThireItem.setImageUrl(i, mChooseSimilar.get(i).coverPicture);
            }
        }

        //换一换
        hoder.getViewById(R.id.change_similar).setOnClickListener(v->{
            mChooseSimilar = radomItem();
            initBookSimilar(hoder, position);
        });

        freeThireItem.setItemClickListener(position1 -> {
            SimilarBookBean.DataBean dataBean = mChooseSimilar.get(position1);
            BookDetailActivity.toActivity(mContext, dataBean.bookId, dataBean.bookType);
        });
    }

    public void setSimilarBookBean(SimilarBookBean similarBookBean) {
        mSimilarBookBean = similarBookBean;
    }

    private List<SimilarBookBean.DataBean> radomItem() {
        List<SimilarBookBean.DataBean> orgin = new ArrayList<>();
        orgin.addAll(mSimilarBookBean.data);

        List<SimilarBookBean.DataBean> dataBeans = new ArrayList<>();
        int totalSize = orgin.size();
        int size = totalSize >= 3 ? 3 : totalSize;
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            SimilarBookBean.DataBean remove = orgin.remove(random.nextInt(orgin.size()));
            dataBeans.add(remove);
        }
        return dataBeans;
    }
}
