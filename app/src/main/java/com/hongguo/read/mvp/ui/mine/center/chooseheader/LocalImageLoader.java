package com.hongguo.read.mvp.ui.mine.center.chooseheader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by losg on 2016/4/1.
 */
public class LocalImageLoader extends AsyncTaskLoader<LinkedHashMap<String, ArrayList<String>>> {

    private LinkedHashMap<String, ArrayList<String>> mImages;
    private final String RECENTPIC = "最近图片";

    public LocalImageLoader(Context context) {
        super(context);
    }

    @Override
    public LinkedHashMap<String, ArrayList<String>> loadInBackground() {
        LinkedHashMap<String, ArrayList<String>> images = new LinkedHashMap<>();
        images.put(RECENTPIC, new ArrayList<String>());
        Cursor imageCursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATE_ADDED,MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATA}, null, null, MediaStore.Images.Media.DATE_ADDED+" desc");
        if(imageCursor != null && imageCursor.getCount() > 0){
            while(imageCursor.moveToNext()){
                if(images.get(imageCursor.getString(1)) == null){
                    images.put(imageCursor.getString(1), new ArrayList<String>());
                }
                List<String> image = images.get(imageCursor.getString(1));
                String path = imageCursor.getString(2);
                long  lastModify = imageCursor.getLong(0);
                image.add(path);
                if(lastModify * 1000 >= (System.currentTimeMillis() - (long)60 *  24 * 60 * 60 * 1000)){
                    ArrayList<String> imageBeen = images.get(RECENTPIC);
                    imageBeen.add(path);
                }
            }
        }
        if(images.get(RECENTPIC).size() == 0){
            images.remove(RECENTPIC);
        }
        return images;
    }

    @Override
    public void deliverResult(LinkedHashMap<String, ArrayList<String>> images) {
        if (isReset()) {
            if (images != null) {
                images.clear();
                images = null;
            }
            return;
        }
        LinkedHashMap<String, ArrayList<String>> oldImages = mImages;
        mImages = images;
        if (isStarted()) {
            super.deliverResult(images);
        }
        if (oldImages != null && oldImages != mImages) {
            oldImages.clear();
            oldImages = null;
        }
    }

    @Override
    protected void onStartLoading() {
        if (mImages != null && mImages.size() > 0) {
            deliverResult(mImages);
        }

        if (takeContentChanged() || mImages == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(LinkedHashMap<String, ArrayList<String>> data) {
        if (data != null) {
            data.clear();
            data = null;
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mImages != null) {
            mImages.clear();
            mImages = null;
        }
    }
}
