package com.payndev.demologinsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase usersDB = null;
    private EditText txt_username, txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);

        try {
            usersDB = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            usersDB.execSQL("CREATE TABLE IF NOT EXISTS users (id integer primary key, username VARCHAR, firstname VARCHAR, lastname VARCHAR, password VARCHAR);");

            File database = getApplicationContext().getDatabasePath("Users.db");
        }
        catch (Exception e)
        {
            Log.e("USERS ERROR", "Error Creating Database");
        }

    }

    public void btn_sign_in_click(View view) {
        String txt_username_value = txt_username.getText().toString();
        String txt_password_value = txt_password.getText().toString();

        if (txt_username_value.length() == 0)
        {
            txt_username.setError("Username is required!");
            return;
        }
        if (txt_username_value.length() < 6)
        {
            txt_username.setError("Username must have at least 6 characters");
            return;
        }

        Cursor cursor = usersDB.rawQuery("SELECT * FROM users WHERE username='"+txt_username_value+"';", null);
        int passwordColumn = cursor.getColumnIndex("password");
        cursor.moveToFirst();
        if (cursor == null || cursor.getCount() <= 0)
        {
            txt_username.setError("Username Not Found!");
            return;
        }

        if (txt_password_value.length() == 0)
        {
            txt_password.setError("Password is required!");
            return;
        }
        if (txt_password_value.length() < 8)
        {
            txt_password.setError("Password must have at least 8 characters");
            return;
        }

        String password = cursor.getString(passwordColumn);
        if (!(password.equals(txt_password_value)))
        {
            txt_password.setError("Wrong Password !");
            return;
        }

    }

    public void txt_goto_sign_up_activity_click(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        usersDB.close();

        super.onDestroy();
    }
}
