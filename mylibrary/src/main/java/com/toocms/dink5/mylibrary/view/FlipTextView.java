package com.toocms.dink5.mylibrary.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.toocms.dink5.mylibrary.R;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.utils.AutoLayoutHelper;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/9.
 */
public class FlipTextView extends TextSwitcher implements TextSwitcher.ViewFactory {

    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    private ArrayList<Map<String, String>> demoBeans;
    private int mIndex;
    // private ItemDataListener itemDataListener;
    // private View view;
    // click view
    private static final int AUTO_RUN_FLIP_TEXT = 11;
    private static final int WAIT_TIME = 3500;
    private Context context;
    private TextView tv_time;

    public FlipTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!this.isInEditMode()) {
            this.mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public FlipTextView.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlipTextView.LayoutParams(this.getContext(), attrs);
    }

    public static class LayoutParams extends TextSwitcher.LayoutParams implements AutoLayoutHelper.AutoLayoutParams {
        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        public AutoLayoutInfo getAutoLayoutInfo() {
            return this.mAutoLayoutInfo;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    public FlipTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        demoBeans = new ArrayList<>();
        setFactory(this);
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.out));
    }

    @Override
    public View makeView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_fiptextview, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSizeBigger(30));
        return view;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_RUN_FLIP_TEXT:

                    if (demoBeans.size() > 0) {
                        setText(demoBeans.get(mIndex).get("nickname") + "提现" + demoBeans.get(mIndex).get("cash") + "元");
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                        String date = sdf.format(new Date(Long.parseLong(demoBeans.get(mIndex).get("add_time")) * 1000L));
                        tv_time.setText(date);
                    }
                    mIndex++;
                    if (mIndex > demoBeans.size() - 1) {
                        mIndex = 0;
                    }
                    mHandler.sendEmptyMessageDelayed(AUTO_RUN_FLIP_TEXT, WAIT_TIME);
//                    if (view != null) {
//                        view.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (mIndex == 0) {
//                                    itemDataListener.onItemClick(demoBeans.size() - 1);
//                                } else {
//                                    itemDataListener.onItemClick(mIndex - 1);
//                                }
//                            }
//                        });
//                    }
                    break;
            }
        }
    };

//    public void setData(List<String> datas, ItemDataListener listener, View v) {
//        view = v;
//        itemDataListener = listener;
//        if (demoBeans.size() > 0) {
//            demoBeans.clear();
//        }
//        demoBeans.addAll(datas);
//        mIndex = 0;
//        mHandler.removeMessages(AUTO_RUN_FLIP_TEXT);
//        mHandler.sendEmptyMessage(AUTO_RUN_FLIP_TEXT);
//    }

    public void setData(ArrayList<Map<String, String>> datas, TextView tv_time, boolean isFromeStart) {
        this.tv_time = tv_time;
        if (demoBeans.size() > 0) {
            demoBeans.clear();
        }
        demoBeans.addAll(datas);
        if (isFromeStart) {
            mIndex = 0;
            PreferencesUtils.putInt(context, "startIndex", mIndex);
        } else {
            mIndex = PreferencesUtils.getInt(context, "startIndex", 0);
        }
        if (mIndex > demoBeans.size() - 1) {
            mIndex = 0;
        }
//        mHandler.removeMessages(AUTO_RUN_FLIP_TEXT);
//        mHandler.sendEmptyMessage(AUTO_RUN_FLIP_TEXT);
    }

//    public abstract interface ItemDataListener {
//        public void onItemClick(int position);
//    }

    public void stopPlay() {
        PreferencesUtils.putInt(context, "startIndex", mIndex);
        mHandler.removeCallbacksAndMessages(null);

    }

    public void startPlay() {
        mHandler.sendEmptyMessageDelayed(AUTO_RUN_FLIP_TEXT, 0);
    }

    public void startIndexZero() {
        mIndex = 0;
    }
}
