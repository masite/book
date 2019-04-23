package com.hongguo.read.widget.emoji;

import java.util.HashMap;

/**
 * Created by losg on 2018/1/1.
 */

public class EmojiImagesFactory {

    protected HashMap<String, String> mEmojiImages;

    public EmojiImagesFactory() {
        mEmojiImages = new HashMap<>();
        mEmojiImages.put("[傲慢]", "aoman");
        mEmojiImages.put("[鄙视]", "bishi");
        mEmojiImages.put("[闭嘴]", "bizui");
        mEmojiImages.put("[吃惊]", "chijing");
        mEmojiImages.put("[大兵]", "dabing");
        mEmojiImages.put("[大笑]", "daxiao");
        mEmojiImages.put("[发呆]", "fadai");
        mEmojiImages.put("[尴尬]", "ganga");
        mEmojiImages.put("[鼓掌]", "guzhang");
        mEmojiImages.put("[害羞]", "haixiu");

        mEmojiImages.put("[憨笑]", "hanxiao");
        mEmojiImages.put("[滑稽]", "huaji");
        mEmojiImages.put("[惊恐]", "jingkong");
        mEmojiImages.put("[惊讶]", "jingya");
        mEmojiImages.put("[可爱]", "keai");
        mEmojiImages.put("[可怜]", "kelian");
        mEmojiImages.put("[哭泣]", "kuqi");
        mEmojiImages.put("[酷]", "ku");
        mEmojiImages.put("[敲打]", "qiaoda");
        mEmojiImages.put("[亲亲]", "qinqin");
        mEmojiImages.put("[色]", "se");
        mEmojiImages.put("[生气]", "shengqi");
        mEmojiImages.put("[衰]", "sui");
        mEmojiImages.put("[帅]", "shuai");
        mEmojiImages.put("[睡觉]", "shuijiao");
        mEmojiImages.put("[偷笑]", "touxiao");
        mEmojiImages.put("[微笑]", "weixiao");
        mEmojiImages.put("[委屈]", "weiqu");
        mEmojiImages.put("[要哭了]", "yaokule");
        mEmojiImages.put("[疑问]", "yiwen");
        mEmojiImages.put("[右哼]", "youheng");
        mEmojiImages.put("[再见]", "zaijian");
        mEmojiImages.put("[折磨]", "zhemo");
        mEmojiImages.put("[左哼]", "zuoheng");
    }

    public HashMap<String, String> getEmojiImages() {
        return mEmojiImages;
    }
}
