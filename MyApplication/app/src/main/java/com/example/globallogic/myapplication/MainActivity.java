package com.example.globallogic.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }

    public void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
    }

    public void initData(){
        MessageAdapter messageAdapter = new MessageAdapter(this);
        recyclerView.setAdapter(messageAdapter);
    }
}
