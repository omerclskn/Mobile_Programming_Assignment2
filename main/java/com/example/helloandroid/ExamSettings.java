package com.example.helloandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExamSettings extends AppCompatActivity {

    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    EditText exam_duration;
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    EditText question_score;
    Spinner exam_diff;
    String[] answers = {"Select","2","3","4","5"};
    ArrayAdapter<String> AdapterAnswers;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_settings);

        defineVariables();
        getSharedPreferences();
        setListeners();
    }

    private void setListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exam_diff.getSelectedItemPosition() != 0){
                    editor.putString("duration", exam_duration.getText().toString());
                    editor.putString("score", question_score.getText().toString());
                    editor.putString("diff", exam_diff.getSelectedItem().toString());

                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Settings Saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText( getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getSharedPreferences() {
        context = getApplicationContext();
        pref = context.getSharedPreferences(this.getPackageName(), context.MODE_PRIVATE);
        editor = pref.edit();

        String duration = pref.getString("duration" , "0");
        String score = pref.getString("score" , "0");
        String diff = pref.getString("diff" , "0");

        exam_duration.setText(duration);
        question_score.setText(score);
        int index = Integer.valueOf(diff) - 1;
        exam_diff.setSelection(index);

    }

    private void defineVariables() {
        exam_duration = findViewById(R.id.exam_duration);
        question_score = findViewById(R.id.question_score);
        exam_diff = findViewById(R.id.exam_diff);
        button = findViewById(R.id.button);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);

        AdapterAnswers = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, answers);
        AdapterAnswers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exam_diff.setAdapter(AdapterAnswers);
    }
}
