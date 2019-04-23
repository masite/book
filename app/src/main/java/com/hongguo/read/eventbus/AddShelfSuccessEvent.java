package com.hongguo.read.eventbus;

/**
 * Created by losg on 2018/2/13.
 */

public class AddShelfSuccessEvent {
    public String mBookId;

    public AddShelfSuccessEvent(String bookId) {
        mBookId = bookId;
    }
}
