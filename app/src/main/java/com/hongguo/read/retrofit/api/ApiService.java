package com.hongguo.read.retrofit.api;

import com.hongguo.common.base.CommonBean;
import com.hongguo.common.model.CmsTopicBean;
import com.hongguo.read.baidu.ShuChengManager;
import com.hongguo.read.mvp.model.CmsBannerBean;
import com.hongguo.read.mvp.model.PackBean;
import com.hongguo.read.mvp.model.book.BaiduBuyOrderBean;
import com.hongguo.read.mvp.model.book.BaiduChapterDownInfo;
import com.hongguo.read.mvp.model.book.ChapterContentBean;
import com.hongguo.read.mvp.model.book.chapter.BaiduBookUpdateBean;
import com.hongguo.read.mvp.model.book.chapter.BaiduCapterBean;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.read.mvp.model.book.detail.BookAuthorBean;
import com.hongguo.read.mvp.model.book.detail.BookDetailBean;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.mvp.model.book.detail.SimilarBookBean;
import com.hongguo.read.mvp.model.book.detail.baidu.BaiduDetailBean;
import com.hongguo.read.mvp.model.bookshelf.BookShelfBean;
import com.hongguo.read.mvp.model.bookshelf.BooksUpDataInfoBean;
import com.hongguo.read.mvp.model.bookshelf.SignBean;
import com.hongguo.read.mvp.model.bookstore.BaiDuTypeBookBean;
import com.hongguo.read.mvp.model.bookstore.BookStoreBookBean;
import com.hongguo.read.mvp.model.bookstore.GuessFavorBean;
import com.hongguo.read.mvp.model.bookstore.HomeBookBean;
import com.hongguo.read.mvp.model.loading.BookPattern;
import com.hongguo.read.mvp.model.loading.LoadingBean;
import com.hongguo.read.mvp.model.mine.QuestionBean;
import com.hongguo.read.mvp.model.mine.mypackage.ConsumeBean;
import com.hongguo.read.mvp.model.mine.mypackage.RechargeHistoryBean;
import com.hongguo.read.mvp.model.mine.recharge.OrderInfoBean;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;
import com.hongguo.read.mvp.model.search.BaiDuSearchResultBean;
import com.hongguo.read.mvp.model.search.RankBean;
import com.hongguo.read.mvp.model.search.SearchBean;
import com.hongguo.read.mvp.model.version.VersionLatestBean;
import com.hongguo.read.mvp.model.vip.VipBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author losg
 * @date 2017/7/18
 */

public interface ApiService {

    /**
     * loading 页的背景
     *
     * @param host
     * @return
     */
    @GET
    Observable<LoadingBean.LoadingImageBean> queryLoadingImage(@Url String host);

    @GET
    Observable<PackBean> queryPackVersionInfo(@Url String url);


    /*******************后台可配项 start************************************/
    /**
     * 获取banner 信息
     *
     * @param typeKey {@link com.hongguo.read.constants.CmsBanner}
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/CmsBannerList")
    @FormUrlEncoded
    Observable<CmsBannerBean> getBanners(@Field("typeKey") String typeKey);

    /**
     * 获取专题 信息
     *
     * @param useToken {@link com.hongguo.read.constants.CmsTopicInfo}
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/CmsATopicInfo")
    @FormUrlEncoded
    Observable<CmsTopicBean> getTopicInfo(@Field("useToken") String useToken);
    /*******************后台可配项 end************************************/


    /**
     * 获取百度免费书籍的正则表达式
     *
     * @return
     */
    @GET("obook/api/3ffa2a52f64e0399b74a5b9d95755166/RegularLatest")
    Observable<BookPattern> queryBaiduFreePattern();

    /**
     * 获取首页书籍
     *
     * @param btype  筛选条件
     *               31300210 说明
     *               方向 --->
     *               [0]      约定 默认3
     *               [1]      类型 百度0 红果1
     *               [2]-[4]  表示(可以组合，最多3个组合)  默认0 推荐1 新书2 置顶3 热门4 精品5 优秀6 原创7 补位8
     *               [5]-[6]  平台 通用00 web（web站:11 M站:12 微信公众号:13）,app(安卓:21,苹果22), 小程序31
     *               [7]      自定义 0
     * @param status 9 全部 默认
     * @param sort   排序  热度1 最新2 字数3
     * @param scope  300 默认
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/BookList")
    @FormUrlEncoded
    Observable<HomeBookBean> queryHomeBooks(@Field("btype") String btype, @Field("status") String status, @Field("sort") String sort, @Field("scope") String scope, @Field("page") String page, @Field("count") String count);

    /**
     * 猜你喜欢
     *
     * @return
     */
    @GET("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetRecommendBookList")
    Observable<GuessFavorBean> queryFavor();


