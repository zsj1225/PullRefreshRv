package com.zsj.recyclerviewheadview;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zsj
 * <p>
 * 添加头布局的Activity
 */
public class AddHeaderActivity extends Activity {

    private WrapRecyclerView mWrapRecyclerView;
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            mWrapRecyclerView.restorePullRefreshView();
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWrapRecyclerView = (WrapRecyclerView) findViewById(R.id.recyclerView);
        mWrapRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        View headView = LayoutInflater.from(this).inflate(R.layout.layout_refresh_header_view, mWrapRecyclerView, false);
        final DefaultRefreshCreator defaultRefreshCreator = new DefaultRefreshCreator();
        mWrapRecyclerView.addHeaderView(defaultRefreshCreator);
        mWrapRecyclerView.setOnRefreshListener(new WrapRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(AddHeaderActivity.this, "onRefresh", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddHeaderActivity.this,NewActivity.class));
                mWrapRecyclerView.onStopPullRefresh();

            }
        });
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item " + i);
        }

        MyAdapter adapter = new MyAdapter(list);
        mWrapRecyclerView.setAdapter(adapter);

    }

}
