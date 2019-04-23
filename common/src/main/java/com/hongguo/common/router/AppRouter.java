package com.hongguo.common.router;

import android.content.Context;
import android.net.Uri;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created time 2017/12/7.
 *
 * @author losg
 */

public class AppRouter implements RouterConstants {

    public static void navigation(String routerUrl) {
        ARouter.getInstance().build(routerUrl).navigation();
    }

    public static void navigation(Context context, Uri uri, NavigationCallbackImp navigationCallbackImp) {
        ARouter.getInstance().build(uri).navigation(context, navigationCallbackImp);
    }

    public static Builder createBuilder(String routerUrl) {
        return new Builder(routerUrl);
    }

    public static class Builder {

        private String   mRouterUrl;
        private Postcard mBuild;

        public Builder(String routerUrl) {
            mRouterUrl = routerUrl;
            mBuild = ARouter.getInstance().build(mRouterUrl);
        }

        public Builder putInt(String key, int value) {
            mBuild.withInt(key, value);
            return this;
        }

        public Builder putString(String key, String value) {
            mBuild.withString(key, value);
            return this;
        }

        public void build() {
            mBuild.navigation();
        }
    }

}
