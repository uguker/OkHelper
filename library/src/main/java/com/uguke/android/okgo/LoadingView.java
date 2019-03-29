package com.uguke.android.okgo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
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
    /** 叶数 **/
    private int arcCount;
    /** 圆弧颜色 **/
    private int[] arcColors;
    /** 旋转圆弧属性 **/
    private RotatingArc rotatingArc;

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
        arcColors = new int[] {ContextCompat.getColor(context, R.color.colorAccent)};
        rotatingArc = new RotatingArc(30, 270, 4);
        rotatingArc.setIntervalAngle(30);
        rotatingArc.setRotateRate(0.1f);
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

    public LoadingView setArcIntervalAngle(float angle) {
        rotatingArc.setIntervalAngle(angle);
        return this;
    }

    public LoadingView setArcStrokeWidth(float width) {
        float strokeWidth = getResources().getDisplayMetrics().density * width;
        rotatingArc.setStrokeWidth(strokeWidth);
        return this;
    }

    public LoadingView setArcColors(@ColorInt int... colors) {
        if (colors.length == 0) {
            this.arcColors = new int[] {ContextCompat.getColor(getContext(), R.color.colorAccent)};
        } else {
            this.arcColors = colors;
            if (colors.length > arcCount) {
                arcCount = colors.length;
            }
        }
        return this;
    }

    /**
     * 设置最小角度（仅对叶数为1有效）
     * @param min 最小角度
     */
    public LoadingView setArcMinAngle(float min) {
        rotatingArc.setMinAngle(min);
        return this;
    }

    /**
     * 设置增量角度（仅对叶数为1有效）
     * @param add 增量角度
     */
    public LoadingView setArcAddAngle(float add) {
        rotatingArc.setAddAngle(add);
        return this;
    }

    /**
     * 设置转一圈需要时间
     * @param time 转一圈需要时间
     */
    public LoadingView setRoundUseTime(int time) {
        rotatingArc.setRotateRate(360 / time);
        return this;
    }

    /**
     * 设置抖动比例(即改变弧的宽度)
     * @param ratio 抖动比例
     */
    public LoadingView setArcShakeRatio(float ratio) {
        rotatingArc.setSnakeRatio(ratio);
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
        rotatingArc.refresh(arcCount);
        for (int i = 0; i < arcCount; i++) {
            int squareLength = Math.min(viewWidth, viewHeight);
            float strokeWidth = rotatingArc.getRealStrokeWidth(squareLength, arcCount);
            paint.setColor(arcColors[i % arcColors.length]);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            float startAngle = rotatingArc.getStartAngle() + i * (360 / arcCount);
            drawArc(canvas, startAngle, rotatingArc.getSweepAngle());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    private void drawArc(Canvas canvas, float startAngle, float sweepAngle) {
        int squareLength = Math.min(viewWidth, viewHeight);
        float strokeWidth = rotatingArc.getRealStrokeWidth(squareLength, arcCount);
        arcRectF.set(
                Math.abs(squareLength - viewWidth) / 2 + strokeWidth,
                Math.abs(squareLength - viewHeight) / 2 + strokeWidth,
                Math.abs(squareLength - viewWidth) / 2 + squareLength - strokeWidth,
                Math.abs(squareLength - viewHeight) / 2 + squareLength - strokeWidth);
        // 画圆弧
        canvas.drawArc(arcRectF, startAngle, sweepAngle, false, paint);
        // 画两个圆圈,是弧线变得圆润
        paint.setStyle(Paint.Style.FILL);
        PointF startPoint = rotatingArc.getStartPoint(arcRectF);
        PointF endPoint = rotatingArc.getEndPoint(arcRectF);
        canvas.drawCircle(startPoint.x, startPoint.y, strokeWidth / 2, paint);
        canvas.drawCircle(endPoint.x, endPoint.y, strokeWidth / 2, paint);
    }

    /**
     * 转动的弧线数据类
     */
    static final class RotatingArc {
        /** 单页最小角度 **/
        private float minAngle;
        /** 单页最小角度 **/
        private float addAngle;
        /** 旋转速度 **/
        private float rotateRate;
        /** 抖动比例 **/
        private float snakeRatio;
        /** 圆弧宽度 **/
        private float strokeWidth;
        /** 叶型圆弧角度间隔 **/
        private float intervalAngle;
        /** 开始画弧线的角度 **/
        private float startAngle;
        /** 需要画弧线的角度 **/
        private float sweepAngle;
        /** 是否正在增加角度 **/
        private boolean angleAdding;
        private PointF startPoint;
        private PointF endPoint;

        RotatingArc(float minAngle, float addAngle, float rotateRate) {
            this.rotateRate = rotateRate;
            this.minAngle = minAngle;
            this.addAngle = addAngle;
            this.angleAdding = true;
            this.startPoint = new PointF(0, 0);
            this.endPoint = new PointF(0, 0);
        }

        void setMinAngle(float minAngle) {
            this.minAngle = minAngle;
        }

        void setAddAngle(float addAngle) {
            this.addAngle = addAngle;
        }

        void setRotateRate(float rotateRate) {
            this.rotateRate = rotateRate;
        }

        void setSnakeRatio(float snakeRatio) {
            this.snakeRatio = snakeRatio;
        }

        void setStrokeWidth(float strokeWidth) {
            this.strokeWidth = strokeWidth;
        }

        void setIntervalAngle(float intervalAngle) {
            this.intervalAngle = intervalAngle;
        }

        float getStartAngle() {
            return startAngle;
        }

        float getSweepAngle() {
            return sweepAngle;
        }

        void refresh(int arcCount) {
            // 开始的幅度角度
            startAngle += angleAdding ? rotateRate : rotateRate * 2;
            if (arcCount > 1) {
                sweepAngle = (360 / arcCount - intervalAngle);
                sweepAngle = (int) (sweepAngle - sweepAngle / 2 * getRouteNumber());
            } else {
                // 需要画的弧度
                sweepAngle += angleAdding ? rotateRate : -rotateRate;
                angleAdding = angleAdding ? sweepAngle < minAngle + addAngle : sweepAngle <= minAngle;
            }
        }

        PointF getStartPoint(RectF rectF) {
            float startX = (float) ((1.0 + Math.cos(Math.PI * startAngle / 180))) / 2 * rectF.width() + rectF.left;
            float startY = (float) ((1.0 + Math.sin(Math.PI * startAngle / 180))) / 2 * rectF.height() + rectF.top;
            startPoint.set(startX, startY);
            return startPoint;
        }

        PointF getEndPoint(RectF rectF) {
            float endAngle = startAngle + sweepAngle;
            float startX = (float) ((1.0 + Math.cos(Math.PI * endAngle / 180))) / 2 * rectF.width() + rectF.left;
            float startY = (float) ((1.0 + Math.sin(Math.PI * endAngle / 180))) / 2 * rectF.height() + rectF.top;
            endPoint.set(startX, startY);
            return endPoint;
        }

        float getRealStrokeWidth(int squareLength, int arcCount) {
            return getRouteNumber() * squareLength / 2 * (arcCount > 1 ? snakeRatio : 0) + strokeWidth;
        }

        float getRouteNumber() {
            return (float) ((1.0 + Math.sin(Math.PI * startAngle / 180))) / 2;
        }

    }

}
