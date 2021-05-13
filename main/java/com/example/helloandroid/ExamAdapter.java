package com.example.helloandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.MyViewHolder> {

    List<Question> questions;
    List<Integer> checks;

    public String findCorrectAnswer(int answer, int position){

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

    public ExamAdapter(List<Integer> checks , List<Question> questions) {
        this.checks = checks;
        this.questions = questions;
    }

    @Override
    public ExamAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_questions_exam,parent,false);
        return new ExamAdapter.MyViewHolder(view);
    }

    public List<Integer> getChecks() {
        return checks;
    }

    @Override
    public void onBindViewHolder(@NonNull ExamAdapter.MyViewHolder holder, int position) {
        holder.text_question.setText(questions.get(position).getQid() + " - " +questions.get(position).getQuestion());
        holder.text_answer1.setText("1) "+questions.get(position).getAnswer1());
        holder.text_answer2.setText("2) "+questions.get(position).getAnswer2());
        holder.text_answer3.setText("3) "+questions.get(position).getAnswer3());
        holder.text_answer4.setText("4) "+questions.get(position).getAnswer4());
        holder.text_answer5.setText("5)"+questions.get(position).getAnswer5());
        String CorrectAnswer = findCorrectAnswer(questions.get(position).getCorrect_answer(), position);
        holder.correct_answer.setText("Correct Answer: " + questions.get(position).getCorrect_answer() + ") " + CorrectAnswer);

        if (questions.get(position).getQuestion_uri()!=null){
            byte[] imageArray = questions.get(position).getQuestion_uri();
            Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0 ,imageArray.length);

            holder.photo.setImageBitmap(bm);
        }

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checks.add(questions.get(position).getQid());
                }
                else{
                    for (int i = 0 ; i < checks.size(); i++){
                        if (checks.get(i) == questions.get(position).getQid()){
                            checks.remove(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView text_question;
        TextView text_answer1;
        TextView text_answer2;
        TextView text_answer3;
        TextView text_answer4;
        TextView text_answer5;
        TextView correct_answer;
        ImageView photo;

        public MyViewHolder(View itemView){
            super(itemView);
            text_question = itemView.findViewById(R.id.text_question);
            text_answer1 = itemView.findViewById(R.id.text_answer1);
            text_answer2 = itemView.findViewById(R.id.text_answer2);
            text_answer3 = itemView.findViewById(R.id.text_answer3);
            text_answer4 = itemView.findViewById(R.id.text_answer4);
            text_answer5 = itemView.findViewById(R.id.text_answer5);
            correct_answer = itemView.findViewById(R.id.correct_answer);
            checkbox = itemView.findViewById(R.id.checkbox);
            photo = itemView.findViewById(R.id.photo);
        }

    }
}
