package com.hongguo.read.repertory.share;

import com.hongguo.common.utils.SpStaticUtil;
import com.hongguo.read.mvp.model.mine.WebCookieUserInfo;
import com.hongguo.common.utils.MathTypeParseUtils;
import com.losg.library.utils.JsonUtils;

/**
 * Created time 2017/11/29.
 *
 * @author losg
 */

public class UserRepertory {

    /**
     * 用户 token
     *
     * @return
     */
    public static String getToken() {
        return SpStaticUtil.getString("token", "");
    }

    public static void setToken(String token) {
        SpStaticUtil.putString("token", token);
    }

    /**
     * 用户 refreshToken
     *
     * @return
     */
    public static String getRefreshToken() {
        return SpStaticUtil.getString("refresh_token", "");
    }

    public static void setRefreshToken(String token) {
        SpStaticUtil.putString("refresh_token", token);
    }


    /**
     * 用户 userid
     *
     * @return
     */
    public static String getUserID() {
        return SpStaticUtil.getString("user_id", "");
    }

    public static void setUserID(String id) {
        SpStaticUtil.putString("user_id", id);
    }

    /**
     * 用户名
     *
     * @return
     */
    public static String getUserName() {
        return SpStaticUtil.getString("user_name", "");
    }

    public static void setUserName(String name) {
        SpStaticUtil.putString("user_name", name);
    }

    /**
     * 授权登录用户名
     *
     * @param userName
     */
    public static void setAuthorUserName(String userName) {
        SpStaticUtil.putString("author_user_name", userName);
    }

    public static String getAuthorUserNmae() {
        return SpStaticUtil.getString("author_user_name");
    }

    /**
     * 用户是否为vip
     *
     * @return
     */
    public static boolean userIsVip() {
        return SpStaticUtil.getBoolean("user_vip", false);
    }

    public static void setUserIsVip(boolean vip) {
        SpStaticUtil.putBoolean("user_vip", vip);
    }

    /**
     * 用户是否为svip
     *
     * @return
     */
    public static boolean userIsSVip() {
        return SpStaticUtil.getBoolean("user_svip", false);
    }

    public static void setUserIsSVip(boolean sVIP) {
        SpStaticUtil.putBoolean("user_svip", sVIP);
    }

    /**
     * 折扣
     * @return
     */
    public static double getUserDiscount(){
        return MathTypeParseUtils.string2Double(SpStaticUtil.getString("user_discount", ""),1);
    }

    public static void setUserDiscount(String discount){
        SpStaticUtil.putString("user_discount", discount);
    }

    /**
     * 用户名 头像
     *
     * @return
     */
    public static String getUserAvatar() {
        return SpStaticUtil.getString("user_avatar", "");
    }

    public static void setUserAvatar(String avatar) {
        SpStaticUtil.putString("user_avatar", avatar);
    }

    /**
     * 授权登录 后的头像信息
     *
     * @return
     */
    public static String getAuhorAvatar() {
        return SpStaticUtil.getString("author_avatar", "");
    }

    public static void setAuthorAvatar(String authorAvatar) {
        SpStaticUtil.putString("author_avatar", authorAvatar);
    }

    /**
     * 登录方式
     *
     * @param loginType
     */
    public static void setLoginType(String loginType) {
        SpStaticUtil.putString("login_type", loginType);
    }

    public static String getLoginType() {
        return SpStaticUtil.getString("login_type", "");
    }

    /**
     * 登录时 账号密码(账号登录方式)
     *
     * @param username
     */
    public static void setLoginName(String username) {
        SpStaticUtil.putString("login_username", username);
    }

    public static String getLoginName() {
        return SpStaticUtil.getString("login_username", "");
    }

    public static void setLoginPassword(String password) {
        SpStaticUtil.putString("login_password", password);
    }

    public static String getLoginPassword() {
        return SpStaticUtil.getString("login_password", "");
    }

    /**
     * 授权登录 的信息
     */
    public static void setAuthorOpenId(String openid) {
        SpStaticUtil.putString("author_openid", openid);
    }

    public static String getAuthorOpenId() {
        return SpStaticUtil.getString("author_openid");
    }

    public static void setAuthorUnionId(String unionId) {
        SpStaticUtil.putString("author_unionId", unionId);
    }

    public static String getAuthorUnionId() {
        return SpStaticUtil.getString("author_unionId");
    }


    public static void clearAuhorInfo() {
        setAuthorUnionId("");
        setAuthorOpenId("");
        setAuthorUserName("");
        setAuthorAvatar("");
    }

    public static void setWebCookieUserInfo(WebCookieUserInfo eventUserInfo){
        SpStaticUtil.putString("web_cookie_user_info", JsonUtils.toJson(eventUserInfo));
    }

    public static String getWebCookieUserInfo(){
        return SpStaticUtil.getString("web_cookie_user_info");
    }

    public static void setUserIsNew(boolean isNew){
        SpStaticUtil.putBoolean("user_is_new", isNew);
    }

    public static boolean currentUserIsNew(){
        return SpStaticUtil.getBoolean("user_is_new", false);
    }


}
