package com.hongguo.read.repertory.share;

import android.text.TextUtils;

import com.hongguo.common.utils.SpStaticUtil;
import com.hongguo.read.mvp.model.search.SearchHistoryBean;
import com.losg.library.utils.JsonUtils;

import java.util.ArrayList;

/**
 * Created by losg on 2018/1/12.
 */

public class SearchRepertory {

    public static void setSearchHistory(SearchHistoryBean searchHistory) {
        if (searchHistory.mHistory == null) {
            searchHistory.mHistory = new ArrayList<>();
        }
        SpStaticUtil.putString("search_history", JsonUtils.toJson(searchHistory));
    }

    public static SearchHistoryBean getHistory() {
        String history = SpStaticUtil.getString("search_history");
        if (TextUtils.isEmpty(history)) {
            SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
            searchHistoryBean.mHistory = new ArrayList<>();
            return searchHistoryBean;
        }
        return JsonUtils.fromJson(history, SearchHistoryBean.class);
    }
}
