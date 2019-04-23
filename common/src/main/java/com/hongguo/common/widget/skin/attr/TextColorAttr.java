package com.hongguo.common.widget.skin.attr;

import android.view.View;
import android.widget.TextView;

import com.hongguo.common.widget.skin.attr.base.SkinAttr;
import com.hongguo.common.widget.skin.utils.SkinResourcesUtils;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:22:53
 */
public class TextColorAttr extends SkinAttr {

    @Override
    protected void applySkin(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (isColor()) {
                tv.setTextColor(SkinResourcesUtils.getColorStateList(attrValueRefId));
            }
        }
    }

    @Override
    protected void applyNightMode(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (isColor()) {
                tv.setTextColor(SkinResourcesUtils.getNightColor(attrValueRefName));
            }
        }
    }
}
