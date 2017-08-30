package com.afap.treeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class TreeLine extends View {

    public final static int TYPE_LINE_1 = 1; // |-
    public final static int TYPE_LINE_2 = 2; // |
    public final static int TYPE_LINE_3 = 3;

    private final float DEFAULT_LINE_WIDTH = 5;
    private final int DEFAULT_LINE_COLOR = Color.parseColor("#FFFF00");

    private float lineWidth = DEFAULT_LINE_WIDTH;
    private int lineColor = DEFAULT_LINE_COLOR;


    public TreeLine(Context context) {
        super(context);
    }

    public TreeLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TreeView);
        lineWidth = typedArray.getDimension(R.styleable.TreeView_lineWidth, DEFAULT_LINE_WIDTH);
        lineColor = typedArray.getColor(R.styleable.TreeView_color, DEFAULT_LINE_COLOR);
    }

    public TreeLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        Paint paint = new Paint();
        paint.setColor(lineColor);
//        paint.setAlpha(164);
//        paint.setAntiAlias(true); //去除锯齿
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(lineWidth);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));

        Path path = new Path();
        path.moveTo(w / 2, 0);
        path.lineTo(w / 2, h);
        canvas.drawPath(path, paint);
    }
}
