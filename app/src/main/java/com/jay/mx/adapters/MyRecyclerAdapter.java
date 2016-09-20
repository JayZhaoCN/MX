package com.jay.mx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private Context mContext;
    private List<String> mList;
    private OnItemClickListener mListener;

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mContentText = null;

        public MyViewHolder(View itemView) {
            super(itemView);
            mContentText = (TextView) itemView.findViewById(R.id.child_text);
            //这里只有设置mContentText的点击事件才有效果
            //使用
            // itemView.setOnClickListener(this);
            //以及
            //itemView.findViewById(R.id.cv_item).setOnClickListener(this);
            //都是无效的，不得其解
            mContentText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("MyRecyclerAdapter", "onClick!");
            mListener.onItemClick(v, getLayoutPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
