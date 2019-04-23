package com.hongguo.common.utils.imageLoad;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hongguo.read.base.R;
import com.losg.library.utils.DisplayUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by losg on 2017/8/1.
 */

public class ImageLoadUtils {

    private static final int ROUND_SIZE = 4;

    public static void loadingBannel(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext()).load(url).transition(new DrawableTransitionOptions()).placeholder(R.mipmap.ic_image_bannel_default).into(imageView);
    }

    public static void loadLoadingPage(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext().getApplicationContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade().crossFade(600))
//                .placeholder(R.mipmap.ic_loading)
                .into(imageView);
    }
    public static void loadUrlWithoutRound(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext()).load(url).transition(new DrawableTransitionOptions()).placeholder(R.mipmap.ic_image_normal_default).into(imageView);
    }

    public static void loadResourceDefault(ImageView imageView, int resource) {
        Glide.with(imageView.getContext().getApplicationContext()).load(resource).into(imageView);
    }

    public static void loadUrlDefault(ImageView imageView, String url) {
        Glide.with(imageView.getContext().getApplicationContext()).load(url).transition(new DrawableTransitionOptions()).into(imageView);
    }

    public static void loadUrl(ImageView imageView, String url) {
        int size = DisplayUtil.dip2px(imageView.getContext().getApplicationContext(), ROUND_SIZE);
        GlideApp.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(size)))
                .transition(new DrawableTransitionOptions().crossFade(300))
                .placeholder(R.mipmap.ic_image_normal_default)
                .into(imageView);
    }

    public static void loadCircleUrl(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext())
                .load(url)
                .transition(new DrawableTransitionOptions())
                .apply(new RequestOptions().transforms(new CenterCrop(), new CircleCrop()))
                .placeholder(R.mipmap.ic_default_avatar)
                .into(imageView);
    }

    public static void loadUrlWithGradient(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext())
                .load(url)
                .transition(new DrawableTransitionOptions())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 10)))
                .transition(new DrawableTransitionOptions().crossFade(300))
                .into(imageView);
    }


    public static void loadUrl(ImageView imageView, String url, ImageLoadLister loadLister) {
        int size = DisplayUtil.dip2px(imageView.getContext(), ROUND_SIZE);
        GlideApp.with(imageView.getContext().getApplicationContext()).load(url).placeholder(R.mipmap.ic_image_normal_default).transform(new RoundedCornersTransformation(size, 0)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (loadLister != null) loadLister.error();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (loadLister != null) loadLister.loadSuccess();
                return false;
            }
        }).into(imageView);
    }

    public static void loadUrlDefault(ImageView imageView, String url, ImageLoadLister loadLister) {
        GlideApp.with(imageView.getContext().getApplicationContext()).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (loadLister != null) loadLister.error();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (loadLister != null) loadLister.loadSuccess();
                return false;
            }
        }).into(imageView);
    }

    public interface ImageLoadLister {
        void loadSuccess();

        void error();
    }
}
