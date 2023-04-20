package com.example.lobby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private final String ip = "192.168.236.1";
    private ArrayList<roomCard> roomList;
    private RecyclerView recyclerView;
    private String name;
    private String hostRoomID;
    public void createRoom(View v)
    {
        createRoom("Chess", name);
    }
    public void refresh(View v)
    {
        getRooms();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        recyclerView = findViewById(R.id.recycler);
        roomList = new ArrayList<>();
        getRooms();
        setAdapter();
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        getRooms();
                    }
                }, 0, 2, TimeUnit.SECONDS);
    }
    private void setAdapter()
    {
        recyclerAdapter adapter = new recyclerAdapter(roomList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    private void getRooms()
    {
        String url = "http://"+ip+":8080/getRooms";
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                roomList.clear();
                String str = new String(responseBody);
                if(str.length()!=0)
                    for (String rm: str.split(" "))
                    {
                        String[] values = rm.split(",");
                        roomList.add(new roomCard(values[1],values[3],values[2],Integer.parseInt(values[0])));
                    }
                setAdapter();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                roomList.add(new roomCard("error","error","error",0));
            }
        });
    }
    private void createRoom(String gameType , String name)
    {
        String url = "http://"+ip+":8080/createRoom?gameType="+gameType+"&playerName="+name;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                getRooms();
                hostRoomID = new String(responseBody);
                ((Button)findViewById(R.id.cancelGameBtn)).setClickable(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    public void leave(View v)
    {
        leaveRoom(hostRoomID,name);
    }
    private void leaveRoom(String roomID , String name)
    {
        String url = "http://"+ip+":8080/leaveRoom?roomID="+roomID+"&playerName="+name;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                getRooms();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    public void joinGame(View v)
    {
        String roomID = (((String)v.getTag()).split(","))[0];
        String userName = (((String)v.getTag()).split(","))[1];
        String url = "http://"+ip+":8080/joinRoom?roomID="+roomID+"&playerName="+userName;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {

            }
        });
        Intent myIntent = new Intent(this, GameActivity.class);
        myIntent.putExtra("turn",false);
        myIntent.putExtra("roomID",Integer.parseInt(roomID));
        myIntent.putExtra("name",name);
        startActivity(myIntent);
    }
}