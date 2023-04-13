package com.example.lobby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;

import com.loopj.android.http.*;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ArrayList<roomCard> roomList;
    private RecyclerView recyclerView;
    public void createRoom(View v)
    {
        createRoom("Chess","Aviran");
    }
    public void refresh(View v)
    {
        getRooms();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        roomList = new ArrayList<>();
        getRooms();
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
    private void getRooms()
    {
        String url = "http://192.168.127.1:8080/getRooms";
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                roomList.clear();
                String str = new String(responseBody);
                if(str.length()==0)
                    return;
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
        String url = "http://192.168.127.1:8080/createRoom?gameType="+gameType+"&playerName="+name;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                getRooms();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void joinRoom(int roomID , String name)
    {
        String url = "http://192.168.127.1:8080/joinRoom";
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                //joinRoom
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void leaveRoom(int roomID , String name)
    {
        String url = "http://192.168.127.1:8080/leaveRoom";
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                //leaveRoom
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}