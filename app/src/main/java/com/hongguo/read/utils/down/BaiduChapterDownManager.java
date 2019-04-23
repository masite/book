package com.hongguo.read.utils.down;

import com.google.gson.Gson;
import com.hongguo.common.base.CommonBean;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.model.book.BaiduBuyOrderBean;
import com.hongguo.read.mvp.model.book.BaiduChapterDownInfo;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.share.BaiduRepertory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.LogUtils;
import com.hongguo.read.utils.baidu.BaiduShuChengUrls;
import com.hongguo.read.utils.down.exception.CoinNotEnoughException;
import com.hongguo.read.utils.down.exception.NetException;
import com.hongguo.read.utils.down.exception.OrderException;
import com.hongguo.common.utils.rxjava.SubscriberImp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by losg on 2018/1/17.
 *
 * 获取免费章节 or 下载描述信息
 *  先获取章节内容(百度对应账户有余额 可以直接获取到)  --> 获取到数据 --> 下载成功
 *                                              --> 获取不到  --> 本章节需要付费，请支持正版(固定)
 * 购买 章节 步骤
 * 先获取章节内容(百度对应账户有余额 可以直接获取到) --> 获取百度订单 ---> 服务器购买章节--->先获取章节内容 ---> 下载章节
 *
 */

public class BaiduChapterDownManager implements IChapterDown {

    @Override
    public HgReadDownManager.DownResult downFreeChapter(ApiService apiService, ChapterBean.Chapters chapter) {
        return getBookContent(apiService, chapter, false, true);
    }

    @Override
    public HgReadDownManager.DownResult downAndBuyChapter(ApiService apiService, ChapterBean.Chapters chapter) {
        return getBookContent(apiService, chapter, true, true);
    }

    /**
     * 获取百度书籍 章节内容
     *
     * @param apiService
     * @param chapter
     * @param buy
     * @param isFirstGetContent
     * @return
     */
    private HgReadDownManager.DownResult getBookContent(ApiService apiService, ChapterBean.Chapters chapter, boolean buy, boolean isFirstGetContent) {
        HgReadDownManager.DownResult result = new HgReadDownManager.DownResult();
        String downUrl = BaiduShuChengUrls.getBaiDuBuyedChapterDownloadUrl(chapter.bookid, chapter.chapterId + "");
        LogUtils.log("百度下载url" + downUrl);
        apiService.getBaiduDownUrl(downUrl).subscribe(new SubscriberImp<BaiduChapterDownInfo>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);

                result.mIsSuccess = false;
                result.mException = new NetException();
            }

