package com.zsj.recyclerviewheadview;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends Adapter<MyAdapter.ViewHolder> {
    private static final String TAG = "MyAdapter";
    private List<String> list;

    public MyAdapter(List<String> list) {
        // TODO Auto-generated constructor stub
        this.list = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        private final View mRoot;

        public ViewHolder(View view) {
            super(view);
            // TODO Auto-generated constructor stub
            tv = (TextView) view.findViewById(R.id.tv);
            mRoot = view.findViewById(R.id.root);
        }

    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv.setText(list.get(position));
        holder.mRoot.setTag(position);
        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(holder.tv.getContext(), "", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: AdapterPosition = "+holder.getAdapterPosition());
                Log.d(TAG, "onClick: LayoutPosition = "+holder.getLayoutPosition());
                Log.d(TAG, "onClick: position = "+position);
                Log.d(TAG, "onClick: Tag = "+holder.mRoot.getTag());

//                list.remove(position);
//                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
}
