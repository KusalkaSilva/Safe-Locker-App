package com.app.appvault;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

public class VideoViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewer);
        String video = getIntent().getStringExtra("video");

        File file=new File(video);
        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.fromFile(file);
        videoview.setVideoURI(uri);
        videoview.start();
    }
}
