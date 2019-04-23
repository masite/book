package com.hongguo.read.utils.baidu;

import android.os.Build;
import android.text.TextUtils;

import com.hongguo.common.utils.SpStaticUtil;
import com.hongguo.read.baidu.ShuChengManager;
import com.hongguo.read.repertory.share.BaiduRepertory;
import com.hongguo.read.utils.AppUtils;
import com.losg.library.utils.MD5;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by losg on 2018/1/2.
 */

public class BaiduShuChengUrls {

    private static final String HOST = "https://lakeshire.xmkanshu.com";

    /**
     *
     * @param bindUserId
     * @param bindUserSecret
     * @return
     */
    public static String getLoginUrl(String bindUserId, String bindUserSecret) {
        Map<String, String> map = new HashMap<>();
        map.put("tao_uid", bindUserId + "");
        map.put("oauthkey", bindUserSecret + "");
        return signUrl(HOST + "/v1/user/auto_register_tao", map, false);
    }

    /**
     * 百度书城打赏排行(猜你喜欢)
     *
     * @return
     */
    public static String getRecommendFavor() {
        Map<String, String> map = new HashMap<>();
        map.put("rankid", "4");
        return signUrl("http://megatron.platform.zongheng.com/v7/top/detail", map, false);
    }

    /**
     * 百度书城筛选
     *
     * @param columntype 99 男生  98 女生
     * @param ranktype   1 热门畅销 8 最新上架
     * @param size       0    字数不限  203  50-100万
     * @param status     1 完本  2不限
     * @return
     */
    public static String getTypeBook(String columntype, String ranktype, String size, String status, String pageid, String pagesize) {
        Map<String, String> map = new HashMap<>();
        map.put("columntype", columntype);
        map.put("ranktype", ranktype);
        map.put("size", size);
        map.put("pageid", pageid);
        map.put("pagesize", pagesize);
        map.put("status", status);
        return signUrl("http://anduril.xmkanshu.com/category/booklist", map, false);
    }

    /**
     * 搜索
     *
     * @param keyWord
     * @return
     * @throws Exception
     */
    public static String getBaiDuSearchUrl(String keyWord, int page, int count) {
        Map<String, String> map = new HashMap<>();
        map.put("page_number", page + "");
        map.put("page_size", count + "");
        map.put("keyword", keyWord + "");
        return signUrl("http://anduril.platform.zongheng.com/v3/search/get_result", map, false);
    }

    /**
     * 获取书籍详情
     *
     * @param bookId
     * @return
     */
    public static String getBaiDuBookDetailUrl(String bookId) {
        Map<String, String> map = new HashMap<>();
        map.put("book_id", bookId + "");
        return signUrl("http://anduril.platform.zongheng.com/v3/book/get_book_info", map, false);
    }

    /**
     * 获取百度书籍最新更新时间
     *
     * @param bookid
     * @return
     */
    public static String getBaiduBookUpate(String bookid) {
        Map<String, String> map = new HashMap<>();
        map.put("bookIds", bookid + "");
        return signUrl("http://anduril.platform.zongheng.com/v1/91book/updatecheck", map, false);
    }

    /**
     * 获取书籍目录url
     *
     * @param bookid
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    public static String getBaiDuBookChapterListUrl(String bookid, int page, int pageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("bookid", bookid + "");
        map.put("page", page + "");
        map.put("pagesize", pageSize + "");
        return signUrl(HOST + "/v1/91book/get_book_chapter_list", map, false);
    }

    /**
     * 获取书籍买到的章节下载地址
     *
     * @param bookid
     * @param chapterId
     * @return
     * @throws Exception
     */
    public static String getBaiDuBuyedChapterDownloadUrl(String bookid, String chapterId) {
        Map<String, String> map = new HashMap<>();
        map.put("bkid", bookid + "");
        map.put("crid", chapterId + "");
        return signUrl(HOST + "/v1/91recharge/chapter", map, false);
    }

    /**
     * 获取购买书籍章节的订单
     *
     * @param coin
     * @param bookId
     * @param chapterId
     * @param num       默认 "1"
     * @return
     * @throws Exception
     */

    public static String getBaiDuBuyedChapterOrderUrl(int coin, String bookId, String chapterId, String num) {
        Map<String, String> map = new HashMap<>();
        map.put("bookid", bookId + "");
        map.put("chpid", chapterId + "");
        map.put("num", num + "");
        map.put("type", "1");
        map.put("needmoney", coin + "");
        return signUrl(HOST + "/v1/91recharge/prepay", map, false);
    }

