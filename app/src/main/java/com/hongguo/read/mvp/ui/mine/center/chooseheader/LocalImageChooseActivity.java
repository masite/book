package com.hongguo.read.mvp.ui.mine.center.chooseheader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;

import com.hongguo.read.R;
import com.hongguo.read.adapter.LocalImageAdapter;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.eventbus.UploadAvatarEvent;
import com.hongguo.read.utils.RecyclerLayoutUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/8.
 */

public class LocalImageChooseActivity extends ActivityEx implements LoaderManager.LoaderCallbacks<LinkedHashMap<String, ArrayList<String>>>{

    @BindView(R.id.image_list)
    RecyclerView mImageList;

    private LinkedHashMap<String, ArrayList<String>> mImages;
    private LocalImageAdapter mAdapter;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, LocalImageChooseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_choose_header;
    }

    @Override
    protected void initView() {
        setTitle("图片列表");
        mImages = new LinkedHashMap<>();
        mImageList.setLayoutManager(RecyclerLayoutUtils.createVertical(mContext));
        mAdapter = new LocalImageAdapter(mContext, mImages);
        mImageList.setAdapter(mAdapter);
        LoaderManager supportLoaderManager = getSupportLoaderManager();
        supportLoaderManager.initLoader(0, null, this);
    }

    @Override
    public Loader<LinkedHashMap<String, ArrayList<String>>> onCreateLoader(int id, Bundle args) {
        return new LocalImageLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LinkedHashMap<String, ArrayList<String>>> loader, LinkedHashMap<String, ArrayList<String>> data) {
        mImages.clear();
        mImages.putAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<LinkedHashMap<String, ArrayList<String>>> loader) {

    }


    /**
     * {@link CropActivity}
     * @param avatarEvent
     */
    @Subscribe
    public void onEvent(UploadAvatarEvent avatarEvent){
        finish();
    }
}
