package com.toocms.dink5.mylibrary.view.ptrrefush;

import android.content.Context;
import android.util.AttributeSet;


import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;

/**
 * Created by Administrator on 2016/9/14.
 */
public class SingleLoadMoreListViewContainer extends LoadMoreListViewContainer implements LoadMoreHandler {
    private Context context;
    private LoadMoreListenter loadMoreListenter;

    public SingleLoadMoreListViewContainer(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SingleLoadMoreListViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setAutoLoadMore(true);//设置是否自动加载更多
        LoadMore footerView = new LoadMore(context);
        footerView.setVisibility(GONE);
        setLoadMoreView(footerView);
        setLoadMoreUIHandler(footerView);
    }

    public void setLoadMoreListenter(LoadMoreListenter loadMoreListenter) {
        this.loadMoreListenter = loadMoreListenter;
        setLoadMoreHandler(this);
    }

    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {
        loadMoreListenter.loadMore();
    }

    public interface LoadMoreListenter {
        void loadMore();
    }
}
