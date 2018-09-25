package com.yuefeng.usercenter.presenter;


/**
 * Created  on 2018-05-29.
 * author:seven
 * email:seven2016s@163.com
 */

public class CollectPresenter {}
      /*  extends BasePresenterImpl<CollectContract.View, CollectListActivity> implements CollectContract.Presenter {
    public CollectPresenter(CollectContract.View view, CollectListActivity activity) {
        super(view, activity);
    }

    @Override
    public void getCollectList(int page) {
        HttpObservable.getObservable(apiRetrofit.getCoolectList(page))
                .subscribe(new HttpResultObserver<ResponseCustom<CollectListInfos>>() {
                    @Override
                    protected void onLoading(Disposable d) {

                    }

                    @Override
                    protected void onSuccess(ResponseCustom<CollectListInfos> o) {
                        EventBus.getDefault().post(new CollectEvent(Constans.COLLECTLIST, o.getData()));
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        EventBus.getDefault().post(new CollectEvent(Constans.COLLECTLISTERROR, e.getMsg()));
                    }

                });
    }

    @Override
    public void deleteCollect(int page) {
        HttpObservable.getObservable(apiRetrofit.deleteCoolect(page))
                .subscribe(new HttpResultObserver<ResponseCustom<Object>>() {
                    @Override
                    protected void onLoading(Disposable d) {

                    }

                    @Override
                    protected void onSuccess(ResponseCustom<Object> o) {
                        EventBus.getDefault().post(new CollectEvent(Constans.DELETECOLLECT, o.getData()));
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        EventBus.getDefault().post(new CollectEvent(Constans.DELETECOLLECTERROR, e.getMsg()));
                    }

                });
    }
}
*/