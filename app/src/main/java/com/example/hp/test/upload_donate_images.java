package com.example.hp.test;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Random;

public class upload_donate_images extends AppCompatActivity {

    EditText discription;
    Button insert;
    ImageButton image;
    FirebaseDatabase database;
    DatabaseReference ref;
    StorageReference imagePath, storage;
    private int Gallary_intent = 2;
    upload_donate Upload_donate;
    ProgressDialog progressDialog;
    String Imagelocation;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    String uid;
    Uri uri;
    String url;
    private Toolbar toolbar;
    private String getImageUrl = "";
    private static final String SAVED_INSTANCE_URI = "uri";
    String imageFilePath,mImageUrl;;
    private static final int CAMERA_REQUEST_CODE = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_donate_images);

        discription = (EditText) findViewById(R.id.discription);
        insert = (Button) findViewById(R.id.insert);
        image = (ImageButton) findViewById(R.id.image);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("donate");
        Upload_donate = new upload_donate();
        storage = FirebaseStorage.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        toolbar=findViewById(R.id.donate_include);
        toolbar.setTitle("  Upload Donation Image");
        setSupportActionBar(toolbar);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        if (savedInstanceState != null) {


            uri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));

        }

        if(currentUser!=null) {
            uid = currentUser.getUid();
        }

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Discription=discription.getText().toString();
                if(!TextUtils.isEmpty(Discription) ) {
                    if (getImageUrl != null) {
                        upload( uri);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_LONG).show();
                    }

                }//  uploadimage();
                else
                {
                    Toast.makeText(upload_donate_images.this,"Discription is Requried",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void getValues() {
        Upload_donate.setDiscription(discription.getText().toString());
        Upload_donate.setImageAddress(Imagelocation);
        Upload_donate.setUid(uid);
    }

    public void btnimage(View view) {

        AlertDialog.Builder  select=new AlertDialog.Builder(this);
        select.setMessage("Select option To Complete The operation").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ContextCompat.checkSelfPermission(upload_donate_images.this, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                    //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 //   if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            Random random = new Random();
                            int key = random.nextInt(1000);
                            File photo = new File(Environment.getExternalStorageDirectory(), "picture" + key + ".jpg");
                            uri = Uri.fromFile(photo);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        }

                }
                else{ // asking for permission
                    ActivityCompat.requestPermissions(upload_donate_images.this,new String[]{Manifest.permission.CAMERA},9);
                }


            }
        }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, Gallary_intent);
            }
        });
        AlertDialog alert=select.create();
        alert.setTitle("Note");
        alert.show();
       /* Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Gallary_intent);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        { //after permission is granted
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 0);
            }
        }
        else{
            Toast.makeText(upload_donate_images.this,"please provide permission...",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uri);
            this.sendBroadcast(mediaScanIntent);
            try {
                scanner_Uploaddonate scanner = new scanner_Uploaddonate();
                final Bitmap bitmap = scanner.decodeBitmapUri(upload_donate_images.this, uri);

                image.setImageBitmap(bitmap);
                uri = this.uri;

                if (uri.getLastPathSegment().indexOf(".") > 0) {

                }
//                    filename = mImageUri.getLastPathSegment().substring(0, mImageUri.getLastPathSegment().lastIndexOf("."));

                // Toast.makeText(getApplicationContext(), filename,Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
            }



        }
        else if (requestCode == Gallary_intent && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            image.setImageURI(uri);
            // imagePath = storage.child("Profile").child(uri.getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/") + 1));

            //data.getData().getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/")+1)
            // String url=taskSnapshot.getDownloadUrl().toString();
            //  Imagelocation=uri.getLastPathSegment();//.substring(data.getData().getLastPathSegment().lastIndexOf("/")+1);

        }
        switch (requestCode) {
            case 77:
                try {
                    //When image is captured successfully
                    if (resultCode == RESULT_OK) {

                        //Check if device SDK is greater than 22 then we get the actual image path via below method
                        if (Build.VERSION.SDK_INT > 22)
                            getImageUrl = ImagePath_MarshMallow.getPath(upload_donate_images.this, uri);
                        else
                            //else we will get path directly
                            getImageUrl = uri.getPath();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }


    }
    public void upload(Uri uri)
    {
        progressDialog=new ProgressDialog(upload_donate_images.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File");
        progressDialog.setProgress(500);
        progressDialog.show();
        if (uri != null) {
            imagePath = storage.child("donate").child(uri.getLastPathSegment().substring(uri.getLastPathSegment().lastIndexOf("/") + 1));

            imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                           url=uri.toString();
                            Imagelocation = url;
                            progressDialog.dismiss();
                            // = imagePath.getDownloadUrl().toString();

                            getValues();
                            String id = ref.push().getKey();
                            ref.child(id).setValue(Upload_donate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(upload_donate_images.this, "Data inserted", Toast.LENGTH_SHORT).show();

                                    discription.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(upload_donate_images.this, "Failed to insert ", Toast.LENGTH_SHORT).show();

                                }
                            });
                            Toast.makeText(upload_donate_images.this, "Upload successfull", Toast.LENGTH_SHORT).show();
                            image.setImageBitmap(null);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(upload_donate_images.this, "Upload Failed", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int currentprogress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentprogress);

                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),selling_swipeview.class));
    }
}
