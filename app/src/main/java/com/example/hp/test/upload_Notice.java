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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class upload_Notice extends AppCompatActivity {
    EditText title, notice_discription;
    TextView selected_file;
    Button upload_notice, selectfile_notice;
    ImageButton image_notice;
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
    String[]  SPINNERLIST={"Select Your Class","F Y MCA","S Y MCA","T Y MCA"};
    String classname;
    String pdf;
    private Toolbar toolbar;
    private String getImageUrl = "";
    private static final String SAVED_INSTANCE_URI = "uri";
    String imageFilePath,mImageUrl;;
    private static final int CAMERA_REQUEST_CODE = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__notice);
        title = (EditText) findViewById(R.id.title);
        notice_discription = (EditText) findViewById(R.id.notice_discription);
        upload_notice = (Button) findViewById(R.id.upload_notice);
        final Spinner login_spinner=(Spinner)findViewById(R.id.notice_spinner);

        toolbar=findViewById(R.id.notice_include);
        toolbar.setTitle("  Upload Notice");
        setSupportActionBar(toolbar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        if (savedInstanceState != null) {


            uri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));

        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        login_spinner.setAdapter(arrayAdapter);


        login_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select Your Class")) {
                }
                else {
                    classname = (String) parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(), "selected " + classname, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        selected_file = (TextView) findViewById(R.id.selected_file);
        selectfile_notice = (Button) findViewById(R.id.selectfile_notice);
        image_notice = (ImageButton) findViewById(R.id.image_notice);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        selectfile_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(upload_Notice.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectpdf();
                } else { // asking for permission
                    ActivityCompat.requestPermissions(upload_Notice.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        image_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder select = new AlertDialog.Builder(upload_Notice.this);
                select.setMessage("Select option To Complete The operation").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(upload_Notice.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                            ActivityCompat.requestPermissions(upload_Notice.this, new String[]{Manifest.permission.CAMERA}, 9);
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

        upload_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(classname!=null) {
                   if (getImageUrl != null) {
                       addnotice(pdfuri, uri);
                   } else {
                       Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_LONG).show();
                   }

               }
               else {
                   Toast.makeText(getApplicationContext(),"Please Select your class ",Toast.LENGTH_LONG).show();

               }
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (uri != null) {
            outState.putString(SAVED_INSTANCE_URI, uri.toString());
        }
        super.onSaveInstanceState(outState);
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
            Toast.makeText(upload_Notice.this, "please provide permission...", Toast.LENGTH_SHORT).show();
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
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uri);
            this.sendBroadcast(mediaScanIntent);
            try {
                Scanner_notice scanner = new Scanner_notice();
                final Bitmap bitmap = scanner.decodeBitmapUri(upload_Notice.this, uri);

                image_notice.setImageBitmap(bitmap);
                uri = this.uri;

                if (uri.getLastPathSegment().indexOf(".") > 0)
//                    filename = mImageUri.getLastPathSegment().substring(0, mImageUri.getLastPathSegment().lastIndexOf("."));

                    Toast.makeText(getApplicationContext(), filename,Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
            }



        }
        else if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfuri = data.getData();//return the uri of sekected file
            selected_file.setText("A File is selected: " + data.getData().getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/") + 1));
            selected_file.setTextColor(this.getResources().getColor(R.color.Green));
            filename = data.getData().getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/") + 1);
        }
       /* else if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Bundle extras = data.getExtras();
            Bitmap uri = (Bitmap) extras.get("data");
            image_notice.setImageBitmap(uri);
        }*/
        else if (requestCode == Gallary_intent && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            image_notice.setImageURI(uri);
            // imagePath = storage.child("Profile").child(uri.getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/") + 1));

            //data.getData().getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/")+1)
            // String url=taskSnapshot.getDownloadUrl().toString();
            //  Imagelocation=uri.getLastPathSegment();//.substring(data.getData().getLastPathSegment().lastIndexOf("/")+1);

        } else {
            Toast.makeText(upload_Notice.this, "please select the file", Toast.LENGTH_SHORT).show();
        }

        switch (requestCode) {
            case 77:
                try {
                    //When image is captured successfully
                    if (resultCode == RESULT_OK) {

                        //Check if device SDK is greater than 22 then we get the actual image path via below method
                        if (Build.VERSION.SDK_INT > 22)
                            getImageUrl = ImagePath_MarshMallow.getPath(upload_Notice.this, uri);
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

    public void addnotice(final Uri pdfuri,Uri uri) {

        final String topic = title.getText().toString();
        final String discription = notice_discription.getText().toString();
        if (!TextUtils.isEmpty(topic) && !TextUtils.isEmpty(discription) && pdfuri!=null && uri!=null) {

       // final String id = currentUser.getUid();
        progressDialog = new ProgressDialog(upload_Notice.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(500);
        progressDialog.show();
           final String saveCurrentTime,saveCurrentDate;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            saveCurrentTime = currentTime.format(calendar.getTime());

        final String fileName = filename;
        final String fileName1 = filename.substring(0, fileName.lastIndexOf('.'));

            storageReference.child(classname).child(fileName1).putFile(pdfuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // on successful Upload
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                pdf=uri.toString();
                                }});}}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               //  String pd=pdfuri.toString();
             //   Log.d("Tag",pd);
                progressDialog.dismiss();
                Toast.makeText(upload_Notice.this, "File Upload Failed !!!", Toast.LENGTH_SHORT).show();

            }
        });
            storageReference.child(classname).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image_notice.setImageBitmap(null);
                            String url = uri.toString();

                            Imagelocation = url;
                            String imageAddress = Imagelocation;
                            notice Notice = new notice(topic, discription, imageAddress,pdf,saveCurrentDate,saveCurrentTime );
                            id = databaseReference.push().getKey();
                            databaseReference.child(classname).child(id).setValue(Notice).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    title.setText("");
                                    notice_discription.setText("");
                                    selected_file.setText("No Attachment is Selected");

                                    Toast.makeText(upload_Notice.this, "Data inserted", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(upload_Notice.this, "Failed to insert ", Toast.LENGTH_SHORT).show();

                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(upload_Notice.this, "Upload successfull", Toast.LENGTH_SHORT).show();
                            image_notice.setImageURI(null);
                        }
                    });
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(upload_Notice.this, "Upload Failed", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int currentprogress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentprogress);

                }
            });

        } else {
            Toast.makeText(upload_Notice.this, "add title & discription", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),notice_recyclerview.class));
    }


}
