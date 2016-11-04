package com.jay.mx.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jay.mx.R;
import com.jay.mx.base.BaseTitleActivity;
import com.jay.mx.widgets.CustomProgressBar;

/**
 * Created by Jay on 2016/11/3.
 * Test about AsyncTask
 */

public class TestAsyncTaskActivity extends BaseTitleActivity {
    private Button mStartBtn;
    private ProgressBar mProgressBar;
    private CustomProgressBar mCustomProgressBar;
    private TextView mProgressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE.SINGLE_BACK, R.color.bg_color_red_dark);
        setContentView(R.layout.activity_test_async_task);

        initViews();
    }

    private void initViews() {
        mStartBtn = (Button) findViewById(R.id.start_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartBtn.setEnabled(false);
                new MyAsyncTask().execute(25);
            }
        });

        mCustomProgressBar = (CustomProgressBar) findViewById(R.id.custom_progressbar);
        mCustomProgressBar.setProgress(170);

        mProgressText = (TextView) findViewById(R.id.progress_text);
        mCustomProgressBar.setOnProgressChangeListener(new CustomProgressBar.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int progress) {
                mProgressText.setText(String.valueOf(progress));
            }
        });
    }

    //泛型接收三个参数Params Progress Result
    //Params 启动时输入的参数，比如HTTP请求的URL
    //Progress 后台任务执行的百分比
    //Result 后台执行任务最终返回的结果
    private class MyAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for(int i=0; i<integers[0]; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mStartBtn.setEnabled(true);
            mStartBtn.setText(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
        }
    }
}
