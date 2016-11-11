package com.shuxiangbaima.task.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jaeger.library.StatusBarUtil;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.Profile;
import com.shuxiangbaima.task.ui.MainAty;
import com.shuxiangbaima.task.ui.mine.below.AboutAty;
import com.shuxiangbaima.task.ui.mine.below.BusAty;
import com.shuxiangbaima.task.ui.mine.below.IdeaAty;
import com.shuxiangbaima.task.ui.mine.below.IncomstateAty;
import com.shuxiangbaima.task.ui.mine.below.RepassAty;
import com.shuxiangbaima.task.ui.mine.below.applay.ApplayMangerAty;
import com.shuxiangbaima.task.ui.mine.mynews.SetnewsAty;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.base.BaseFragment;
import com.toocms.dink5.mylibrary.baserx.RxBus;
import com.toocms.dink5.mylibrary.commonutils.StatusBarUtil2;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.toocms.dink5.mylibrary.net.ApiListener;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/8/8.
 */
public class MineFrg extends BaseFragment implements LoadingTip.onReloadListener {

    @ViewInject(R.id.mine_tv_nickname)
    private TextView tv_nickname;
    @ViewInject(R.id.mine_imgv_head)
    private CircleImageView imgv_head;
    @ViewInject(R.id.mine_top)
    private TextView mine_top;

    private Profile profile;
    private ImageOptions imageOptions;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;

    @Override
    protected int getLayoutResource() {
        return R.layout.frg_mine;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initView() {
        profile = new Profile();
        imageOptions = new ImageOptions.Builder().setCircular(true).setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Config.isLogin()) {
//            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
            profile.profile(getActivity(), this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) return;
        MainAty activity;
        switch (requestCode) {
            case AppConfig.ATY_PAI:
                activity = (MainAty) getActivity();
                activity.permo(2);
                break;
            case AppConfig.OUT_LOGIN:
                activity = (MainAty) getActivity();
                activity.permo(0);
                RxBus.getInstance().post("100", true);
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadedTip.setOnReloadListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarHeight = StatusBarUtil2.getStatusBarHeight(getActivity());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mine_top.getLayoutParams();
            layoutParams.topMargin = statusBarHeight;
            mine_top.setLayoutParams(layoutParams);
        }
    }

    @Event(value = {R.id.mine_relay_mine, R.id.mine_linlay_yq, R.id.mine_linlay_mon, R.id.mine_linlay_pai, R.id.mine_relay_incomestate, R.id.mine_relay_idea
            , R.id.mine_relay_bincomestate, R.id.mine_relay_repass, R.id.mine_relay_bus, R.id.mine_relay_applaymanger, R.id.mine_relay_about})
    private void onTestBaidulClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mine_relay_mine:
                intent = new Intent(getActivity(), SetnewsAty.class);
                startActivityForResult(intent, AppConfig.OUT_LOGIN);
                break;
            case R.id.mine_linlay_yq:
                startActivity(YqAty.class, null);
                break;
            case R.id.mine_linlay_mon:
                startActivity(MonAty.class, null);
                break;
            case R.id.mine_linlay_pai:
                intent = new Intent(getActivity(), PaiAty.class);
                startActivityForResult(intent, AppConfig.ATY_PAI);
                break;
            case R.id.mine_relay_incomestate:
                startActivity(IncomstateAty.class, null);
                break;
            case R.id.mine_relay_bincomestate:
                startActivity(MonAty.class, null);
                break;
            case R.id.mine_relay_idea:
                startActivity(IdeaAty.class, null);
                break;
            case R.id.mine_relay_repass:
                startActivity(RepassAty.class, null);
                break;
            case R.id.mine_relay_bus:
                startActivity(BusAty.class, null);
                break;
            case R.id.mine_relay_applaymanger:
                startActivity(ApplayMangerAty.class, null);
                break;
            case R.id.mine_relay_about:
                startActivity(AboutAty.class, null);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (var1.getUri().contains("profile") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            Map<String, String> map1 = JSONUtils.parseDataToMap(var2);
            tv_nickname.setText("昵称：" + map1.get("nickname"));
            saveUserNews(var2);
            SimpleTarget target = new SimpleTarget<Bitmap>(100, 100) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imgv_head.setImageBitmap(resource);
                }
            };
            if (!TextUtils.isEmpty(map1.get("avatar"))) {
                Glide.with(getActivity())
                        .load(map1.get("avatar"))
                        .placeholder(R.drawable.default_head)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                imgv_head.setImageDrawable(resource);
                            }
                        });
//                Glide.with(getActivity()).load(R.drawable.default_head).into(imgv_head);
            }
        }
    }


    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        showNetError();
    }

    private void saveUserNews(String var2) {
        Map<String, String> map1 = JSONUtils.parseDataToMap(var2);
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray hobbies = new JSONArray(map1.get("hobbies"));
            for (int i = 0; i < hobbies.length(); i++) {
                list.add(hobbies.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                stringBuffer.append(list.get(i));
            } else {
                stringBuffer.append("," + list.get(i));
            }
        }
//        map1.replace("hobbies", stringBuffer.toString());
        application.setUserInfo(map1);
        application.setUserInfoItem("hobbies", stringBuffer.toString());
    }

    @Override
    public void reload() {
        if (Config.isLogin()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
            profile.profile(getActivity(), this);
        }
    }
}
