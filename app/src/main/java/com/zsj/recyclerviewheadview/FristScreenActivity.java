package com.zsj.recyclerviewheadview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FristScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist_screen);
    }

    public void headerView(View view) {
        startActivity(new Intent(this, AddHeaderActivity.class));
    }

    public void emptyLayout(View view) {
        startActivity(new Intent(this,EmptyLayoutActivity.class));
    }
}
