package com.zamanak.msglib.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zamanak.msglib.MsgSdk;
import com.zamanak.msglib.R;
import com.zamanak.msglib.base.BaseActivity;

public class MsgActivity extends BaseActivity {

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        pushFragment(FragmentMessages.class, R.id.fragment_msg);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(MsgSdk.PRIMARY_COLOR_DARK);
        }
    }

    @Override
    protected int getToolbarMenuLayout() {
        return 0;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_msg;
    }

    @Override
    protected int getToolbarTitle() {
        return 0;
    }

    @Override
    protected void setListener() {
    }
}
