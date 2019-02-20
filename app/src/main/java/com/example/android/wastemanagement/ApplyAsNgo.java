package com.example.android.wastemanagement;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wastemanagement.Models.Ngo;
import com.example.android.wastemanagement.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplyAsNgo extends AppCompatActivity {

    EditText name,email,mobile_no,address;
    Button submit, submitstep1;
    LinearLayout clicksubmit,step1,step2;
    TextView afterText;
    RadioGroup radioGroup;
    RadioButton radioButton;
    private static final int GALLERY_INTETN=2;
    public ProgressDialog dialog;
    private StorageReference mStorage;
    private DatabaseReference accountref, db;
    FirebaseAuth auth;
    Bitmap compressed;
    Uri path;
    User user;
    String radioSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_as_ngo);

        name = findViewById(R.id.form_name);
        email = findViewById(R.id.form_email);
        mobile_no = findViewById(R.id.form_mobile);
        address = findViewById(R.id.form_address);
        submit = findViewById(R.id.form_submit);
        submitstep1 = findViewById(R.id.form_step1submit);
        clicksubmit = findViewById(R.id.clicksubmit);
        afterText = findViewById(R.id.afterText);
        radioGroup = findViewById(R.id.radio);
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);

        dialog = new ProgressDialog(ApplyAsNgo.this);
        dialog.setMessage("Updating please wait...");

        mStorage = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        accountref = FirebaseDatabase.getInstance().getReference().child("user_details").child(auth.getUid());
        accountref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                //name.setText(user.getUserName());
                //email.setText(user.getUserEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        clicksubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ApplyAsNgo.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ApplyAsNgo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    Intent mintetnt = new Intent(Intent.ACTION_PICK);
                    mintetnt.setType("image/*");
                    startActivityForResult(mintetnt, GALLERY_INTETN);
                }
            }
        });

        submitstep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selected);
                radioSelected = radioButton.getText().toString();
                step1.setVisibility(View.GONE);
                step2.setVisibility(View.VISIBLE);
            }
        });

        /*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submit code
                accountref.child("ngo_req_status").setValue(1);
                Ngo ngo = new Ngo(user.getUserName(), user.getUserEmail(), address.getText().toString(),
                        user.getUserImgUrl(), String.valueOf(path),Long.valueOf(mobile_no.getText().toString()));
                db = FirebaseDatabase.getInstance().getReference()
                        .child("ngo").child(radioSelected).child(auth.getUid());
                db.setValue(ngo);
                Intent intent = new Intent(ApplyAsNgo.this, Home.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GALLERY_INTETN && resultCode==RESULT_OK) {
            dialog.setMessage("Uploading");
            dialog.show();
            Uri uri= data.getData();

            StorageReference filepath= mStorage.child("ngo_official_pic").child(uri.getLastPathSegment());
            try
            {
                compressed = MediaStore.Images.Media.getBitmap(ApplyAsNgo.this.getContentResolver(), uri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressed.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] cimg = baos.toByteArray();
            filepath.putBytes(cimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    path = taskSnapshot.getDownloadUrl();
                    //accountref = FirebaseDatabase.getInstance().getReference().child("user_details").child(auth.getUid());
                    //accountref.child("userImgUrl").setValue(String.valueOf(path));
                    Toast.makeText(ApplyAsNgo.this, "Document uploaded", Toast.LENGTH_LONG).show();
                    //finish();
                    //startActivity(getIntent());
                    afterText.setVisibility(View.VISIBLE);
                    clicksubmit.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            });
        }
    }

    public boolean onSupportNavigateUp(){

        onBackPressed();
        return true;
    }
}
