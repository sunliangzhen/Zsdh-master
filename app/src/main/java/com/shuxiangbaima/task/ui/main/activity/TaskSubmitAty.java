package com.shuxiangbaima.task.ui.main.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linearlistview.LinearListView;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.interfaces.Task;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.ImageUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;


/**
 * Created by Administrator on 2016/9/13.
 */
public class TaskSubmitAty extends BaseAty {


    @ViewInject(R.id.submit_list)
    private LinearListView list_sub;
    @ViewInject(R.id.submit_gv)
    private GridView gridView;

    private String data;
    private Myadapter myadapter;
    private Myadapter2 myadapter2;
    private ArrayList<Map<String, String>> list;
    private ArrayList<String> list_image;
    private Task task;
    private AlertDialog mAlertDialog;
    private static final int REQUEST_IMAGE = 2;
    private ArrayList<String> mSelectPath;
    private int k = 0;
    private Map<String, String> map;
    private String screenshot;
    private String task_id;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_task_submit;
    }

    @Override
    protected void initialized() {
        task_id = getIntent().getStringExtra("task_id");
        map = new HashMap<>();
        list_image = new ArrayList<>();
        list_image.add("ss");
        task = new Task();
        myadapter = new Myadapter();
        myadapter2 = new Myadapter2();
        data = getIntent().getStringExtra("data");
    }

    @Override
    public void requestData() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = JSONUtils.parseKeyAndValueToMapList(JSONUtils.parseDataToMap(data).get("fields"));
        list_sub.setAdapter(myadapter);
        gridView.setAdapter(myadapter2);
        screenshot = JSONUtils.parseDataToMap(data).get("screenshot");
        if (screenshot.equals("true")) {
            gridView.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.GONE);
        }
    }

    @Override
    public void initPresenter() {
    }

    @Event(value = {R.id.submit_back, R.id.sub_btn_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.submit_back:
                finish();
                break;
            case R.id.sub_btn_ok:

                for (int i = 0; i < list.size(); i++) {
                    LinearLayout childAt = (LinearLayout) list_sub.getChildAt(i);
                    EditText childAt1 = (EditText) childAt.getChildAt(0);
                    if (TextUtils.isEmpty(childAt1.getText().toString())) {
                        showToast(list.get(i).get("placeholder"));
                        return;
                    } else {
                        map.put(list.get(i).get("name"), childAt1.getText().toString());
                    }
                }
                if (screenshot.equals("true") && list_image.size() < 2) {
                    showToast("请上传图片");
                    return;
                } else {
                    if (k == 1) {
                        map.put("sr_num", list_image.size() + "");
                        for (int i = 0; i < list_image.size(); i++) {
                            map.put("sr_" + i, list_image.get(i));
                        }
                    } else {
                        for (int i = 0; i < list_image.size() - 1; i++) {
                            map.put("sr_" + i, list_image.get(i));
                        }
                        map.put("sr_num", list_image.size() - 1 + "");
                    }
                }
                showProgressContent();
                task.submit_task(map, task_id, this, this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        if (var1.getUri().contains("submit_task") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                LogUtil.e(mSelectPath.toString());
                list_image.remove(list_image.size() - 1);
                list_image.add(mSelectPath.get(0));
                list_image.add("ss");
                if (list_image.size() > 3) {
                    k = 1;
                    list_image.remove(list_image.size() - 1);
                }
                myadapter2.notifyDataSetChanged();
            }
        }
    }


    @Event(value = R.id.submit_gv, type = AdapterView.OnItemClickListener.class)
    private void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == list_image.size() - 1 && k == 0) {
            upImg();
        }
    }

    private void upImg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    "Storage read permission is needed to pick files",
                    101);

        } else {
            pickFromGallery();
        }
    }

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog("权限需要", rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(TaskSubmitAty.this,
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

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(TaskSubmitAty.this).inflate(R.layout.item_submit_task, viewGroup, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.etxt.setHint(list.get(i).get("placeholder"));
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_sub_etxt)
            public EditText etxt;
            @ViewInject(R.id.item_sub_v)
            public View v;
        }
    }

    private class Myadapter2 extends BaseAdapter {

        @Override
        public int getCount() {
            return list_image.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(TaskSubmitAty.this).inflate(R.layout.item_sub_gv, viewGroup, false);
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
            if (k == 0) {
                if (i == (list_image.size() - 1)) {
                    viewHolder.imgv.setImageResource(R.drawable.ic_photo);
                    viewHolder.imgv_cancel.setVisibility(View.GONE);
                } else {
                    viewHolder.imgv_cancel.setVisibility(View.VISIBLE);
                    ImageUtils.display(TaskSubmitAty.this, list_image.get(i), viewHolder.imgv);
                }

            } else {
                viewHolder.imgv_cancel.setVisibility(View.VISIBLE);
                ImageUtils.display(TaskSubmitAty.this, list_image.get(i), viewHolder.imgv);

            }
            viewHolder.imgv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (k == 1) {
                        list_image.add("s");
                    }
                    list_image.remove(i);
                    k = 0;
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_sub_imgv)
            public ImageView imgv;
            @ViewInject(R.id.item_sub_imgv_cancel)
            public ImageView imgv_cancel;
        }
    }

}

