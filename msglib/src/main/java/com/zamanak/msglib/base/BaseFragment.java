package com.zamanak.msglib.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zamanak.msglib.tools.CommonUtils;
import com.zamanak.msglib.tools.FontUtils;

/**
 * Created by PirFazel on 5/23/2017.
 */

public abstract class BaseFragment extends Fragment implements
        View.OnClickListener, ViewBehavior {

    protected View mContentView;
    protected ActionBar actionBar;
    protected String fragment_TAG;
    protected BaseActivity mActivity;

    boolean hasToolbar = true;
    boolean hasToolbarIcon = true;
    boolean hasOptionMenu = true;
    boolean hasToolbarBackPressedBtn = true;
    private ProgressDialog mProgressDialog;

    protected abstract void setListener();

    protected abstract int getToolbarIcon();

    protected abstract int getToolbarTitle();

    protected abstract int getLayoutResource();

    protected abstract int getToolbarMenuLayout();

    protected abstract boolean hasToolbarBackPressedBtn();

    protected abstract void initToolbarOnFragmentDestroy();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void processLogic(Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = mActivity.actionBar;
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutResource() != 0) {
            mContentView = LayoutInflater.from(mActivity).inflate(getLayoutResource(), null);
        }
        if (mContentView != null) {
            initView(savedInstanceState);
            initToolbarItems();
            initToolbar();
            processLogic(savedInstanceState);
            setListener();
        }
        return mContentView;
    }

    private void initToolbarItems() {
        hasToolbar = getToolbarTitle() != 0;
        hasOptionMenu = getToolbarMenuLayout() != 0;
        hasToolbarBackPressedBtn = hasToolbarBackPressedBtn();
        hasToolbarIcon = getToolbarIcon() != 0;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        if (getToolbarMenuLayout() != 0 && hasOptionMenu) {
            inflater.inflate(getToolbarMenuLayout(), menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mActivity.onBackPressed();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    protected void initToolbar() {
        if (actionBar != null) {
            if (hasToolbar) {
                actionBar.show();
                setToolbarBackButton();
                actionBar.setDisplayShowHomeEnabled(true);
                setToolbarIcon();
                setToolbarTitle();
            } else {
                actionBar.hide();
            }
        }
    }

    private void setToolbarBackButton() {
        if (hasToolbarBackPressedBtn()) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setToolbarIcon() {
        if (hasToolbarIcon) {
            actionBar.setIcon(getToolbarIcon());
        } else {
            actionBar.setIcon(null);
        }
    }

    private void setToolbarTitle() {
        if (hasToolbar) {
            actionBar.setTitle(FontUtils.textWithFont(mActivity, getToolbarTitle()));
        } else {
            actionBar.setTitle("");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragment_TAG = this.getClass().getSimpleName();
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            mActivity = (BaseActivity) context;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragment_TAG = this.getClass().getSimpleName();
        if (activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;
            mActivity = (BaseActivity) activity;
            baseActivity.onFragmentAttached();
        }
    }

    public void onClick(View v) {
        //TODO in subclasses!
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewById(@IdRes int id) {
        return (T) mContentView.findViewById(id);
    }

    protected int getFmBackStackEntryCount() {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        return fm.getBackStackEntryCount();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onUserVisible();
        } else {
            onUserInVisible();
        }
    }

    public void onUserVisible() {
    }

    public void onUserInVisible() {
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this.getContext());
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onError(String message) {
        if (mActivity != null) {
            mActivity.onError(message);
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.onError(resId);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.showMessage(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (mActivity != null) {
            return mActivity.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {

        if (mActivity != null) {
            mActivity.openActivityOnTokenExpire();
        }
    }

    @Override
    public void onDestroyView() {

        initToolbarOnFragmentDestroy();
        hideKeyboard();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    public String get_string(int string_id) {

        return mActivity.getString(string_id);
    }
}
