package com.common.network;


import io.reactivex.disposables.Disposable;

/**
 * Created  on 2018-03-20.
 * author:seven
 * email:seven2016s@163.com
 */

//public abstract class HttpResultObserver<T extends ResponseCustom> extends HttpCommonObserver<T> {
public abstract class HttpResultObserver<T> extends HttpCommonObserver<T> {

    protected abstract void onLoading(Disposable d);

    protected abstract void onSuccess(T o);

    protected abstract void onFail(ApiException e);

    @Override
    protected void onStart(Disposable d) {
        onLoading(d);
    }

    @Override
    protected void _onNext(T responseCustom) {
        onSuccess(responseCustom);
//        if (responseCustom.isSuccess()) {
//        } else {
//            onFail(new ApiException(1, (String) responseCustom.getData()));
//        }
    }

    @Override
    protected void _onError(ApiException error) {
        onFail(error);
//        Log.e("网络处理异常", error.getMessage());
    }
}
