package com.teenvan.deliver;

import android.content.Intent;
import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class SplashScreenActivity extends AppCompatActivity {
    // Declaration of member variables
    private TextView mDeliverText;
    private Button mSignUpButton , mLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Hide the action bar


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Referencing the UI elements
        mDeliverText = (TextView)findViewById(R.id.deliverText);
        mSignUpButton = (Button)findViewById(R.id.signUpButton);
        mLoginButton = (Button)findViewById(R.id.loginButton);

        // Code for button event listeners
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the sign up activity
                Intent intent = new Intent(SplashScreenActivity.this,
                        SignUpActivity.class);
                startActivity(intent);
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the login activity
                Intent intent = new Intent(SplashScreenActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        ParseUser user = ParseUser.getCurrentUser();
        if( user != null){
            // User already logged in
            Intent intent = new Intent(SplashScreenActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }

    }

}
