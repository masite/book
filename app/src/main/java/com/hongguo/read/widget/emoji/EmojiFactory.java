package com.hongguo.read.widget.emoji;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.LruCache;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by losg on 2017/12/30.
 */

public class EmojiFactory extends EmojiImagesFactory{

    //表情最大长度
    public static final String EMOJI_PREFIX = "";
    public static final String EMOJI_SUFFIX = ".png";
    public static final String EMOJI_PATH   = "emoji/";

    private static final int MIN_LENGTH       = 3;
    private static final int MAX_EMOJI_LENGTH = 10;

    private        LruCache<String, Drawable> mCache;
    private        List<String>               mEmojis;
    private        Context                    mContext;
    private static EmojiFactory               sEmojiFactory;


    public static void init(Context context) {
        sEmojiFactory = new EmojiFactory(context.getApplicationContext());
    }

    public static EmojiFactory getInstance() {
        return sEmojiFactory;
    }

    private EmojiFactory(Context context) {
        super();
        mContext = context;
        initCache();
        initEmojis();
    }

    private void initEmojis() {
        mEmojis = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = mEmojiImages.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            mEmojis.add(entry.getKey());
        }
    }

    private void initCache() {
        mCache = new LruCache<String, Drawable>(4 * 1024 * 1024) {
            @Override
            protected int sizeOf(String key, Drawable value) {
                return value.getIntrinsicHeight() * value.getIntrinsicWidth();
            }
        };
    }

    public Drawable getEmoji(String name) {
        if (mCache.get(name) != null) {
            return mCache.get(name);
        }
        try {
            AssetManager assets = mContext.getAssets();
            name = mEmojiImages.get(name);
            InputStream open = assets.open(EMOJI_PATH + name + EMOJI_SUFFIX);
            return Drawable.createFromStream(open, name + EMOJI_SUFFIX);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<String> getEmojis() {
        return mEmojis;
    }


    public CharSequence parseEmojiChar(Spannable span, int size) {
        EmojiconSpan[] spans = span.getSpans(0, span.length(), EmojiconSpan.class);
        //删除以前
        for (int i = 0; i < spans.length; i++) {
            span.removeSpan(spans[i]);
        }

        String patternStr = "(\\[[^\\[]+])";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(span);
        while (matcher.find()) {
            String name = matcher.group(1);
            if (mEmojis.contains(name)) {
                int start = matcher.start();
                int end = matcher.end();
                EmojiconSpan emojiconSpan = new EmojiconSpan(mContext, name, size, EmojiconSpan.ALIGN_BASELINE, size, 0);
                span.setSpan(emojiconSpan, start, end, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return span;
    }


    public void parseTextEmojiChar(EditText editText, int changeStart, int length, int size) {
        Editable span = editText.getText();

        int endPosition = 0;
        int startPosition = 0;

        //输入的是表情,手动输入的表情不去处理
        String insertWords = editText.getText().toString().substring(changeStart, changeStart + length);
        if (insertWords.length() >= MIN_LENGTH && insertWords.startsWith("[") && insertWords.endsWith("]")) {
            EmojiconSpan[] spans = span.getSpans(0, editText.getText().length(), EmojiconSpan.class);
            endPosition = changeStart + length;
            startPosition = changeStart;
            if (span != null && spans.length >= MAX_EMOJI_LENGTH) {
                //删除现在输入的表情
                span.delete(startPosition, endPosition);
                return;

            }
        } else {
            //前面插入数据,删除后面的表情，防止前面插入的数据看不到
            if (editText.getText().length() > changeStart + length && editText.getText().toString().substring(changeStart + length).startsWith("[" + EMOJI_PREFIX)) {
                endPosition = editText.getText().length();
                EmojiconSpan[] spans = span.getSpans(startPosition, endPosition, EmojiconSpan.class);
                for (EmojiconSpan emojiconSpan : spans) {
                    span.removeSpan(emojiconSpan);
                }
            } else {
                return;
            }
        }
        String matchString = span.toString().substring(startPosition, endPosition);
        String patternStr = "(\\[[^\\[]+])";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(matchString);
        while (matcher.find()) {
            String name = matcher.group(1);
            if (mEmojis.contains(name)) {
                int start = matcher.start();
                int end = matcher.end();
                EmojiconSpan emojiconSpan = new EmojiconSpan(mContext, name, size, EmojiconSpan.ALIGN_BASELINE, size, start + startPosition);
                span.setSpan(emojiconSpan, start + startPosition, end + startPosition, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
