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
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final List<String> mList = new ArrayList<>();
        mList.add("Change Animation");
        mList.add("Loading Dialog");
        mList.add("Full Screen Style");
        mList.add("TableLayout");
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this, mList);
        adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, mList.get(position) + " clicked!");
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
