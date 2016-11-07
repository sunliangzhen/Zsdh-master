package com.toocms.dink5.mylibrary.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.toocms.dink5.mylibrary.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/13.
 */

public class SingleFlowLayout extends FlowLayout implements CheckableButton.OnCheckedChangeListener {

    private ArrayList<Map<String, String>> list;
    private ArrayList<CheckableButton> list_button;
    private Context context;
    private String str;
    private int modle;
    private String[] hobbies;

    public SingleFlowLayout(Context context) {
        super(context);
        this.context = context;
    }

    public SingleFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setData(ArrayList<Map<String, String>> list, int modle) {
        this.list = list;
        this.modle = modle;
        init();
    }

    private void init() {
        list_button = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CheckableButton btn = new CheckableButton(context, i);
            btn.setClickable(true);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSizeBigger(30));
            btn.setTextColor(getResources().getColorStateList(R.color.checkable_text_color));
            btn.setBackgroundResource(R.drawable.checkable_background);
            btn.setText(list.get(i).get("value"));
            btn.setPadding(AutoUtils.getPercentHeightSize(20), AutoUtils.getPercentHeightSize(20),
                    AutoUtils.getPercentHeightSize(20), AutoUtils.getPercentHeightSize(15));
            addView(btn);
            list_button.add(btn);
        }
        for (int i = 0; i < getChildCount(); i++) {
            CheckableButton childAt = (CheckableButton) getChildAt(i);
            childAt.setOnCheckedChangeWidgetListener(this);
        }
    }

    public void setCheckeItem(String str) {
        this.str = str;
        if (modle == 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get("key").equals(str)) {
                    list_button.get(i).setChecked(true);
                } else {
                    list_button.get(i).setChecked(false);
                }
            }
        } else {
            hobbies = str.split(",");
            for (int i = 0; i < list.size(); i++) {
                for (int k = 0; k < hobbies.length; k++) {
                    if (hobbies[k].equals(list.get(i).get("key"))) {
                        list_button.get(i).setChecked(true);
                    }
                }
            }
        }
    }


    @Override
    public void onCheckedChanged(CheckableButton buttonView, boolean isChecked, int index) {
    }

    @Override
    public void onClickListen(boolean isChecked, int index) {
        if (modle == 0) {
            if (isChecked) {
                str = list.get(index).get("key");
                for (int i = 0; i < list_button.size(); i++) {
                    if (i != index) {
                        list_button.get(i).setChecked(false);
                    }
                }
            } else {
                str = "";
            }
        }
    }

    public String getCheckedString() {
        if (modle == 0) {
            return str;
        } else {
            int k = 0;
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < getChildCount(); i++) {
                CheckableButton childAt = (CheckableButton) getChildAt(i);
                if (childAt.isChecked()) {
                    if (k != 0) {
                        buffer.append("," + list.get(childAt.Index()).get("key"));
                    } else {
                        buffer.append(list.get(childAt.Index()).get("key"));
                    }
                    k++;
                }
            }
            return buffer.toString();
        }
    }
}
