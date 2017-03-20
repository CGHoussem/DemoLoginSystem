package com.payndev.demologinsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class SignUpActivity extends AppCompatActivity {

    SQLiteDatabase usersDB = null;

    private EditText txt_username, txt_firstname, txt_lastname, txt_password, txt_password_confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_firstname = (EditText) findViewById(R.id.txt_firstname);
        txt_lastname = (EditText) findViewById(R.id.txt_lastname);
        txt_password = (EditText) findViewById(R.id.txt_password);
        txt_password_confirmation = (EditText) findViewById(R.id.txt_password_confirmation);

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

    public void txt_sign_up_click(View view) {
        String txt_username_value = txt_username.getText().toString();
        String txt_password_value = txt_password.getText().toString();
        String txt_password_confirmation_value = txt_password_confirmation.getText().toString();
        String txt_firstname_value = txt_firstname.getText().toString();
        String txt_lastname_value = txt_lastname.getText().toString();

        // check if username doesn't exist in database
        Cursor cursor = usersDB.rawQuery("SELECT * FROM users WHERE username='"+txt_username_value+"';", null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0)
        {
            Toast.makeText(this, "cursor.getCount() = "+cursor.getCount(), Toast.LENGTH_LONG).show();
            txt_username.setError("Username already exists!");
            return;
        }

        if (txt_username_value.length() == 0)
        {
            txt_username.setError("Username is required!");
            return;
        }
        if (txt_username_value.length() < 6)
        {
            txt_username.setError("Username must have at least 6 characters!");
            return;
        }

        if (txt_password_value.length() == 0)
        {
            txt_password.setError("Password is required!");
            return;
        }
        if (txt_password_value.length() < 8)
        {
            txt_password.setError("Password must have at least 8 characters!");
            return;
        }
        if (!(txt_password_confirmation_value.equals(txt_password_value))){
            txt_password_confirmation.setError("The confirmation password is not similar to the password");
            return;
        }

        if (txt_firstname_value.length() == 0) {
            txt_firstname.setError("First Name is required!");
            return;
        }

        if (txt_lastname_value.length() == 0)
        {
            txt_lastname.setError("Last Name is required!");
            return;
        }

        usersDB.execSQL("INSERT INTO users (username, firstname, lastname, password) " +
                "VALUES('"+txt_username_value+"', '"+txt_firstname_value+"', '"+txt_lastname_value+"', '"+txt_password_value+"');");

        Toast.makeText(this, "User Created Successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("USERNAME", txt_username_value);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        usersDB.close();

        super.onDestroy();
    }

}
