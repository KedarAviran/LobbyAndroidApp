package com.example.lobby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
    private Drawable green;
    private Drawable grey;
    private Drawable none;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        from=null;
        to=null;
        lastMove=null;
        green = getDrawable(android.R.drawable.presence_online);
        grey = getDrawable(android.R.drawable.presence_invisible);
        none = findViewById(R.id.b00).getForeground();
        Intent intent = getIntent();
        roomID = intent.getIntExtra("roomID",-1);
        turn = intent.getBooleanExtra("turn",false);
        setupBoard();
    }
    public void setupBoard()
    {
        setBtnColor("01",grey);setBtnColor("50",green);
        setBtnColor("03",grey);setBtnColor("52",green);
        setBtnColor("05",grey);setBtnColor("54",green);
        setBtnColor("07",grey);setBtnColor("56",green);
        setBtnColor("10",grey);setBtnColor("61",green);
        setBtnColor("12",grey);setBtnColor("63",green);
        setBtnColor("14",grey);setBtnColor("65",green);
        setBtnColor("16",grey);setBtnColor("67",green);
        setBtnColor("21",grey);setBtnColor("70",green);
        setBtnColor("23",grey);setBtnColor("72",green);
        setBtnColor("25",grey);setBtnColor("74",green);
        setBtnColor("27",grey);setBtnColor("76",green);
    }
    private void setBtnColor(String pos, Drawable color)
    {
        int resID = getResources().getIdentifier("b"+pos, "id", getPackageName());
        AppCompatButton button = (AppCompatButton)findViewById(resID);
        button.setForeground(color);
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