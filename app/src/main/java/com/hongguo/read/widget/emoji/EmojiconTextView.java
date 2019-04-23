package com.hongguo.read.widget.emoji;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;


public class EmojiconTextView extends AppCompatTextView {

    public EmojiconTextView(Context context) {
        super(context);
    }

    public EmojiconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        CharSequence charSequence = EmojiFactory.getInstance().parseEmojiChar(spannableStringBuilder, (int) getTextSize());
        super.setText(charSequence, type);
    }

}
