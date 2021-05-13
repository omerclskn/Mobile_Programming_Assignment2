package com.example.helloandroid;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddQuestion extends AppCompatActivity{

    TextView title;
    EditText question;
    EditText answer1;
    EditText answer2;
    EditText answer3;
    EditText answer4;
    EditText answer5;
    Button button1;
    Button button_media;
    Spinner spinner;
    Button add_photo;
    String[] answers = {"Select","1","2","3","4","5"};
    ArrayAdapter<String> AdapterAnswers;
    String currentMail;
    DatabaseHelper qdatab;
    Question questions;
    String media_path;
    byte[] byteArray;
    final int CHOOSE_IMAGE_REQ_CODE = 1;
    final int CHOOSE_MEDIA_REQ_CODE = 2;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        if (getIntent().getStringExtra("email")!=null)   currentMail = getIntent().getStringExtra("email");
        defineVariables();
        defineListeners();
        Check_update();
    }

    public void Check_update(){
        id = getIntent().getIntExtra("quest_id", -1);
        if (id != -1){
            List<Question> oneQuestion = qdatab.getOneQuestion( Integer.toString(id) );
            question.setText(oneQuestion.get(0).getQuestion());
            answer1.setText(oneQuestion.get(0).getAnswer1());
            answer2.setText(oneQuestion.get(0).getAnswer2());
            answer3.setText(oneQuestion.get(0).getAnswer3());
            answer4.setText(oneQuestion.get(0).getAnswer4());
            answer5.setText(oneQuestion.get(0).getAnswer5());
            int selected = oneQuestion.get(0).getCorrect_answer();
            spinner.setSelection(selected);
            byteArray = oneQuestion.get(0).getQuestion_uri();
            media_path = oneQuestion.get(0).getMedia_path();


            button1.setText("Update Question");

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NullControl()) {
                        qdatab.updateQuestion(id, question.getText().toString(), answer1.getText().toString(), answer2.getText().toString(), answer3.getText().toString(), answer4.getText().toString(), answer5.getText().toString(), spinner.getSelectedItemPosition(), byteArray, media_path);
                        Toast.makeText(getApplicationContext(), "Question Update Success ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(v.getContext(), ShowQuestions.class);
                        intent.putExtra("email", oneQuestion.get(0).getEmail());
                        v.getContext().startActivity(intent);
                    }
                    else {
                        Toast.makeText( getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
        if(requestCode == CHOOSE_MEDIA_REQ_CODE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            media_path = getPathFromUri(getApplicationContext(),uri);
            System.out.println(media_path);
        }
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void defineVariables(){
        qdatab = new DatabaseHelper(AddQuestion.this);
        questions = new Question();
        title = (TextView) findViewById(R.id.title);
        question = (EditText) findViewById(R.id.textquestion);
        answer1 = (EditText) findViewById(R.id.textanswer1);
        answer2 = (EditText) findViewById(R.id.textanswer2);
        answer3 = (EditText) findViewById(R.id.textanswer3);
        answer4 = (EditText) findViewById(R.id.textanswer4);
        answer5 = (EditText) findViewById(R.id.textanswer5);
        button1 = (Button) findViewById(R.id.button1);
        add_photo = (Button) findViewById(R.id.add_photo);
        button_media = (Button) findViewById(R.id.button_media);

        spinner = (Spinner) findViewById(R.id.spinner);
        AdapterAnswers = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, answers);
        AdapterAnswers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(AdapterAnswers);
    }

    private void clean(){
        question.setText("");
        answer1.setText("");
        answer2.setText("");
        answer3.setText("");
        answer4.setText("");
        answer5.setText("");
        spinner.setSelection(0);
    }

    private boolean NullControl(){
        if (!question.getText().toString().equals("") && !answer1.getText().toString().equals("") && !answer2.getText().toString().equals("") &&
                !answer3.getText().toString().equals("") && !answer4.getText().toString().equals("") && !answer5.getText().toString().equals("") && spinner.getSelectedItemPosition() != 0){
            return true;
        }
        return false;
    }

    public void defineListeners() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToSQLite();
            }
        });
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CHOOSE_IMAGE_REQ_CODE);
            }
        });
        button_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                String[] mimetypes = {"audio/*", "video/*"};
                i.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(i, CHOOSE_MEDIA_REQ_CODE);
            }
        });
    }

    private void postDataToSQLite() {

        if (NullControl()) {
            questions.setEmail(currentMail.trim());
            questions.setQuestion(question.getText().toString().trim());
            questions.setAnswer1(answer1.getText().toString().trim());
            questions.setAnswer2(answer2.getText().toString().trim());
            questions.setAnswer3(answer3.getText().toString().trim());
            questions.setAnswer4(answer4.getText().toString().trim());
            questions.setAnswer5(answer5.getText().toString().trim());
            questions.setCorrect_answer(spinner.getSelectedItemPosition());
            questions.setQuestion_uri(byteArray);
            questions.setMedia_path(media_path);
            qdatab.addQuestion(questions);

            Toast.makeText(getApplicationContext(), "Question Success ", Toast.LENGTH_SHORT).show();
            clean();
        }
        else {
            Toast.makeText( getApplicationContext(), "Error ! Please check the form ", Toast.LENGTH_SHORT).show();
        }
    }

}
