package com.jay.mx.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jay.mx.R;
import com.jay.mx.adapters.MyRecyclerAdapter;
import com.jay.mx.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        List<String> mList = new ArrayList<>();
        mList.add("a");
        mList.add("b");
        mList.add("c");
        mList.add("d");
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this, mList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
