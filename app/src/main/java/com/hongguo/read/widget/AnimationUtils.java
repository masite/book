package com.hongguo.read.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by losg on 2018/1/16.
 */

public class AnimationUtils {

    public static void animInFromTop(View view, int time, AnimationSuccessListener animationSuccessListener) {
        if(view.getMeasuredHeight() == 0){
            view.measure(0, 0);
        }
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", -view.getMeasuredHeight() + 1, 0);
        translationY.setDuration(time);
        translationY.setInterpolator(new DecelerateInterpolator());
        translationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(animationSuccessListener != null){
                    animationSuccessListener.success();
                }
            }
        });
        translationY.start();
    }

    public static void animOutToTop(View view, int time, AnimationSuccessListener animationSuccessListener) {
        if(view.getMeasuredHeight() == 0){
            view.measure(0, 0);
        }
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0, -view.getMeasuredHeight() + 1);
        translationY.setDuration(time);
        translationY.setInterpolator(new DecelerateInterpolator());
        translationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(animationSuccessListener != null){
                    animationSuccessListener.success();
                }
            }
        });
        translationY.start();
    }

    public static void animInFromBottom(View view, int time, AnimationSuccessListener animationSuccessListener) {
        if(view.getMeasuredHeight() == 0){
            view.measure(0, 0);
        }
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight() - 1, 0);
        translationY.setDuration(time);
        translationY.setInterpolator(new DecelerateInterpolator());
        translationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(animationSuccessListener != null){
                    animationSuccessListener.success();
                }
            }
        });
        translationY.start();
    }


    public static void animOutToBottom(View view, int time, AnimationSuccessListener animationSuccessListener) {
        if(view.getMeasuredHeight() == 0){
            view.measure(0, 0);
        }
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0, view.getMeasuredHeight()  - 1);
        translationY.setDuration(time);
        translationY.setInterpolator(new DecelerateInterpolator());
        translationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(animationSuccessListener != null){
                    animationSuccessListener.success();
                }
            }
        });
        translationY.start();
    }

    public interface AnimationSuccessListener {
        void success();
    }
}
