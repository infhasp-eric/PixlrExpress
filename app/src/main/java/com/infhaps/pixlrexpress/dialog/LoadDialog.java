package com.infhaps.pixlrexpress.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.infhaps.pixlrexpress.R;

/**
 * Created by Admin on 2015/11/23.
 */
public class LoadDialog extends Dialog {

    public LoadDialog(Context context) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_dialog);
    }

}