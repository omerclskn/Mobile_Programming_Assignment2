package com.example.helloandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ExamCreate extends AppCompatActivity {

    DatabaseHelper qdatab;
    RecyclerView.LayoutManager layoutManager;
    List<Question> questions;
    RecyclerView recyclerView;
    ExamAdapter examAdapter;
    String currentMail;

    List<Integer> checkedlistsget;
    List<Integer> checkedlist;
    List<Integer> addedQuestions;
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
    String[] answers = {"Select", "2", "3", "4", "5"};
    ArrayAdapter<String> AdapterAnswers;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_create);
        defineVariables();
        listeners();
    }

    public int findIndexOfq_id(int q_id) {
        int position = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getQid() == q_id) {
                position = i;
            }
        }
        return position;
    }

    public String findCorrectAnswer(int q_id) {
        int position = findIndexOfq_id(q_id);

        if (questions.get(position).getCorrect_answer() == 1)
            return questions.get(position).getAnswer1();
        if (questions.get(position).getCorrect_answer() == 2)
            return questions.get(position).getAnswer2();
        if (questions.get(position).getCorrect_answer() == 3)
            return questions.get(position).getAnswer3();
        if (questions.get(position).getCorrect_answer() == 4)
            return questions.get(position).getAnswer4();
        return questions.get(position).getAnswer5();
    }

    public String addQuestion(List<Integer> addedQuestions, int index ) {
        index = findIndexOfq_id(index);
        Random r = new Random();
        int question_random;

        do {
         question_random = r.nextInt(5) + 1;
        }while (addedQuestions.contains(question_random));

        addedQuestions.add(question_random);

        if (question_random == 1)
            return questions.get(index).getAnswer1();
        if (question_random == 2)
            return questions.get(index).getAnswer2();
        if (question_random == 3)
            return questions.get(index).getAnswer3();
        if (question_random == 4)
            return questions.get(index).getAnswer4();
        return questions.get(index).getAnswer5();
    }

    private void listeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedQuestions.clear();
                if (checkedlistsget != null) {
                    int diff = exam_diff.getSelectedItemPosition() + 1;
                    String data = "Exam Duration: " + exam_duration.getText().toString() + "\n" + "Question Score: " + question_score.getText().toString() + "\n" + "Exam Difficulty: " + diff + "\n\n";
                    for (int index : checkedlistsget) {
                        for (Question q_id : questions) {
                            if (q_id.getQid() == index) {
                                data += "Question " + q_id.getQid() + ") " + q_id.getQuestion() + "\n";

                                addedQuestions.add(q_id.getCorrect_answer());
                                String correct_answer = findCorrectAnswer(index);
                                data += "1) " + correct_answer + "\n";
                                switch (diff) {
                                    case 2:
                                        data += "2) " + addQuestion(addedQuestions, index) + "\n";
                                        break;
                                    case 3:
                                        data += "2) " + addQuestion(addedQuestions, index) + "\n";
                                        data += "3) " + addQuestion(addedQuestions, index) + "\n";
                                        break;
                                    case 4:
                                        data += "2) " + addQuestion(addedQuestions, index) + "\n";
                                        data += "3) " + addQuestion(addedQuestions, index) + "\n";
                                        data += "4) " + addQuestion(addedQuestions, index) + "\n";
                                        break;
                                    case 5:
                                        data += "2) " + addQuestion(addedQuestions, index) + "\n";
                                        data += "3) " + addQuestion(addedQuestions, index) + "\n";
                                        data += "4) " + addQuestion(addedQuestions, index) + "\n";
                                        data += "5) " + addQuestion(addedQuestions, index) + "\n";
                                        break;
                                }
                                data += "Correct Answer is: " + correct_answer + "\n\n";
                            }
                            addedQuestions.clear();
                        }
                    }
                    //System.out.println(data);
                    FileOutputStream fos = null;

                    try {
                        fos = openFileOutput("exam.txt", MODE_PRIVATE);
                        fos.write(data.getBytes());

                        Toast.makeText(getApplicationContext(), "Saved Success", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(getFilesDir().toString());
                    File examFile = new File( getFilesDir(), "exam.txt" );

                    Uri content = FileProvider.getUriForFile(context.getApplicationContext(),context.getApplicationContext().getPackageName(),examFile);
                    getApplicationContext().grantUriPermission(context.getApplicationContext().getPackageName(),content, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    Intent share = new Intent( Intent.ACTION_SEND );
                    share.setType( "text/plain" );
                    share.putExtra(Intent.EXTRA_STREAM, content);
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    startActivity( Intent.createChooser( share, "Send Exam" ) );
                }
            }
        });
    }

    public void defineVariables() {

        exam_duration = findViewById(R.id.exam_duration);
        question_score = findViewById(R.id.question_score);
        exam_diff = findViewById(R.id.exam_diff);
        button = findViewById(R.id.button);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);

        currentMail = getIntent().getStringExtra("email");

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        checkedlist = new ArrayList<>();
        questions = new ArrayList<>();
        addedQuestions = new ArrayList<>();
        qdatab = new DatabaseHelper(ExamCreate.this);
        examAdapter = new ExamAdapter(checkedlist, questions);
        checkedlistsget = examAdapter.getChecks();

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(examAdapter);

        AdapterAnswers = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, answers);
        AdapterAnswers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exam_diff.setAdapter(AdapterAnswers);

        getDataFromSQLite(currentMail);
        getSharedPreferences();
    }

    private void getSharedPreferences() {
        context = getApplicationContext();
        pref = context.getSharedPreferences(this.getPackageName(), context.MODE_PRIVATE);
        editor = pref.edit();

        String duration = pref.getString("duration", "0");
        String score = pref.getString("score", "0");
        String diff = pref.getString("diff", "0");

        exam_duration.setText(duration);
        question_score.setText(score);
        int index = Integer.valueOf(diff) - 1;
        exam_diff.setSelection(index);
    }

    private void getDataFromSQLite(String currentMail) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                questions.clear();
                questions.addAll(qdatab.getAllQuestions(currentMail));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                examAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
