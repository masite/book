package com.hongguo.common.base;

import android.app.Application;

import com.losg.library.base.BaseView;
import com.losg.library.widget.dialog.MessageInfoDialog;

/**
 * Created by losg on 2016/11/1.
 */

public interface BaseViewEx extends BaseView {

    Application findApp();

    void showMessDialog(String title, String content, String cancelLeft, String okRight, MessageInfoDialog.DialogButtonClick dialogButtonClick);

    void finishView();

    void setPresener(BasePresenter basePresenter);

    void checkPermission(String... permission);

    void checkAllPermission(String... permission);

}
