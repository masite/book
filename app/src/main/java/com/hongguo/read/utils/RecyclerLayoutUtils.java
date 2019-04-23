package com.hongguo.read.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created time 2017/12/1.
 *
 * @author losg
 */

public class RecyclerLayoutUtils {

    public static LinearLayoutManager createVertical(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

    public static LinearLayoutManager createHorizontal(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        return linearLayoutManager;
    }

    public static GridLayoutManager createTitleGridManager(Context context, int cell) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, cell);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0)
                    return cell;
                return 1;
            }
        });
        return layoutManager;
    }

    public static <T> T getViewByTag(RecyclerView recyclerView, Object tag){
        return (T) recyclerView.findViewWithTag(tag);
    }



}