    /**
     * 从百度服务器获取 百度类型书籍
     *
     * @param url
     * @return
     */
    @GET
    Observable<BaiDuTypeBookBean> queryBaiduTypeBooks(@Url String url);

    /**
     * 通过分类查找我们自己的书籍
     *
     * @param categoryIds 分类 逗号分隔
     *                    http://x1.hgread.com/fenlei/fenlei.json
     * @param status      s0:连载,s1:完结,s9:全部
     * @param sort        s0:更新,s1:畅销(暂为收藏量),s9:默认
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetCategoryBookList")
    @FormUrlEncoded
    Observable<BookStoreBookBean> querySelfBookByType(@Field("categoryIds") String categoryIds, @Field("status") String status, @Field("sort") String sort, @Field("page") int page, @Field("count") int count);

    /**
     * 通过分类查找百度的书籍
     *
     * @param categoryIds 分类 逗号分隔 逗号分隔
     *                    http://m.hgread.com/obook/api/hgreadapptest/BaiduGetBookCategoryList
     * @param status      s0:连载,s1:完结,s9:全部
     * @param sort        s0:更新,s1:畅销(暂为收藏量),s9:默认
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/BaiduGetCategoryBookList")
    @FormUrlEncoded
    Observable<BookStoreBookBean> queryBaiduBookByType(@Field("categoryIds") String categoryIds, @Field("status") String status, @Field("sort") String sort, @Field("page") int page, @Field("count") int count);

    /**
     * 百度免费书籍
     *
     * @param baiduFreeUrl {@link BookPattern.Data.PatternContent.BaiduPattern.BaiduFreePage}
     * @return
     */
    @GET
    Observable<String> queryBaiduFree(@Url String baiduFreeUrl);


    /************************* 书架相关 start***********************************/

    /**
     * 获取服务器上该用户的书架信息
     *
     * @param shelfType 0
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetShelfBookList")
    @FormUrlEncoded
    Observable<BookShelfBean> queryNetBookShelf(@Field("shelfType") int shelfType);

    /**
     * 添加书架
     *
     * @param bookId
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/BookShelf")
    @FormUrlEncoded
    Observable<CommonBean> addBookShelf(@Field("bookId") String bookId);

    /**
     * 删除书架
     *
     * @param bookId
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/BookShelfDelBook")
    @FormUrlEncoded
    Observable<CommonBean> deleteBookShelf(@Field("bookId") String bookId);

    /**
     * 获取书籍最新更新时间
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetBookLastChapter")
    @FormUrlEncoded
    Observable<BooksUpDataInfoBean> queryBookShelfUpdateTime(@Field("bookIds") String bookIds);

    /**
     *********************** 书架相关 end **********************************
     */


    /*********************** 书籍详情 start********************************/
    /**
     * 书籍基本信息
     *
     * @param bookId
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetBookInfo")
    @FormUrlEncoded
    Observable<BookDetailBean> queryBookDetail(@Field("bookId") String bookId);

    /**
     * 查询百度书籍基本信息
     *
     * @param url
     * @return
     */
    @POST
    Observable<BaiduDetailBean> queryBaiduDetail(@Url String url);


    /**
     * 书籍评论信息
     *
     * @param bookId
     * @param bookType
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetReviewList")
    @FormUrlEncoded
    Observable<BookDiscussBean> queryBookDiscuss(@Field("bookId") String bookId, @Field("bookType") String bookType, @Field("page") int page, @Field("count") int count);

    /**
     * 书籍打赏榜
     *
     * @param bookId
     * @param rankType "3407" ??
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetRewardRankList")
    @FormUrlEncoded
    Observable<AwardRankBean> queryRewardRank(@Field("bookId") String bookId, @Field("rankType") String rankType, @Field("page") int page, @Field("count") int count);

    /**
     * 获取相似书籍列表
     *
     * @param bookId
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetRelatedBookList")
    @FormUrlEncoded
    Observable<SimilarBookBean> querySimilarBooks(@Field("bookId") String bookId);

    /**
     * 获取书籍授权信息
     *
     * @param bookId
     * @param scope  bds_forminfo 固定值
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetBookDynamicInfo")
    @FormUrlEncoded
    Observable<BookAuthorBean> queryAuthorBooks(@Field("bookId") String bookId, @Field("scope") String scope);

    /*********************** 书籍详情 end********************************/


