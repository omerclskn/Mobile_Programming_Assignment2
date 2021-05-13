package com.example.helloandroid;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    private final AppCompatActivity activity = SignupActivity.this;
    EditText text_name;
    EditText text_surname;
    EditText text_email;
    EditText text_phone;
    EditText text_password;
    EditText text_retype;
    TextView message;
    Button button_signup;
    CheckBox showpass;
    DatabaseHelper dbhelper;
    Person person;
    Button button_photo;
    byte[] byteArray;
    final int CHOOSE_IMAGE_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        defineVariables();
        defineListeners();
        showPass();
    }

    private void clean() {
        text_name.setText("");
        text_surname.setText("");
        text_email.setText("");
        text_phone.setText("");
        text_password.setText("");
        text_retype.setText("");
    }

    private boolean checkPasswords() {
        if (text_password.getText().toString().equals(text_retype.getText().toString())) {
            return true;
        }
        return false;
    }

    public void defineVariables() {
        dbhelper = new DatabaseHelper(activity);
        person = new Person();
        text_name = (EditText) findViewById(R.id.text_name);
        text_surname = (EditText) findViewById(R.id.text_surname);
        text_email = (EditText) findViewById(R.id.text_email);
        text_phone = (EditText) findViewById(R.id.text_phone);
        text_password = (EditText) findViewById(R.id.text_password);
        text_retype = (EditText) findViewById(R.id.text_retype);
        message = (TextView) findViewById(R.id.message);
        button_signup = (Button) findViewById(R.id.button_signup);
        showpass = (CheckBox) findViewById(R.id.checkBox);
        button_photo = (Button) findViewById(R.id.button_photo);
    }

    private boolean NullControl() {
        if (!text_name.getText().toString().equals("") && !text_surname.getText().toString().equals("") && !text_email.getText().toString().equals("") &&
                !text_phone.getText().toString().equals("") && !text_password.getText().toString().equals("") && byteArray!=null ) {
            return true;
        }
        return false;
    }

    public void defineListeners() {
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToSQLite();
            }
        });
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CHOOSE_IMAGE_REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE_REQ_CODE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void postDataToSQLite() {

        if (NullControl() && checkPasswords()) {
            if (!dbhelper.checkUser(text_email.getText().toString().trim())) {
                person.setName(text_name.getText().toString().trim());
                person.setSurname(text_surname.getText().toString().trim());
                person.setPhone(text_phone.getText().toString().trim());
                person.setEmail(text_email.getText().toString().trim());
                person.setPassword(text_password.getText().toString().trim());
                person.setImage_uri(byteArray);
                dbhelper.addPerson(person);

                Toast.makeText(getApplicationContext(), "Signup Success ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                clean();
            } else {
                Toast.makeText(getApplicationContext(), "User Already Exist ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
        }
    }

    public void showPass() {
        showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    text_password.setTransformationMethod(null);
                    text_retype.setTransformationMethod(null);
                } else {
                    text_password.setTransformationMethod(new PasswordTransformationMethod());
                    text_retype.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
    }
}
