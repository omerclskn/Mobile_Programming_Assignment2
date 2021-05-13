package com.example.helloandroid;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class UserAccount extends AppCompatActivity implements View.OnClickListener{

    Button addQuestion;
    Button listQuestions;
    Button examSettings;
    Button setExam;
    String currentMail;
    ImageView image;
    TextView text_welcome;
    TextView text_email;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        defineVariables();
        defineListeners();
        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
    }

    private void getUserFromSqlite(String currentMail) {
        List<Person> user = dbhelper.getOnePerson(currentMail);
        byte[] imageArray = user.get(0).getImage_uri();
        Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0 ,imageArray.length);

        image.setImageBitmap(bm);

        text_email.setText(user.get(0).getEmail());
        text_welcome.setText("Welcome "+user.get(0).getName() + " " + user.get(0).getSurname());
    }

    public void defineVariables(){
        currentMail = getIntent().getStringExtra("email");
        dbhelper = new DatabaseHelper(UserAccount.this);
        addQuestion = (Button) findViewById(R.id.questions);
        listQuestions = (Button) findViewById(R.id.listquestions);
        examSettings = (Button) findViewById(R.id.examsettings);
        setExam = (Button) findViewById(R.id.setexam);
        image = (ImageView) findViewById(R.id.personImage);
        text_welcome = (TextView) findViewById(R.id.text_welcome);
        text_email = (TextView) findViewById(R.id.text_email);
        getUserFromSqlite(currentMail);
    }
    public void defineListeners(){
        addQuestion.setOnClickListener(this);
        listQuestions.setOnClickListener(this);
        examSettings.setOnClickListener(this);
        setExam.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.questions:
                intent = new Intent(v.getContext(),AddQuestion.class);
                intent.putExtra("email", currentMail);
                startActivity(intent);
                break;

            case R.id.listquestions:
                intent = new Intent(v.getContext(),ShowQuestions.class);
                intent.putExtra("email", currentMail);
                startActivity(intent);
                break;

            case R.id.examsettings:
                intent = new Intent(v.getContext(), ExamSettings.class);
                startActivity(intent);
                break;
            case R.id.setexam:
                intent = new Intent(v.getContext(), ExamCreate.class);
                intent.putExtra("email", currentMail);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            default:
                break;

        }
    }
}
