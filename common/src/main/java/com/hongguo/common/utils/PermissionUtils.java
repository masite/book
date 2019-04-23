package com.hongguo.common.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.losg.library.widget.dialog.MessageInfoDialog;


/**
 * Created by losg on 2016/5/30.
 */
public class PermissionUtils {

    private Context  mContext;
    private String   mPermission;
    private String[] mPermissions;
    private boolean isMSDK                = false;
    private boolean isToRequestPermission = false;
    private PermissionListener permissionListener;
    private boolean isMust = false;

    public PermissionUtils(Context mContext) {
        this.mContext = mContext;
        if (Build.VERSION.SDK_INT >= 23) {
            isMSDK = true;
        }
    }

    public PermissionUtils setMust(boolean must) {
        isMust = must;
        return this;
    }

    public String getmPermission() {
        return mPermission;
    }

    //获取权限
    public void permissionCheck(final String permission) {
        //低版本不需要动态权限，直接返回成功后续做其它操作
        if (!isMSDK) {
            if (permissionListener != null) {
                permissionListener.permissionSuccess();
            }
            return;
        }
        mPermission = permission;
        //没有获取到权限
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
            //没有获取权限
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, mPermission)) {
                //需要提示用户
                MessageInfoDialog messageInfoDialog = new MessageInfoDialog(mContext);
                messageInfoDialog.setTitle("提醒");
                messageInfoDialog.setMessage("部分功能需要您的授权，否则影响使用");
                messageInfoDialog.setButtonTitle("知道了", "");
                messageInfoDialog.setDialogButtonClick(messageInfoDialog1 -> {
                    //请求获取权限
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, 100);
                    messageInfoDialog1.dismiss();
                });
                messageInfoDialog.show();
                return;
            }
            //请求获取权限
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, 100);
            return;
        }
        if (permissionListener != null) {
            permissionListener.permissionSuccess();
        }
    }


    //获取权限
    public void permissionCheckAll(final String... permission) {
        //低版本不需要动态权限，直接返回成功后续做其它操作
        if (!isMSDK) {
            if (permissionListener != null) {
                permissionListener.permissionSuccess();
            }
            return;
        }
        mPermissions = permission;

        boolean needPermission = false;
        boolean needDialog = false;

        for (String s : permission) {
            if (ContextCompat.checkSelfPermission(mContext, s) != PackageManager.PERMISSION_GRANTED) {
                needPermission = true;
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, s)) {
                needDialog = true;
            }
        }

        //没有获取到权限
        if (needPermission) {
            //没有获取权限
            if (needDialog) {
                //需要提示用户
                MessageInfoDialog messageInfoDialog = new MessageInfoDialog(mContext);
                messageInfoDialog.setTitle("提醒");
                messageInfoDialog.setMessage("部分功能需要您的授权，否则影响使用");
                messageInfoDialog.setButtonTitle("知道了", "");
                messageInfoDialog.setDialogButtonClick(messageInfoDialog1 -> {
                    //请求获取权限
                    ActivityCompat.requestPermissions((Activity) mContext, mPermissions, 100);
                    messageInfoDialog1.dismiss();
                });
                messageInfoDialog.show();
                return;
            }
            //请求获取权限
            ActivityCompat.requestPermissions((Activity) mContext, mPermissions, 100);
            return;
        }
        if (permissionListener != null) {
            permissionListener.permissionSuccess();
        }
    }

    public void onResume() {
        if (!isMSDK) {
            return;
        }
        //去请求权限
        if (isToRequestPermission) {
            checkUserPermission();
        }
        isToRequestPermission = false;
    }

    private void checkUserPermission() {
        if (permissionListener == null) {
            return;
        }

        if(!TextUtils.isEmpty(mPermission)) {
            if (ContextCompat.checkSelfPermission(mContext, mPermission) != PackageManager.PERMISSION_GRANTED) {
                permissionListener.permissionFailure();
            } else {
                permissionListener.permissionSuccess();
            }
        }

        if(mPermissions != null && mPermissions.length >= 1){
            boolean success = true;
            for (String permission : mPermissions) {
                if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    success = false;
                }
            }
            if(success){
                permissionListener.permissionSuccess();
            }else{
                permissionListener.permissionFailure();
            }

        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isMust", isMust);
        outState.putString("mPermission", mPermission);
        outState.putStringArray("mPermissions", mPermissions);
        outState.putBoolean("isToRequestPermission", isToRequestPermission);
    }

    public void onReBackState(Bundle saveState) {
        if (saveState == null) {
            return;
        }
        isMust = saveState.getBoolean("isMust");
        isToRequestPermission = saveState.getBoolean("isToRequestPermission");
        mPermission = saveState.getString("mPermission");
        mPermissions = saveState.getStringArray("mPermissions");
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!isMSDK) {
            return;
        }
        if (grantResults == null) {
            return;
        }

        boolean success = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                success = false;
            }
        }

        if (success) {
            if (permissionListener != null) {
                permissionListener.permissionSuccess();
            }
            return;
        }
        //授权失败
        permissionDialog();
    }

    //设置权限提示框
    private void permissionDialog() {

        String negativeString = isMust ? "退出应用" : "关闭";

        MessageInfoDialog messageInfoDialog = new MessageInfoDialog(mContext);
        messageInfoDialog.setTitle("提醒");
        messageInfoDialog.setMessage("您拒绝权限申请。\n请点击\"设置\" -\"权限\" - 打开所需的权限。 ");
        messageInfoDialog.setButtonTitle("设置", negativeString);
        messageInfoDialog.setDialogButtonClick(messageInfoDialog1 -> {
            startSetting();
            messageInfoDialog1.dismiss();
        });

        messageInfoDialog.setDialogCancelButtonClick(messageInfoDialog12 -> {
            if (permissionListener != null) {
                permissionListener.permissionFailure();
            }
            messageInfoDialog12.dismissWithoutAnim();
        });
        messageInfoDialog.show();
    }

    //打开应用的设置
    private void startSetting() {
        isToRequestPermission = true;
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:" + mContext.getPackageName()));
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            mContext.startActivity(intent);
        }
    }

    public void setPermissionListener(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    public interface PermissionListener {

        //授权成功
        void permissionSuccess();

        //授权失败
        void permissionFailure();
    }
}
