package com.app.appvault;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.appvault.util.HideFile;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ImageTrashActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    MyRecyclerViewAdapter adapter;
    ArrayList<File> selectedImages = new ArrayList<>();
    Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_trash);
        storage = new Storage(getApplicationContext());
        loadImages();
    }

    private void loadImages() {
        Iterator<File> allHiddenFiles = HideFile.getAllTrash();
        ArrayList<File> files = new ArrayList<>();
        while (allHiddenFiles.hasNext()) {
            File fileNeed = allHiddenFiles.next();
            if (HideFile.isImage(fileNeed)) {
                files.add(fileNeed);
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_trash_image);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, files);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        ImageView imgSelect = view.findViewById(R.id.imgSelect);
        if (imgSelect.getVisibility() == View.GONE) {
            selectedImages.add(adapter.getItem(position));
            imgSelect.setVisibility(View.VISIBLE);
        } else {
            imgSelect.setVisibility(View.GONE);
            selectedImages.remove(adapter.getItem(position));
        }
    }


    public void renameFile(File from, File to, int type) {
        if (storage.rename(from.getAbsolutePath(), to.getAbsolutePath())) {
            System.out.println("PPPPPPPPPPPPP : true");
            ContentResolver resolver = this.getContentResolver();
            if (type == 0) {
                resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{from.getAbsolutePath()});
            } else if (type == 1) {
                resolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.DATA + "=?", new String[]{from.getAbsolutePath()});
            }

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(to));
            this.sendBroadcast(intent);
        } else {

        }
        loadImages();
    }

    public void recoverOnClicked(View view) {
        for (File f : selectedImages) {
            renameFile(f, HideFile.restoreDelete(f), 0);
        }
        loadImages();
        Toast.makeText(this, "Images Recovered", Toast.LENGTH_LONG).show();
    }

    public void deleteOnClicked(View view) {
        for (File f : selectedImages) {
            f.delete();
        }
        loadImages();
        Toast.makeText(this, "Images Deleted", Toast.LENGTH_LONG).show();
    }
}
