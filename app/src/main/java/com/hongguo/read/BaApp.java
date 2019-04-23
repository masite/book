package com.hongguo.read;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hongguo.common.CommonApp;
import com.hongguo.common.dagger.module.BaseApiModule;
import com.hongguo.common.dagger.module.UserApiModule;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.common.utils.SpStaticUtil;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.read.dagger.component.AppComponent;
import com.hongguo.read.dagger.component.DaggerAppComponent;
import com.hongguo.read.dagger.module.ApiModule;
import com.hongguo.read.dagger.module.AppModule;
import com.hongguo.read.utils.BuglyCrashReportUtil;
import com.hongguo.read.utils.atlas.AtlasUtils;
import com.hongguo.read.utils.down.ChapterDown;
import com.hongguo.read.utils.down.HgReadDownManager;
import com.hongguo.read.utils.umeng.UmengPushUtil;
import com.hongguo.read.utils.update.DownService;
import com.hongguo.read.widget.emoji.EmojiFactory;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;


/**
 * Created by losg on 2017/7/18.
 */

public class BaApp extends CommonApp {

    private AppComponent      mAppComponent;
    private DownService       mDownService;
    private HgReadDownManager mChapterDownService;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.sIsBug = BuildConfig.DEBUG;
        //loading 中使用,不放在异步
        StatisticsUtils.init(this);
        // SharedPreferences 工具类
        SpStaticUtil.init(this);
        //下载服务
        initDownService();
        //阿里路由
        ARouter.init(this);
        //异步信息
        delayInit();

        //dagger 全局变量组件
        mAppComponent = DaggerAppComponent.builder().apiModule(new ApiModule()).appModule(new AppModule(this)).baseApiModule(new BaseApiModule(BuildConfig.DEBUG)).userApiModule(new UserApiModule()).build();
    }

    /**
     * 下载服务
     */
    private void initDownService() {
        mDownService = DownService.createDownService();
        mDownService.init();
    }

    /**
     * 延时初始化，加快启动速度
     */
    private void delayInit() {
        RxJavaUtils.delayRun(1000, () -> {
            EmojiFactory.init(getApplicationContext());
            mChapterDownService = HgReadDownManager.createDownService(new ChapterDown(mAppComponent.getSelfApiService()));
            mChapterDownService.init();

            //讯飞语音
            SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59631680");

            //友盟推送
            initPush();
            initAtlas();
            initX5WebView();

            //崩溃日志操作
            BuglyCrashReportUtil.init(getApplicationContext());
        });
    }

    /**
     * 初始化 atlas (增量更新)
     *
     * @link "https://github.com/alibaba/atlas"
     */
    private void initAtlas() {
        AtlasUtils.initAtlas(this, mAppComponent.getSelfApiService(), mDownService);
    }

    /**
     * 友盟推送信息
     */
    private void initPush() {
        UmengPushUtil.init(this);
    }

    public HgReadDownManager getChapterDownService() {
        return mChapterDownService;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public DownService getDownService() {
        return mDownService;
    }

}
