package com.example.parseemailverification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameTextView, passwordTextView;
    private Button loginButton;
    private String userName, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameTextView = findViewById(R.id.emailLogin);
        passwordTextView = findViewById(R.id.passwordLogin);

    }

    public void loginButtonPressed(View v) {
        if (!TextUtils.isEmpty(userNameTextView.getText().toString().trim()) &&
                !TextUtils.isEmpty(passwordTextView.getText().toString().trim())
        ) {
            userName = userNameTextView.getText().toString().trim();
            password = passwordTextView.getText().toString().trim();

        }

// Login with Parse
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    if (parseUser.getBoolean("emailVerified")) {
                        alertDisplayer("Login Sucessful", "Welcome, " + userName + "!", false);
                    } else {
                        ParseUser.logOut();
                        alertDisplayer("Login Fail", "Please Verify Your Email first", true);
                    }
                } else {
                    ParseUser.logOut();
                    alertDisplayer("Login Fail", e.getMessage() + " Please re-try", true);
                }
            }
        });
    }

    private void alertDisplayer(String title, String message, final boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (!error) {
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
