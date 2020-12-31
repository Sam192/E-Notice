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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class upload_event extends AppCompatActivity {
    EditText event_title, event_discription;
    TextView event_selected_file;
    Button upload_event, selectfile_event;
    ImageButton event_image;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    Uri pdfuri;
    String filename, Imagelocation;
    private int Gallary_intent = 2;
    Uri uri;
    ProgressDialog progressDialog;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    String id;
    StorageReference storageReference;
    event_class event;
    String saveCurrentTime,saveCurrentDate;
    String email;
    String pdf;
    String pdflink,url;
    private Toolbar toolbar;
    private String getImageUrl = "";
    private static final String SAVED_INSTANCE_URI = "uri";
    String imageFilePath,mImageUrl;;
    private static final int CAMERA_REQUEST_CODE = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_event);

        toolbar=findViewById(R.id.event_include);
        toolbar.setTitle("  Upload Event");
        setSupportActionBar(toolbar);
        event_title = (EditText) findViewById(R.id.event_title);
        event_discription = (EditText) findViewById(R.id.event_discription);
        upload_event = (Button) findViewById(R.id.upload_event);
        databaseReference = FirebaseDatabase.getInstance().getReference("event");

        event_selected_file = (TextView) findViewById(R.id.event_selected_file);
        selectfile_event = (Button) findViewById(R.id.event_selectfile);
        event_image = (ImageButton) findViewById(R.id.event_image);
        storage = FirebaseStorage.getInstance();
        storageReference= storage.getReference("event");
        event=new event_class();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        id = databaseReference.push().getKey();

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

        email=currentUser.getEmail();

        selectfile_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(upload_event.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectpdf();
                } else { // asking for permission
                    ActivityCompat.requestPermissions(upload_event.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });
        event_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder select = new AlertDialog.Builder(upload_event.this);
                select.setMessage("Select option To Complete The operation").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(upload_event.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                Random random = new Random();
                                int key = random.nextInt(1000);
                                File photo = new File(Environment.getExternalStorageDirectory(), "picture" + key + ".jpg");
                                uri = Uri.fromFile(photo);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                            }
                        } else { // asking for permission
                            ActivityCompat.requestPermissions(upload_event.this, new String[]{Manifest.permission.CAMERA}, 9);
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
                AlertDialog alert = select.create();
                alert.setTitle("Note");
                alert.show();
            }
        });

        upload_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getImageUrl != null) {
                    addevent(pdfuri, uri);
                } else {
                    Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_LONG).show();
                }
                // upload();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //after permission is granted
            selectpdf();
        }
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //after permission is granted
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 0);
            }
        } else {
            Toast.makeText(upload_event.this, "please provide permission...", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectpdf() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//to fetch files
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check whether user has selected a file or not (ex:pdf)
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfuri = data.getData();//return the uri of sekected file
            event_selected_file.setText("A File is selected: " + data.getData().getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/") + 1));
            filename = data.getData().getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/") + 1);
        }
        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uri);
            this.sendBroadcast(mediaScanIntent);
            try {
                scanner_event scanner = new scanner_event();
                final Bitmap bitmap = scanner.decodeBitmapUri(upload_event.this, uri);

                event_image.setImageBitmap(bitmap);
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
            event_image.setImageURI(uri);
            }
            else
                {
            Toast.makeText(upload_event.this, "please select the file", Toast.LENGTH_SHORT).show();
        }
        switch (requestCode) {
            case 77:
                try {
                    //When image is captured successfully
                    if (resultCode == RESULT_OK) {

                        //Check if device SDK is greater than 22 then we get the actual image path via below method
                        if (Build.VERSION.SDK_INT > 22)
                            getImageUrl = ImagePath_MarshMallow.getPath(upload_event.this, uri);
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
    public  void getValues(){
        event.setTopic(event_title.getText().toString());
        event.setDiscription(event_discription.getText().toString());
        event.setImageAddress(Imagelocation);
        event.setEmail(email);
        event.setPdf(pdf);
        event.setSaveCurrentDate(saveCurrentDate);
        event.setSaveCurrentTime(saveCurrentTime);
    }

    public void addevent(final Uri pdfuri,Uri uri) {

        final String topic = event_title.getText().toString();
        final String discription = event_discription.getText().toString();
        if (!TextUtils.isEmpty(topic) && !TextUtils.isEmpty(discription)) {
            // final String id = currentUser.getUid();
            progressDialog = new ProgressDialog(upload_event.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Uploading File");
            progressDialog.setProgress(500);
            progressDialog.show();


          //  }

            final String fileName = filename;
            final String fileName1 = filename.substring(0, fileName.lastIndexOf('.'));
            // StorageReference storageReference = storage.getReference();//returns the root path and stores in the storageReferenc object

            storageReference.child(fileName).putFile(pdfuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pdf=uri.toString();

                                }
                            });


                           // Log.d("pdf",pdf);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                     Toast.makeText(upload_event.this, "File Upload Failed !!!", Toast.LENGTH_SHORT).show();
                     }
            });
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // String url = storageReference.getDownloadUrl().toString();
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url=uri.toString();
                            Imagelocation = url;
                            Log.d("img",Imagelocation);
                            getValues();
                            String id = databaseReference.push().getKey();
                            databaseReference.child(id).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    event_title.setText("");
                                    event_discription.setText("");
                                    event_selected_file.setText("No Attachment is Selected");
                                    event_image.setImageBitmap(null);
                                    event_image.setImageURI(null);

                                    Toast.makeText(upload_event.this, "Data inserted", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(upload_event.this, "Failed to insert ", Toast.LENGTH_SHORT).show();

                                }
                            });
                            Toast.makeText(upload_event.this, "Upload successfull", Toast.LENGTH_SHORT).show();
                            event_image.setImageURI(null);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(upload_event.this, "Upload Failed", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int currentprogress = (int) (100 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentprogress);
                }
            });
        } else {
            Toast.makeText(upload_event.this, "add title & discription", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),event_recyclerview.class));
    }
}
