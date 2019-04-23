package com.hongguo.read.repertory.db.read;

import android.text.TextUtils;

import com.hongguo.common.utils.TimeUtils;


/**
 * Created by losg on 17-12-19.
 */

public class BookReader {
    public String  bookId;
    public String  bookName;
    public String  coverPicture;
    public String  bookAuthor;
    public String  readChapterId;
    public String  lastUpdateTime;
    public String  netLastUpdateTime;
    public boolean isSelected;
    public boolean isInBookShelf;
    public float   readProgress;
    public int     bookType;
    public int     readPage;

    public boolean bookHasUpdate() {
        if (TextUtils.isEmpty(netLastUpdateTime) || TextUtils.isEmpty(lastUpdateTime)) {
            return false;
        }
        String cSharpTime = TimeUtils.getCSharpTime(netLastUpdateTime);
        return cSharpTime.compareTo(lastUpdateTime) > 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BookReader)) return false;
        return ((BookReader) obj).bookType == bookType && ((BookReader) obj).bookId.equals(bookId);
    }
}
