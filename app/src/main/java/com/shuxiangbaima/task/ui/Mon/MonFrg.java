package com.shuxiangbaima.task.ui.Mon;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BasFragment;
import com.shuxiangbaima.task.interfaces.Profit;
import com.shuxiangbaima.task.ui.mine.PaiAty;
import com.shuxiangbaima.task.ui.mine.SunpaiAty;
import com.shuxiangbaima.task.ui.mine.YqAty;
import com.toocms.dink5.mylibrary.app.AppConstant;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.commonutils.StatusBarUtil2;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.toocms.dink5.mylibrary.view.CycleView;
import com.toocms.dink5.mylibrary.view.LinerView;
import com.toocms.dink5.mylibrary.view.MySwiperefreshlayout;
import com.toocms.dink5.mylibrary.view.TopScrollView;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/8.
 */
public class MonFrg extends BasFragment implements LoadingTip.onReloadListener {


    @ViewInject(R.id.ll_point_group1)
    private LinearLayout lin_group;
    @ViewInject(R.id.mon_vp1)
    private ViewPager mon_vp;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;
    @ViewInject(R.id.mon_top)
    private RelativeLayout top;
    @ViewInject(R.id.swipeLayout)
    private MySwiperefreshlayout swipeLayout;
    @ViewInject(R.id.scrollview)
    private TopScrollView scrollview;
    @ViewInject(R.id.line_chart)
    private LinerView lineChart;
    @ViewInject(R.id.money_total)
    private TextView tv_money_total;
    @ViewInject(R.id.mon_tv_applay)
    private TextView mon_tv_applay;

    private CycleView v1_cp, v11_cp;
    private CycleView v2_cp, v22_cp;
    private TextView tv_today_profit, tv_today_profit2;                         //今日收益
    private TextView tv_remainder, tv_remainder_t;                            //账户余额
    private TextView tv_assist_profit, tv_assist_profit2;                        //帮赚收益
    private TextView tv_remainder2, tv_remainder22;                           //账户余额
    private TextView tv_pai;
    private TextView tv_pai2;

    private ArrayList<View> list;
    private int previousSelectedPosition = 0;
    private View v_1, v_11;
    private View v_2, v_22;
    private ArrayList<Integer> localImages_list;
    private MyAdapter2 myAdapter2;
    private Profit profit;
    private ArrayList<Map<String, String>> recent_profit;
    private ArrayList<String> date_list;
    private ArrayList<Float> data = new ArrayList<>();


