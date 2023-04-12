package com.example.lobby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<roomCard> roomList;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        roomList = new ArrayList<>();
        test();
        setAdapter();
    }
    private void setAdapter()
    {
        recyclerAdapter adapter = new recyclerAdapter(roomList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    private void test()
    {
        roomList.add(new roomCard("wate","chess",false));
        roomList.add(new roomCard("the ","chess",false));
        roomList.add(new roomCard("fuck","chess",false));
    }
}