package com.jay.mx.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.jay.mx.R;

/**
 * Created by Jay on 2016/11/4.
 * custom progressbar
 */

public class CustomProgressBar extends View {
    private static final String TAG = "CustomProgressBar";
    private float mHeight;
    private float mWidth;
    private float mPerLength;
    private float mIndex;
    //the current location of seek circle center
    private float mSeekLocation;

    private static float mDefaultHeight;
    private static float mDefaultWidth;

    private float mProgressHeight;

    private float mSeekRadius;

    private float mProgressTop;

    private Context mContext;

    private Paint mBgPaint;
    private Paint mSeekPaint;
    private Paint mTransparentPaint;

    private int[] mColors;
    private int mCount = 5;

    //movable
    public static final int TYPE_MOVABLE = 0;
    //unmovable
    public static final int TYPE_UNMOVABLE = 1;
    //default type: movable
    private int mType = TYPE_MOVABLE;

    //multi color
    public static final int STYLE_MULTI_COLOR = 0;
    //single color
    public static final int STYLE_SINGLE_COLOR = 1;
    //gradual color
    public static final int STYLE_GRADUAL_COLOR = 2;
    //default style: STYLE_MULTI_COLOR
    private int mStyle = STYLE_MULTI_COLOR;

    private boolean isSeekColorChangeable = true;

    //default min progress
    private int mMinProgress = 80;
    //default max progress
    private int mMaxProgress = 180;
    //current progress
    private int mProgress;

    private int mSeekColor;

    @ColorInt private int mSingleProgressColor;

    private int mStartColor;
    private int mEndColor;

    LinearGradient mGradient;

    public void setProgress(int progress) {
        mProgress = progress;
        mSeekLocation = (mProgress - mMinProgress) * mWidth / (mMaxProgress - mMinProgress) + mSeekRadius;
        postInvalidate();
    }

    public int getProgress() {
       return mProgress;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    public int getStyle() {
        return mStyle;
    }

    public void setSeekColorChangeable(boolean changeable) {
        isSeekColorChangeable = changeable;
    }

    public boolean getSeekColorChangeable() {
        return isSeekColorChangeable;
    }

    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }

    public void setMinProgress(int minProgress) {
        mMinProgress = minProgress;
    }

    public void setSeekColor(@ColorInt int color) {
        mSeekColor = color;
    }

    public @ColorInt int getSeekColor() {
        return mSeekColor;
    }

    public void setSingleProgressColor(@ColorInt int color) {
        mSingleProgressColor = color;
    }

    public @ColorInt int getSingleProgressColor() {
        return mSingleProgressColor;
    }

    public void setStartColor(int color) {
        mStartColor = color;
    }

    public int getStartColor() {
        return mStartColor;
    }

    public void setEndColor(int color) {
        mEndColor = color;
    }

    public int getEndColor() {
        return mEndColor;
    }


