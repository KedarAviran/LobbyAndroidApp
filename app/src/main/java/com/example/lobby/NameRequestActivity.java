package com.example.lobby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NameRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_request);
    }
    public void enterLobby(View v)
    {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("name",((EditText)findViewById(R.id.nameTextBox)).getText().toString());
        startActivity(myIntent);
    }
}