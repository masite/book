package com.hongguo.read.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;


/**
 * Created by Administrator on 2017/7/3.
 */

public class MenuContenter extends LinearLayout implements View.OnClickListener {

    private ColorStateList        mColor;
    private int                   mItemMargin;
    private int                   mViewPadding;
    private float                 mTextSize;
    private MenuAddListener       mMenuAddListener;
    private MenuItemClickListener mMenuItemClickListener;


    public MenuContenter(Context context) {
        this(context, null);
    }

    public MenuContenter(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuContenter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.MenuContenter);
        mColor = ty.getColorStateList(R.styleable.MenuContenter_Menu_Color);
        float defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        float defaultPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        float defaultMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        mTextSize = ty.getDimension(R.styleable.MenuContenter_Menu_textSize, defaultSize);
        mItemMargin = (int) ty.getDimension(R.styleable.MenuContenter_Menu_menuMargin, defaultMargin);
        mViewPadding = (int) ty.getDimension(R.styleable.MenuContenter_Menu_menuPadding, defaultPadding);
        ty.recycle();
    }

    public void showMenus(String[] menus, int[] menuResource) {
        for (int i = 0; i < menus.length; i++) {
            LinearLayout menuItem = createMenuItem(menus[i], menuResource[i], i);
            menuItem.setOnClickListener(this);
            addView(menuItem);
        }
        setMenuSelected(0);
    }


    public void setMenuSelected(int position) {
        if (position > getChildCount()) return;
        if (getChildAt(position).isSelected()) return;
        normalAll();
        LinearLayout linearLayout = (LinearLayout) getChildAt(position);
        linearLayout.setSelected(true);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            linearLayout.getChildAt(i).setSelected(true);
        }

        if (mMenuItemClickListener != null) {
            mMenuItemClickListener.menuClick(position);
        }
    }


    private void normalAll() {
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) getChildAt(i);
            linearLayout.setSelected(false);
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                linearLayout.getChildAt(j).setSelected(false);
            }
        }
    }

    private LinearLayout createMenuItem(String menu, int icon, int position) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setPadding(0, mItemMargin, 0, mItemMargin);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setClickable(true);

        MenuImageView imageView = new MenuImageView(getContext());
        imageView.setImageResource(icon);
        linearLayout.addView(imageView, new LayoutParams(-2, -2));

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        textView.setText(menu);
        if (mColor != null) {
            textView.setTextColor(mColor);
        }
        if(mMenuAddListener != null){
            mMenuAddListener.menuAdd(imageView, textView, position);
        }
        LayoutParams params = new LayoutParams(-2, -2);
        params.setMargins(0, mItemMargin, 0, 0);
        linearLayout.addView(textView, params);

        params = new LayoutParams(0, -2);
        params.weight = 1;
        params.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }


    public void setMenuItemClickListener(MenuItemClickListener menuItemClickListener) {
        mMenuItemClickListener = menuItemClickListener;
    }


    public void setMenuAddListener(MenuAddListener menuAddListener) {
        mMenuAddListener = menuAddListener;
    }

    @Override
    public void onClick(View v) {
        if (v.isSelected()) return;
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == v) {
                setMenuSelected(i);
                break;
            }
        }
    }

    public interface MenuItemClickListener {
        void menuClick(int position);
    }

    public interface MenuAddListener {
        void menuAdd(ImageView menuIcon, TextView menuText, int position);
    }

}
