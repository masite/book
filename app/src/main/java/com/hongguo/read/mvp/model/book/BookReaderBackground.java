package com.hongguo.read.mvp.model.book;

/**
 * Created by losg on 2018/1/16.
 */

public class BookReaderBackground {
    public int    backgroundColor;
    public String backgroundResource;
    public int    textColor;
    public int    index;


    public BookReaderBackground(int backgroundColor, int textColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public BookReaderBackground(int backgroundColor, String backgroundResource, int textColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundResource = backgroundResource;
        this.textColor = textColor;
    }
}

