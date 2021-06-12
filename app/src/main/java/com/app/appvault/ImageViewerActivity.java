package com.app.appvault;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        String image = getIntent().getStringExtra("image");

        File file=new File(image);
        Glide.with(this).load(file).into((ImageView) findViewById(R.id.imgViewr));
    }
}
