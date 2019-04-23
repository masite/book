package com.hongguo.read.widget.openbook;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hongguo.common.utils.rxjava.RxJavaUtils;

/**
 * Created by losg on 2017/4/28.
 */


public class OpenBookManager implements OpenBookView.OpenBookListener {

    private OpenBookView  mOpenBookViews;
    private WindowManager mWindowManager;

    private boolean mOpenStart = false;
    private OpenBookView.OpenBookListener mOpenBookListener;

    public OpenBookManager(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mOpenBookViews = new OpenBookView(context);
        mOpenBookViews.setOpenBookListener(this);

    }

    public void onResume(RecyclerView recyclerView){
        closeBook(recyclerView);
    }

    public void openBook(ImageView imageView) {
        if (mOpenStart) {
            return;
        }
        mOpenStart = true;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, 0, 0, WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_FULLSCREEN, PixelFormat.RGBA_8888);
        mWindowManager.addView(mOpenBookViews, params);
        mOpenBookViews.setCoverBitmap(imageView);
        boolean start = mOpenBookViews.start();
        if(!start){
            mOpenStart = false;
            mWindowManager.removeView(mOpenBookViews);
        }
    }

    public void closeBook(RecyclerView recyclerView) {
        if (!mOpenStart) {
            return;
        }

        RxJavaUtils.delayRun(300, () -> {
                LinearLayout linearLayout = (LinearLayout) recyclerView.getLayoutManager().getChildAt(1);
                ImageView currentImage = (ImageView) ((FrameLayout) linearLayout.getChildAt(0)).getChildAt(0);
                RectF imageLocal = mOpenBookViews.getImageLocal(currentImage);
                mOpenBookViews.close(imageLocal);
        });
    }

    @Override
    public void openFinish() {
        RxJavaUtils.delayRun(200, () -> mOpenBookListener.openFinish());
    }

    @Override
    public void closeFinish() {
        mOpenStart = false;
        RxJavaUtils.delayRun(200, () -> {
            try {
                mWindowManager.removeView(mOpenBookViews);
                mOpenBookListener.closeFinish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setOpenBookListener(OpenBookView.OpenBookListener openBookListener) {
        mOpenBookListener = openBookListener;
    }

}
