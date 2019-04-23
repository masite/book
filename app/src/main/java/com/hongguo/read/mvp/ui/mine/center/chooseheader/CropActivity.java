package com.hongguo.read.mvp.ui.mine.center.chooseheader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.hongguo.read.R;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.eventbus.UploadAvatarEvent;
import com.hongguo.read.utils.BitmapUtil;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;

/**
 * Created by losg on 2018/1/8.
 */

public class CropActivity extends ActivityEx implements CropImageView.OnBitmapSaveCompleteListener {

    private static final String INTENT_IMAGE_PATH = "intent_image_path";

    @BindView(R.id.crop_image)
    CropImageView mCropImage;

    private int mSaveWidth = 0;

    private Bitmap mBitmap;

    public static void toActivity(Context context, String imagePath) {
        Intent intent = new Intent(context, CropActivity.class);
        intent.putExtra(INTENT_IMAGE_PATH, imagePath);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_crop_image;
    }

    @Override
    protected void initView() {
        setTitle("图片剪切");

        String imagePath = getIntent().getStringExtra(INTENT_IMAGE_PATH);
        ImageLoadUtils.loadUrlDefault(mCropImage, imagePath);
        mCropImage.setFocusStyle(CropImageView.Style.RECTANGLE);
        mSaveWidth = getResources().getDisplayMetrics().widthPixels * 2 / 3;
        mCropImage.setFocusWidth(mSaveWidth);
        mCropImage.setFocusHeight(mSaveWidth);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(imagePath, options);
        mCropImage.setImageBitmap(mCropImage.rotate(mBitmap, BitmapUtil.getBitmapDegree(imagePath)));

        mCropImage.setOnBitmapSaveCompleteListener(this);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = width / reqWidth;
            } else {
                inSampleSize = height / reqHeight;
            }
        }
        return inSampleSize;
    }

    @Override
    public void onBitmapSaveSuccess(File file) {
        dismissWaitDialogWithoutAnim();
        EventBus.getDefault().post(new UploadAvatarEvent(file.getAbsolutePath()));
        finish();
    }

    @Override
    public void onBitmapSaveError(File file) {
        dismissWaitDialog();
        toastMessage("保存失败");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem save = menu.add(0, 0, 0, "保存");
        save.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            saveFile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFile() {
        showWaitDialog(true, "图片处理中", null);
        mCropImage.saveBitmapToFile(new File(FileManager.getTempPath("avatar")), mSaveWidth, mSaveWidth, true);
    }
}
