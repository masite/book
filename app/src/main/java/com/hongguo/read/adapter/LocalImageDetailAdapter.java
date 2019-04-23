package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.read.R;
import com.hongguo.read.mvp.ui.mine.center.chooseheader.CropActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2018/1/8.
 */

public class LocalImageDetailAdapter extends IosRecyclerAdapter {

    private List<String> mImages;

    public LocalImageDetailAdapter(Context context, List<String> images) {
        super(context);
        mImages = images;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_local_image_picker_detail;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        ImageLoadUtils.loadUrlDefault(hoder.getViewById(R.id.local_image), mImages.get(index));
        hoder.itemView.setOnClickListener(v->{
            CropActivity.toActivity(mContext, mImages.get(index));
        });
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mImages.size();
    }
}
