package com.hongguo.common.widget.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by losg on 2017/6/19.
 */

public class GridCell extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private int startNumber;

    public GridCell(int spanCount, int spacing, int startNumber) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.startNumber = startNumber;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position < startNumber) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        position = position - startNumber;
        int column = position % spanCount;
        int totalSpace = (spanCount + 1) * spacing;
        int perItemSpace = totalSpace / spanCount;
        outRect.set((column + 1) * spacing - column * perItemSpace, spacing, (column + 1) * perItemSpace - (column + 1) * spacing, 0);
    }
}
