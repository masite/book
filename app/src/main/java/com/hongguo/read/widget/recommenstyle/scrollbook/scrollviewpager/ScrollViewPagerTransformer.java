package com.hongguo.read.widget.recommenstyle.scrollbook.scrollviewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by losg
 */

public class ScrollViewPagerTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SIZE = 0.6f;
    private static final float MAX_MIN_SIZE = 0.4f;

    private static final float MIN_ALPAH_SIZE = 0.6f;
    private static final float MAX_MIN__ALPAH_SIZE = 0.3f;

    @Override
    public void transformPage(View view, float position) {
        if(position < -1){
            view.setScaleX(0);
            view.setScaleY(0);
            view.setAlpha(0);
        }else if(position < 0){
            // -1 - 0   MAX_MIN_SIZE - MIN_SIZE
            view.setScaleX(MIN_SIZE + (MIN_SIZE - MAX_MIN_SIZE) * position);
            view.setScaleY(MIN_SIZE + (MIN_SIZE - MAX_MIN_SIZE) * position);
            view.setAlpha(MIN_ALPAH_SIZE + (MIN_ALPAH_SIZE - MAX_MIN__ALPAH_SIZE) * position);
        }else if(position < 1){
            // 0 - 1  MIN_SIZE - 1
            view.setScaleX(MIN_SIZE + (1 - MIN_SIZE) * position);
            view.setScaleY(MIN_SIZE + (1 - MIN_SIZE) * position);
            view.setAlpha(MIN_ALPAH_SIZE + (1 - MIN_ALPAH_SIZE) * position);
        }else if(position < 2){
            // 1 - 2   1 - MIN_SIZE
            view.setScaleX(MIN_SIZE + (1 - MIN_SIZE) * (2 - position));
            view.setScaleY(MIN_SIZE + (1 - MIN_SIZE) * (2 - position));
            view.setAlpha(MIN_ALPAH_SIZE + (1 - MIN_ALPAH_SIZE) * (2 - position));
        }else if(position < 3){
            //  2 - 3 MIN_SIZE - MAX_MIN_SIZE
            view.setScaleX(MAX_MIN_SIZE + (MAX_MIN_SIZE - MIN_SIZE) * (position - 3));
            view.setScaleY(MAX_MIN_SIZE + (MAX_MIN_SIZE - MIN_SIZE) * (position - 3));
            view.setAlpha(MAX_MIN__ALPAH_SIZE + (MAX_MIN__ALPAH_SIZE - MIN_ALPAH_SIZE) * (position - 3));
        }else{
            view.setScaleX(0);
            view.setScaleY(0);
            view.setAlpha(0);
        }

    }
}