    private static String signUrl(String url, Map<String, String> map, boolean login) {
        Map<String, String> params = addCommonParams(login);
        if (map != null)
            params.putAll(map);
        String signParams = sign(params);
        StringBuilder urlBuilder = new StringBuilder();

        int index = url.indexOf("?");
        if (index == -1) {
            urlBuilder.append(url).append("?").append(signParams);
        } else {
            urlBuilder.append(url.substring(0, index)).append("?").append(signParams);
        }
        return urlBuilder.toString();
    }


    /**
     * 说明
     *
     * @param login appid	10016
     * @return 参数说明
     * p1      store								    channeltype
     * p2      PandaBookAndroid5952				        channelid
     * p3      aa574b2a678da4bf97ed569587927930         md5数  UUID+设备信息(不用考虑，直接md5(设备号+时间戳)) 只用做充数(吓人?) 与token放在一起)
     * p4      zerofltectc							    制造商       Build.PRODUCT
     * p5      samsung 							        手机名称     Build.MANUFACTURER
     * p6      SM-G9209							        手机型号     Build.MODEL
     * p7      android                                  系统
     * p8      23							 	        系统版本	    VERSION.SDK_INT
     * p9      wlan								        网络环境
     * p10     1.0.0.12                                sdk版本号
     * p11     1024								        屏幕 宽
     * p12		1920								    屏幕高
     * p13		216318380 							    百度用户id
     * p14		10012 							        versioncode
     * p15	    10016                                   appid
     * p17 	99000584173945                              手机IMEI
     * p18     PandaBookAndroid5952                     channelid
     * p19		0
     */
    private static Map<String, String> addCommonParams(boolean login) {

        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("appid", ShuChengManager.BAI_DU_APPID);
        stringMap.put("p1", "store");
        stringMap.put("p2", "PandaBookAndroid5952");

        stringMap.put("p3", BaiduRepertory.getBaiduRandom());
        stringMap.put("p4", Build.PRODUCT);
        stringMap.put("p5", Build.MANUFACTURER);
        stringMap.put("p6", Build.MODEL);
        stringMap.put("p7", "android");
        stringMap.put("p8", Build.VERSION.SDK_INT + "");
        stringMap.put("p9", "wlan");

        stringMap.put("p10", "1.0.0.12");
        stringMap.put("p11", AppUtils.getWindowWidth(SpStaticUtil.getApplicationContext()) + "");
        stringMap.put("p12", AppUtils.getWindowHeight(SpStaticUtil.getApplicationContext()) + "");

        String baiduUserInfo = BaiduRepertory.getBaiduUserInfo();
        if (!TextUtils.isEmpty(baiduUserInfo) && !login) {
            stringMap.put("p13", baiduUserInfo.split("@")[0]);
            stringMap.put("token", baiduUserInfo.split("@")[1]);
        }
        stringMap.put("p14", "10012");
        stringMap.put("p15", ShuChengManager.BAI_DU_APPID);
        stringMap.put("p17", AppUtils.getPhoneImei(SpStaticUtil.getApplicationContext()));
        stringMap.put("p18", "PandaBookAndroid5952");
        stringMap.put("p19", "0");

        return stringMap;
    }

    private static String sign(Map<String, String> params) {
        if (params != null && params.size() != 0) {
            StringBuilder paramsString = new StringBuilder();
            StringBuilder paramsEncode = new StringBuilder();
            //会排序
            TreeMap sortMap = new TreeMap(params);
            Iterator iterator = sortMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = String.valueOf(entry.getValue());
                if ((value != null && !value.equals("")) && !"sign".equals(key)) {

                    paramsString.append(key).append("=").append(value).append("&");
                    try {
                        paramsEncode.append(key).append("=").append(URLEncoder.encode(value, "utf-8")).append("&");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (paramsString.length() > 0) {
                paramsString.deleteCharAt(paramsString.length() - 1);
            }

            String param = (new StringBuilder()).append(paramsString).append(ShuChengManager.BAI_DU_SECRET).toString();
            String md5Params = MD5.md5(param).toLowerCase();
            return paramsEncode.append("sign=").append(md5Params).toString();

        } else {
            return null;
        }
    }
}
