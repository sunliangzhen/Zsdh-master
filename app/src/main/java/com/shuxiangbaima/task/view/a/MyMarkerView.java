//
//package com.shuxiangbaima.task.view.a;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.github.mikephil.charting.components.MarkerView;
//import com.github.mikephil.charting.data.CandleEntry;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.highlight.Highlight;
//import com.github.mikephil.charting.utils.Utils;
//import com.shuxiangbaima.task.R;
//
//import java.util.ArrayList;
//
///**
// * Custom implementation of the MarkerView.
// *
// * @author Philipp Jahoda
// */
//public class MyMarkerView extends MarkerView {
//
//    private TextView tvContent;
//    private TextView title;
//    private LinearLayout marker_linlay;
//    private ArrayList<String> list;
//
//    public MyMarkerView(Context context, int layoutResource, boolean isEmpty, ArrayList<String> list) {
//        super(context, layoutResource);
//        this.list = list;
//        tvContent = (TextView) findViewById(R.id.tvContent);
//        title = (TextView) findViewById(R.id.title);
//        marker_linlay = (LinearLayout) findViewById(R.id.marker_linlay);
//        if (isEmpty) {
//            marker_linlay.setVisibility(GONE);
//        } else {
//            marker_linlay.setVisibility(VISIBLE);
//        }
//    }
//
//    CandleEntry ce;
//
//    @Override
//    public void refreshContent(Entry e, Highlight highlight) {
//
//        if (e instanceof CandleEntry) {
//
//            ce = (CandleEntry) e;
//
//            tvContent.setText("收益：" + Utils.formatNumber(ce.getHigh(), 0, true));
//        } else {
//            int x = (int) e.getX();
//            float x1 = highlight.getXPx();
////            if (x == list.size() - 1) {
////                marker_linlay.setVisibility(GONE);
////            } else {
////                marker_linlay.setVisibility(VISIBLE);
////            }
//            title.setText(list.get(x) + "日");
//            tvContent.setText("收益:" + Utils.formatNumber(e.getY(), 2, false) + "元");
//        }
//    }
//
//    @Override
//    public int getXOffset(float xpos) {
//
////        int x = (int) getX();
////        String s = list.get(0);
////        int id = getId();
////        float y = getY();
////        int i = Integer.parseInt(s);
////        Log.e("df", y + "x");
////        Log.e("df", xpos + "xpos");
////        Log.e("df", id + "id");
////        Log.e("df", Integer.parseInt(list.get(0)) + "");
//
//        if ((int) xpos < 100) {
//            return 0;
//        } else {
//            return -getWidth();
//        }
//    }
//
//    @Override
//    public int getYOffset(float ypos) {
//        return -getHeight();
//    }
//}
