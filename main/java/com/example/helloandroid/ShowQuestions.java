package com.example.helloandroid;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ShowQuestions extends AppCompatActivity {

    DatabaseHelper qdatab;
    RecyclerView.LayoutManager layoutManager;
    List<Question> questions;
    RecyclerView recyclerView;
    QuestionAdapter questionAdapter;
    String currentMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        defineVariables();
    }

    public void defineVariables(){
        currentMail = getIntent().getStringExtra("email");

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        questions = new ArrayList<>();
        qdatab = new DatabaseHelper(ShowQuestions.this);
        questionAdapter = new QuestionAdapter(questions);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(questionAdapter);

        getDataFromSQLite(currentMail);
    }



    private void getDataFromSQLite(String currentMail) {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
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
                questionAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