    /*********************** 评论 start***************************************/
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/BookPostReview")
    @FormUrlEncoded
    Observable<CommonBean> writeDiscuss(@Field("bookId") String bookid, @Field("bookType") int bookType, @Field("bookName") String bookName, @Field("nickName") String nickName, @Field("headImgUrl") String headImgUrl, @Field("title") String title, @Field("content") String content);

    /**
     * 获取回复列表
     *
     * @param bookid
     * @param commentId
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetRevertList")
    @FormUrlEncoded
    Observable<BookDiscussBean> discussReply(@Field("bookId") String bookid, @Field("commentId") String commentId, @Field("page") int page, @Field("count") int count);

    /**
     * 回复
     *
     * @param bookid
     * @param commentId
     * @param nickName
     * @param headImgUrl
     * @param title
     * @param content
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/BookPostRevert")
    @FormUrlEncoded
    Observable<CommonBean> writeDiscussForPerson(@Field("bookId") String bookid, @Field("commentId") String commentId, @Field("nickName") String nickName, @Field("headImgUrl") String headImgUrl, @Field("title") String title, @Field("content") String content);


    /*********************** 评论 end***************************************/


    /*********************会员中心 start************************************/

    /**
     * 充值记录
     *
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/OrderList")
    @FormUrlEncoded
    Observable<RechargeHistoryBean> queryUserRechargeHistory(@Field("page") int page, @Field("count") int count);

    /**
     * 消费记录
     *
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/SellList")
    @FormUrlEncoded
    Observable<ConsumeBean.SellListBean> queryUserConsumeHistory(@Field("sellType") int sellType, @Field("page") int page, @Field("count") int count);


    /**
     * 充值金额列表
     *
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/ActivityLatest")
    @FormUrlEncoded
    Observable<RechargeBean> queryRechargeList(@Field("terminal") String terminal);

    /**
     * 创建订单
     *
     * @param payMethod
     * @param payAmount
     * @param ruleId
     * @param ruleSign
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/TradeCreate")
    @FormUrlEncoded
    Observable<OrderInfoBean> getPayOrder(@Field("payMethod") String payMethod, @Field("payAmount") String payAmount, @Field("ruleId") String ruleId, @Field("ruleSign") String ruleSign);

    /**
     * 查询订单是否完成
     *
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/TradeQuery")
    @FormUrlEncoded
    Observable<CommonBean> queryOrderStatus(@Field("orderNo") String orderNo);

    /**
     * 常见问题
     *
     * @param url {@link com.hongguo.read.constants.Constants.Request}
     *            http://x1.hgread.com//faq/faq.json
     * @return
     */
    @GET
    Observable<QuestionBean> queryCommonQuestion(@Url String url);

    /**
     * 用户反馈
     *
     * @param typeDesc
     * @param ver
     * @param reportDesc
     * @param userContact
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/ReportSubmit")
    @FormUrlEncoded
    Observable<CommonBean> feedBack(@Field("typeDesc") String typeDesc, @Field("ver") String ver, @Field("reportDesc") String reportDesc, @Field("userContact") String userContact);

    /**
     * 获取当前服务器版本信息
     *
     * @param userId
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/VersionLatest")
    @FormUrlEncoded
    Observable<VersionLatestBean> queryVersion(@Field("userId") String userId);

    /*********************会员中心 end************************************/


    /*******************************会员相关 start***********************************/
    /**
     * 获取会员书籍 / 超级会员
     *
     * @param limitType {@link com.hongguo.read.constants.Constants.BOOK_FREE_TYPE}
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetLimitFreeBookList")
    @FormUrlEncoded
    Observable<VipBean> queryVipBooks(@Field("limitType") int limitType, @Field("page") int page, @Field("count") int count);

    /*******************************会员相关 end***********************************/

