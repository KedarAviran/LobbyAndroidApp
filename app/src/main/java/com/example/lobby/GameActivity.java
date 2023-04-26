package com.example.lobby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class GameActivity extends AppCompatActivity {

    private String ip = "192.168.236.1";
    private String name;
    private String from;
    private String to;
    private String lastMove;
    private boolean turn;
    private boolean preview;
    private boolean isGameOver = false;
    private String[] moveHistory;
    private int moveIndex = 0;
    private int roomID;
    private Drawable green;
    private Drawable grey;
    private Drawable none;
    private boolean returnToLobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        returnToLobby = false;
        from = null;
        to = null;
        lastMove = null;
        green = getDrawable(android.R.drawable.presence_online);
        grey = getDrawable(android.R.drawable.presence_invisible);
        none = findViewById(R.id.b00).getForeground();
        Intent intent = getIntent();
        roomID = intent.getIntExtra("roomID", -1);
        turn = intent.getBooleanExtra("turn", false);
        name = intent.getStringExtra("name");
        preview = intent.getBooleanExtra("preview", false);
        ((Button) findViewById(R.id.nextMoveBtn)).setVisibility(View.INVISIBLE);
        setupBoard();
        if (preview) {
            getMoveHistory(String.valueOf(roomID));
            ((TextView) findViewById(R.id.lastMoveTextView)).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.yourMoveTextView)).setVisibility(View.INVISIBLE);
            ((Button) findViewById(R.id.endTurnButtun)).setVisibility(View.INVISIBLE);
            ((Button) findViewById(R.id.nextMoveBtn)).setVisibility(View.VISIBLE);
            returnToLobby = true;
            return;
        }
        setLooper();
    }
    private void setLooper()
    {
        Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                try {
                    if(!isGameOver) {
                        isPlayerLeft();
                        getLastMove();
                    }
                }
                catch (Exception e){
                    Log.d("MyError", e.toString());
                }
                handler.postDelayed(this, 2000);
            }
        };
        handler.post(runnableCode);
    }
    public void setupBoard() {
        setBtnColor("01", grey);
        setBtnColor("50", green);
        setBtnColor("03", grey);
        setBtnColor("52", green);
        setBtnColor("05", grey);
        setBtnColor("54", green);
        setBtnColor("07", grey);
        setBtnColor("56", green);
        setBtnColor("10", grey);
        setBtnColor("61", green);
        setBtnColor("12", grey);
        setBtnColor("63", green);
        setBtnColor("14", grey);
        setBtnColor("65", green);
        setBtnColor("16", grey);
        setBtnColor("67", green);
        setBtnColor("21", grey);
        setBtnColor("70", green);
        setBtnColor("23", grey);
        setBtnColor("72", green);
        setBtnColor("25", grey);
        setBtnColor("74", green);
        setBtnColor("27", grey);
        setBtnColor("76", green);
    }

    public void nextMove(View v) {
        if (moveIndex > moveHistory.length - 1)
            return;
        applyMove(moveHistory[moveIndex]);
        moveIndex++;
    }

    private void getMoveHistory(String ID) {
        String url = "http://" + ip + ":8080/getGameHistory?roomID=" + roomID;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                moveHistory = str.split("\\.");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void setBtnColor(String pos, Drawable color) {
        int resID = getResources().getIdentifier("b" + pos, "id", getPackageName());
        AppCompatButton button = (AppCompatButton) findViewById(resID);
        button.setForeground(color);
    }

    public void onButtonClick(View v) {
        if (to != null) {
            to = null;
            from = (String) v.getTag();
            updateYourMove();
            return;
        }
        if (from != null) {
            to = (String) v.getTag();
            updateYourMove();
            return;
        }
        from = (String) v.getTag();
        updateYourMove();
    }

    private void updateYourMove() {
        TextView txt = (TextView) findViewById(R.id.yourMoveTextView);
        if (from == null)
            return;
        txt.setText("Your Move is: " + from);
        if (to == null)
            return;
        txt.setText(txt.getText() + " To: " + to);
    }

    public void leaveBtn(View v) {
        if (returnToLobby)
            returnToLobby();
        isGameOver=true;
        leaveRoom(String.valueOf(roomID), name);
    }

    private void returnToLobby() {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("name", name);
        startActivity(myIntent);
    }

    private void leaveRoom(String roomID, String name) {
        String url = "http://" + ip + ":8080/leaveRoom?roomID=" + roomID + "&playerName=" + name;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                returnToLobby();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void isPlayerLeft() {
        String url = "http://" + ip + ":8080/isPlayerLeft?roomID=" + roomID;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                if (str.length() == 0)
                    return;
                isGameOver=true;
                try {
                    runOnUiThread(() -> {

                        ((TextView) findViewById(R.id.playerLeftTxt)).setText(str + " HAS LEFT, YOU WIN.");
                        ((TextView) findViewById(R.id.playerLeftTxt)).setVisibility(View.VISIBLE);
                        returnToLobby = true;
                        ((Button) findViewById(R.id.forfitBtn)).setText("Leave to Lobby");

                    });

                }
                catch (Exception e)
                {
                    Log.d("error", "error");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }

    private void applyMove(String move) {
        String from = move.substring(0, 2);
        String to = move.substring(2, 4);
        int resID = getResources().getIdentifier("b" + from, "id", getPackageName());
        AppCompatButton fromBtn = (AppCompatButton) findViewById(resID);
        resID = getResources().getIdentifier("b" + to, "id", getPackageName());
        AppCompatButton toBtn = (AppCompatButton) findViewById(resID);
        toBtn.setForeground(fromBtn.getForeground());
        fromBtn.setForeground(none);
        int fromY = Integer.parseInt(move.charAt(0) + "");
        int fromX = Integer.parseInt(move.charAt(1) + "");
        int toY = Integer.parseInt(move.charAt(2) + "");
        int toX = Integer.parseInt(move.charAt(3) + "");
        int midX = (toX + fromX) / 2;
        int midY = (toY + fromY) / 2;
        if (Math.abs(fromX - toX) == 2) {
            resID = getResources().getIdentifier("b" + midY + midX, "id", getPackageName());
            AppCompatButton midBtn = (AppCompatButton) findViewById(resID);
            midBtn.setForeground(none);
        }
    }

    private void getLastMove() {
        if (isGameOver)
            return;
        String url = "http://" + ip + ":8080/getLastMove?roomID=" + roomID;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                try {
                    runOnUiThread(() -> {
                        if (str.equals("true")) {
                            isGameOver = true;
                            ((TextView) findViewById(R.id.lastMoveTextView)).setText("GAME OVER YOU LOST");
                        }
                        if(str.equals(""))
                            return;
                        if (lastMove != null)
                            if (lastMove.equals(str))
                                return;
                        lastMove = str;
                        applyMove(str);
                        turn = !turn;
                        ((TextView) findViewById(R.id.lastMoveTextView)).setText("Last move is: " + str);

                    });

                }
                catch (Exception e)
                {
                    Log.d("MyError", e.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }

    public void submitMove(View v) {
        //if(!turn || from==null || to == null)
        //   return;
        if (isGameOver)
            return;
        String url = "http://" + ip + ":8080/setMove?roomID=" + roomID + "&move=" + from + to;
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                if (str.equals("true")) {
                    isGameOver = true;
                    ((TextView) findViewById(R.id.lastMoveTextView)).setText("GAME OVER YOU WON");
                }
                getLastMove();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

}