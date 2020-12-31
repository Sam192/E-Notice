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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class upload_maintanence extends AppCompatActivity {
    EditText room_no, maintaince_discription;
    Button upload_maintaince;
    ImageButton maintaince_image;
    FirebaseDatabase database;
    DatabaseReference ref;
    StorageReference imagePath, storage;
    private int Gallary_intent = 2;
    maintainence_class maintainenceClass;
    ProgressDialog progressDialog;
    String Imagelocation;
    Uri uri;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    String uid;
    String saveCurrentTime,saveCurrentDate,url;
    private Toolbar toolbar;
    private String getImageUrl = "";
    private static final String SAVED_INSTANCE_URI = "uri";
    String imageFilePath,mImageUrl;;
    private static final int CAMERA_REQUEST_CODE = 90;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_maintanence);

        toolbar=findViewById(R.id.maintaince_include);
        toolbar.setTitle("  Upload maintance");
        setSupportActionBar(toolbar);
        room_no = (EditText) findViewById(R.id.room_no);
        maintaince_discription = (EditText) findViewById(R.id.maintaince_discription);
        upload_maintaince = (Button) findViewById(R.id.upload_maintaince);
        maintaince_image = (ImageButton) findViewById(R.id.maintaince_image);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("maintainance");
        maintainenceClass = new maintainence_class();
        storage = FirebaseStorage.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        if(currentUser!=null) {
            uid = currentUser.getUid();
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        if (savedInstanceState != null) {


            uri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));

        }



        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        upload_maintaince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Room=room_no.getText().toString();
                String Discription=maintaince_discription.getText().toString();
                if(!TextUtils.isEmpty(Room) && !TextUtils.isEmpty(Discription) ) {
                    if (getImageUrl != null) {
                        upload( uri);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_LONG).show();
                    }
                }//  uploadimage();
                else
                {
                    Toast.makeText(upload_maintanence.this,"Price & Discription is Requried",Toast.LENGTH_LONG).show();
                }
            }
        });
        maintaince_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  select=new AlertDialog.Builder(upload_maintanence.this);
                select.setMessage("Select option To Complete The operation").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(ContextCompat.checkSelfPermission(upload_maintanence.this, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
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
                            ActivityCompat.requestPermissions(upload_maintanence.this,new String[]{Manifest.permission.CAMERA},9);
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
            }
        });
    }
    public  void getValues(){
        maintainenceClass.setRoom(room_no.getText().toString());
        maintainenceClass.setDiscription(maintaince_discription.getText().toString());
        maintainenceClass.setImageAddress(Imagelocation);
        maintainenceClass.setUid(uid);
        maintainenceClass.setSaveCurrentDate(saveCurrentDate);
        maintainenceClass.setSaveCurrentTime(saveCurrentTime);
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
            Toast.makeText(upload_maintanence.this,"please provide permission...",Toast.LENGTH_SHORT).show();
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
                scanner_maintaince scanner = new scanner_maintaince();
                final Bitmap bitmap = scanner.decodeBitmapUri(upload_maintanence.this, uri);

                maintaince_image.setImageBitmap(bitmap);
                uri = this.uri;

                if (uri.getLastPathSegment().indexOf(".") > 0) {

                }
//                    filename = mImageUri.getLastPathSegment().substring(0, mImageUri.getLastPathSegment().lastIndexOf("."));

                // Toast.makeText(getApplicationContext(), filename,Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
            }

            }
        if (requestCode == Gallary_intent && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            maintaince_image.setImageURI(uri);

        }
        switch (requestCode) {
            case 77:
                try {
                    //When image is captured successfully
                    if (resultCode == RESULT_OK) {

                        //Check if device SDK is greater than 22 then we get the actual image path via below method
                        if (Build.VERSION.SDK_INT > 22)
                            getImageUrl = ImagePath_MarshMallow.getPath(upload_maintanence.this, uri);
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
        String room = room_no.getText().toString();
        String discrip = maintaince_discription.getText().toString();
        if (!TextUtils.isEmpty(room) && !TextUtils.isEmpty(discrip)) {



            if (uri != null) {
                progressDialog = new ProgressDialog(upload_maintanence.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("Uploading File");
                progressDialog.setProgress(500);
                progressDialog.show();
                imagePath = storage.child("maintainance").child(uri.getLastPathSegment().substring(uri.getLastPathSegment().lastIndexOf("/") + 1));

                imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url=uri.toString();
                                progressDialog.dismiss();
                                Imagelocation = url;
                                getValues();
                                String id = ref.push().getKey();
                                ref.child(id).setValue(maintainenceClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(upload_maintanence.this, "Data inserted", Toast.LENGTH_SHORT).show();
                                        room_no.setText("");
                                        maintaince_discription.setText("");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(upload_maintanence.this, "Failed to insert ", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                Toast.makeText(upload_maintanence.this, "Upload successfull", Toast.LENGTH_SHORT).show();
                                maintaince_image.setImageURI(null);
                                maintaince_image.setImageBitmap(null);
                            }
                        });
                       // String url = imagePath.getDownloadUrl().toString();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(upload_maintanence.this, "Upload Failed", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        int currentprogress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress(currentprogress);

                    }
                });
            }
            else{
                Toast.makeText(upload_maintanence.this, "Select Image ", Toast.LENGTH_SHORT).show();

            }
        }
        else
        {
            Toast.makeText(upload_maintanence.this, "Enter Room Detail & discription", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),maitainance.class));
    }

}
