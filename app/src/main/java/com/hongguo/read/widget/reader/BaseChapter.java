package com.hongguo.read.widget.reader;

/**
 * Created by losg on 2018/1/21.
 */

public abstract class BaseChapter {

    public enum PageLoad {
        LOADING,            //页面正在加载中
        SUCCESS,            //页面加载成功
        NEND_PAY,           //页面需要支付
        NET_ERROR,          //网络异常
        NONE                //没有上一页或者下一页
    }

    //文件路径
    public String   filePath;
    public String   chapterName;
    //书的加载状态(success 后filepath 不能为空)
    public PageLoad pageLoad;

}
