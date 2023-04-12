package com.example.leasehub;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import model.Property;

public class AddPropertyActivity extends AppCompatActivity implements View.OnClickListener, OnSuccessListener, OnFailureListener {


    EditText edPropSize,edPropDesc,edPropAddress,edAmenities,edPropRent;
    RadioGroup rgPropType;

    ImageView imgProp;
    Button btnRegister;
    Uri photoPath;

    ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference  storageReference,sRef;

    ActivityResultLauncher activityResultLauncher;
    DatabaseReference databaseReference;

    Property property;
    String propSize,propDesc,propAddress,propAmenities,propType;
    Float propRent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        initialize();
    }

    private void initialize() {
        imgProp = findViewById(R.id.imgProperty);
        imgProp.setOnClickListener(this);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                            getPhoto(result);
                    }
        });

        edPropSize = findViewById(R.id.edPropSize);
        edPropDesc = findViewById(R.id.edPropDesc);
        edPropAddress = findViewById(R.id.edPropAddress);
        edAmenities = findViewById(R.id.edAmenities);
        rgPropType = findViewById(R.id.rgPropType);
        edPropRent = findViewById(R.id.edPropRent);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


    }

    private void getPhoto(ActivityResult result) {
        if(result.getResultCode() == RESULT_OK && result.getData() != null)
        {
            photoPath = result.getData().getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),photoPath);
                imgProp.setImageBitmap(bitmap);

            }catch (IOException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnRegister:
                addRecord();
                break;
            case R.id.imgProperty:
                browsePicture();
                break;
        }

    }

    private void browsePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Photo"));
    }

    private void addRecord() {

        propSize = edPropSize.getText().toString().trim();
        propDesc = edPropDesc.getText().toString().trim();
        propAddress = edPropAddress.getText().toString().trim();
        propAmenities = edAmenities.getText().toString().trim();
        String rent = edPropRent.getText().toString().trim();

        
        propType="";

        int rbid = rgPropType.getCheckedRadioButtonId();
        switch (rbid){
            case R.id.rbHouse:
                propType = "House";
                break;
            case R.id.rbBasement:
                propType = "Basement";
                break;
            case R.id.rbApartment:
                propType = "Apartment";
                break;
        }
        if(validateData(propSize, propDesc, propAddress, propAmenities, rent)){
            try {
                propRent = Float.valueOf(rent);
                uploadPhoto();
            }
            catch (Exception e){
                Toast.makeText(this, "Enter valid value for rent", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadPhoto() {

        try{
            if(photoPath != null){
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading the photo in progress...");
                progressDialog.show();
                sRef = storageReference.child("images/"+ UUID.randomUUID());
                sRef.putFile(photoPath).addOnSuccessListener(this);
                sRef.putFile(photoPath).addOnFailureListener(this);
               // sRef.getDownloadUrl().addOnCompleteListener(this);

                UploadTask uploadTask = sRef.putFile(photoPath);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return  sRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            if (downloadUri != null) {
                                String imageUrl = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!

                                // Adding property detail
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String propertyId = UUID.randomUUID().toString();

                                property = new Property(propertyId,userId,propSize, propDesc, propAddress, propAmenities, propType, imageUrl,propRent);

                                FirebaseDatabase.getInstance().getReference("Properties").child(propertyId).setValue(property).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(AddPropertyActivity.this, "Property added successfully", Toast.LENGTH_SHORT).show();
                                            AlertDialog.Builder alert = new AlertDialog.Builder(AddPropertyActivity.this);
                                            alert.setMessage("Do you want to another property ? ")
                                                    .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    clearFields();
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            });

                                            AlertDialog alertDialog = alert.create();
                                            alertDialog.setTitle("Property Added Successfully");
                                            alertDialog.show();

                                        } else {
                                            Toast.makeText(AddPropertyActivity.this, "Failed to add property ! Try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                        }
                    }
                });

            }
            else
            {
                Toast.makeText(this, "No photo to upload", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this,"Error 1"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }

    private void clearFields() {
        edPropSize.setText(null);
        edPropDesc.setText(null);
        edPropAddress.setText(null);
        edAmenities.setText(null);
        edPropRent.setText(null);
        photoPath = null;
        imgProp.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);

    }


    private boolean validateData(String propSize, String propDesc, String propAddress, String propAmenities, String rent) {
        if(propSize.isEmpty()){
            edPropSize.setError("Property size is required");
            edPropSize.requestFocus();
            return false;
        }
        if(propDesc.isEmpty()){
            edPropDesc.setError("Property description is required");
            edPropDesc.requestFocus();
            return false;
        }
        if(propAddress.isEmpty()){
            edPropAddress.setError("Property address is required");
            edPropAddress.requestFocus();
            return false;
        }
        if(propAmenities.isEmpty()){
            edAmenities.setError("Property description is required");
            edAmenities.requestFocus();
            return false;
        }
        if(rent.isEmpty()){
            edPropRent.setError("Propert rent is required");
            edPropRent.requestFocus();
            return false;
        }

        if(photoPath == null) {
            Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Object o) {
        progressDialog.dismiss();
    }
}
