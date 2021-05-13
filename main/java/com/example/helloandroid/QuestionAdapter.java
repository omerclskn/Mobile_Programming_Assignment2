package com.example.helloandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {
    List<Question> questions;

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

    public QuestionAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text_question.setText(questions.get(position).getQuestion());
        holder.text_answer1.setText("1) "+questions.get(position).getAnswer1());
        holder.text_answer2.setText("2) "+questions.get(position).getAnswer2());
        holder.text_answer3.setText("3) "+questions.get(position).getAnswer3());
        holder.text_answer4.setText("4) "+questions.get(position).getAnswer4());
        holder.text_answer5.setText("5) "+questions.get(position).getAnswer5());
        String CorrectAnswer = findCorrectAnswer(questions.get(position).getCorrect_answer(), position);
        holder.correct_answer.setText("Correct Answer: " + questions.get(position).getCorrect_answer() + ") " + CorrectAnswer);

        if (questions.get(position).getQuestion_uri()!=null){
            byte[] imageArray = questions.get(position).getQuestion_uri();
            Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0 ,imageArray.length);

            holder.photo.setImageBitmap(bm);
        }

        String media_path = questions.get(position).getMedia_path();

        if (questions.get(position).getMedia_path() == null){
            holder.media.setEnabled(false);
        }


        holder.delete.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alert = new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Question")
                        .setMessage("Are you sure want to delete this question?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
                                int id = questions.get(position).getQid();
                                databaseHelper.deleteQuestion(id);
                                questions.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(v.getContext(),"Question Successfully Deleted",Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        holder.update.setOnClickListener(new AdapterView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddQuestion.class);
                intent.putExtra("quest_id", questions.get(position).getQid());
                v.getContext().startActivity(intent);
            }
        });

        holder.media.setOnClickListener(new AdapterView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VideoViewer.class);
                intent.putExtra("media_path", media_path);
                v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_question;
        TextView text_answer1;
        TextView text_answer2;
        TextView text_answer3;
        TextView text_answer4;
        TextView text_answer5;
        TextView correct_answer;
        Button delete;
        Button update;
        ImageView photo;
        Button media;

        public MyViewHolder(View itemView){
            super(itemView);
            text_question = itemView.findViewById(R.id.text_question);
            text_answer1 = itemView.findViewById(R.id.text_answer1);
            text_answer2 = itemView.findViewById(R.id.text_answer2);
            text_answer3 = itemView.findViewById(R.id.text_answer3);
            text_answer4 = itemView.findViewById(R.id.text_answer4);
            text_answer5 = itemView.findViewById(R.id.text_answer5);
            correct_answer = itemView.findViewById(R.id.correct_answer);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
            photo = itemView.findViewById(R.id.photo);
            media = itemView.findViewById(R.id.button_media);
        }

    }
}
