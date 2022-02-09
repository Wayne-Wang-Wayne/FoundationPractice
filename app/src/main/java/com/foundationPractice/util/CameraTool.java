package com.set.newsapp.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;

import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.set.newsapp.R;
import com.set.newsapp.broke.BrokeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by user on 2017/5/25.
 */

public class CameraTool {
    private Context mContext;
    private Activity mActivity;

    public static final int MAX_ATTACHMENT_UPLOAD_SIZE = (5 * 1024 * 1024);  //上傳文件5MB限制

    //onRequestPermissionsResult switch 判斷
    public static final int CameraPermission = 1;   //相機使用權限
    public static final int ExternalPermission = 2; //SD卡使用權限
    public static final int CameraExternalPermission = 3; //

    public static final int CameraRequest = 4;
    public static final int ExternalStorage = 5;
    private final static int mVersion = Build.VERSION.SDK_INT; //SDK版本

    //寫入SD卡權限
    int mWriteExternalStoragePermission;

    //讀取SD卡權限
    int mReadExternalStoragePermission;
    int mCameraPermission;


    //存放照片的資料夾
    protected File mDirFile = new File(Environment.getExternalStorageDirectory() + File.separator + "setn");
    protected Dialog mDialog;


    public CameraTool(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;

        mWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE);
        mReadExternalStoragePermission = ActivityCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE);
        mCameraPermission = ActivityCompat.checkSelfPermission(mContext, CAMERA);
    }

    /*-----跳出dialog相關-----*/
    //dialog 相機/圖片選擇
    public void showSelectPhotoDialog(int diagWidth, int diagHeight) {
        mDialog = new Dialog(mContext, R.style.popupDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout selectPhoto = (LinearLayout) LinearLayout.inflate(mActivity.getLayoutInflater().getContext(), R.layout.dialog_select_photo, null);
        mDialog.setContentView(selectPhoto, new ViewGroup.LayoutParams(diagWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//        mDialog.setContentView(selectPhoto, new ViewGroup.LayoutParams(diagWidth, diagHeight));

        //設定dialog出現在最下面
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.windowAnimations = R.style.DialogAnimationTheme;
        window.setAttributes(wlp);

        RelativeLayout camera_layout = mDialog.findViewById(R.id.camera_layout);
        RelativeLayout photo_layout = mDialog.findViewById(R.id.photo_layout);

        //使用相機
        camera_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromCamera();
                mDialog.dismiss();
            }
        });

        //從圖片庫找
        photo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromExternal();
                mDialog.dismiss();
            }
        });

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    /*-----使用相機相關------*/

    //指定照片儲存位置並打開相機
    public void getPicFromCamera() {
        int writeExternalStoragePermission = ActivityCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE);
        int readExternalStoragePermission = ActivityCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(mContext, CAMERA);
        int audioPermission = ActivityCompat.checkSelfPermission(mContext, RECORD_AUDIO);
        if (writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED &&
                readExternalStoragePermission == PackageManager.PERMISSION_GRANTED &&
                cameraPermission == PackageManager.PERMISSION_GRANTED &&
                audioPermission == PackageManager.PERMISSION_GRANTED) {
            SandriosCamera
                    .with()
                    .setShowPicker(true)
                    .setVideoFileSize(10)
                    .setShowPicker(false)
                    .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                    .enableImageCropping(true)
                    .launchCamera(mActivity);
        } else {
            ActivityCompat.requestPermissions(mActivity, new String[]{RECORD_AUDIO, CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, CameraExternalPermission);
        }
    }

    public String getCameraImgPath() {
        return mDirFile + "/0.jpg";
    }
    /*---------選取照片相關------------*/


    public void getPicFromExternal() {
        int writeExternalStoragePermission = ActivityCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE);
        int readExternalStoragePermission = ActivityCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE);
        if (writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED && readExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
            //使用Intent.ACTION_GET_CONTENT這個Action
            // 會開啟選取圖檔視窗讓您選取手機內圖檔
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //讓使用者複選檔案
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            //取得相片後返回本畫面
            mActivity.startActivityForResult(intent, ExternalStorage);
        } else {
            ActivityCompat.requestPermissions(mActivity, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, ExternalPermission);
        }

    }


    /**
     * 將圖片按照某個角度進行旋轉並縮圖
     *
     * @param bitmap 需要旋轉、縮圖的圖片
     * @return 旋轉、縮圖後的圖片
     */
    public Bitmap rotateBitmap(Bitmap bitmap, String path) {
        ExifInterface exifInterface = null;
        Bitmap returnBm;
        int degree = 0;
        try {
            exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientation == 3) {//需要旋轉180度
                degree = 180;
            } else if (orientation == 6) {//需要旋轉900度
                degree = 90;
            } else if (orientation == 8) {//需要旋轉270度
                degree = 270;
            }
            // 根據旋轉角度，生成旋轉矩陣
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return returnBm;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //判斷文件大小
    public boolean FormatFileSize(long fileS) {
        if (fileS > MAX_ATTACHMENT_UPLOAD_SIZE) {
            return false;
        }
        return true;
    }

    //從路徑取得文件名稱
//    public String getPhotoNameFormPath(Context context, Uri uri) {
//        String[] pathArray = getPath(context, uri).split("/");
//        return pathArray[pathArray.length - 1];
//    }


    //取得選擇圖片路徑
    public void getPath(Context context, Uri uri, BrokeView.BrokeInfoView brokeInfoView) {
        ((Activity) context).runOnUiThread(() -> {
            try (ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r")) {
                if (pfd != null) {
                    String fileName = "";
                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                    if (bitmap == null) {
                        fileName = "tmp.mp4";
                    } else {
                        fileName = "tmp.png";
                    }
                    File f = new File(context.getCacheDir(), fileName);
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    OutputStream output = new FileOutputStream(f);
                    byte[] buffer = new byte[4 * 2048]; // or other buffer size
                    int read;

                    while ((read = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                    brokeInfoView.onFileLoad(f.getPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//                // TODO handle non-primary volumes
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri) || isGoogleDrive(uri)) {
//                try (ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r")) {
//                    if (pfd != null) {
//                        String fileName = "";
//                        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
//                        if (bitmap == null) {
//                            fileName = "tmp.mp4";
//                        } else {
//                            fileName = "tmp.png";
//                        }
//                        File f = new File(context.getCacheDir(), fileName);
//                        InputStream inputStream = context.getContentResolver().openInputStream(uri);
//                        OutputStream output = new FileOutputStream(f);
//                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
//                        int read;
//
//                        while ((read = inputStream.read(buffer)) != -1) {
//                            output.write(buffer, 0, read);
//                        }
//                        output.flush();
//                        return f.getPath();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{split[1]};
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            // Return the remote address
//            if (isGooglePhotosUri(uri))
//                return uri.getLastPathSegment();
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[]
            selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public boolean isGoogleDrive(Uri uri) {
        return String.valueOf(uri).toLowerCase().contains("com.google.android.apps");
    }
}
