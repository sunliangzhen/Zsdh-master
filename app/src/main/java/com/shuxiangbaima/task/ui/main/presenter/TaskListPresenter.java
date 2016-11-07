package com.shuxiangbaima.task.ui.main.presenter;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.ui.main.bean.TaskBean;
import com.shuxiangbaima.task.ui.main.contract.TaskListContract;
import com.toocms.dink5.mylibrary.baserx.RxSubscriber;

import java.util.Map;


public class TaskListPresenter extends TaskListContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
        //监听返回顶部动作
//        mRxManage.on(AppConstant.NEWS_LIST_TO_TOP, new Action1<Object>() {
//            @Override
//            public void call(Object o) {
////            mView.scrolltoTop();
//            }
//        });
    }

    /**
     * 请求获取列表数据
     */
    @Override
    public void getTaskListDataRequest(Map<String, String> fields) {
        mRxManage.add(mModel.getTaskListData(fields).subscribe(new RxSubscriber<TaskBean>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                if (mView.getDataCount() == 0) {
                    mView.showLoading(mContext.getString(R.string.loading));
                }
            }

            @Override
            protected void _onNext(TaskBean newsSummaries) {
                mView.stopLoading();
                mView.returnTaskListData(newsSummaries);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
