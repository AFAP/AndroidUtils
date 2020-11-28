package com.afap.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.afap.utils.R;


public class QuickIndexBar extends LinearLayout {

    private int padding = getResources().getDimensionPixelSize(R.dimen.af_quick_index_bar_padding);
    private String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };
    private int mTouchIndex = -1;//用于记录当前触摸的索引值

    //暴露一个字母的监听
    public interface OnLetterUpdateListener {
        void onLetterUpdate(String letter);

        void onLetterCancel();
    }

    private OnLetterUpdateListener mListener;

    public OnLetterUpdateListener getOnLetterUpdateListener() {
        return mListener;
    }

    public void setOnLetterUpdateListener(OnLetterUpdateListener listener) {
        mListener = listener;
    }

    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public QuickIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        setPadding(padding, padding, padding, padding);
        refreshLetters();
    }

    public void setLetters(String[] arr) {
        LETTERS = arr;
        refreshLetters();
    }

    void refreshLetters() {
        removeAllViews();
        for (int i = 0; i < LETTERS.length; i++) {
            TextView tv = (TextView) View.inflate(getContext(), R.layout.af_quick_index_bar_text, null);
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
            tv.setText(LETTERS[i]);
            tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
            addView(tv);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                index = (int) (event.getY() * LETTERS.length / getMeasuredHeight());
                if (index >= 0 && index < LETTERS.length) {
                    if (index != mTouchIndex) {
                        if (mListener != null) {
                            mListener.onLetterUpdate(LETTERS[index]);
                        }
                        if (mTouchIndex != -1) {
                            getChildAt(mTouchIndex).setSelected(false);
                        }
                        mTouchIndex = index;
                        getChildAt(index).setSelected(true);
                    } else {
                    }
                }
                setBackgroundColor(getResources().getColor(R.color.af_quick_index_bar_bg_pressed));
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchIndex != -1) {
                    getChildAt(mTouchIndex).setSelected(false);
                }
                mTouchIndex = -1;
                if (mListener != null) {
                    mListener.onLetterCancel();
                }
                setBackgroundColor(getResources().getColor(R.color.af_quick_index_bar_bg));
                break;
            default:
                break;
        }
        return true;
    }
}
