package com.jay.mx.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jay.mx.R;

/**
 * Created by Jay on 2016/9/26.
 * 可变色的ImageView
 */

public class ColorfulImageView extends View {
    private static final String TAG = "ColorfulImageView";
    private Paint mPaint;
    private Rect mIconRect;
    private Context mContext;
    private BitmapDrawable mDrawable;
    private Bitmap mBitmap;

    public ColorfulImageView(Context context) {
        this(context, null);
    }

    public ColorfulImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mContext.getResources().getColor(R.color.bg_color_red));
        mPaint.setDither(true);
        mContext = context;

        mDrawable = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.mm_title_btn_contact_normal);
        mBitmap = mDrawable.getBitmap();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mIconRect = new Rect(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, mIconRect.toString());
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(mIconRect, mPaint);
        canvas.drawBitmap(mBitmap, null, mIconRect, mPaint);
    }
}
