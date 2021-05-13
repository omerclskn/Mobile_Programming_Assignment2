package com.example.helloandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mydatabase";

    private String TableName = "users";
    private String ColumnID = "id";
    private String ColumnFirstName = "firstname";
    private String ColumnLastName = "lastname";
    private String ColumnEmail = "email";
    private String ColumnMobile = "mobile";
    private String ColumnPassword = "password";
    private String ColumnPhoto = "photouri";

    public String CreateQuery = "CREATE TABLE " + TableName + " ( " +
            ColumnID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ColumnFirstName + " TEXT, " +
            ColumnLastName + " TEXT, " +
            ColumnEmail + " TEXT, " +
            ColumnMobile + " TEXT, " +
            ColumnPassword + " TEXT, " +
            ColumnPhoto + " BLOB)";

    // ----------------------------------------

    private String TableNameq = "questions";
    private String Columnidq = "id";
    private String ColumnEmailq = "email";
    private String ColumnQuestion = "question";
    private String ColumnAnswer1 = "answer1";
    private String ColumnAnswer2 = "answer2";
    private String ColumnAnswer3 = "answer3";
    private String ColumnAnswer4 = "answer4";
    private String ColumnAnswer5 = "answer5";
    private String ColumnCorrectAnswer = "correct_answer";
    private String ColumnQuestionPhoto = "questphoto";
    private String ColumnQuestionMedia = "questmedia";

    public String CreateQueryq = "CREATE TABLE " + TableNameq + " ( " +
            Columnidq + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ColumnEmailq + " TEXT, " +
            ColumnQuestion + " TEXT, " +
            ColumnAnswer1 + " TEXT, " +
            ColumnAnswer2 + " TEXT, " +
            ColumnAnswer3 + " TEXT, " +
            ColumnAnswer4 + " TEXT, " +
            ColumnAnswer5 + " TEXT, " +
            ColumnCorrectAnswer + " INTEGER, " +
            ColumnQuestionPhoto + " BLOB, " +
            ColumnQuestionMedia + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateQuery);
        db.execSQL(CreateQueryq);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        db.execSQL("DROP TABLE IF EXISTS " + TableNameq);

        onCreate(db);
    }

    public void addPerson(Person person){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ColumnFirstName, person.getName());
        values.put(ColumnLastName, person.getSurname());
        values.put(ColumnEmail, person.getEmail());
        values.put(ColumnMobile, person.getPhone());
        values.put(ColumnPassword, person.getPassword());
        values.put(ColumnPhoto, person.getImage_uri());

        db.insert(TableName,null,values);
        db.close();
    }

    public List<Person> getAllUser(){
        String[] columns = {
                ColumnID,
                ColumnFirstName,
                ColumnLastName,
                ColumnEmail,
                ColumnMobile,
                ColumnPassword,
                ColumnPhoto
        };
        String sort = ColumnFirstName + " ASC";
        List<Person> personList = new ArrayList<Person>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TableName, columns,
                null,
                null,
                null,
                null,
                sort);

        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ColumnID))));
                person.setName(cursor.getString(cursor.getColumnIndex(ColumnFirstName)));
                person.setSurname(cursor.getString(cursor.getColumnIndex(ColumnLastName)));
                person.setEmail(cursor.getString(cursor.getColumnIndex(ColumnEmail)));
                person.setPhone(cursor.getString(cursor.getColumnIndex(ColumnMobile)));
                person.setPassword(cursor.getString(cursor.getColumnIndex(ColumnPassword)));
                person.setImage_uri(cursor.getBlob(cursor.getColumnIndex(ColumnPhoto)));

                personList.add(person);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return personList;
        }

    public List<Person> getOnePerson(String email){
        String[] columns = {
                ColumnID,
                ColumnFirstName,
                ColumnLastName,
                ColumnEmail,
                ColumnMobile,
                ColumnPassword,
                ColumnPhoto
        };

        List<Person> personList = new ArrayList<Person>();

        String selection = ColumnEmail + " = ?";
        String[] selectionArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TableName,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ColumnID))));
                person.setName(cursor.getString(cursor.getColumnIndex(ColumnFirstName)));
                person.setSurname(cursor.getString(cursor.getColumnIndex(ColumnLastName)));
                person.setEmail(cursor.getString(cursor.getColumnIndex(ColumnEmail)));
                person.setPhone(cursor.getString(cursor.getColumnIndex(ColumnMobile)));
                person.setPassword(cursor.getString(cursor.getColumnIndex(ColumnPassword)));
                person.setImage_uri(cursor.getBlob(cursor.getColumnIndex(ColumnPhoto)));

                personList.add(person);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return personList;
    }

    public boolean checkUser(String email) {

        String[] columns = {
                ColumnID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ColumnEmail + " = ?";

        String[] selectionArgs = {email};

        Cursor cursor = db.query(TableName,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkUser(String email, String password) {

        String[] columns = {
                ColumnID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ColumnEmail + " = ?" + " AND " + ColumnPassword + " = ?";

        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TableName,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public void addQuestion(Question question){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ColumnEmailq, question.getEmail());
        values.put(ColumnQuestion, question.getQuestion());
        values.put(ColumnAnswer1, question.getAnswer1());
        values.put(ColumnAnswer2, question.getAnswer2());
        values.put(ColumnAnswer3, question.getAnswer3());
        values.put(ColumnAnswer4, question.getAnswer4());
        values.put(ColumnAnswer5, question.getAnswer5());
        values.put(ColumnCorrectAnswer, question.getCorrect_answer());
        values.put(ColumnQuestionPhoto, question.getQuestion_uri());
        values.put(ColumnQuestionMedia, question.getMedia_path());

        db.insert(TableNameq,null,values);
        db.close();
    }

    public List<Question> getAllQuestions(String email){
        String[] columns = {
                Columnidq,
                ColumnEmailq,
                ColumnQuestion,
                ColumnAnswer1,
                ColumnAnswer2,
                ColumnAnswer3,
                ColumnAnswer4,
                ColumnAnswer5,
                ColumnCorrectAnswer,
                ColumnQuestionPhoto,
                ColumnQuestionMedia
        };

        List<Question> questionList = new ArrayList<Question>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ColumnEmailq + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TableNameq,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Columnidq))));
                question.setEmail(cursor.getString(cursor.getColumnIndex(ColumnEmailq)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(ColumnQuestion)));
                question.setAnswer1(cursor.getString(cursor.getColumnIndex(ColumnAnswer1)));
                question.setAnswer2(cursor.getString(cursor.getColumnIndex(ColumnAnswer2)));
                question.setAnswer3(cursor.getString(cursor.getColumnIndex(ColumnAnswer3)));
                question.setAnswer4(cursor.getString(cursor.getColumnIndex(ColumnAnswer4)));
                question.setAnswer5(cursor.getString(cursor.getColumnIndex(ColumnAnswer5)));
                question.setCorrect_answer( Integer.parseInt(cursor.getString(cursor.getColumnIndex(ColumnCorrectAnswer))));
                question.setQuestion_uri(cursor.getBlob(cursor.getColumnIndex(ColumnQuestionPhoto)));
                question.setMedia_path(cursor.getString(cursor.getColumnIndex(ColumnQuestionMedia)));

                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return questionList;
    }

    public List<Question> getOneQuestion(String quest_id){
        String[] columns = {
                Columnidq,
                ColumnEmailq,
                ColumnQuestion,
                ColumnAnswer1,
                ColumnAnswer2,
                ColumnAnswer3,
                ColumnAnswer4,
                ColumnAnswer5,
                ColumnCorrectAnswer,
                ColumnQuestionPhoto,
                ColumnQuestionMedia
        };

        List<Question> questionList = new ArrayList<Question>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = Columnidq + " = ?";
        String[] selectionArgs = {quest_id};

        Cursor cursor = db.query(TableNameq,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Columnidq))));
                question.setEmail(cursor.getString(cursor.getColumnIndex(ColumnEmailq)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(ColumnQuestion)));
                question.setAnswer1(cursor.getString(cursor.getColumnIndex(ColumnAnswer1)));
                question.setAnswer2(cursor.getString(cursor.getColumnIndex(ColumnAnswer2)));
                question.setAnswer3(cursor.getString(cursor.getColumnIndex(ColumnAnswer3)));
                question.setAnswer4(cursor.getString(cursor.getColumnIndex(ColumnAnswer4)));
                question.setAnswer5(cursor.getString(cursor.getColumnIndex(ColumnAnswer5)));
                question.setCorrect_answer( Integer.parseInt(cursor.getString(cursor.getColumnIndex(ColumnCorrectAnswer))));
                question.setQuestion_uri(cursor.getBlob(cursor.getColumnIndex(ColumnQuestionPhoto)));
                question.setMedia_path(cursor.getString(cursor.getColumnIndex(ColumnQuestionMedia)));

                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return questionList;
    }

    public boolean deleteQuestion(int question_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableNameq, Columnidq + "=" + question_id, null) > 0;
    }

    public void updateQuestion(int id, String question, String answer1,String answer2, String answer3 , String answer4, String answer5, int correctanswer, byte[] photo, String media) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Columnidq, id);
        values.put(ColumnQuestion, question);
        values.put(ColumnAnswer1, answer1);
        values.put(ColumnAnswer2, answer2);
        values.put(ColumnAnswer3, answer3);
        values.put(ColumnAnswer4, answer4);
        values.put(ColumnAnswer5, answer5);
        values.put(ColumnCorrectAnswer, correctanswer);
        values.put(ColumnQuestionPhoto, photo);
        values.put(ColumnQuestionMedia, media);

        db.update(TableNameq, values, Columnidq + " = ?",
                new String[] { String.valueOf(id) });
    }

    }