    @Override
    protected int getLayoutResource() {
        return R.layout.frg_mon2;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initView() {
        localImages_list = new ArrayList<>();
        localImages_list.add(1);
        localImages_list.add(1);
        myAdapter2 = new MyAdapter2();
        profit = new Profit();
        recent_profit = new ArrayList<>();
        date_list = new ArrayList<>();
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Config.isLogin()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
            profit.recent_profit(getActivity(), this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeLayout.setScrollview(scrollview);
        v_1 = getLayoutInflater(savedInstanceState).inflate(R.layout.frg_mondetails, null);
        v_11 = getLayoutInflater(savedInstanceState).inflate(R.layout.frg_mondetails1, null);
        tv_today_profit = (TextView) v_1.findViewById(R.id.mon_tv_today_profit);
        tv_today_profit2 = (TextView) v_11.findViewById(R.id.mon_tv_today_profit);
        tv_remainder = (TextView) v_1.findViewById(R.id.mon_tv_remainder);
        tv_remainder_t = (TextView) v_11.findViewById(R.id.mon_tv_remainder);
        TextView tv_yq = (TextView) v_1.findViewById(R.id.mondetails_tv_yq);
        v1_cp = (CycleView) v_1.findViewById(R.id.arc);
        v11_cp = (CycleView) v_11.findViewById(R.id.arc11);
        tv_pai = (TextView) v_1.findViewById(R.id.mondetails_tv_pai);
        TextView tv_paii = (TextView) v_11.findViewById(R.id.mondetails_tv_pai);
        tv_pai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PaiAty.class, null);
            }
        });
        tv_paii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PaiAty.class, null);
            }
        });
        tv_yq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(YqAty.class, null);
            }
        });

        v_2 = getLayoutInflater(savedInstanceState).inflate(R.layout.frg_mondetails2, null);
        v_22 = getLayoutInflater(savedInstanceState).inflate(R.layout.frg_mondetails22, null);
        TextView tv_yq2 = (TextView) v_2.findViewById(R.id.mondetails_tv_yq);
        v2_cp = (CycleView) v_2.findViewById(R.id.arc2);
        v22_cp = (CycleView) v_22.findViewById(R.id.arc22);
        tv_assist_profit = (TextView) v_2.findViewById(R.id.mon2_tv_assist_profit);
        tv_assist_profit2 = (TextView) v_22.findViewById(R.id.mon2_tv_assist_profit);
        tv_remainder2 = (TextView) v_2.findViewById(R.id.mon2_tv_remainder);
        tv_remainder22 = (TextView) v_22.findViewById(R.id.mon2_tv_remainder);
        tv_pai2 = (TextView) v_2.findViewById(R.id.mondetails_tv_pai);
        TextView tv_pai22 = (TextView) v_22.findViewById(R.id.mondetails_tv_pai);
        tv_pai2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PaiAty.class, null);
            }
        });
        tv_pai22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PaiAty.class, null);
            }
        });
        tv_yq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(YqAty.class, null);
            }
        });
        initview();
        initAdapter();
        loadedTip.setOnReloadListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarHeight = StatusBarUtil2.getStatusBarHeight(getActivity());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) top.getLayoutParams();
            layoutParams.topMargin = statusBarHeight;
            top.setLayoutParams(layoutParams);
        }
        swipeLayout.setProgressViewEndTarget(true, AutoUtils.getPercentHeightSize(300));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Config.isLogin()) {
                    profit.recent_profit(getActivity(), MonFrg.this);
                }
            }
        });
        scrollview.setTopScrollViewListen(new TopScrollView.ScrollowListen() {
            @Override
            public void top() {
                swipeLayout.setEnabled(true);
            }

            @Override
            public void center() {
                swipeLayout.setEnabled(false);
            }
        });
        swipeLayout.setColorSchemeResources(R.color.red, R.color.blue, R.color.yellow, R.color.green);
    }


    @Event(value = {R.id.mon_tv_applay})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.mon_tv_applay:
                startActivity(ApplayAty.class, null);
                break;
        }
    }

    @Override
    public void onCancelled(Callback.CancelledException var1) {
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        swipeLayout.setRefreshing(false);
        super.onComplete(var1, var2);
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
        if (var1.getUri().contains("recent_profit")) {
            if (map.get("status").equals("200")) {
                Map<String, String> data_map = JSONUtils.parseDataToMap(var2);
                tv_money_total.setText(data_map.get("remainder") + "元");
                tv_remainder.setText("余额：" + data_map.get("remainder") + "元");
                tv_remainder_t.setText("余额：" + data_map.get("remainder") + "元");
                tv_remainder2.setText("余额：" + data_map.get("remainder") + "元");
                tv_remainder22.setText("余额：" + data_map.get("remainder") + "元");
                tv_today_profit.setText("￥" + data_map.get("today_profit"));
                tv_today_profit2.setText("￥" + data_map.get("today_profit"));
                tv_assist_profit.setText("￥" + data_map.get("assist_profit"));
                tv_assist_profit2.setText("￥" + data_map.get("assist_profit"));
                recent_profit = JSONUtils.parseKeyAndValueToMapList(data_map.get("recent_profit"));
                date_list.clear();
                data.clear();
                for (int i = 0; i < recent_profit.size(); i++) {
                    String earn_date = recent_profit.get(i).get("earn_date");
                    earn_date = earn_date.substring(earn_date.length() - 2, earn_date.length());
                    date_list.add(earn_date + "日");
                    data.add(Float.valueOf(recent_profit.get(i).get("profit")));
                }
                lineChart.setData(data, date_list);
            }
        }
    }

    @Override
    protected void loginSuccess() {
        if (Config.isLogin()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
            profit.recent_profit(getActivity(), this);
        }
    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        swipeLayout.setRefreshing(false);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        showNetError();
    }

    private void initview() {
        list = new ArrayList<>();
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < 2; i++) {
            pointView = new View(getActivity());
            pointView.setBackgroundResource(R.drawable.selector_page_point);
            layoutParams = new LinearLayout.LayoutParams(10, 10);
            if (i != 0)
                layoutParams.leftMargin = 10;
            pointView.setEnabled(false);
            lin_group.addView(pointView, layoutParams);
        }
        list.add(v_22);
        list.add(v_1);
        list.add(v_2);
        list.add(v_11);
    }

    private void initAdapter() {
        lin_group.getChildAt(0).setEnabled(true);
        lin_group.getChildAt(1).setEnabled(false);
        previousSelectedPosition = 0;
        mon_vp.setAdapter(myAdapter2);
        mon_vp.addOnPageChangeListener(new PageListener());
        mon_vp.setOffscreenPageLimit(4);
        mon_vp.setCurrentItem(1);
    }

    private void getData() {
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String day = df.format(new Date());
        int day_today = Integer.parseInt(day);
        for (int i = 0; i < 7; i++) {
            if (day_today < 10) {
                date_list.add("0" + day_today);
            } else {
                date_list.add(day_today + "");
            }
            day_today++;
        }
    }


    @Override
    public void reload() {
        if (Config.isLogin()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
            profit.recent_profit(getActivity(), this);
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mon_vp.setCurrentItem(list.size() - 2, false);
                    }
                }, 500);
            } else if (position == list.size() - 1) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mon_vp.setCurrentItem(1, false);
                    }
                }, 500);
            }
            if (mon_vp.getCurrentItem() == 1) {
                lin_group.getChildAt(0).setEnabled(true);
                lin_group.getChildAt(1).setEnabled(false);
            } else if (mon_vp.getCurrentItem() == 2) {
                lin_group.getChildAt(0).setEnabled(false);
                lin_group.getChildAt(1).setEnabled(true);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private class MyAdapter2 extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = list.get(position % list.size());
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
