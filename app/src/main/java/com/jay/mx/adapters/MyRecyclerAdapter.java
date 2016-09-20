package com.jay.mx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jay.mx.R;

import java.util.List;

/**
 * Created by Jay on 2016/9/20.
 * the adapter of recyclerView
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
    private Context mContext = null;
    private List<String> mList = null;

    public MyRecyclerAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_child, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mContentText.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mContentText = null;

        public MyViewHolder(View itemView) {
            super(itemView);
            mContentText = (TextView) itemView.findViewById(R.id.child_text);
        }
    }
}
