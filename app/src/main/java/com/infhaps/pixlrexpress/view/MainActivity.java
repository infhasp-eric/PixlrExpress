package com.infhaps.pixlrexpress.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infhaps.pixlrexpress.R;
import com.infhaps.pixlrexpress.base.BaseActivity;
import com.infhaps.pixlrexpress.dialog.LoadDialog;
import com.infhaps.pixlrexpress.utils.BitmapUtils;

import java.util.Date;

public class MainActivity extends Activity {
    private LinearLayout linImgPix;//具体操作显示的容器
    private TextView btChoseImg, btChose1, btChose2, btChose3, bt_save;
    private Button btShowOld;
    private ImageView showImg;
    private static int RESULT_LOAD_IMAGE = 123;
    private Uri imageFileUri;
    private Bitmap choseBitmap;
    private Bitmap newBitmap;
    private LoadDialog loadDialog;
    private boolean isShowOld = false;

    private Handler upHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadDialog.cancel();
            showImg.setImageBitmap(newBitmap);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化控件的方法
     */
    private void initView() {
        loadDialog = new LoadDialog(this);

        linImgPix = (LinearLayout) findViewById(R.id.lin_img_pix);
        btChose1 = (TextView) findViewById(R.id.bt_chose_1);
        btChose2 = (TextView) findViewById(R.id.bt_chose_2);
        btChose3 = (TextView) findViewById(R.id.bt_chose_3);
        btChoseImg = (TextView) findViewById(R.id.bt_chose_img);
        showImg = (ImageView) findViewById(R.id.show_img);
        btShowOld = (Button) findViewById(R.id.bt_show_old);
        bt_save = (TextView) findViewById(R.id.bt_save);
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        btChoseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        btChose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ImgeditActivity.class);
//                intent.putExtra("imgPath", imageFileUri);
//                startActivity(intent);
                loadDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(newBitmap == null) {
                            newBitmap = BitmapUtils.changeLight(choseBitmap, 10);
                        } else {
                            newBitmap = BitmapUtils.changeLight(newBitmap, 10);
                        }
                        upHandler.sendMessage(new Message());
                    }
                }).start();
            }
        });

        btChose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(newBitmap != null) {
                            newBitmap = BitmapUtils.averageFilter(3, 3, newBitmap);
                        } else {
                            newBitmap = BitmapUtils.averageFilter(3, 3, choseBitmap);
                        }
                        upHandler.sendMessage(new Message());
                    }
                }).start();
            }
        });

        btChose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (newBitmap != null) {
                            newBitmap = BitmapUtils.medianFilter(3, 3, newBitmap);
                        } else {
                            newBitmap = BitmapUtils.medianFilter(3, 3, choseBitmap);
                        }
                        upHandler.sendMessage(new Message());
                    }
                }).start();
            }
        });

        btShowOld.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(newBitmap != null) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            showImg.setImageBitmap(choseBitmap);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            showImg.setImageBitmap(newBitmap);
                            break;
                        case MotionEvent.ACTION_UP:
                            showImg.setImageBitmap(newBitmap);
                            break;
                    }
                }
                return false;
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newBitmap != null) {
                    Date now = new Date();
                    DateFormat df = new DateFormat();
                    String date = df.format("yyyy_MM_dd_hh_mm_ss", now).toString();
                    Log.i("Save", date);
                    String fileName = date + ".jpeg";
                    String savePath = BitmapUtils.saveBitmap(newBitmap, fileName);
                    if(savePath != null && savePath.length() > 0) {
                        Toast.makeText(MainActivity.this, "已保存" + savePath, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != intent) {
            imageFileUri = intent.getData();
            /*String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imageFileUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //picturePath = cursor.getString(columnIndex);
            cursor.close();*/
            choseBitmap = getBitmapFromUri(imageFileUri);
            showImg.setImageBitmap(choseBitmap);
            linImgPix.setVisibility(View.VISIBLE);
            btChoseImg.setVisibility(View.GONE);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}