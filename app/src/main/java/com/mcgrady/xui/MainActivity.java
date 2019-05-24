package com.mcgrady.xui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mcgrady.xui.linkagerecyclerview.LinkageRecyclerView;

public class MainActivity extends AppCompatActivity {

    private LinkageRecyclerView linkageRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        linkageRecyclerView = findViewById(R.id.linkage_recycler);
    }
}
