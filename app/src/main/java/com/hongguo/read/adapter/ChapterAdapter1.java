package com.hongguo.read.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2017/8/22.
 */

public class ChapterAdapter1 extends RecyclerView.Adapter<IosRecyclerAdapter.BaseHoder> {

    private List<ChapterBean.Chapters> mDataBeen;

    private Context mContext;

    //vip svip 限时免费
    private boolean mIsFree = false;

    public ChapterAdapter1(Context context, List<ChapterBean.Chapters> dataBeen) {
        mContext = context;
        mDataBeen = dataBeen;
    }


    private boolean bookIsFree(ChapterBean.Chapters chapter) {
        return chapter.isBuy == 1 || chapter.coin == 0 || mIsFree;
    }


    public void setFree(boolean free) {
        mIsFree = free;
    }

    @Override
    public IosRecyclerAdapter.BaseHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(mContext, R.layout.adapter_chapter1, null);
        inflate.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        IosRecyclerAdapter.BaseHoder baseHoder = new IosRecyclerAdapter.BaseHoder(inflate);
        return baseHoder;
    }

    @Override
    public void onBindViewHolder(IosRecyclerAdapter.BaseHoder holder, int position) {
        ChapterBean.Chapters dataBean = mDataBeen.get(position);
        holder.setText(R.id.number, position + ". ");
        holder.setText(R.id.chapter_name, dataBean.chapterName.trim());
        TextView textView = holder.getViewById(R.id.chapter_name);
        TextView number = holder.getViewById(R.id.number);
        TextView downDescribe = holder.getViewById(R.id.down_describe);

//        boolean free = bookIsFree(dataBean);
//
//        if (dataBean.hasDownTotal && free) {
//            textView.setTextColor(0xff666666);
//            number.setTextColor(0xff666666);
//        } else {
//            textView.setTextColor(0xff999999);
//            number.setTextColor(0xff999999);
//        }
//
//        if (free) {
//            hoder.getViewById(R.id.chapter_lock).setVisibility(View.GONE);
//            downDescribe.setVisibility(View.VISIBLE);
//            if (mIsFree && dataBean.coin != 0) {
//                downDescribe.setText("限免");
//            } else if (dataBean.isBuy == 1 && dataBean.coin != 0) {
//                downDescribe.setText("已购买");
//            } else {
//                downDescribe.setText("");
//            }
//        } else {
//            hoder.getViewById(R.id.chapter_lock).setVisibility(View.VISIBLE);
//            downDescribe.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return mDataBeen.size();
    }
}
