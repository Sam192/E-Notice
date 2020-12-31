package com.example.hp.test;

//Upload_notesfile.java to Upload notes

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload_notesfile extends AppCompatActivity {

    Button selectfile,uploadfile;
    TextView notification;
    Uri pdfUri;//uri are actully URLs that are meant for local storage
    FirebaseStorage storage;// used for uploading file
    FirebaseDatabase database;// used to store URLS of Upload files
    ProgressDialog progressDialog;
    String filename;
    String ClassName;
    String[]  SPINNERLIST={"Select Semester","sem1","sem2","sem3","sem4","sem5","sem6"};
    private Toolbar toolbar;

   

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notesfile);

        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

        selectfile=findViewById(R.id.selectfile);
        uploadfile=findViewById(R.id.uploadfile);
        notification=findViewById(R.id.notification);
        final Spinner spinner=(Spinner)findViewById(R.id.spinner);

        toolbar=findViewById(R.id.notes_include);
        toolbar.setTitle("  Upload  Notes");
        setSupportActionBar(toolbar);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        spinner.setAdapter(arrayAdapter);

       /* ClassName=spinner.getSelectedItem().toString();
        Log.d("value",ClassName);

*/
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if (parent.getItemAtPosition(position).equals("Select Your Class")) {

               } else {
                   ClassName = (String) parent.getItemAtPosition(position);
                   Toast.makeText(getApplicationContext(), "selected " + ClassName, Toast.LENGTH_SHORT).show();
               }
           }
           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(Upload_notesfile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                  selectpdf();
                  }
                  else{ // asking for permission
                    ActivityCompat.requestPermissions(Upload_notesfile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }

            }
        });

        uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfUri!=null)// denotes user has succesfully selected the file
                {   if(ClassName!=null) {
                    uploadfile(pdfUri);
                }
                else{
                    Toast.makeText(Upload_notesfile.this,"Plese Select Semester",Toast.LENGTH_SHORT).show();

                }
                }
                else
                {
                    Toast.makeText(Upload_notesfile.this,"select a file",Toast.LENGTH_SHORT).show();
                }
            }

           
        });
    }

    private void uploadfile(Uri pdfUri) {

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName=filename;
        final String fileName1=filename.substring(0, fileName.lastIndexOf('.'));
        final StorageReference storageReference=storage.getReference().child(ClassName);//returns the root path and stores in the storageReferenc object

        storageReference.child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // on successful Upload
                      //  String url=taskSnapshot.getDownloadUrl().toString();//return the url of you uploaded file
                        //now store this url in real time database

                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url=uri.toString();

                                DatabaseReference reference=database.getReference(ClassName);//return the path to root
                                reference.child(fileName1).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // on completion of succesfully uploaded
                                        if(task.isSuccessful())
                                        {
                                            notification.setText("");
                                            Toast.makeText(Upload_notesfile.this,"File succesfully Uploaded !!!",Toast.LENGTH_SHORT).show();
                                        }
                               /* else
                                {
                                    Toast.makeText(Upload_notesfile.this,"File Upload Failed !!!",Toast.LENGTH_SHORT).show();

                                }*/
                                    }
                                });
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //on failer of Upload
                        // if our storagereference is not able to store the perticular file in the storage of firebase
                        Toast.makeText(Upload_notesfile.this,"File Upload Failed !!!",Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // while Upload is on progress
                // track the progress of our Upload
                int currentprogress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentprogress);
                progressDialog.cancel();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        { //after permission is granted
            selectpdf();
        }
        else{
            Toast.makeText(Upload_notesfile.this,"please provide permission...",Toast.LENGTH_SHORT).show();
        }
    }

    private void selectpdf() {
        //to offer user to select a using file manager
        //we will be using an intent
        Intent intent=new Intent();
        intent.setType("application/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);//to fetch files
        startActivityForResult(intent,86);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //check whether user has selected a file or not (ex:pdf)
        if(requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
            pdfUri=data.getData();//return the uri of sekected file
            notification.setText("A File is selected:" +data.getData().getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/")+1));
            filename=data.getData().getLastPathSegment().substring(data.getData().getLastPathSegment().lastIndexOf("/")+1);
        }
        else{
            Toast.makeText(Upload_notesfile.this,"please select the file",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),notes.class));
    }
}
