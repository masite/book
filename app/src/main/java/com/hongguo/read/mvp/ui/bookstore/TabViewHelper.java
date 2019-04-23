package com.hongguo.read.mvp.ui.bookstore;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.read.R;

/**
 * Created by losg on 2018/2/9.
 */

public class TabViewHelper {

    private Context   mContext;
    private View      mView;
    private ImageView mTabView;
    private TextView  mTabText;

    public TabViewHelper(Context context) {
        mContext = context;
        mView = View.inflate(mContext, R.layout.view_tab, null);
        mTabView = (ImageView) mView.findViewById(R.id.tab_image);
        mTabText = (TextView) mView.findViewById(R.id.tab_text);
    }

    public void setInfo(Drawable tabImage, String tabText, int tabTextColor) {
        mTabView.setImageDrawable(tabImage);
        mTabText.setText(tabText);
        mTabText.setTextColor(tabTextColor);
    }

    public View getView(){
        return mView;
    }

}
