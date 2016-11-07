package com.toocms.dink5.mylibrary.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

/**
 * A checkable button which has two states (selected and unselected)
 *
 * @author liangfeizc
 */
public class CheckableButton extends TextView implements Checkable {
    private boolean mChecked;
    private static final int[] CHECKED_STATE_LIST = new int[]{android.R.attr.state_checked};

    private boolean mBroadcasting;
    private int index;

    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public CheckableButton(Context context, int index) {
        super(context);
        this.index = index;
    }

    public CheckableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_LIST);
        }
        return drawableState;
    }


    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it.
     */
    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (checked) {

            }
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked, index);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked, index);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    public int Index() {
        return index;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public boolean performClick() {
        toggle();
        mOnCheckedChangeWidgetListener.onClickListen(isChecked(), index);
        return super.performClick();

    }

    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a checkable button changed.
     */
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a checkable button has changed.
         *
         * @param buttonView The checkable button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(CheckableButton buttonView, boolean isChecked, int index);

        void onClickListen(boolean isChecked, int index);
    }


}
