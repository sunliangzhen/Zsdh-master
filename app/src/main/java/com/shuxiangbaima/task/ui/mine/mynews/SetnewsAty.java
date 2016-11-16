package com.shuxiangbaima.task.ui.mine.mynews;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Profile;
import com.shuxiangbaima.task.interfaces.User;
import com.shuxiangbaima.task.view.GlideCircleTransform;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Administrator on 2016/8/9.
 */
public class SetnewsAty extends BasAty {

    @ViewInject(R.id.setnews_imgv_head)
    private ImageView imgv_head;
    @ViewInject(R.id.setnews_tv_name)
    private TextView tv_name;
    @ViewInject(R.id.setnews_tv_gender)
    private TextView tv_gender;
    @ViewInject(R.id.setnews_tv_phone)
    private TextView tv_phone;

    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;
    private User user;
    private Profile profile;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_setnews;
    }

    @Override
    public void requestData() {
    }

    @Override
    protected void initialized() {
        profile = new Profile();
        user = new User();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_name.setText(application.getUserInfo().get("nickname"));
        if (!application.getUserInfo().get("gender").equals("0")) {
            if (application.getUserInfo().get("gender").equals("1")) {
                tv_gender.setText("先生");
            } else {
                tv_gender.setText("小姐");
            }
        }
        tv_phone.setText(application.getUserInfo().get("username"));
//        Glide.with(this).load(R.drawable.default_head)
//                .transform(new GlideCircleTransform(this))
//                .into(imgv_head);
        Glide.with(this)
                .load(application.getUserInfo().get("avatar"))
                .bitmapTransform(new CropCircleTransformation(this))
                .into(imgv_head);

    }

    @Event(value = {R.id.mynews_relay_head, R.id.setnews_relay_name, R.id.setnews_relay_sex, R.id.mine_imgv_back, R.id.setnews_relay_morenews, R.id.setnews_tv_logout})
    private void onTestBaidulClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.mynews_relay_head:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
//                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
//                            "Storage read permission is needed to pick files",
//                            101);
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                } else {
                    pickFromGallery();
                }
                break;
            case R.id.mine_imgv_back:
                finish();
                break;
            case R.id.setnews_relay_name:
                bundle = new Bundle();
                bundle.putString("type", "rename");
                startActivity(RenewsAty.class, bundle);
                break;
            case R.id.setnews_relay_sex:
                bundle = new Bundle();
                bundle.putString("type", "resex");
                startActivity(RenewsAty.class, bundle);
                break;
            case R.id.setnews_relay_morenews:
                startActivity(Morenews.class, null);
                break;
            case R.id.setnews_tv_logout:
                showBuilder(2);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickFromGallery();
            } else {

            }
        }
    }

    public void showBuilder(final int index) {
        View view = View.inflate(SetnewsAty.this, R.layout.dlg_exit, null);
        TextView tv_content = (TextView) view.findViewById(R.id.buildeexti_tv_content);
        TextView tv_no = (TextView) view.findViewById(R.id.buildeexti_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderexit_tv_ok);
        final Dialog dialog = new Dialog(SetnewsAty.this, R.style.dialog);
        if (index == 1) {
        } else if (index == 2) {
            tv_content.setText("你确定要退出账号吗？");
        } else if (index == 3) {
            tv_content.setText("你确定要清除缓存吗？");
        }
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (index == 1) {
                } else if (index == 2) {
                    showProgressContent();
                    user.logout(SetnewsAty.this, SetnewsAty.this);
                }

            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }


    private AlertDialog mAlertDialog;

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog("权限需要", rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SetnewsAty.this,
                                    new String[]{permission}, requestCode);
                        }
                    }, "OK", null, "cancel");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

    private void pickFromGallery() {
        mSelectPath = new ArrayList<>();
        MultiImageSelector selector = MultiImageSelector.create();
        selector.showCamera(true);
        selector.count(9);
        selector.single();
        selector.origin(mSelectPath);
        selector.start(this, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                showProgressContent();
                profile.modify_avatar(mSelectPath.get(0), this, this);
            }
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
        if (var1.getUri().contains("logout")) {
            if (map.get("status").equals("200")) {
                Config.setLoginState(false);
                SetnewsAty.this.setResult(-1);
                PreferencesUtils.remove(SetnewsAty.this, "isBind");
                finish();
            }
        }
        if (var1.getUri().contains("modify_avatar")) {
            if (map.get("status").equals("200")) {
                String avatar = JSONUtils.parseDataToMap(var2).get("avatar");
                application.setUserInfoItem(avatar, avatar);
                Glide.with(this)
                        .load(avatar)
                        .transform(new GlideCircleTransform(this))
                        .into(imgv_head);
            }
        }
    }
}
