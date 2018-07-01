package com.zamanak.msglib.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zamanak.msglib.R;
import com.zamanak.msglib.tools.CommonUtils;
import com.zamanak.msglib.tools.FontUtils;
import com.zamanak.msglib.tools.NetworkUtils;

import java.util.List;


/**
 * Created by PirFazel on 5/23/2017.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener, ViewBehavior, BaseFragment.Callback {

    protected String activity_TAG;
    protected Activity mActivity;
    public Menu menu;
    private ProgressDialog mProgressDialog;
    private View actionBarView;
    protected ActionBar actionBar;
    public View toolbarIcon;
    public Fragment currentFragment;
    private View toolbarTitle;

    protected abstract void processLogic(Bundle savedInstanceState);

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract int getToolbarMenuLayout();

    protected abstract int getLayoutResource();

    protected abstract int getToolbarTitle();

    protected abstract void setListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivity = this;
        //App.setAppContext(mActivity);
        activity_TAG = this.getClass().getSimpleName();
        actionBar = getSupportActionBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutResource());
        initCustomToolbar();
        initView(savedInstanceState);
        processLogic(savedInstanceState);
        forceRTLIfSupported();
        setListener();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        if (getToolbarMenuLayout() != 0) {
            mActivity.getMenuInflater().inflate(getToolbarMenuLayout(), menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    private void initToolbar() {

        if (actionBar != null && getToolbarTitle() != 0) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(FontUtils.textWithFont(mActivity, getToolbarTitle()));
        }
    }

    private void initCustomToolbar() {

        LayoutInflater inflater = (LayoutInflater)
                mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            actionBarView = inflater.inflate(R.layout.layout_custom_toolbar, null);
            toolbarIcon = actionBarView.findViewById(R.id.toolbarIcon);
            toolbarTitle = actionBarView.findViewById(R.id.toolbarTitle);
        }
    }

    public void enableToolbarTitle() {
        toolbarTitle.setVisibility(View.VISIBLE);
    }

    public void disableToolbarTitle() {
        toolbarTitle.setVisibility(View.GONE);
    }

    public void initToolbar(String title, int icon, boolean hasBackBtn) {

        try {
            ((ImageView) actionBarView.findViewById(R.id.toolbarIcon)).setImageResource(icon);
            ((TextView) actionBarView.findViewById(R.id.toolbarTitle)).
                    setText(FontUtils.textWithFont(mActivity, title));
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(hasBackBtn);
            actionBar.setCustomView(actionBarView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
    }

    public void pushFragment(Class<?> cls, int id) {

        pushFragment(cls, null, id, true);
    }

    public void addFragment(Class<?> cls, int id) {

        addFragment(cls, null, id, true);
    }

    public void addFragment(Class<?> cls, int id, int anim_in, int anim_out) {

        addFragment(cls, null, id, true, anim_in, anim_out);
    }

    public void pushFragment(Class<?> cls, Bundle bundle, int id, boolean addToBackStack) {

        hideKeyboard();
        Fragment f = Fragment.instantiate(mActivity, cls.getName());
        if (bundle != null) f.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, f);
        // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            ft.addToBackStack(f.getTag());
        }
        ft.commit();
    }

    public void addFragment(Class<?> cls, Bundle bundle, int id, boolean addToBackStack) {
        hideKeyboard();
        Fragment f = Fragment.instantiate(mActivity, cls.getName());
        if (bundle != null) f.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(id, f);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addToBackStack) {
            ft.addToBackStack(f.getTag());
        }
        ft.commit();
    }

    public void addFragment(Class<?> cls, Bundle bundle, int id, boolean addToBackStack, int anim_in, int anim_out) {
        hideKeyboard();
        Fragment f = Fragment.instantiate(mActivity, cls.getName());
        if (bundle != null) f.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(id, f);
        //  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            ft.addToBackStack(f.getTag());
        }
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        //ft.setCustomAnimations(anim_in, anim_out);
        ft.commit();
    }

    public Fragment getTopFragment() {

        @SuppressLint("RestrictedApi")
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (int i = fragmentList.size() - 1; i >= 0; i--) {
            Fragment top = fragmentList.get(i);
            if (top != null) {
                return top;
            }
        }
        return null;
    }

    public void clearBackStack() {

        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void popBackStack() {

        getSupportFragmentManager().popBackStack();
    }


    private void showSnackBar(String message) {

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    public void hideKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void showLoading() {

        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    public void hideLoading() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onError(String message) {

        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.error));
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void showMessage(String message) {

        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {

        return NetworkUtils.isNetworkAvailable(getApplicationContext());
    }

    @Override
    public void openActivityOnTokenExpire() {

        //Utility.runActivity(mActivity, ActivityLogin.class, true);
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

