package com.hongguo.read.mvp.ui.book.detail;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.hongguo.common.utils.StringUtil;
import com.hongguo.common.utils.TimeUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.eventbus.DiscussTitleClickEvent;
import com.hongguo.read.eventbus.WriteDiscussEvent;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.mvp.ui.discuss.ReplyActivity;


import org.greenrobot.eventbus.EventBus;


/**
 * Created by losg on 2017/12/26.
 */

public class BookDetailDiscussHelper {

    private Context         mContext;
    private BookDiscussBean mBookDiscussBean;
    private String          mBookId;

    public BookDetailDiscussHelper(Context context, String bookId) {
        mContext = context;
        mBookId = bookId;
    }

    public int getTileResouce() {
        if (mBookDiscussBean == null) return 0;
        return R.layout.view_book_discuss_title;
    }

    public void initTitle(IosRecyclerAdapter.BaseHoder hoder) {
        hoder.setText(R.id.discuss_number, mBookDiscussBean.pager.total + "人评论");
        hoder.itemView.setOnClickListener(v -> {
            EventBus.getDefault().post(new DiscussTitleClickEvent(mBookId));
        });
    }

    public int getCellCount() {
        if (mBookDiscussBean == null) return 0;
        return mBookDiscussBean.data.size();
    }

    public int getItemResouce() {
        return R.layout.view_book_discuss_item;
    }

    public int getFooterResource() {
        if (mBookDiscussBean == null || mBookDiscussBean.data.size() != 0) return 0;
        return R.layout.view_discuss_null;
    }

    public void initFooter(IosRecyclerAdapter.BaseHoder baseHoder) {
        View viewById = baseHoder.getViewById(R.id.write_discuss);
        if (viewById != null) {
            viewById.setOnClickListener(v -> {
                EventBus.getDefault().post(new WriteDiscussEvent(mBookId));
            });
        }
    }

    public void initBookDiscuss(IosRecyclerAdapter.BaseHoder hoder, int position) {
        BookDiscussBean.DataBean dataBean = mBookDiscussBean.data.get(position);
        ImageLoadUtils.loadCircleUrl(hoder.getViewById(R.id.user_avatar), dataBean.headImgUrl);
        hoder.setText(R.id.user_name, dataBean.nickName);

        dataBean.title = TextUtils.isEmpty(dataBean.title) ? dataBean.content : dataBean.title;
        dataBean.title = TextUtils.isEmpty(dataBean.title) ? "" : dataBean.title;

        String discussContent = StringUtil.unicode2String(dataBean.title);

        if (discussContent.endsWith("//////")) {
            discussContent = discussContent.replace("//////", "");
        }

        hoder.setText(R.id.discuss_content, discussContent.trim());
        hoder.setText(R.id.reply_number, dataBean.replyTimes + "条回复");
        hoder.setText(R.id.reply_time, TimeUtils.getTime(dataBean.cDate));

        hoder.itemView.setOnClickListener(v -> {
            ReplyActivity.toActivity(mContext, mBookId, dataBean);
        });
    }

    public void setBookDiscussBean(BookDiscussBean bookDiscussBean) {
        mBookDiscussBean = bookDiscussBean;
    }

}
