package com.hongguo.common.base;

/**
 * Created time 2017/11/30.
 *
 * @author losg
 */

public class CommonBean {

    /**
     * 默认 每页的大小
     */
    public static final int PAGE_SIZE = 20;

    public int code;

    public int currentPage;

    public int total_number;

    public PagerBean pager;

    public static class PagerBean {
        /**
         * page : 1
         * size : 30
         * total : 124
         */
        public int page;
        public int size;
        public int total;
    }

    public boolean hasMore() {
        if (pager == null) {
            if (currentPage * PAGE_SIZE >= total_number) {
                return false;
            }
            return true;
        }
        if (pager.page * PAGE_SIZE >= pager.total) {
            return false;
        }
        return true;
    }
}
