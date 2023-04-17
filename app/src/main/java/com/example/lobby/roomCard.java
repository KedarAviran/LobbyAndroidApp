package com.example.lobby;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class roomCard extends AppCompatActivity {
    private String userName;
    private String playerTwo;
    private int roomID;
    private String gameType;

    public roomCard(String userName, String gameType, String playerTwo, int roomID) {
        this.userName = userName;
        this.gameType = gameType;
        this.playerTwo = playerTwo;
        this.roomID = roomID;
    }
    public String getUserName()
    {
        return userName;
    }
    public int getRoomID()
    {
        return roomID;
    }
    public void joinGame(View v)
    {
        String url = "http://192.168.127.1:8080/getLastMove?roomID="+roomID;
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
        myIntent.putExtra("roomID",roomID);
        startActivity(myIntent);
    }
}
