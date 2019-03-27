package com.uguke.android.okgo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 加载控件
 * @author LeiJue
 */
public class LoadingView extends View {

    private int[] colors;
    private Paint paint;
    private RectF arcRectF;

    private int viewWidth;
    private int viewHeight;
    private int squareLength;
    /** 叶数 **/
    private int leafCount;
    /** 叶型圆弧角度间隔 **/
    private float leafSpace;
    /** 前进比例 **/
    private float distance;
    /** 圆弧宽度 **/
    private float arcWidth;
    /** 开始画弧线的角度 **/
    private float startAngle;
    /** 需要画弧线的角度 **/
    private float sweepAngle;
    /** 正在增加弧线角度 **/
    private boolean addArcAngle;
    /** 单页最小角度 **/
    private float minAngle;
    /** 单页增量角度 **/
    private float addAngle;
    /** 旋转速度 **/
    private float rotateSpeed;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            invalidate();
            handler.sendEmptyMessageDelayed(0, 10);
            return false;
        }
    });

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        arcRectF = new RectF();
        leafCount = 1;
        leafSpace = 30;
        distance = 0.1f;
        addArcAngle = true;
        minAngle = 30;
        addAngle = 270;
        rotateSpeed = 4;
        colors = new int[] {ContextCompat.getColor(context, R.color.colorAccent)};
        arcWidth = context.getResources().getDisplayMetrics().density * 3;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    public LoadingView setLeafCount(@IntRange(from = 1) int count) {
        leafCount = count > 1 ? count : 1;
        return this;
    }

    public LoadingView setLeafSpace(float space) {
        leafSpace = space;
        return this;
    }

    public LoadingView setColors(@ColorInt int... colors) {
        if (colors.length == 0) {
            this.colors = new int[] {ContextCompat.getColor(getContext(), R.color.colorAccent)};
        } else {
            this.colors = colors;
        }
        return this;
    }

    public LoadingView setArcWidth(float width) {
        arcWidth = getResources().getDisplayMetrics().density * width;
        return this;
    }

    /**
     * 设置最小角度（仅对叶数为1有效）
     * @param min 最小角度
     */
    public LoadingView setMinAngle(float min) {
        minAngle = min;
        return this;
    }

    /**
     * 设置增量角度（仅对叶数为1有效）
     * @param add 增量角度
     */
    public LoadingView setAddAngle(float add) {
        addAngle = add;
        return this;
    }

    /**
     * 每秒转多少度
     * @param speed 每秒转多少度
     */
    public LoadingView setRotateSpeed(int speed) {
        rotateSpeed = speed / 100;
        return this;
    }

    /**
     * 设置前进比例(即改变弧的宽度)
     * @param distance 前进比例
     */
    public LoadingView setDistance(float distance) {
        this.distance = distance;
        return this;
    }

    public void start() {
        handler.sendEmptyMessageDelayed(0, 10);
    }

    public void stop() {
        handler.removeMessages(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 刷新圆弧角度
        refreshStartAngle();
        refreshSweepAngle();
        for (int i = 0; i < leafCount; i++) {
            paint.setColor(colors[i % colors.length]);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(getArcWidth());
            float startDegree = startAngle + i * (360 / leafCount);
            drawArc(canvas, startDegree, sweepAngle);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        squareLength = Math.min(viewWidth, viewHeight) / 2;
    }

    private void drawArc(Canvas canvas, float startAngle, float sweepAngle) {
        float endAngle = startAngle + sweepAngle;
        int length = Math.min(viewWidth, viewHeight);
        arcRectF.set(
                Math.abs(length - viewWidth) / 2 + getArcWidth(),
                Math.abs(length - viewHeight) / 2 + getArcWidth(),
                Math.abs(length - viewWidth) / 2 + length - getArcWidth(),
                Math.abs(length - viewHeight) / 2 + length - getArcWidth());
        // 画圆弧
        canvas.drawArc(arcRectF, startAngle, sweepAngle, false, paint);
        float startX = (float) ((1.0 + Math.cos(Math.PI * startAngle / 180))) / 2 * arcRectF.width() + arcRectF.left;
        float startY = (float) ((1.0 + Math.sin(Math.PI * startAngle / 180))) / 2 * arcRectF.height() + arcRectF.top;
        float endX = (float) ((1.0 + Math.cos(Math.PI * endAngle / 180))) / 2 * arcRectF.width() + arcRectF.left;
        float endY = (float) ((1.0 + Math.sin(Math.PI * endAngle / 180))) / 2 * arcRectF.height() + arcRectF.top;
        paint.setStyle(Paint.Style.FILL);
        // 画两个圆圈去掉菱角
        canvas.drawCircle(startX, startY, getArcWidth() / 2, paint);
        canvas.drawCircle(endX, endY, getArcWidth() / 2, paint);
    }

    private void refreshStartAngle() {
        startAngle += addArcAngle ? rotateSpeed : rotateSpeed * 2;
    }

    private void refreshSweepAngle() {
        if (leafCount > 1) {
            sweepAngle = (360 / leafCount - leafSpace);
            sweepAngle = (int) (sweepAngle - sweepAngle / 2 * getRouteNumber());
        } else {
            sweepAngle += addArcAngle ? rotateSpeed : - rotateSpeed;
            addArcAngle = addArcAngle ? sweepAngle < minAngle + addAngle : sweepAngle <= minAngle;
        }
    }

    private float getArcWidth() {
        return getRouteNumber() * squareLength * (leafCount > 1 ? distance : 0) + arcWidth;
    }

    /**
     * 获取旋转系数,该系数为0到1的sin值
     */
    private float getRouteNumber() {
        return (float) ((1.0 + Math.sin(Math.PI * startAngle / 180))) / 2;
    }
}
