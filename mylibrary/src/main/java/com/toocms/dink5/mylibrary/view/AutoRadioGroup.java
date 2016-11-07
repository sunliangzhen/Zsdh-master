package com.toocms.dink5.mylibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.utils.AutoLayoutHelper;

/**
 * @author Zero
 * @date 2016/7/16 10:45
 */
public class AutoRadioGroup extends RadioGroup {
    private AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public AutoRadioGroup(Context context) {
        super(context);
    }

    public AutoRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!this.isInEditMode()) {
            this.mHelper.adjustChildren();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public AutoRadioGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AutoRadioGroup.LayoutParams(this.getContext(), attrs);
    }

    public static class LayoutParams extends RadioGroup.LayoutParams implements AutoLayoutHelper.AutoLayoutParams {
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
}

