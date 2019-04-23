package com.hongguo.common.retrofit.api;

import com.hongguo.common.base.CommonBean;
import com.hongguo.common.model.login.BaiDuLoginBean;
import com.hongguo.common.model.login.UserOauthBean;
import com.hongguo.common.model.login.UserbindBean;
import com.hongguo.common.model.userInfo.UploadResponseBean;
import com.hongguo.common.model.userInfo.UploadUrlPathBean;
import com.hongguo.common.model.userInfo.UserAmountLatestBean;
import com.hongguo.common.model.userInfo.UserBindInfo;
import com.hongguo.common.model.userInfo.UserInfoBean;
import com.hongguo.common.utils.UmengAuthorHelper;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by losg on 2018/3/6.
 *
 * 用户相关的api用作通用信息 (其它模块可能会用到用户信息，单独提取出来)
 */

public interface UserApiService {

    /********************登录相关 start**************************************/
    /**
     * 匿名登录
     *
     * @param utmId    2146 首次注册送3天svip 特殊渠道才有，其余没有
     * @param deviceID 设备id
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UserOauth")
    @FormUrlEncoded
    Observable<UserOauthBean> userLogin(@Field("deviceid") String deviceID, @Field("utmId") String utmId);

    /**
     * 查询用户绑定的百度账号信息
     *
     * @param userId 用户id
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UserBindInfo")
    @FormUrlEncoded
    Observable<UserbindBean> baiduBind(@Field("userId") String userId);


    /**
     * 百度登录
     * @param url
     * @return
     */
    @GET
    Observable<BaiDuLoginBean> baiduLogin(@Url String url);

    /**
     * 保存百度账号
     *
     * @param userId
     * @param userkey 百度账户对应秘钥
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/BaiduAccountBind")
    @FormUrlEncoded
    Observable<CommonBean> saveBaiduPassword(@Field("userId") String userId, @Field("userKey") String userkey);

    /********************登录相关 end**************************************/


    /**
     * 查询当前用户的红果币、赠送币
     *
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UserAmountLatest")
    @FormUrlEncoded
    Observable<UserAmountLatestBean> queryUserCurrentAmount(@Field("userId") String userId);

    /**
     * 查询当前用户信息
     *
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UserInfo")
    @FormUrlEncoded
    Observable<UserInfoBean> queryUserInfo(@Field("userId") String userId);

    /**
     * 获取头像上传地址
     *
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UploadLatest")
    @FormUrlEncoded
    Observable<UploadUrlPathBean> queryUploadPath(@Field("userId") String userId);

    /**
     * 上传头像返回数据
     *
     * @param url    queryUploadPath 返回的用户上传头像地址
     * @param userId
     * @param token
     * @param file
     * @return
     */
    @POST
    @Multipart
    Observable<UploadResponseBean> postImage(@Url String url, @Part("userId") String userId, @Part("token") String token, @Part("image\";filename=\"avatar.png\"") RequestBody file);

    /**
     * 更新用户头像
     *
     * @param uaType     315 ??
     * @param headImgUrl
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UserUpdateAvatar")
    @FormUrlEncoded
    Observable<CommonBean> updateUserAvatar(@Field("uaType") String uaType, @Field("headImgUrl") String headImgUrl);

    /**
     * 更新用户信息 (昵称 和 性别)
     *
     * @param nickName
     * @param gender
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UserUpdateInfo")
    @FormUrlEncoded
    Observable<CommonBean> updateUserInfo(@Field("nickName") String nickName, @Field("gender") String gender);


    /***************************第三方绑定 start********************************/
    /**
     * 获取第三方绑定信息
     *
     * @param userId
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UserBindInfo")
    @FormUrlEncoded
    Observable<UserBindInfo> getUserBindInfo(@Field("userId") String userId);

    /**
     * 绑定QQ
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/QQConnectAccountBind")
    @FormUrlEncoded
    Observable<CommonBean> bindQQ(@Field("openId") String openId, @Field("nickName") String nickName, @Field("headImgUrl") String headImgUrl, @Field("snsInfo") UmengAuthorHelper.AuthorResult snsInfo);

    /**
     * 解绑QQ
     *
     * @param userId
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/QQConnectAccountUnBind")
    @FormUrlEncoded
    Observable<CommonBean> unbindQQ(@Field("userId") String userId);

    /**
     * 绑定微信
     *
     * @param uaType     {@link com.hongguo.common.constants.Constants.AUHOR_TYPE}
     * @param openId
     * @param nickName
     * @param headImgUrl
     * @param snsInfo
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/OauthAssociateBind")
    @FormUrlEncoded
    Observable<CommonBean> bindWeiXin(@Field("uaType") String uaType, @Field("openId") String openId, @Field("unionId") String unionId, @Field("nickName") String nickName, @Field("headImgUrl") String headImgUrl, @Field("snsInfo") UmengAuthorHelper.AuthorResult snsInfo);

    /**
     * 解绑微信
     *
     * @param uaType {@link }
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/OauthAssociateUnBind")
    @FormUrlEncoded
    Observable<CommonBean> unbindQWeiXin(@Field("uaType") String uaType);

    /**
     * 红果账号密码登录
     *
     * @param uaType   {@link com.hongguo.common.constants.Constants.AUHOR_TYPE}
     * @param username
     * @param password
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/OauthLogin")
    @FormUrlEncoded
    Observable<UserOauthBean> loginByUserName(@Field("uaType") String uaType, @Field("accessKey") String username, @Field("secretKey") String password);

    /**
     * QQ授权登录
     *
     * @param uaType {@link com.hongguo.common.constants.Constants.AUHOR_TYPE}
     * @param openid
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/UserOauth")
    @FormUrlEncoded
    Observable<UserOauthBean> loginByQQ(@Field("uaType") String uaType, @Field("deviceid") String openid);

    /**
     * 微信授权登录
     *
     * @param uaType  {@link com.hongguo.common.constants.Constants.AUHOR_TYPE}
     * @param openid
     * @param unionId unionId 为空用 openid 代替
     * @return
     */
    @POST("obook/api/3ffa2a52f64e0399b74a5b9d95755166/OauthLogin")
    @FormUrlEncoded
    Observable<UserOauthBean> loginByWeiXin(@Field("uaType") String uaType, @Field("accessKey") String openid, @Field("unionId") String unionId);

    /***************************第三方绑定 end********************************/

}
