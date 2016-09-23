package com.jay.mx.activities;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jay.mx.R;
import com.jay.mx.base.BaseTitleActivity;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jay on 2016/9/23.
 * test OkHttp
 */

public class OKHttpTestActivity extends BaseTitleActivity implements View.OnClickListener{
    private static final String TAG = "OKHttpTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE.SINGLE_BACK, R.color.bg_color_red);
        setContentView(R.layout.test_ok_http);
        setTitle("OkHttpTest");

        init();
    }

    private void init() {
        TextView getText = (TextView) findViewById(R.id.get_text);
        TextView postText = (TextView) findViewById(R.id.post_text);
        TextView uploadText = (TextView) findViewById(R.id.upload_text);
        TextView downloadText = (TextView) findViewById(R.id.download_text);
        getText.setOnClickListener(this);
        postText.setOnClickListener(this);
        uploadText.setOnClickListener(this);
        downloadText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        OkHttpClient okHttpClient = new OkHttpClient();
        switch (v.getId()) {
            //异步的get请求
            case R.id.get_text:
                Request request = new Request.Builder().url("https://www.baidu.com").build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String htmlStr =  response.body().string();
                        Log.i(TAG, htmlStr);
                    }
                });
                break;
            //异步的post请求
            case R.id.post_text:
                FormEncodingBuilder builder = new FormEncodingBuilder();
                builder.add("username", "Jay Zhao");
                Request request1 = new Request.Builder().url("https://www.baidu.com").post(builder.build()).build();
                okHttpClient.newCall(request1).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        //do something when failure
                        Log.i(TAG, "post fail");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        //do something when success
                        Log.i(TAG, "post success: " + response.body().string());
                    }
                });
                break;
            //上传文件
            case R.id.upload_text:
                break;
            //下载文件
            case R.id.download_text:
                Log.i(TAG, "download file button clicked!");
                Request request2 = new Request.Builder().url("http://i.imgur.com/NG7m01W.jpg").build();
                okHttpClient.newCall(request2).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.i(TAG, "download fail!");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        InputStream inputStream = response.body().byteStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + "/demo.jpg"));
                        byte[] bytes = new byte[1024];
                        int len;
                        while((len = inputStream.read(bytes)) != -1) {
                            fileOutputStream.write(bytes, 0, len);
                        }
                        fileOutputStream.flush();
                        Log.i(TAG, "download success!");
                    }
                });
                break;
        }
    }
}
