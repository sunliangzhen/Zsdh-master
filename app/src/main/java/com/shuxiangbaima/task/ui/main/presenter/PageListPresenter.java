package com.shuxiangbaima.task.ui.main.presenter;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.ui.main.bean.PageBean;
import com.shuxiangbaima.task.ui.main.contract.PageListContract;
import com.toocms.dink5.mylibrary.baserx.RxSubscriber;
import com.toocms.dink5.mylibrary.viewpager.BannerBean;

import java.util.Map;


public class PageListPresenter extends PageListContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void getPageListDataRequest(Map<String, String> fields) {
        mRxManage.add(mModel.getTaskListData(fields).subscribe(new RxSubscriber<PageBean>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                if(mView.getDataCount()==0){
                    mView.showLoading(mContext.getString(R.string.loading));
                }
            }

            @Override
            protected void _onNext(PageBean newsSummaries) {
                mView.returnTaskListData(newsSummaries);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    @Override
    public void getBannerListDataRequest(Map<String, String> fields) {
        mRxManage.add(mModel.getBannerListData(fields).subscribe(new RxSubscriber<BannerBean>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(BannerBean newsSummaries) {
                mView.returnBannerListData(newsSummaries);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
