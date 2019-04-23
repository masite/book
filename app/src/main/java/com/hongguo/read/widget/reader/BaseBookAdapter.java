package com.hongguo.read.widget.reader;


import java.util.List;

public abstract class BaseBookAdapter<T extends BaseChapter> {

    public  List<T>  mChapters;
    private BookView mBookView;

    public BaseBookAdapter(List<T> chapters) {
        mChapters = chapters;
    }

    protected BaseChapter bindChapter(int chapterPosition) {
        T baseChapter = mChapters.get(chapterPosition);
        initChater(baseChapter, chapterPosition);
        return baseChapter;
    }

    public abstract void initChater(T t, int position);

    public int getChapterCount() {
        return mChapters.size();
    }

    public List<T> getChapters() {
        return mChapters;
    }


    public void setBookView(BookView bookView) {
        mBookView = bookView;
    }

}
