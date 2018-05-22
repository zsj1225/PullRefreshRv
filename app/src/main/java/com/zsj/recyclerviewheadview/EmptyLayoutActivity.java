package com.zsj.recyclerviewheadview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class EmptyLayoutActivity extends AppCompatActivity {

    private WrapEmptyView mWrapEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_layout);

        mWrapEmptyView = (WrapEmptyView) findViewById(R.id.empty_view);
        DefaultRefreshCreator defaultRefreshCreator = new DefaultRefreshCreator();
        mWrapEmptyView.addRefreshViewCreator(defaultRefreshCreator);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, mWrapEmptyView, false);
        mWrapEmptyView.addEmptyView(emptyView);

    }
}
