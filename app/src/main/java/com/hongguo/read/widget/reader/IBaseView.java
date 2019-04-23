package com.hongguo.read.widget.reader;

/**
 * Created by losg on 2017/5/23.
 */

public interface IBaseView {

    void setCurrentPage(int index);

    void setBackgroundColor(int setBackgroundColor);

    void setBackgroundResource(int resource);

    void setAnimationType(int type);

    void setBookViewClickListener(BookViewListener bookViewClickListener);

    int getCurrentPage();

    void notifyDateChange();
}
