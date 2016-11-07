package com.toocms.dink5.mylibrary.view.ptrrefush;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.toocms.dink5.mylibrary.R;
import com.zhy.autolayout.utils.AutoUtils;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by Administrator on 2016/9/14.
 */
public class SinglePtrFrameLayout extends PtrFrameLayout implements PtrHandler {

    private Context context;
    private ListView listView;
    private RefreshListenter refreshListenter;

    public SinglePtrFrameLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SinglePtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SinglePtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }


    private void init() {

        MaterialHeader header = new MaterialHeader(context);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        LayoutParams params2 = new LayoutParams(-1, -2);
        params2.topMargin = AutoUtils.getPercentHeightSize(130);
        header.setLayoutParams(params2);
        header.setPadding(0, AutoUtils.getPercentHeightSize(0), 0, AutoUtils.getPercentHeightSize(0));
        header.setPtrFrameLayout(this);
        setEnabledNextPtrAtOnce(true);
        setLoadingMinTime(1000);
        setDurationToCloseHeader(1500);
        setHeaderView(header);
        addPtrUIHandler(header);
        setDurationToClose(100);
        setPinContent(true);

    }

    public void setRefreshListenter(ListView listView, RefreshListenter refreshListenter) {
        this.listView = listView;
        this.refreshListenter = refreshListenter;
        setPtrHandler(this);
    }

    /**
     * Check can do refresh or not. For example the content is empty or the first child is in view.
     * <p>
     * {@link PtrDefaultHandler#checkContentCanBePulledDown}
     *
     * @param frame
     * @param content
     * @param header
     */
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
    }

    /**
     * When refresh begin
     *
     * @param frame
     */
    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        refreshListenter.startRresh();
    }

    public interface RefreshListenter {
        void startRresh();
    }

}
