package com.common.base.codereview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.utils.ToastUtils;
import com.common.view.dialog.LoadingDialog;
import com.common.view.dialog.SucessCacheSureDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created  on 2018-01-05.
 * author:seven
 * email:seven2016s@163.com
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public View rootView;
    private Unbinder unbinder;
    private LoadingDialog loadingDialog;
    private SucessCacheSureDialog sureDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        initData();
        setLisenter();
        return rootView;
    }


//    protected abstract void initView(View rootView, Bundle savedInstanceState);

    /*
     * 带图片的toast
     * */
    public void showSuccessToast(String msg) {
        ToastUtils.success(msg);
    }

    /*
     * error的toast
     * */
    public void showErrorToast(String msg) {
        ToastUtils.error(msg);
    }

    protected abstract int getLayoutId();

    protected void initView() {

        loadingDialog = new LoadingDialog(getActivity());
        sureDialog = new SucessCacheSureDialog(getActivity());
    }

    public SucessCacheSureDialog showSuccessDialog(String txt) {
        if (sureDialog == null) {
            sureDialog = new SucessCacheSureDialog(getActivity());
        }
        sureDialog.setTextContent(txt);
        sureDialog.setCancleGone();
        sureDialog.setDeletaCacheListener(new SucessCacheSureDialog.DeletaCacheListener() {
            @Override
            public void sure() {
                sureDialog.dismiss();
                getActivity().finish();
            }

            @Override
            public void cancle() {
                sureDialog.dismiss();
            }
        });

        if (!getActivity().isFinishing()) {
            sureDialog.show();
        }
        return null;
    }

    public void showLoadingDialog(String txt) {
        if (loadingDialog != null && !getActivity().isFinishing()) {
            loadingDialog.setMessage(txt);
            loadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null && !getActivity().isFinishing()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadingDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    protected abstract void initData();

    protected abstract void setLisenter();

    protected abstract void widgetClick(View v);

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}