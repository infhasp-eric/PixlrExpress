package com.infhaps.pixlrexpress.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infhaps.pixlrexpress.R;
import com.infhaps.pixlrexpress.base.BaseActivity;

public class MainActivity extends Activity {
    private LinearLayout linImgPix;//具体操作显示的容器
    private TextView btChoseImg, btChose1, btChose2;
    private int PHOTO_REQUEST_GALLERY = 1;

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
        linImgPix = (LinearLayout) findViewById(R.id.lin_img_pix);
        btChose1 = (TextView) findViewById(R.id.bt_chose_1);
        btChose2 = (TextView) findViewById(R.id.bt_chose_2);
        btChoseImg = (TextView) findViewById(R.id.bt_chose_img);
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        btChoseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType("image/*");
                startActivityForResult(getAlbum, PHOTO_REQUEST_GALLERY);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_GALLERY ://选择图库
                    break;
            }
        }
    }
}