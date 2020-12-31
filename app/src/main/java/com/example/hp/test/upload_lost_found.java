package com.example.hp.test;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class upload_lost_found extends AppCompatActivity {

    private Button mButtonUpload;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 90;
    private StorageTask mUploadTask;
    String mName,mImageUrl;
    private String filename;
    private Uri mImageUri;
    private String getImageUrl = "";
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Toolbar toolbar;
    String imageFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_lost_found);

        toolbar=findViewById(R.id.lost_found_include);
        toolbar.setTitle("  Upload Lost & Found");
        setSupportActionBar(toolbar);
        mButtonUpload = findViewById(R.id.insert);
        mEditTextFileName = findViewById(R.id.discription);
        mImageView = findViewById(R.id.image);
        mProgressBar = findViewById(R.id.progress_bar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        mStorageRef = FirebaseStorage.getInstance().getReference("lost");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("lost");
        if (savedInstanceState != null) {


            mImageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));

        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  select=new AlertDialog.Builder(upload_lost_found.this);
                select.setMessage("Select option To Complete The operation").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(ContextCompat.checkSelfPermission(upload_lost_found.this, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                          //  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                               // startActivityForResult(takePictureIntent, 0);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                Random random = new Random();
                                int key = random.nextInt(1000);
                                File photo = new File(Environment.getExternalStorageDirectory(), "picture" + key + ".jpg");
                                mImageUri = Uri.fromFile(photo);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                            }

                        }
                        else{ // asking for permission
                            ActivityCompat.requestPermissions(upload_lost_found.this,new String[]{Manifest.permission.CAMERA},9);
                        }


                    }
                }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, PICK_IMAGE_REQUEST);
                    }
                });
                AlertDialog alert=select.create();
                alert.setTitle("Note");
                alert.show();

            }
        });
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discription= mEditTextFileName.getText().toString();
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(upload_lost_found.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else if (!TextUtils.isEmpty(discription)) {
                    if (getImageUrl != null) {
                        uploadFile(mImageUri);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_LONG).show();
                    }
                  //  uploadFile();
                }
                else
                    {

                        Toast.makeText(upload_lost_found.this,"Please insert Discription",Toast.LENGTH_LONG).show();

                    }
            }
        });

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mImageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, mImageUri.toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(mImageUri);
            this.sendBroadcast(mediaScanIntent);
            try {
                com.example.hp.test.Scanner scanner = new com.example.hp.test.Scanner();
                final Bitmap bitmap = scanner.decodeBitmapUri(upload_lost_found.this, mImageUri);

                mImageView.setImageBitmap(bitmap);
                mImageUri = this.mImageUri;

                if (mImageUri.getLastPathSegment().indexOf(".") > 0)
//                    filename = mImageUri.getLastPathSegment().substring(0, mImageUri.getLastPathSegment().lastIndexOf("."));

                    Toast.makeText(getApplicationContext(), filename,Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
            }



        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
        }
        else {
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
        }
        switch (requestCode) {
            case 77:
                try {
                    //When image is captured successfully
                    if (resultCode == RESULT_OK) {

                        //Check if device SDK is greater than 22 then we get the actual image path via below method
                        if (Build.VERSION.SDK_INT > 22)
                            getImageUrl = ImagePath_MarshMallow.getPath(upload_lost_found.this, mImageUri);
                        else
                            //else we will get path directly
                            getImageUrl = mImageUri.getPath();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }
    }
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }
    private void uploadFile(Uri mImageUri) {

        if (mImageUri != null) { //(mImageUri)
            final StorageReference fileReference = mStorageRef.child("lost").child(mImageUri.getLastPathSegment().substring(mImageUri.getLastPathSegment().lastIndexOf("/") + 1));;

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                           mName=mEditTextFileName.getText().toString();
                           taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   mImageUrl=uri.toString();
                                   Upload upload = new Upload(mName,mImageUrl);
                                   String uploadId = mDatabaseRef.push().getKey();
                                   mDatabaseRef.child(uploadId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           mEditTextFileName.setText("");
                                           mImageView.setImageDrawable(null);
                                           Toast.makeText(upload_lost_found.this, "Upload successful", Toast.LENGTH_LONG).show();
                                           }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(upload_lost_found.this, "Upload Failed", Toast.LENGTH_LONG).show();
                                           }
                                   });

                               }
                           });
                           //mImageUrl=fileReference.getDownloadUrl().toString();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(upload_lost_found.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No Image selected", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),lost_found.class));
    }
}
