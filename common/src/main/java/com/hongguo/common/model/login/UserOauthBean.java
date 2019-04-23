package com.hongguo.common.model.login;


public class UserOauthBean {

    /**
     * data : {"userId":207431,"accessToken":"3210fa664246493380da8f42db78df4f","expiresIn":7200,"refreshToken":"b4d810f65a304b76bba06cfc2663b182"}
     * code : 0
     * msg : Successful
     *
     */

    public DataBean data;
    public int      code;
    public String   msg;
    public int      subcode;
    public String   subdesc;

    public  static class DataBean {
        /**
         * userId : 207431
         * accessToken : 3210fa664246493380da8f42db78df4f
         * expiresIn : 7200
         * refreshToken : b4d810f65a304b76bba06cfc2663b182
         */

        public String userId;
        public String accessToken;
        public int    expiresIn;
        public String refreshToken;

        public UserCreateInfo data;

        public static class UserCreateInfo {
            public boolean newUser;
        }
    }
}
