package com.hongguo.read.mvp.ui.error;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.hongguo.common.utils.FileUtils;
import com.hongguo.read.R;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.constants.FileManager;

import java.io.File;

/**
 * Created by Administrator on 2017/8/22.
 */

public class ErrorReportActivity extends ActivityEx {

    private TextView mErrorMessage;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, ErrorReportActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("错误日志");
        mErrorMessage = findViewById(R.id.error_message);

        String errorInfo = FileUtils.readFullFile(new File(FileManager.getErrorReport()));
        if (TextUtils.isEmpty(errorInfo)) {
            toastMessage("没有错误信息");
            finish();
            return;
        }
        mErrorMessage.setText(errorInfo);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_error;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem delete = menu.add(0, 0, 0, "删除");
        delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 0){
            FileUtils.deleteFile(new File(FileManager.getErrorReport()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
