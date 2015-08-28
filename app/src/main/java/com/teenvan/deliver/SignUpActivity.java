package com.teenvan.deliver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {

    // Declaration of member variables
    private MaterialEditText mRollNumber , mPassword , mRePassword ,
                             mEmail,mName;
    private Button mLoginButton , mSignUpButton;
    private TextView mAlreadyUser;
    private final String TAG = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Referencing the UI elements
        mRollNumber = (MaterialEditText)findViewById(R.id.signUpRollNumberEdit);
        mPassword = (MaterialEditText)findViewById(R.id.signUpPasswordEdit);
        mRePassword = (MaterialEditText)findViewById(R.id.reenterPassEdit);
        mEmail = (MaterialEditText)findViewById(R.id.emailEdit);
        mName = (MaterialEditText)findViewById(R.id.nameEdit);
        mLoginButton = (Button)findViewById(R.id.loginDirectButton);
        mSignUpButton = (Button)findViewById(R.id.signUpActivityButton);
        mAlreadyUser = (TextView)findViewById(R.id.alreadyUserText);

        // Already a user
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the login Activity
                Intent intent = new Intent(SignUpActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        // Signing Up the user

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the relevant inputs
                String rollNumber = mRollNumber.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String reenterpass = mRePassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String name = mName.getText().toString();

                // Check if all values are present or not
                if(rollNumber.isEmpty() || password.isEmpty() ||
                        reenterpass.isEmpty() || email.isEmpty() ||
                        name.isEmpty()){
                    // Show a snackbar
                    SnackBar bar = new SnackBar(SignUpActivity.this,
                            "Please enter all details");
                    bar.show();
                }else{
                    if(!password.equals(reenterpass)){
                        Toast.makeText(SignUpActivity.this,"Re-enter the same password"
                        ,Toast.LENGTH_SHORT).show();
                    }else{
                       // Sign Up the user in background
                        ParseUser user = new ParseUser();
                        user.setUsername(rollNumber);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.put("Name", name);
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if( e== null){
                                    // Successfully signed Up
                                    Intent intent = new Intent(SignUpActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    // Failure
                                    Log.e(TAG,"Failure Signing Up",e);
                                    SnackBar bar = new SnackBar(SignUpActivity.this,
                                            "There was some problem signing you up");
                                    bar.show();
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
