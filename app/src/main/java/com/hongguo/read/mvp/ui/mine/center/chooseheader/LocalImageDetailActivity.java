package com.hongguo.read.mvp.ui.mine.center.chooseheader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hongguo.common.widget.recycler.GridCell;
import com.hongguo.read.R;
import com.hongguo.read.adapter.LocalImageDetailAdapter;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.eventbus.UploadAvatarEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/8.
 */

public class LocalImageDetailActivity extends ActivityEx {

    private static final String INTENT_IMAGES = "intent_images";
    private static final String INTENT_TITLE  = "intent_title";

    @BindView(R.id.image_list)
    RecyclerView mImageList;

    private ArrayList<String> mImages;

    public static void toActivity(Context context, String title, ArrayList<String> images) {
        Intent intent = new Intent(context, LocalImageDetailActivity.class);
        intent.putStringArrayListExtra(INTENT_IMAGES, images);
        intent.putExtra(INTENT_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_local_image_detail;
    }

    @Override
    protected void initView() {
        mImages = getIntent().getStringArrayListExtra(INTENT_IMAGES);
        String title = getIntent().getStringExtra(INTENT_TITLE);
        setTitle(title);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        mImageList.setLayoutManager(gridLayoutManager);
        mImageList.addItemDecoration(new GridCell(4, (int) getResources().getDimension(R.dimen.line_space), 0));
        mImageList.setAdapter(new LocalImageDetailAdapter(mContext, mImages));
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