            @Override
            public void onNext(BaiduChapterDownInfo baiduChapterDownInfo) {
                super.onNext(baiduChapterDownInfo);
                LogUtils.log("百度下载结果" + new Gson().toJson(baiduChapterDownInfo));
                LogUtils.log(chapter.chapterName + buy + "--" + "可以获取到百度书籍");
                if (baiduChapterDownInfo.code != 0) {
                    //不购买
                    if (!buy) {
                        LogUtils.log(chapter.chapterName + buy + "--" + "获取描述信息");
                        String downPath = FileManager.getBookDownDescribePath(Constants.BOOK_FROM.FROM_BAIDU, chapter.bookid, chapter.chapterId);
                        FileUtils.writeFile(downPath, "本章节需要付费，请支持正版");
                        result.mIsSuccess = true;
                    } else {
                        //第一请求失败，去下订单购买(防止百度账号里有余额 ~ ~)
                        if (isFirstGetContent) {
                            LogUtils.log(chapter.chapterName + buy + "--" + "第一次没哟获取书籍，下单");
                            HgReadDownManager.DownResult downResult = buyChapter(apiService, chapter);
                            result.mIsSuccess = downResult.mIsSuccess;
                            result.mException = downResult.mException;
                        } else {
                            LogUtils.log(chapter.chapterName + buy + "--" + "第二次下载失败");
                            result.mIsSuccess = false;
                            result.mException = new NetException();
                        }
                    }
                } else {
                    //下载文件
                    String tempPath = FileManager.getTempPath("1_" + chapter.bookid + "_" + chapter.chapterId + ".rar");
                    FileUtils.downLoadFile(baiduChapterDownInfo.result.data.paySuccess.downloadUrl, tempPath, new FileUtils.FileDownLoadCallBack() {
                        @Override
                        public void downError(String errorMessage) {
                            result.mIsSuccess = false;
                            result.mException = new NetException();
                            LogUtils.log(chapter.chapterName + buy + "--" + "下载失败");
                        }

                        @Override
                        public void success(String savePath) {
                            chapter.hasDownTotal = true;
                            LogUtils.log(chapter.chapterName + buy + "--" + "下载成功");
                            String bookChapterDownPath = FileManager.getBookChapterDownPath(UserRepertory.getUserID(), Constants.BOOK_FROM.FROM_BAIDU, chapter.bookid, chapter.chapterId);
                            unZipChapter(new File(savePath), bookChapterDownPath);
                            result.mIsSuccess = true;
                        }
                    });
                }
            }
        });
        return result;
    }

    /**
     * 购买 章节 步骤  获取百度订单 ---> 服务器购买章节 --->下载章节
     *
     * @param apiService
     * @param chapter
     * @return
     */
    private HgReadDownManager.DownResult buyChapter(ApiService apiService, ChapterBean.Chapters chapter) {
        HgReadDownManager.DownResult downResult = new HgReadDownManager.DownResult();
        String orderUlr = BaiduShuChengUrls.getBaiDuBuyedChapterOrderUrl(chapter.coin, chapter.bookid, chapter.chapterId, "1");
        List<Integer> errorSize = new ArrayList<>();
        apiService.getBaiduBuyChapterOrder(orderUlr).subscribe(new SubscriberImp<BaiduBuyOrderBean>() {
            @Override
            public void onError(Throwable e) {
                LogUtils.log(chapter.chapterName+"--"+"获取百度书籍订单失败");
                super.onError(e);
                errorSize.add(1);
            }

            @Override
            public void onNext(BaiduBuyOrderBean baiduBuyOrderBean) {
                LogUtils.log(chapter.chapterName+"--"+"获取百度书籍订单成功");
                super.onNext(baiduBuyOrderBean);
                HgReadDownManager.DownResult result = buyChapterService(apiService, chapter, baiduBuyOrderBean.result);
                downResult.mIsSuccess = result.mIsSuccess;
                downResult.mException = result.mException;
            }
        });
        if (errorSize.size() != 0) {
            downResult.mIsSuccess = false;
            downResult.mException = new OrderException();
        }
        return downResult;
    }

    /**
     * 服务器购买章节并请求下载章节
     * @param apiService
     * @param chapter
     * @param result
     * @return
     */
    private HgReadDownManager.DownResult buyChapterService(ApiService apiService, ChapterBean.Chapters chapter, BaiduBuyOrderBean.ResultBean result) {
        HgReadDownManager.DownResult downResult = new HgReadDownManager.DownResult();
        apiService.buyBaiduChapter(result.order_id, result.order_money, BaiduRepertory.getBaiduPayEXTRA()).subscribe(new SubscriberImp<CommonBean>(){

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                downResult.mIsSuccess = false;
                downResult.mException = new NetException();
                LogUtils.log(chapter.chapterName+"--"+"服务器购买章节失败---网络" );
            }

            @Override
            public void onNext(CommonBean commonBean) {
                LogUtils.log(chapter.chapterName+"--"+"服务器购买章节");
                super.onNext(commonBean);
                if(commonBean.code == 0){
                    LogUtils.log(chapter.chapterName+"--"+"服务器购买章节成功");
                    downResult.mIsSuccess = true;
                }else{
                    LogUtils.log(chapter.chapterName+"--"+"服务器购买章节失败");
                    downResult.mIsSuccess = false;
                    downResult.mException = new CoinNotEnoughException();

                }
            }
        });
        if(downResult.mIsSuccess){
            HgReadDownManager.DownResult bookContent = getBookContent(apiService, chapter, true, false);
            downResult.mIsSuccess = bookContent.mIsSuccess;
            downResult.mException = bookContent.mException;
        }
        return downResult;
    }


    private void unZipChapter(File zipFile, String savePath) {
        FileOutputStream out = null;
        ZipInputStream inZip = null;
        try {
            inZip = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                } else {
                    File file = new File(savePath);
                    out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[4 * 1024];
                    while ((len = inZip.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.log("解压失败:--" + zipFile + "---" + savePath);
        } finally {
            FileUtils.closeInput(inZip);
            FileUtils.closeOutput(out);
            FileUtils.deleteFile(zipFile);
        }
    }

}
