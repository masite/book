package com.hongguo.common.utils;

import android.content.Context;


import com.hongguo.common.constants.Constants;
import com.hongguo.common.repertory.share.UserRepertory;
import com.lbsw.stat.LBStat;

/**
 * Created time 2017/11/29.
 * 统计工具
 *
 * @author losg
 */

public class StatisticsUtils {

    //充值
    public static final int RECHARGE        = 0;
    //充值vip
    public static final int VIP_RECHARGE    = 1;
    //充值svip
    public static final int SVIP_RECHARGE   = 2;
    //打赏
    public static final int REWARD_RECHARGE = 3;

    /**
     * 初始化统计
     *
     * @param context
     */
    public static void init(Context context) {
        LBStat.init(context);
    }

    /**
     * 开启统计
     */
    public static void start() {
        LBStat.start();
        LBStat.active();
    }

    public static void click(String value) {
        LBStat.collect("点击", value);
    }

    public static void collect(String key, String value) {
        LBStat.collect(key, value);
    }


    public static void bookDetail(String value) {
        LBStat.collect("书本详情", value);
    }

    public static void hgPay(String value) {
        LBStat.collect("红果消费", value);
    }

    public static void baiduPay(String value) {
        LBStat.collect("百度消费", value);
    }

    public static void bookRead(String value) {
        LBStat.collect("阅读页", value);
    }

    public static void bookShelf(String value) {
        LBStat.collect("书架", value);
    }

    public static void mine(String value) {
        LBStat.collect("我的", value);
    }

    public static void bookstore(String value) {
        LBStat.collect("首页", value);
    }

    public static void vip(String value) {
        LBStat.collect("vip专区", value);
    }

    public static void svip(String value) {
        LBStat.collect("vip专区", value);
    }

    public static void search(String value) {
        LBStat.collect("搜索", value);
    }

    public static void function(String value) {
        LBStat.collect("功能区", value);
    }

    public static void event(String value) {
        LBStat.collect("活动点击", value);
    }

    public static void rechargeClick(int value) {
        LBStat.collect("充值点击", "充值红果币-" + (value / 100));
    }

    public static void vipRechargeClick(int value) {
        LBStat.collect("充值点击", "开通vip" + (value / 100));
    }

    public static void svipRechargeClick(int value) {
        LBStat.collect("充值点击", "开通svip" + (value / 100));
    }

    public static void rewardRechargeClick(int value) {
        LBStat.collect("充值点击", "打赏" + (value / 100));
    }

    public static void rechargeSuccess(int rechargeType, String payType, String order, float payNumber) {
        payNumber = payNumber / 100;
        switch (rechargeType) {
            case RECHARGE:
                rechargeSuccess(payType, order, payNumber);
                break;
            case VIP_RECHARGE:
                vipRechargeSuccess(payType, order, payNumber);
                break;
            case SVIP_RECHARGE:
                svipRechargeSuccess(payType, order, payNumber);
                break;
            case REWARD_RECHARGE:
                rewardRechargeSuccess(payType, order, payNumber);
                break;
        }
    }

    public static void rechargeFailure(int rechargeType, String payType, String order, float payNumber) {
        payNumber = payNumber / 100;
        switch (rechargeType) {
            case RECHARGE:
                rechargeFailure(payType, order, payNumber);
                break;
            case VIP_RECHARGE:
                vipRechargeFailure(payType, order, payNumber);
                break;
            case SVIP_RECHARGE:
                svipRechargeFailure(payType, order, payNumber);
                break;
            case REWARD_RECHARGE:
                rewardRechargeFailure(payType, order, payNumber);
                break;
        }
    }

    public static void rechargeSuccess(String payType, String order, float payNumber) {
        payStatistics(payType, true, order, payNumber, "recharge");
    }

    public static void rechargeFailure(String payType, String order, float payNumber) {
        payStatistics(payType, false, order, payNumber, "recharge");
    }

    public static void svipRechargeSuccess(String payType, String order, float payNumber) {
        payStatistics(payType, true, order, payNumber, "svip");
    }

    public static void svipRechargeFailure(String payType, String order, float payNumber) {
        payStatistics(payType, false, order, payNumber, "svip");
    }

    public static void vipRechargeSuccess(String payType, String order, float payNumber) {
        payStatistics(payType, true, order, payNumber, "vip");
    }

    public static void vipRechargeFailure(String payType, String order, float payNumber) {
        payStatistics(payType, false, order, payNumber, "vip");
    }

    public static void rewardRechargeSuccess(String payType, String order, float payNumber) {
        payStatistics(payType, true, order, payNumber, "reward");
    }

    public static void rewardRechargeFailure(String payType, String order, float payNumber) {
        payStatistics(payType, false, order, payNumber, "reward");
    }

    private static void payStatistics(String payType, boolean success, String order, float payNumber, String rechargeType) {
        String payChanel;
        if (payType.equals(Constants.PayType.ALIPAY)) {
            payChanel = "alipay";
        } else {
            payChanel = "weixin";
        }
        LBStat.pay(payChanel, order, success, rechargeType, payNumber, UserRepertory.getUserID());
    }

}
