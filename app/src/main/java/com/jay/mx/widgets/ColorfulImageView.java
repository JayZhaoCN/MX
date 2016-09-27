package com.jay.mx.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jay.mx.R;

/**
 * Created by Jay on 2016/9/26.
 * 可变色的ImageView
 */

public class ColorfulImageView extends View {
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Rect mIconRect;
    private Context mContext;
    private int mAlpha = 0;

    public ColorfulImageView(Context context) {
        this(context, null);
    }

    public ColorfulImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        mPaint.setDither(true);
        mPaint.setAlpha(0);
        mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mIconRect = new Rect(0, 0, w, h);
        BitmapDrawable drawable = (BitmapDrawable) mContext.getDrawable(R.drawable.mm_title_btn_contact_normal);
        mBitmap = drawable.getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int alpha = (int) Math.ceil((255 * mAlpha));
        mPaint.setAlpha(alpha);
        canvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        canvas.drawBitmap(mBitmap, null, mIconRect, mPaint);
    }
}
