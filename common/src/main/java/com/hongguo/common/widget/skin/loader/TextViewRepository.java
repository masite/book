package com.hongguo.common.widget.skin.loader;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

import com.hongguo.common.widget.skin.utils.TypefaceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by _SOLID
 * Date:2016/7/12
 * Time:17:58
 */
class TextViewRepository {
    private static Map<Activity, List<TextView>> mTextViewMap = new HashMap<>();

    static void add(Activity activity, TextView textView) {

        if (mTextViewMap.containsKey(activity)) {
            mTextViewMap.get(activity).add(textView);
        } else {
            List<TextView> textViews = new ArrayList<>();
            textViews.add(textView);
            mTextViewMap.put(activity, textViews);
        }
        textView.setTypeface(TypefaceUtils.CURRENT_TYPEFACE);
    }

    static void remove(Activity activity) {
        mTextViewMap.remove(activity);
    }

    static boolean remove(Activity activity, TextView textView) {
        return mTextViewMap.containsKey(activity) && mTextViewMap.get(activity).remove(textView);
    }

    static void applyFont(Typeface tf) {
        for (Activity activity : mTextViewMap.keySet()) {
            for (TextView textView : mTextViewMap.get(activity)) {
                textView.setTypeface(tf);
            }
        }
    }
}
