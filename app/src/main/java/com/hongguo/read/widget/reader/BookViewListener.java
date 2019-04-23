package com.hongguo.read.widget.reader;

/**
 * Created by losg on 2018/1/18.
 * 书籍信息回调
 */
public interface BookViewListener {

    /**
     * 阅读页设置点击(书本中间部分点击 弹出设置菜单操作)
     */
    void bookSettingClick();

    /**
     * 阅读页滚动(主要隐藏设置的panel信息)
     */
    void bookScrolled();

    /**
     * 阅读页数变换(实时更新阅读进度 防止异常操作，没有保存阅读进度)
     *
     * @param currentPage
     */
    void pageChanged(int currentPage);

    /**
     * 章节变更
     * @param currentChapterIndex
     */
    void chapterChanged(int currentChapterIndex);

    /**
     * 支付按钮被点击
     *
     * @param currentPage
     */
    void bookPayClicked(int currentPage);

    /**
     * 开通vip 按钮点击
     *
     * @param currentPage
     */
    void bookVipClicked(int currentPage);

    /**
     * 阅读页已经是末页了
     */
    void bookIsEndChapter();

    /**
     * 阅读页已经是首页了
     */
    void bookIsFirstChapter();

    /**
     * 网络错误点击重新加载
     * @param currentIndex
     */
    void errorReloadClick(int currentIndex);

    /**
     * 打赏按钮点击
     */
    void bookRewardClick();

}
