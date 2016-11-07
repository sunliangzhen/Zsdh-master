package com.toocms.dink5.mylibrary.view.ptrrefush;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.toocms.dink5.mylibrary.R;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreUIHandler;

/**
 * Created by slz on 2016/7/29.
 */

public class LoadMore extends RelativeLayout implements LoadMoreUIHandler {

    private TextView mTextView;

    public LoadMore(Context context) {
        this(context, null);
    }

    public LoadMore(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMore(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.more_ite, this);
        mTextView = (TextView) findViewById(R.id.more_tv);
    }

    @Override
    public void onLoading(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mTextView.setText("正在加载...");
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        if (!hasMore) {
            setVisibility(VISIBLE);
            if (empty) {
                mTextView.setText("没更多内容了");
            }
        } else {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mTextView.setText("等待加载");
    }

    @Override
    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        mTextView.setText("加载出现错误");
    }
}
