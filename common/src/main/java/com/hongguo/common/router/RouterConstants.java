package com.hongguo.common.router;

/**
 * Created time 2017/12/8.
 *
 * @author losg
 */

public interface RouterConstants {

    /**
     * svip 说明页面
     * {@link  com.hongguo.read.mvp.ui.vip.VipDescribeActivity}
     * hgread://www.hgread.com/vip/svip
     */
    String VIP_DESCRIBE = "/vip/svip";

    /**
     * 网页跳转
     * {@link com.hongguo.read.base.AppWebView}
     * hgread://www.hgread.com/web/direct
     */
    String WEB_DIRECT = "/web/direct";

    /**
     * 大转盘活动
     * {@link  com.hongguo.read.mvp.ui.event.TurntableEventActivity}
     * hgread://www.hgread.com/event/turntable
     */
    String TRRN_TABLE_EVENT = "/event/turntable";

    /**
     * 常见问题
     * {@link com.hongguo.read.mvp.ui.mine.QuestionActivity}
     * hgread://www.hgread.com/view/request
     */
    String VIEW_REQUEST = "/view/request";

    /**
     * 书本详情
     * {@link com.hongguo.read.mvp.ui.book.detail.BookDetailActivity
     * 参数  bookfrom  bookid   hgread://www.hgread.com/book/reader?bookid=xxxx&bookfrom=xxx
     */
    String BOOK_DETAIL = "/book/bookdetail";

    /**
     * 阅读页面
     * {@link com.hongguo.read.mvp.ui.book.BookReaderActivity
     * 参数  bookFrom  bookId   hgread://www.hgread.com/book/reader?bookId=xxxx&bookFrom=xxx
     */
    String BOOK_READER = "/book/reader";

    /**
     * 用户充值
     * {@link com.hongguo.read.mvp.ui.mine.recharge.RechargeActivity}
     * 参数  key    hgread://www.hgread.com/user/recharge?key=xxxx
     */
    String USER_RECHAGE = "/user/recharge";

    /**
     * 用户反馈
     * {@link com.hongguo.read.mvp.ui.mine.mypackage.FeedBackActivity}
     * hgread://www.hgread.com/user/feedback
     */
    String USER_FEEDBACK = "/user/feedback";

    /**
     * 用户搜索
     * {@link com.hongguo.read.mvp.ui.search.SearchActivity}
     * hgread://www.hgread.com/book/search
     */
    String BOOK_SEARCH = "/book/search";

}
