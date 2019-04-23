package com.hongguo.read.repertory.share;

import com.hongguo.read.mvp.model.book.BookReaderBackground;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/1/16.
 */

public class ReaderBackgroundRepertory {

    private static List<BookReaderBackground> sBookReaderBackgrounds;

    static {
        sBookReaderBackgrounds = new ArrayList<>();
        sBookReaderBackgrounds.add(new BookReaderBackground(0xfff7ecce, "ic_read_orange", 0xff000000));
        sBookReaderBackgrounds.add(new BookReaderBackground(0xffe5cec9, "ic_book_pink",0xff7a2040));
        sBookReaderBackgrounds.add(new BookReaderBackground(0xff141b28, "ic_night_sky",0xff667486));
        sBookReaderBackgrounds.add(new BookReaderBackground(0xffe7ddba, 0xff000000));
        sBookReaderBackgrounds.add(new BookReaderBackground(0xffceebce, 0xff000000));
        sBookReaderBackgrounds.add(new BookReaderBackground(0xffcfcbc4, 0xff000000));
        sBookReaderBackgrounds.add(new BookReaderBackground(0xffd4c1a6, 0xff000000));
        sBookReaderBackgrounds.add(new BookReaderBackground(0xffffffff, 0xff000000));
        sBookReaderBackgrounds.add(new BookReaderBackground(0xff291c00, 0xff98814f));
        sBookReaderBackgrounds.add(new BookReaderBackground(0xff061c2b, 0xff637079));
    }

    public static List<BookReaderBackground> getBookReaderBackgrounds() {
        return sBookReaderBackgrounds;
    }
}
