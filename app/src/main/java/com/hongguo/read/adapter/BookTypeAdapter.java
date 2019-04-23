package com.hongguo.read.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.ui.booktype.BookTypeDetailActivity;
import com.hongguo.read.repertory.data.ClassifyRepertory;

import java.util.List;

/**
 * Created by losg
 */

public class BookTypeAdapter extends IosRecyclerAdapter {


    private List<ClassifyRepertory.Classify> mClassifies;

    public BookTypeAdapter(Context context, List<ClassifyRepertory.Classify> classifies) {
        super(context);
        mClassifies = classifies;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_book_type_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {

        ClassifyRepertory.Classify classify = mClassifies.get(index);

        ImageView classifyIcon = hoder.getViewById(R.id.classify_icon);
        TextView classifyName = hoder.getViewById(R.id.classify_name);

        classifyIcon.setImageResource(classify.resource);
        classifyName.setText(classify.name);

        hoder.itemView.setOnClickListener(v -> {
            BookTypeDetailActivity.toActivity(mContext, classify.id);
        });
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mClassifies.size();
    }

}
