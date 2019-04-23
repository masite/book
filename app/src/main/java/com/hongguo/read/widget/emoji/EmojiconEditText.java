package com.hongguo.read.widget.emoji;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class EmojiconEditText extends AppCompatEditText {

    private EditClickUp mEditClickUp;

    public EmojiconEditText(Context context) {
        super(context);
    }

    public EmojiconEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiconEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (lengthAfter > 0) {
            updateText(start, lengthAfter);
        } else {
            //删除 删除的表情
            EmojiconSpan[] spans = getText().getSpans(0, text.length(), EmojiconSpan.class);
            for (EmojiconSpan emojiconSpan : spans) {
                if (start <= emojiconSpan.getStartPosition()) {
                    if(start + lengthBefore - 1 >= emojiconSpan.getStartPosition()){
                        getText().removeSpan(emojiconSpan);
                        //只是删除了一部分 把剩余的删除
                        int end = emojiconSpan.getStartPosition() + emojiconSpan.getName().length();
                        if(start + lengthBefore < end){
                            getText().delete(start, start + end - start - lengthAfter);
                        }

                    }else{
                        emojiconSpan.setStartPosition(emojiconSpan.getStartPosition() - lengthBefore);
                    }
                }
            }
        }
    }

    private void updateText(int start, int lengthAfter) {
        EmojiFactory.getInstance().parseTextEmojiChar(this, start, lengthAfter, (int) getTextSize());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
            if(mEditClickUp != null){
                mEditClickUp.editUp();
            }
        }
        return super.onTouchEvent(event);
    }

    public void setEditClickUp(EditClickUp editClickUp) {
        mEditClickUp = editClickUp;
    }

    public interface EditClickUp{
        void editUp();
    }

}
