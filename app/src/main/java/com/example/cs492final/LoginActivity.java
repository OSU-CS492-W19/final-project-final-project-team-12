package com.example.cs492final;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginET;
    private EditText mPasswordET;
    private Button mLoginButton;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login);

        mLoginET = findViewById(R.id.et_username);
        mPasswordET = findViewById(R.id.et_password);
        //Hide the password from outsiders.
        mPasswordET.setTransformationMethod(new PasswordTransformationMethod());
        mLoginButton = findViewById(R.id.b_login);

        //Create our onClickListener
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Store username and password
                String username = mLoginET.getText().toString();
                String password = mPasswordET.getText().toString();

                //Send to get authorized by Slack API.
            }
        });

    }

}
