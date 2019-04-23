package com.hongguo.read.mvp.ui.discuss;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.common.utils.StringUtil;
import com.hongguo.common.utils.TimeUtils;
import com.hongguo.read.R;
import com.hongguo.read.base.BaseViewHelper;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.emoji.EmojiconTextView;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/14.
 */

public class ReplyHeaderHelper extends BaseViewHelper {

    @BindView(R.id.user_avatar)
    ImageView        mUserAvatar;
    @BindView(R.id.user_name)
    TextView         mUserName;
    @BindView(R.id.discuss_content)
    EmojiconTextView mDiscussContent;
    @BindView(R.id.reply_time)
    TextView         mReplyTime;
    @BindView(R.id.reply_number)
    TextView         mReplyNumber;
    @BindView(R.id.all_discuss)
    TextView         mAllDiscuss;

    public ReplyHeaderHelper(Context context) {
        super(context);
    }

    @Override
    protected int initLayout() {
        return R.layout.view_reply_title;
    }

    public void setDiscussInfo(BookDiscussBean.DataBean discussInfo) {
        ImageLoadUtils.loadCircleUrl(mUserAvatar, discussInfo.headImgUrl);
        mUserName.setText(discussInfo.nickName);
        mReplyTime.setText(TimeUtils.getTime(discussInfo.cDate));

        String discussContent = StringUtil.unicode2String(discussInfo.title);
        if (discussContent.endsWith("//////")) {
            discussContent = discussContent.replace("//////", "");
        }
        mDiscussContent.setText(discussContent.trim());
        mReplyNumber.setText(discussInfo.replyTimes + "条回复");
    }
}
