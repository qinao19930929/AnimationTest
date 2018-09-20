package view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import animationtest.qin.com.animationtest.R;

public class MdStyleProgress extends View {

    private static final int PROGRESS_START_COLOR = Color.parseColor("#45B7FE");
    private static final int PROGRESS_END_COLOR = Color.parseColor("#7A6BFD");
    private static final int PROGRESS_WIDTH = 3;
    private static final int RADIUS = 30;

    private int mProgressStartColor = PROGRESS_START_COLOR;
    private int mProgressEndColor = PROGRESS_END_COLOR;
    private int mProgressWidth = dp2px(PROGRESS_WIDTH);
    private int mRadius = dp2px(RADIUS);

    private Paint progressPaint;

    private final static int INITI_ALANGLE = 270;
    private int startAngle = 270;
    private int sweepAngle = 0;
    private ValueAnimator mAnimator;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private RectF mRectF;


    public MdStyleProgress(Context context) {
        this(context, null);
    }

    public MdStyleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MdStyleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MdStyleProgress);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MdStyleProgress_progress_start_color:
                    mProgressStartColor = typedArray.getColor(attr, PROGRESS_START_COLOR);
                    break;
                    case R.styleable.MdStyleProgress_progress_end_color:
                    mProgressEndColor = typedArray.getColor(attr, PROGRESS_END_COLOR);
                    break;
                case R.styleable.MdStyleProgress_progress_width:
                    mProgressWidth = (int) typedArray.getDimension(attr, mProgressWidth);
                    break;
                case R.styleable.MdStyleProgress_radius:
                    mRadius = (int) typedArray.getDimension(attr, mRadius);
                    break;
            }
        }
        //回收TypedArray对象
        typedArray.recycle();
        //设置画笔
        setPaint();
        initAnimator();
        //定义圈圈区域
        mRectF = new RectF(mProgressWidth / 2, mProgressWidth / 2, (mRadius) * 2 + mProgressWidth / 2, (mRadius) * 2 + mProgressWidth / 2);
    }

    private void setPaint() {
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setDither(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(mProgressWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        //设置渐变色
        LinearGradient linearGradient = new LinearGradient(0, 0, mRadius*2, mRadius*2, mProgressStartColor, mProgressEndColor, Shader.TileMode.CLAMP);
        progressPaint.setShader(linearGradient);
    }

    private void initAnimator() {
        mAnimator = new ValueAnimator();
        mAnimator = ValueAnimator.ofInt(0, 504);  //504是360 （转一圈）+ 160*0.4（收缩尾巴）
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setDuration(1200);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setInterpolator(new LinearInterpolator());
        // 设置动画的回调
        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                //开始弧线逐渐增长
                if (value < 144) {
                    startAngle = INITI_ALANGLE - value;
                    //ui备注说为了动画平滑
                    if (value < 5) {
                        sweepAngle = 5;
                    } else {
                        sweepAngle = value;
                    }
                }
                //到了40%长度
                else if (value >= 144 && value < 360) {
                    sweepAngle = 144;
                    startAngle = INITI_ALANGLE - value;
                }
                //收缩尾巴
                else if (value > 360) {
                    if (sweepAngle > 5) {
                        sweepAngle = 144 - (value - 360);
                    }
                }
                Log.d("MDProgress", "start:" + startAngle + " value:" + value);
                invalidate();
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = getPaddingLeft() + mProgressWidth + mRadius * 2 + getPaddingRight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);

        int heightSize = getPaddingTop() + mProgressWidth + mRadius * 2 + getPaddingBottom();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, progressPaint);
        canvas.restore();
    }

    /**
     * dp 2 px
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public void startAnimator() {
        mAnimator.addUpdateListener(mAnimatorUpdateListener);
        mAnimator.start();
    }

    public void stopAnimator() {
        mAnimator.removeAllUpdateListeners();
        mAnimator.end();
    }

    /**
     * sp 2 px
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());
    }
}