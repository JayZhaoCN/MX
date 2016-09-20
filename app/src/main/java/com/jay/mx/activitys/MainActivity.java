package com.jay.mx.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jay.mx.R;
import com.jay.mx.adapters.MyRecyclerAdapter;
import com.jay.mx.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseTitleActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //如何将一个数组转化为List，下面给出了方法：
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.function_1));

        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this, list);
        adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, list.get(position) + " clicked!");
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
