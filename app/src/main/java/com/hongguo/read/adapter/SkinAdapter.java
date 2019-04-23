package com.hongguo.read.adapter;

import android.content.Context;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.mine.SkinBean;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2018/1/8.
 */

public class SkinAdapter extends IosRecyclerAdapter {

    private List<SkinBean.SkinlistBean> mSkinlistBeans;
    private String                      mCurrentSelected;
    private SkinItemClick               mSkinItemClick;

    public SkinAdapter(Context context, List<SkinBean.SkinlistBean> skinlistBeans) {
        super(context);
        mSkinlistBeans = skinlistBeans;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_skin_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        SkinBean.SkinlistBean skinlistBean = mSkinlistBeans.get(index);
        ImageLoadUtils.loadUrlDefault(hoder.getViewById(R.id.skinImageView), skinlistBean.skinimage);
        hoder.setText(R.id.skin_title, skinlistBean.name);
        TextView down = hoder.getViewById(R.id.down_skin);
        down.setTag(skinlistBean.skinurl);

        down.setEnabled(skinlistBean.enable);
        down.setText(skinlistBean.showMsg);

        if (skinlistBean.name.equals(mCurrentSelected) && skinlistBean.fileExist) {
            down.setText("已使用");
            down.setEnabled(false);
        }

        down.setOnClickListener(v -> {
            if (mSkinItemClick != null) {
                mSkinItemClick.skinClick(index, skinlistBean.skinurl, skinlistBean.name, !skinlistBean.fileExist);
            }
        });

    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mSkinlistBeans.size();
    }


    public void setCurrentSelected(String currentSelected) {
        mCurrentSelected = currentSelected;
    }

    public void setSkinItemClick(SkinItemClick skinItemClick) {
        mSkinItemClick = skinItemClick;
    }

    public interface SkinItemClick {
        void skinClick(int position, String url, String name, boolean needDown);
    }
}
