package com.hongguo.read.repertory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/1/16.
 */

public class ReaderLineRepertory {

    private static List<BookLineType> sLineTypes;

    static {
        sLineTypes = new ArrayList<>();
        sLineTypes.add(new BookLineType(15, 40));
        sLineTypes.add(new BookLineType(20, 50));
        sLineTypes.add(new BookLineType(30, 60));
    }

    public static List<BookLineType> getBookReaderLineTypes() {
        return sLineTypes;
    }

    public static class BookLineType {
        public int lineHeight;
        public int paragraphHeight;

        public BookLineType(int lineHeight, int paragraphHeight) {
            this.lineHeight = lineHeight;
            this.paragraphHeight = paragraphHeight;
        }
    }
}
