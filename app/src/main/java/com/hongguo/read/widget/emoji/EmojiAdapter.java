package com.hongguo.read.widget.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.hongguo.read.R;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;


/**
 * Created by losg on 2017/12/30.
 */

public class EmojiAdapter extends IosRecyclerAdapter {

    private final EmojiFactory       mEmojiFactory;
    private       EmojiClikcListener mEmojiClikcListener;

    public EmojiAdapter(Context context) {
        super(context);
        mEmojiFactory = EmojiFactory.getInstance();
    }


    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.view_emoji;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        ImageView imageView = hoder.getViewById(R.id.image1);
        Log.e("losg_log", "---"+(mEmojiFactory.getEmojis().get(index)));
        Drawable emoji = mEmojiFactory.getEmoji(mEmojiFactory.getEmojis().get(index));
        emoji.setBounds(0, 0, emoji.getIntrinsicWidth(), emoji.getIntrinsicHeight());
        imageView.setImageDrawable(emoji);
        if (mEmojiClikcListener != null) {
            hoder.itemView.setOnClickListener(v -> {
                mEmojiClikcListener.emojiClick(mEmojiFactory.getEmojis().get(index));
            });
        }
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mEmojiFactory.getEmojis().size();
    }

    public void setEmojiClikcListener(EmojiClikcListener emojiClikcListener) {
        mEmojiClikcListener = emojiClikcListener;
    }

    public interface EmojiClikcListener {
        void emojiClick(String str);
    }
}
