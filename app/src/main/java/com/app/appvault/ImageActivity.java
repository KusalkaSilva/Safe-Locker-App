package com.app.appvault;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.appvault.util.DelayedProgressDialog;
import com.app.appvault.util.HideFile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.snatik.storage.Storage;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class ImageActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, MyRecyclerViewAdapter.ItemLongClickListener {
    ArrayList<File> selectedImages = new ArrayList<>();
    MyRecyclerViewAdapter adapter;
    Storage storage;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        storage = new Storage(getApplicationContext());
        loadImages();

    }


    private void loadImages() {
        Iterator<File> allHiddenFiles = HideFile.getAllHiddenFiles();
        ArrayList<File> files = new ArrayList<>();
        while (allHiddenFiles.hasNext()) {
            File fileNeed = allHiddenFiles.next();
            if (HideFile.isImage(fileNeed)) {
                files.add(fileNeed);
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_image);
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

    public void addImageOnClicked(View view) {
        pickImagesFromGallery();
    }

    public void restoreOnClicked(View view) {
        if (selectedImages.isEmpty()) {
            Toast.makeText(this, "No Images selected to export", Toast.LENGTH_LONG).show();
        }
        for (File f : selectedImages) {
            File file = HideFile.restoreHideFile(f);

            if (HideFile.isImage(file)) {
                renameFile(f, file, 0);
            }
            if (HideFile.isVideo(file)) {
                renameFile(f, file, 1);
            }
            Toast.makeText(this, "All Items exported", Toast.LENGTH_LONG).show();
//            String mimeType = URLConnection.guessContentTypeFromName(file.getAbsolutePath());
//            if (mimeType != null) {
//                if (mimeType.startsWith("image")) {
//                    renameFile(f, file, 0);
//                }
//                if (mimeType.startsWith("video")) {
//                    renameFile(f, file, 1);
//                }
//            }
        }
        selectedImages.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 15:
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();
                        File file = new File(imgDecodableString);
                        File to = HideFile.getNewNameToHide(file);
                        renameFile(file, to, 0);
                        Toast.makeText(this, "Image Added", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
//                case 16:
//                    try {
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = {MediaStore.Video.Media.DATA};
//                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                        cursor.moveToFirst();
//                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//                        String imgDecodableString = cursor.getString(columnIndex);
//                        cursor.close();
//                        File file = new File(imgDecodableString);
//                        File to = HideFile.getNewNameToHide(file);
//                        renameFile(file, to, 1);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
            }
    }


    private void pickImagesFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, 15);
    }

//
//    private void pickVideosFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("video/*");
////        String[] mimeTypes = {"image/jpeg", "image/png"};
////        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        startActivityForResult(intent, 16);
//    }


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


    public void backupOnClicked(View view) {
        final DelayedProgressDialog progressDialog = new DelayedProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "tag");
        for (File f : selectedImages) {
            Uri file = Uri.fromFile(f);
            StorageReference riversRef = mStorageRef.child("images/" + HideFile.restoreHideFile(f).getName());

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ImageActivity.this, "All Selected Images Uploaded", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(ImageActivity.this, "Unable to Upload Images. Try Again.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
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
                renameFile(f, HideFile.getNewNameToTrash(f), 0);
            }
            loadImages();
            Toast.makeText(this, "Moved to trash", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void trashOnClicked(View view) {
        startActivity(new Intent(this, ImageTrashActivity.class));
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
}

