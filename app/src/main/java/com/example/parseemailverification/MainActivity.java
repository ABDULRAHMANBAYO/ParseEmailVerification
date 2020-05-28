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

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditTextView, passwordEditTextView, emailEditTextView;
    private Button signUpButton;
    private String username;
    private String password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditTextView = findViewById(R.id.username_editText);
        passwordEditTextView = findViewById(R.id.password_EditText);
        emailEditTextView = findViewById(R.id.email_EditText);
        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

    public void signUpPressed(View v) {

        try {
            if (!TextUtils.isEmpty(usernameEditTextView.getText().toString().trim()) &&
                    !TextUtils.isEmpty(passwordEditTextView.getText().toString().trim()) &&
                    !TextUtils.isEmpty(emailEditTextView.getText().toString().trim())) {
                username = usernameEditTextView.getText().toString().trim();
                password = passwordEditTextView.getText().toString().trim();
                email = emailEditTextView.getText().toString().trim();
            }
            // Sign up with Parse
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ParseUser.logOut();
                        alertDisplayer("Account Created Successfully!", "Please verify your email before Login", false);
                    } else {
                        ParseUser.logOut();
                        alertDisplayer("Error Account Creation failed", "Account could not be created" + " :" + e.getMessage(), true);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void alertDisplayer(String title,String message, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!error) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
