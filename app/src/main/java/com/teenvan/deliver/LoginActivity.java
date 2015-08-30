package com.teenvan.deliver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {
    // Declaration of member variables
    private MaterialEditText mRollNumber , mPassWord;
    private Button mLoginActivityButton , mSignUpActivityButton;
    private TextView mNotAUser;
    private final String TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencing the UI elements
        mRollNumber = (MaterialEditText)findViewById(R.id.rollNumberEditText);
        mPassWord = (MaterialEditText)findViewById(R.id.passwordEditText);
        mLoginActivityButton = (Button)findViewById(R.id.loginActivityButton);
        mSignUpActivityButton = (Button)findViewById(R.id.signUpDirectButton);
        mNotAUser = (TextView)findViewById(R.id.notAUserText);

        // Button Touch event listeners
        mSignUpActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the SignUpActivity
                Intent intent = new Intent(LoginActivity.this,
                        SignUpActivity.class);
                startActivity(intent);
            }
        });
        mLoginActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the required inputs
                String rollNumber = mRollNumber.getText().toString().trim();
                String password = mPassWord.getText().toString().trim();
                ParseUser.logInInBackground(rollNumber, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if( e== null){
                            // Success Logging in
                            // Open the MainActivity
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        }else{
                            // Failure
                            Log.e(TAG, "Failed to login the user", e);
                            // Show a snackbar

                        }
                    }
                });
            }
        });

    }

}
