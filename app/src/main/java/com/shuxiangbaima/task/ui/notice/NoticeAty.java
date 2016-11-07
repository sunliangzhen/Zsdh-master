package com.shuxiangbaima.task.ui.notice;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Notice;
import com.toocms.dink5.mylibrary.base.BaseActivity;

import org.xutils.DbManager;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */
public class NoticeAty extends BaseActivity {

    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;
    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;
    private DbManager db;
    private CustomAdapter customAdapter;
    private ArrayList<Fragment> list;
    private ArrayList<Map<String, String>> notice_list;
    private Notice notice;

    @Override
    public int getLayoutId() {
        return R.layout.aty_notice;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        customAdapter = new CustomAdapter(getSupportFragmentManager());
        list = new ArrayList<>();
        notice_list = new ArrayList<>();
        list.add(new NoticeFragment1().newInstance("1"));
        list.add(new NoticeFragment1().newInstance("2"));
        list.add(new NoticeFragment1().newInstance("3"));
        notice = new Notice();
//        db = x.getDb(((Myappcation) getApplicationContext()).getDaoConfig());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager.setAdapter(customAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Event(value = {R.id.notice_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.notice_imgv_back:
                finish();
                break;
        }
    }

//    @Override
//    public void onComplete(RequestParams var1, String var2) {
//        super.onComplete(var1, var2);
//        if (var1.getUri().contains("notice_list") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
//            Map<String, String> map = JSONUtils.parseDataToMap(var2);
//            if (!map.get("count").equals("0")) {
//                Notice2 notice2;
//                notice_list = JSONUtils.parseKeyAndValueToMapList(map.get("notice_list"));
//                for (int i = 0; i < notice_list.size(); i++) {
//                    notice2 = new Notice2();
//                    notice2.setNotice_id(notice_list.get(i).get("notice_id"));
//                    notice2.setNotice_type(notice_list.get(i).get("notice_type"));
//                    notice2.setSend_time(notice_list.get(i).get("send_time"));
//                    notice2.setTitle(notice_list.get(i).get("title"));
//                    notice2.setLink(notice_list.get(i).get("link"));
//                    notice2.setIsRead("false");
//                    try {
//                        db.save(notice2);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                }
//                viewPager.setAdapter(customAdapter);
//            } else {
//                viewPager.setAdapter(customAdapter);
//            }
//        }
//    }

//    private void seletorDateBase() {
//        String isRead = "true";
//        List<Notice2> list_notice2 = null;
//        try {
//            list_notice2 = db.selector(Notice2.class).orderBy("id", true).findAll();
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        if (list_notice2 != null) {
//            for (int i = 0; i < list_notice2.size(); i++) {
//                if (list_notice2.get(i).getIsRead().equals("false")) {
//                    isRead = "false";
//                }
//            }
//            if (isRead.equals("true")) {
//                notice.notice_list(this, this);
//            } else {
//                viewPager.setAdapter(customAdapter);
//            }
//        } else {
//            notice.notice_list(this, this);
//        }
//    }

    private class CustomAdapter extends FragmentPagerAdapter {
        private String fragments[] = {"公告", "通知", "回复"};

        public CustomAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }

}
