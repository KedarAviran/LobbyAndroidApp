package com.example.lobby;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class roomCard {
    private String userName;
    private String playerTwo;
    private int roomID;
    private String gameType;
    public int btnID;

    public roomCard(String userName, String gameType, String playerTwo, int roomID) {
        this.userName = userName;
        this.gameType = gameType;
        this.playerTwo = playerTwo;
        this.roomID = roomID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public int getRoomID() {
        return roomID;
    }

}