    private OnProgressChangeListener mListener;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        obtainStyledAttr(attrs, defStyleAttr);
        init();
    }

    public void setColors(@ColorInt int[] colors) {
        mColors = colors;
    }

    private void obtainStyledAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, defStyleAttr);
        mProgressHeight = ta.getDimensionPixelOffset(R.styleable.CustomProgressBar_progress_height, (int) dp2px(mContext, 10f));
        mSeekRadius = ta.getDimensionPixelOffset(R.styleable.CustomProgressBar_seek_radius, (int) dp2px(mContext, 9f));
        mType = ta.getInt(R.styleable.CustomProgressBar_type, TYPE_MOVABLE);
        mMaxProgress = ta.getInt(R.styleable.CustomProgressBar_max_progress, TYPE_MOVABLE);
        mMinProgress = ta.getInt(R.styleable.CustomProgressBar_min_progress, TYPE_MOVABLE);
        isSeekColorChangeable = ta.getBoolean(R.styleable.CustomProgressBar_seek_color_changeable, true);
        mStyle = ta.getInt(R.styleable.CustomProgressBar_style, STYLE_MULTI_COLOR);
        mSeekColor = ta.getColor(R.styleable.CustomProgressBar_seek_color, Color.rgb(206, 115, 6));
        mSingleProgressColor = ta.getColor(R.styleable.CustomProgressBar_single_progress_color, Color.rgb(206, 115, 6));
        mStartColor = ta.getColor(R.styleable.CustomProgressBar_gradual_start_color, Color.rgb(214, 197, 114));
        mEndColor = ta.getColor(R.styleable.CustomProgressBar_gradual_end_color, Color.rgb(255, 41, 25));
        ta.recycle();
    }

    private void init() {
        //default color of progressbar
        mColors = new int[]{
                Color.rgb(214, 197, 114),
                Color.rgb(214, 184, 21),
                Color.rgb(229, 146, 23),
                Color.rgb(242, 89, 24),
                Color.rgb(255, 41, 25)
        };
        mCount = mColors.length;

        //init paints
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSeekPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSeekPaint.setColor(mSeekColor);
        mTransparentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTransparentPaint.setColor(Color.WHITE);

        mDefaultHeight = dp2px(mContext, 18f);
        mDefaultWidth = dp2px(mContext, 300f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float desireHeight = mDefaultHeight + getPaddingStart() + getPaddingEnd();
        float desireWidth = mDefaultWidth + getPaddingTop() + getPaddingBottom();

        mHeight = View.resolveSize((int) desireHeight, heightMeasureSpec);
        mWidth = View.resolveSize((int) desireWidth, widthMeasureSpec);


        mPerLength = mWidth / mCount;
        mProgressTop = (mHeight - mProgressHeight) / 2;

        setMeasuredDimension((int)(mWidth + mSeekRadius * 2), (int)mHeight);

        if(mProgress != 0) {
            setProgress(mProgress);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBgPaint.setShader(null);
        mSeekPaint.setShader(null);
        switch (mStyle) {
            case STYLE_MULTI_COLOR:
                drawMultiColorProgressBar(canvas);
                drawSeek(canvas);
                break;
            case STYLE_GRADUAL_COLOR:
                drawGradualColorProgressBar(canvas);
                drawSeek(canvas);
                break;
            case STYLE_SINGLE_COLOR:
                drawSingleColorProgressBar(canvas);
                drawSeek(canvas);
                break;
            default:
                break;
        }
    }

    private void drawGradualColorProgressBar(Canvas canvas) {
        mGradient = new LinearGradient(0, 0, mWidth + mSeekRadius * 2, 0, new int[] {mStartColor,mEndColor}, null, Shader.TileMode.REPEAT);
        mBgPaint.setShader(mGradient);
        if(isSeekColorChangeable) mSeekPaint.setShader(mGradient);
        canvas.drawCircle(mHeight / 2 - mProgressTop + mSeekRadius, mHeight / 2, mHeight / 2 - mProgressTop, mBgPaint);
        canvas.drawRect(mHeight / 2 - mProgressTop + mSeekRadius, mProgressTop,
                mWidth - mHeight / 2 + mProgressTop + mSeekRadius, mHeight - mProgressTop, mBgPaint);
        canvas.drawCircle(mWidth - mHeight / 2 + mProgressTop + mSeekRadius,
                mHeight / 2, mHeight / 2 - mProgressTop, mBgPaint);
    }

    private void drawSingleColorProgressBar(Canvas canvas) {
        mBgPaint.setColor(mSingleProgressColor);
        canvas.drawCircle(mHeight / 2 - mProgressTop + mSeekRadius, mHeight / 2, mHeight / 2 - mProgressTop, mBgPaint);
        canvas.drawRect(mHeight / 2 - mProgressTop + mSeekRadius, mProgressTop,
                mWidth - mHeight / 2 + mProgressTop + mSeekRadius, mHeight - mProgressTop, mBgPaint);
        canvas.drawCircle(mWidth - mHeight / 2 + mProgressTop + mSeekRadius,
                mHeight / 2, mHeight / 2 - mProgressTop, mBgPaint);
    }

    private void drawMultiColorProgressBar(Canvas canvas) {
        for(int i=0; i<mCount; i++) {
            mIndex = mPerLength * i;
            mBgPaint.setColor(mColors[i]);
            if(i == 0) {
                canvas.drawCircle(mHeight / 2 - mProgressTop + mSeekRadius, mHeight / 2, mHeight / 2 - mProgressTop, mBgPaint);
                canvas.drawRect(mHeight / 2 - mProgressTop + mSeekRadius, mProgressTop,
                        mIndex + mPerLength + mSeekRadius, mHeight - mProgressTop, mBgPaint);
                continue;
            } else if(i== mCount - 1) {
                canvas.drawRect(mIndex + mSeekRadius, mProgressTop,
                        mIndex + mPerLength - mHeight / 2 + mProgressTop + mSeekRadius, mHeight - mProgressTop, mBgPaint);
                canvas.drawCircle(mIndex + mPerLength - mHeight / 2 + mProgressTop + mSeekRadius,
                        mHeight / 2, mHeight / 2 - mProgressTop, mBgPaint);
                continue;
            }
            canvas.drawRect(mIndex + mSeekRadius, mProgressTop, mIndex + mPerLength + mSeekRadius, mHeight - mProgressTop, mBgPaint);
        }
    }

    private void drawSeek(Canvas canvas) {
        if(isSeekColorChangeable && mStyle == STYLE_MULTI_COLOR) setSeekPaintColor();

        if(mSeekLocation <= mSeekRadius) {
            canvas.drawCircle(mSeekRadius, mHeight / 2, mSeekRadius + 1, mTransparentPaint);
            canvas.drawCircle(mSeekRadius, mHeight / 2, mSeekRadius, mSeekPaint);
        } else if(mSeekLocation >= mWidth + mSeekRadius) {
            canvas.drawCircle(mWidth + mSeekRadius, mHeight / 2, mSeekRadius + 1, mTransparentPaint);
            canvas.drawCircle(mWidth + mSeekRadius, mHeight / 2, mSeekRadius, mSeekPaint);
        } else {
            canvas.drawCircle(mSeekLocation, mHeight / 2, mSeekRadius + 1, mTransparentPaint);
            canvas.drawCircle(mSeekLocation, mHeight / 2, mSeekRadius, mSeekPaint);
        }
    }

    private void setSeekPaintColor() {
        int section = (int) ((mSeekLocation - mSeekRadius) / mPerLength);
        if(section >= 4) section = 4;
        mSeekPaint.setColor(mColors[section]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mType == TYPE_MOVABLE) {
            mSeekLocation = event.getX();
            if (mListener != null) {
                mListener.onProgressChange(calculateProgress());
            }
            postInvalidate();
        }
        return true;
    }

    private int calculateProgress() {
        if(mSeekLocation > mWidth + mSeekRadius) mProgress = mMaxProgress;
        else if(mSeekLocation < mSeekRadius) mProgress = mMinProgress;
        else mProgress = (int) ((mSeekLocation - mSeekRadius) / mWidth * (mMaxProgress - mMinProgress) + mMinProgress);
        return mProgress;
    }


    public static float dp2px(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnProgressChangeListener {
        void onProgressChange(int progress);
    }
}
