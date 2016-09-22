package com.jay.mx.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jay.mx.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by Jay on 2016/4/7.
 * 项目中所有Activity的基类
 */
public class BaseTitleActivity extends FragmentActivity {

    private static final String TAG = "MyBaseTitleActivity";
    private FrameLayout mContentParent;
    private Button mRightButton;

    private ISearch mSearchListener;

    private View mContentView;
    private TextView mTitleText;
    private EditText mEditText;

    private Button mSearchButton;

    private RelativeLayout mTitle;

    private View mStatusView;
    private ViewGroup mTitleParent;

    //默认的风格是BACK_AND_MORE
    private STYLE mStyle = STYLE.BACK_AND_MORE;

    /**
     * Style枚举
     */
    public enum STYLE {
        BACK_AND_MORE,
        SINGLE_BACK,
        FULL_SCREEN,
        BACK_AND_EDIT
    }

    /**
     * 得到右边的Button
     * @return 右Button
     */
    public Button getRightButton() {
        return mRightButton;
    }

    /**
     * 设置Activity的标题
     * @param title 标题
     */
    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    /**
     * 得到标题所对应的TextView
     * @return TextView
     */
    public TextView getTitleText() {
        return mTitleText;
    }

    /**
     * 得到标题栏上的编辑框
     * @return EditText
     */
    public EditText getEditText() {
        return mEditText;
    }

    /**
     * 得到搜索按钮
     * @return Search Button
     */
    public Button getSearchButton() {
        return mSearchButton;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_title_parent);
        initViews();

        //设置透明状态栏
        //实现沉浸式状态栏的关键
        //这里可能需要添加一个判断，KITKAT以下的版本，没有透明状态栏，无法实现沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            mStatusView.setVisibility(View.GONE);
        }
        setMiuiStatusBarDarkMode(this, false);
    }

    private void initViews() {
        mContentParent = (FrameLayout) findViewById(R.id.title_parent);
        mTitle = (RelativeLayout) findViewById(R.id.title);
        mTitleText = (TextView) findViewById(R.id.title_text);
        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRightButton = (Button) findViewById(R.id.right);
        setRightButtonBackground();

        mTitleParent = (ViewGroup) findViewById(R.id.title_container);
        mStatusView = findViewById(R.id.status_view);
        mStatusView.getLayoutParams().height = getStatusBarHeight();
    }

    /**
     * 设置右边Button的背景图片
     */
    private void setRightButtonBackground() {
        //x方向上翻转一张图片
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.title_back_icon);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        Matrix matrix = canvas.getMatrix();
        matrix.setScale(-1, 1);
        matrix.postTranslate(bitmap1.getWidth(), 0);
        canvas.drawBitmap(bitmap1, matrix, null);
        //将Bitmap转换成Drawable
        Drawable drawable =new BitmapDrawable(getResources(), bitmap2);
        mRightButton.setBackground(drawable);
    }

    /**
     * 设置显示风格
     * @param style 显示风格
     */
    public void setStyle(STYLE style) {
        mStyle = style;
        switch(style) {
            case BACK_AND_MORE:
                //默认的样式，不用做操作
                break;
            case SINGLE_BACK:
                mRightButton.setVisibility(View.GONE);
                break;
            case FULL_SCREEN:
                mTitle.setVisibility(View.GONE);
                updateView();
                break;
            case BACK_AND_EDIT:
                mSearchButton = (Button) findViewById(R.id.search_button);
                mRightButton.setBackgroundResource(R.drawable.edit_press);
                mEditText = (EditText) findViewById(R.id.edit_text);
                //BACK_AND_EDIT模式下，点击RightButton会转换编辑模式。
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch(mStyle) {
                            case BACK_AND_EDIT:
                                changeToEdit();
                                //调起软键盘
                                mEditText.setFocusable(true);
                                mEditText.setFocusableInTouchMode(true);
                                mEditText.requestFocus();
                                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
                                break;
                        }
                    }
                });
                mSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSearchListener.onSearchClicked();
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 需要注意的是，子类在继承时。该方法需要在super.onCreate()方法之后调用，否则颜色的设置可能不起作用
     * @param style 风格
     * @param color 颜色
     */
    public void setStyle(STYLE style, int color) {
        setStyle(style);
        setColor(color);
    }

    /**
     * set the color of titleBar and statusBar
     * @param color colorRes, not colorInt
     */
    public void setColor(@ColorRes int color) {
        if(mStatusView != null) {
            mStatusView.setBackgroundResource(color);
        }
        if(null != mTitleParent) {
            mTitleParent.setBackgroundResource(color);
        }
    }

    /**
     * set the color of titleBar and statusBar
     * @param color colorInt, not colorRes
     */
    public void setColorValue(@ColorInt int color) {
        if(mStatusView != null) {
            mStatusView.setBackgroundColor(color);
        }
        if(null != mTitleParent) {
            mTitleParent.setBackgroundColor(color);
        }
    }

    /**
     * 将标题栏转换为编辑模式
     */
    public void changeToEdit() {
        mTitleText.setVisibility(View.GONE);
        mEditText.setVisibility(View.VISIBLE);
        mRightButton.setVisibility(View.GONE);
        mSearchButton.setVisibility(View.VISIBLE);
    }

    /**
     * 将标题栏转换为正常模式
     */
    public void returnToNormal() {
        mTitleText.setVisibility(View.VISIBLE);
        mEditText.setVisibility(View.GONE);
        mRightButton.setVisibility(View.VISIBLE);
        mSearchButton.setVisibility(View.GONE);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(this, layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.TOP;
        setContentView(view, params);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentView = view;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) params;
        int titleHeight = this.getResources().getDimensionPixelSize(R.dimen.title_height);
        layoutParams.topMargin = titleHeight + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? getStatusBarHeight() : 0);

        if(mContentParent.getChildCount() > 1) {
            mContentParent.removeViewAt(1);
        }
        mContentParent.addView(mContentView, layoutParams);
        updateView();
    }

    private void updateView() {
        if(mContentView != null && mStyle == STYLE.FULL_SCREEN) {
            /*
             Log.e(TAG, "mContentView is not null");
             直接从mContentView中拿到的LayoutParams似乎是NULL，具体为什么不知道。
             mContentView.getLayoutParams();  //the value is NULL
             (mContentParent.addView())，当然是null啊
             */
            if(mContentParent.getChildAt(1) != null) {
                FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) mContentParent.getChildAt(1).getLayoutParams();
                params.topMargin = 0;
                mContentView.setLayoutParams(params);
            }
        }
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setSearchListener(ISearch listener) {
        this.mSearchListener = listener;
    }

    public interface ISearch {
        void onSearchClicked();
    }
}
