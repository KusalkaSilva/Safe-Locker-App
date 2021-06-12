package com.app.appvault;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.appvault.util.HideFile;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class FaudActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, MyRecyclerViewAdapter.ItemLongClickListener {
    MyRecyclerViewAdapter adapter;
    ArrayList<File> selectedImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faud);
        loadImages();
    }

    private void loadImages() {
        Iterator<File> allFaudFiles = HideFile.getAllFaudFiles();
        ArrayList<File> files = new ArrayList<>();
        while (allFaudFiles.hasNext()) {
            File fileNeed = allFaudFiles.next();
            files.add(fileNeed);

        }

        RecyclerView recyclerView = findViewById(R.id.recycler_faud);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, files);
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        File item = adapter.getItem(position);
        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.putExtra("image", item.getAbsolutePath());
        startActivity(intent);
    }


    @Override
    public void onItemLongClick(View view, int position) {
        ImageView imgSelect = view.findViewById(R.id.imgSelect);
        if (imgSelect.getVisibility() == View.GONE) {
            selectedImages.add(adapter.getItem(position));
            imgSelect.setVisibility(View.VISIBLE);
        } else {
            imgSelect.setVisibility(View.GONE);
            selectedImages.remove(adapter.getItem(position));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            for (File f : selectedImages) {
                f.delete();
            }
            loadImages();
            Toast.makeText(this, "Images Deleted", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
