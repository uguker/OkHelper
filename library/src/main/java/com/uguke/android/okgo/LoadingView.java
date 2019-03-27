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

    private Paint paint;
    private RectF arcRectF;
    private int viewWidth;
    private int viewHeight;
    private int squareLength;

    /** 开始画弧线的角度 **/
    private float startAngle;
    /** 需要画弧线的角度 **/
    private float sweepAngle;
    /** 旋转速度 **/
    private float rotateSpeed;
    /** 正在增加弧线角度 **/
    private boolean addArcAngle;

    /** 叶数 **/
    private int arcCount;
    /** 叶型圆弧角度间隔 **/
    private float arcIntervalAngle;
    /** 抖动比例 **/
    private float arcShakeRatio;
    /** 圆弧宽度 **/
    private float arcStrokeWidth;
    /** 圆弧颜色 **/
    private int[] arcColors;

    /** 单页最小角度 **/
    private float minAngle;
    /** 单页增量角度 **/
    private float addAngle;

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
        arcCount = 1;
        arcIntervalAngle = 30;
        arcShakeRatio = 0.1f;
        addArcAngle = true;
        minAngle = 30;
        addAngle = 270;
        rotateSpeed = 4;
        arcColors = new int[] {ContextCompat.getColor(context, R.color.colorAccent)};
        arcStrokeWidth = context.getResources().getDisplayMetrics().density * 3;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    public LoadingView setArcCount(@IntRange(from = 1) int count) {
        arcCount = count > 1 ? count : 1;
        return this;
    }

    public LoadingView setArcIntervalAngle(float space) {
        arcIntervalAngle = space;
        return this;
    }

    public LoadingView setArcStrokeWidth(float width) {
        arcStrokeWidth = getResources().getDisplayMetrics().density * width;
        return this;
    }

    public LoadingView setArcColors(@ColorInt int... colors) {
        if (colors.length == 0) {
            this.arcColors = new int[] {ContextCompat.getColor(getContext(), R.color.colorAccent)};
        } else {
            this.arcColors = colors;
        }
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
     * 设置转一圈需要时间
     * @param time 转一圈需要时间
     */
    public LoadingView setRoundUseTime(int time) {
        rotateSpeed = 360 / time;
        return this;
    }

    /**
     * 设置抖动比例(即改变弧的宽度)
     * @param ratio 抖动比例
     */
    public LoadingView setArcShakeRatio(float ratio) {
        this.arcShakeRatio = ratio;
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
        refreshArcAngle();
        for (int i = 0; i < arcCount; i++) {
            paint.setColor(arcColors[i % arcColors.length]);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(getRealArcStrokeWidth());
            float startDegree = startAngle + i * (360 / arcCount);
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
                Math.abs(length - viewWidth) / 2 + getRealArcStrokeWidth(),
                Math.abs(length - viewHeight) / 2 + getRealArcStrokeWidth(),
                Math.abs(length - viewWidth) / 2 + length - getRealArcStrokeWidth(),
                Math.abs(length - viewHeight) / 2 + length - getRealArcStrokeWidth());
        // 画圆弧
        canvas.drawArc(arcRectF, startAngle, sweepAngle, false, paint);
        float startX = (float) ((1.0 + Math.cos(Math.PI * startAngle / 180))) / 2 * arcRectF.width() + arcRectF.left;
        float startY = (float) ((1.0 + Math.sin(Math.PI * startAngle / 180))) / 2 * arcRectF.height() + arcRectF.top;
        float endX = (float) ((1.0 + Math.cos(Math.PI * endAngle / 180))) / 2 * arcRectF.width() + arcRectF.left;
        float endY = (float) ((1.0 + Math.sin(Math.PI * endAngle / 180))) / 2 * arcRectF.height() + arcRectF.top;
        paint.setStyle(Paint.Style.FILL);
        // 画两个圆圈去掉菱角
        canvas.drawCircle(startX, startY, getRealArcStrokeWidth() / 2, paint);
        canvas.drawCircle(endX, endY, getRealArcStrokeWidth() / 2, paint);
    }

    private void refreshArcAngle() {
        // 开始的幅度角度
        startAngle += addArcAngle ? rotateSpeed : rotateSpeed * 2;
        if (arcCount > 1) {
            sweepAngle = (360 / arcCount - arcIntervalAngle);
            sweepAngle = (int) (sweepAngle - sweepAngle / 2 * getRouteNumber());
        } else {
            // 需要画的弧度
            sweepAngle += addArcAngle ? rotateSpeed : -rotateSpeed;
            addArcAngle = addArcAngle ? sweepAngle < minAngle + addAngle : sweepAngle <= minAngle;
        }
    }

    private float getRealArcStrokeWidth() {
        return getRouteNumber() * squareLength * (arcCount > 1 ? arcShakeRatio : 0) + arcStrokeWidth;
    }

    /**
     * 获取旋转系数,该系数为0到1的sin值
     */
    private float getRouteNumber() {
        return (float) ((1.0 + Math.sin(Math.PI * startAngle / 180))) / 2;
    }
}
