package com.zsj.recyclerviewheadview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

/**
 * @author zsj
 * 下拉刷新
 */
public class RefreshActivity extends AppCompatActivity {

    private RefreshRecyclerView mRefreshRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_acitivity);

        mRefreshRecyclerView = (RefreshRecyclerView) findViewById(R.id.refresh_recycler_view);
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> mList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mList.add("item =" + i);
        }
        MyAdapter myAdapter = new MyAdapter(mList);
        mRefreshRecyclerView.setAdapter(myAdapter);
        mRefreshRecyclerView.addRefreshViewCreator(new DefaultRefreshCreator());
        myAdapter.notifyDataSetChanged();
    }
}
