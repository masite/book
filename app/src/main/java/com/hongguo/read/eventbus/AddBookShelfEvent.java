package com.hongguo.read.eventbus;

import com.hongguo.read.repertory.db.read.BookReader;

/**
 * Created by losg on 2017/12/25.
 */

public class AddBookShelfEvent {
    public BookReader mBookShelf;

    public AddBookShelfEvent(BookReader bookShelf) {
        mBookShelf = bookShelf;
    }
}