    /*******************************搜索相关 start********************************/
    /**
     * 排行
     *
     * @param rankType {@link com.hongguo.read.constants.RankType}
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/RankList")
    @FormUrlEncoded
    Observable<RankBean> queryRank(@Field("rankType") String rankType);

    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/RankList")
    @FormUrlEncoded
    Observable<RankBean> queryRank(@Field("rankType") String rankType, @Field("page") int page, @Field("count") int count);

    /**
     * 书籍搜索
     *
     * @param keywords
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/Search")
    @FormUrlEncoded
    Observable<SearchBean> searchBooks(@Field("q") String keywords, @Field("page") int page, @Field("count") int count);

    /**
     * 搜索百度书籍
     *
     * @param url {@link com.hongguo.read.utils.baidu.BaiduShuChengUrls}
     * @return
     */
    @GET
    Observable<BaiDuSearchResultBean> searchBaiduBooks(@Url String url);


    /*******************************搜索相关 end********************************/

    /*******************************章节信息 start************************************/

    /**
     * 获取章节列表
     *
     * @param bookId
     * @param page
     * @param count
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/GetBookChapterList")
    @FormUrlEncoded
    Observable<ChapterBean> queryBookChapter(@Field("bookId") String bookId, @Field("page") int page, @Field("count") int count);


    /**
     * 获取百度书籍 章节列表
     *
     * @return
     */
    @GET
    Observable<BaiduCapterBean> queryBaiduChapter(@Url String url);

    /**
     * 获取百度 最新更新时间
     *
     * @return
     */
    @GET
    Observable<BaiduBookUpdateBean> queryBaiduUpdate(@Url String url);

    /*******************************章节信息 end  ************************************/

    /*******************************章节下载 start************************************/
    /**
     * 下载章节,并购买章节
     *
     * @param bookid
     * @param chapterId
     * @param newline   10 固定？
     * @param nowBuy    true 单一章节扣费 false 全本订阅(自动扣除---不用~~) 传空表示默认操作(下载简介 或者 已经购买过章节)
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/Chapter")
    @FormUrlEncoded
    Observable<ChapterContentBean> downChapter(@Field("bookId") String bookid, @Field("chapterId") String chapterId, @Field("newline") String newline, @Field("nowBuy") String nowBuy);


    /**
     * 获取百度书籍支付订单
     * {@link com.hongguo.read.utils.baidu.BaiduShuChengUrls#getBaiDuBuyedChapterOrderUrl}
     *
     * @param url
     * @return
     */
    @GET
    Observable<BaiduBuyOrderBean> getBaiduBuyChapterOrder(@Url String url);

    /**
     * 服务器购购买百度书籍章节
     *
     * @param orderId   {@link #getBaiduBuyChapterOrder} 获取的订单
     * @param payAmount 支付金额
     * @param extInfo   其它信息 {@link ShuChengManager#bindBaidu} 保存的数据
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/BaiduRecharge")
    @FormUrlEncoded
    Observable<CommonBean> buyBaiduChapter(@Field("tradeNo") String orderId, @Field("payAmount") float payAmount, @Field("extInfo") String extInfo);

    /**
     * 获取百度书籍下载地址
     *
     * @param url {@link com.hongguo.read.utils.baidu.BaiduShuChengUrls#getBaiDuBuyedChapterDownloadUrl}
     * @return
     */
    @GET
    Observable<BaiduChapterDownInfo> getBaiduDownUrl(@Url String url);

    /*******************************章节下载 end************************************/

    /*******************************打赏 end************************************/
    /**
     * 红果币打赏
     *
     * @param bookId
     * @param bookName
     * @param bookType
     * @param rewardAmount
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/RewardBook")
    @FormUrlEncoded
    Observable<CommonBean> rewardCoin(@Field("bookId") String bookId, @Field("bookName") String bookName, @Field("bookType") int bookType, @Field("rewardAmount") String rewardAmount);

    /**
     * 现金打赏获取订单信息
     *
     * @param bookId
     * @param bookName
     * @param bookType
     * @param payMethod
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/RewardTradeCreate")
    @FormUrlEncoded
    Observable<OrderInfoBean> rewardOrder(@Field("bookId") String bookId, @Field("bookName") String bookName, @Field("bookType") int bookType, @Field("payAmount") int payAmount, @Field("payMethod") String payMethod);

    /**
     * 签到
     *
     * @param count 查询签到信息 0- 7  0 签到  1-7查询一周内签到的信息
     * @param qtag  签到标志位  1 只查询当天签到信息 0 直接签到
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UserSignIn")
    @FormUrlEncoded
    Observable<SignBean> sign(@Field("count") String count, @Field("qtag") String qtag);

}
