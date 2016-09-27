package com.jay.mx.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jay.mx.R;

/**
 * Created by Jay on 2016/9/27.
 * Test View
 */

public class TestView extends View {
    private static final String TAG = "TestView";
    private Paint mPaint;
    private Paint mTestPaint;
    private Context mContext;
    private RectF mRectF;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);  //设置抗锯齿
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.bg_color_red));  //设置画笔颜色 参数：16进制带Alpha颜色值
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);  //设置画笔类型，FILL、STROKE、FILL_AND_STROKE
        //mPaint.setStrokeWidth(5);  //设置空隙宽度
        mPaint.setShadowLayer(10, 15, 15, Color.CYAN);
        mPaint.setTextSize(40);
        mPaint.setTextAlign(Paint.Align.LEFT);  // 默认是Paint.Align.LEFT

        mTestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTestPaint.setAntiAlias(true);
        mTestPaint.setColor(ContextCompat.getColor(mContext, R.color.bg_color_red));
        mTestPaint.setStyle(Paint.Style.STROKE);
        mTestPaint.setStrokeWidth(10);

        mRectF = new RectF(500, 500, 700, 600);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(400, 100, 30, mPaint);
        //关于canvas.drawCircle()方法：
        //前两个参数表示圆心的坐标。
        //第三个参数表示要画的圆的半径

        canvas.drawText("Jay Zhao", 100, 100, mPaint);
        //关于canvas.drawText()方法：
        //"Jay Zhao"是要画的字。
        //0表示文字的左边距离控件左边的距离。默认是左边。如果设置了mPaint.setTextAlign(Paint.Align.CENTER)则表示文字的中间距离控件左边的距离。
        //如果设置了mPaint.setTextAlign(Paint.Align.RIGHT)则表示文字的右边距离控件左边的距离。
        //第三个参数100表示文字的Baseline距离控件上边界的距离。关于Baseline，又是一个大坑。

        canvas.drawLine(0, 400, 400, 400, mPaint);
        //画直线，很简单了，起点和终点坐标以及画笔

        //canvas.drawRoundRect(mRectF, 20, 10, mPaint);
        //画圆角矩形 Canvas.drawRoundRect(RectF rectF, float cx, float cy, Paint paint);
        //RectF rectF:要画的矩形
        //float rx:生成圆角的椭圆的X轴半径
        //float ry:生成圆角的椭圆的Y轴半径
        //Paint paint:画笔

        //canvas.drawOval(mRectF, mPaint);
        //画椭圆，很简单了

        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(mRectF, 0, 180, false, mPaint);
        //画弧
        //Canvas.drawArc(RectF rectF, float startAngle, float sweepAngle, boolean useCenter, Paint paint);
        //RectF rectF:生成椭圆的矩形
        //float startAngle:弧开始的角度以X轴正方向为0度
        //float sweepAngle:弧划过的角度，顺时针方向划过
        //boolean useCenter:是否画弧的两边，true表示画，false表示只画弧
        //Paint paint:画笔

        //下面探索StrokeWidth和边界之间的关系
        canvas.drawCircle(400, 400, 100, mTestPaint);
        canvas.drawLine(500, 100, 500, 700, mPaint);
        //结论：
        //如果线或弧有宽度，是以线或弧的中心为基准，向两边分别扩散半个StrokeWidth宽度
        //为了画图时不至于让边界遮挡部分内容，需要考虑到这一点并做相应处理。
    }
}
