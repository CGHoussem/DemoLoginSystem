package com.payndev.demologinsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private TextView txt_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        txt_welcome = (TextView) findViewById(R.id.txt_welcome);
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        txt_welcome.setText("Welcome " + username);
    }
}
