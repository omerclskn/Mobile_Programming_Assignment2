package com.example.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private final AppCompatActivity activity = LoginActivity.this;

    EditText email ;
    EditText password ;
    TextView textReport;
    Button button_sign;
    Integer attempt;
    CheckBox showpass;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        defineVariables();
        defineListeners();
        showPass();
    }

    private void clean(){
        email.setText("");
        password.setText("");
        textReport.setText("");
    }

    public void defineVariables(){
        dbhelper = new DatabaseHelper(activity);
        attempt = 0;
        showpass = (CheckBox) findViewById(R.id.checkBox);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        button_sign = (Button) findViewById(R.id.button_sign);
        textReport = (TextView) findViewById(R.id.textReport);
    }

    public void defineListeners(){
        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyFromSQLite();
            }
        });
    }

    private void verifyFromSQLite() {
        if (dbhelper.checkUser(email.getText().toString().trim(), password.getText().toString().trim())) {
            Intent intent = new Intent(LoginActivity.this, UserAccount.class);
            intent.putExtra("email", email.getText().toString());
            startActivity(intent);
        } else {
            clean();
            attempt += 1;
            textReport.setText("Error ! You have "+ (3-attempt) + " attemtps left");
            if(attempt >= 3){
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
            //Toast.makeText( getApplicationContext(), "Error ! Please check credentials ", Toast.LENGTH_SHORT).show();
        }
    }


    public void showPass(){
        showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    password.setTransformationMethod(null);
                }
                else{
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
    }
}
