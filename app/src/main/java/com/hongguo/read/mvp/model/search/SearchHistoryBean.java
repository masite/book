package com.hongguo.read.mvp.model.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/1/12.
 */

public class SearchHistoryBean {

    private static final int MAX_SIZE = 10;

    public List<String> mHistory;

    public void addHistory(String history) {
        if (mHistory == null) {
            mHistory = new ArrayList<>();
        }
        int index = mHistory.indexOf(history);
        if (index == -1) {
            if (mHistory.size() >= MAX_SIZE) {
                mHistory.remove(mHistory.size() - 1);
            }
            mHistory.add(0, history);
        } else {
            mHistory.remove(index);
            mHistory.add(0, history);
        }

    }
}
