package com.example.lobby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class GameActivity extends AppCompatActivity {

    private String from;
    private String to;
    private String lastMove;
    private boolean turn;
    private int roomID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        from=null;
        to=null;
        lastMove=null;
        Intent intent = getIntent();
        roomID = intent.getIntExtra("roomID",-1);
        turn = intent.getBooleanExtra("turn",false);
    }
    public void setupBoard()
    {

    }
    public void onButtonClick(View v)
    {
        if(to != null) {
            to = null;
            from = (String) v.getTag();
        }
        if(from!=null&& to==null)
            to = (String) v.getTag();
        if(to==null&&from==null)
            from = (String) v.getTag();
        updateYourMove();
    }
    private void updateYourMove()
    {
        TextView txt = (TextView)findViewById(R.id.yourMoveTextView);
        if(from == null)
            return;
        txt.setText("Your Move is: "+from);
        if(to == null)
            return;
        txt.setText(txt.getText()+" To: "+to);
    }
    private void getLastMove()
    {
        String url = "http://192.168.127.1:8080/getLastMove?roomID="+roomID;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                //if(!lastMove.equals(str))
                //    ApplyMoveAndUpdateLastMove;
                //    update turn
                ((TextView)findViewById(R.id.lastMoveTextView)).setText(str);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {

            }
        });
    }
    public void submitMove(View v)
    {
        if(!turn)
            return;
        String url = "http://192.168.127.1:8080/setMove?roomID="+roomID +"&move="+from+to;
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
        getLastMove();
    }
}