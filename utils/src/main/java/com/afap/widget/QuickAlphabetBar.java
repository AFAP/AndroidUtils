package com.afap.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcelable;
import androidx.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afap.utils.R;

/**
 * 快速字母索引条--废弃！！！
 */
public class QuickAlphabetBar extends View {
    private OnLetterClickListener mOnItemClickListener = null;

    String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Boolean initFlag = false; // 控件是否初始化
    private int mViewHeight, mViewWidth, mSingleHeight, mCharHeight;// 高、宽、每行高度、字符高度
    private int choose = -1; // 当前游标
    private Paint paint = new Paint();
    private boolean showBkg = false;// 是否显示背景，触摸滑动时显示
    private PopupWindow mPopupWindow; // 弹出窗口，显示游标所到处字母
    private TextView mPopupText; // 弹出窗口文字
    private int color_bg = -1, color_text = -1, color_current = -1, color_pop = -1;

    public QuickAlphabetBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuickAlphabetBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置字母集合
     *
     * @param letters 字母集合
     */
    public void setLetters(String[] letters) {
        this.letters = letters;
    }

    /**
     * 设置QuickBar相关颜色
     *
     * @param bg      触摸时背景颜色
     * @param text    文字颜色
     * @param current 当前选中的文字颜色
     * @param pop     pop弹窗文字颜色
     */
    public void setColors(@ColorInt int bg, @ColorInt int text, @ColorInt int current, @ColorInt int pop) {
        this.color_bg = bg;
        this.color_text = text;
        this.color_current = current;
        this.color_pop = pop;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!initFlag) {// 初始化一次尺寸
            mViewHeight = getHeight();
            mViewWidth = getWidth();
            mSingleHeight = mViewHeight / letters.length;
            mCharHeight = (int) (mSingleHeight * 0.7);
            if (mCharHeight > mViewWidth * 0.6) {
                mCharHeight = (int) (mViewWidth * 0.6);
            }
            initFlag = true;

            color_bg = color_bg == -1 ? getResources().getColor(R.color.quick_alphabet_bar_bg) : color_bg;
            color_text = color_text == -1 ? getResources().getColor(R.color.quick_alphabet_bar_text) : color_text;
            color_current = color_current == -1 ? getResources().getColor(R.color.quick_alphabet_bar_current) :
                    color_current;
            color_pop = color_pop == -1 ? getResources().getColor(R.color.quick_alphabet_bar_pop) : color_pop;
        }
        if (showBkg) {// 按条件绘制背景
            canvas.drawColor(color_bg);
        } else {
            canvas.drawColor(Color.TRANSPARENT);
        }
        for (int i = 0; i < letters.length; i++) {
            paint.setColor(color_text);
            paint.setTextSize(mCharHeight);
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(color_current);
            }
            float xPos = mViewWidth / 2 - paint.measureText(letters[i]) / 2;
            float yPos = mSingleHeight * i + mSingleHeight;
            canvas.drawText(letters[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        dismissPopup();
        return super.onSaveInstanceState();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final int c = (int) (y / getHeight() * letters.length);// 计算出当前点击的索引位置

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;// 显示快速查找条背景
                if (oldChoose != c) {
                    if (c >= 0 && c < letters.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c) {
                    if (c >= 0 && c < letters.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;// 隐藏快速查找条背景
                dismissPopup();
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 显示pop提示窗
     *
     * @param item 游标所处位置
     */
    private void showPopup(int item) {
        // 初始化弹出窗口
        if (mPopupWindow == null) {
            mPopupText = new TextView(getContext());
            mPopupText.setBackgroundResource(R.drawable.quickalpbar_pop_bg);
            mPopupText.setTextColor(color_pop);
            mPopupText.setTextSize(50);
            mPopupText.setGravity(Gravity.CENTER);
            mPopupWindow = new PopupWindow(mPopupText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        String text = letters[item];
        mPopupText.setText(text);
        if (mPopupWindow.isShowing()) {
            // mPopupWindow.update();
        } else {
            mPopupWindow.showAtLocation(getRootView(), Gravity.CENTER, 0, 0);// 居中显示
        }
    }

    private void dismissPopup() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public OnLetterClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * 设置字符被点击后的事件
     *
     * @param listener 快速索引字符点击事件
     */
    public void setOnItemClickListener(OnLetterClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 执行字母被点击后动作
     */
    private void performItemClicked(int item) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(letters[item]);
        }
        showPopup(item);
    }

    public interface OnLetterClickListener {
        void onItemClick(String s);
    }

    /**
     * 动态设置游标所处位置
     *
     * @param position 当前索引位置
     */
    public void setPosition(int position) {
        choose = position;
        invalidate();
    }

    /**
     * 根据字符动态设置游标所处位置
     *
     * @param letter 根据索引字符集的字符来设置到指定位置
     */
    public void setPositionByLetter(String letter) {
        for (int i = 0; i < letters.length; i++) {
            if (letters[i].endsWith(letter)) {
                setPosition(i);
                break;
            }
        }
    }
}