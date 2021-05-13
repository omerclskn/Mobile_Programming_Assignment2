package com.example.helloandroid;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoViewer extends AppCompatActivity {

    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introvid);
        defineVariable();
    }

    private void defineVariable() {
        video = (VideoView) findViewById(R.id.video);
        String media_path = getIntent().getStringExtra("media_path");
        video.setVideoPath(media_path);
        MediaController mediaController = new MediaController(this);
        video.setMediaController(mediaController);
        video.start();
    }
}
