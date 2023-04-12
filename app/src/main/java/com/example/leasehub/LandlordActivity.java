package com.example.leasehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import model.User;

public class LandlordActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvUserName;
    Button btnUploadProp,btnViewEnquires,btnLogout,btnViewTenant,btnViewProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord);
        initialize();
    }

    private void initialize() {
        tvUserName = findViewById(R.id.tvUserName);
        User user = new User();
        user = (User)getIntent().getExtras().getSerializable("userDetail");
        tvUserName.setText(user.getFirstName() +" "+ user.getLastName());

        btnUploadProp = findViewById(R.id.btnUploadProp);
        btnUploadProp.setOnClickListener(this);

        btnViewEnquires = findViewById(R.id.btnViewEnquires);
        btnViewEnquires.setOnClickListener(this);
        
        btnViewTenant = findViewById(R.id.btnViewTenants);
        btnViewTenant.setOnClickListener(this);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        btnViewProperties = findViewById(R.id.btnViewProperties);
        btnViewProperties.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnUploadProp:
                goToAddProperty();
                break;
            case R.id.btnViewEnquires:
                gotoViewEnquiries();
                break;
            case R.id.btnViewTenants:
                gotoViewTenants();
                break;
            case R.id.btnViewProperties:
                gotoViewProperties();
                break;
            case R.id.btnLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;

        }
    }

    private void gotoViewProperties() {

        Intent intent = new Intent(this,PropertiesList.class);
        intent.putExtra("userType","Landlord");
        startActivity(intent);
    }

    private void gotoViewTenants() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Intent intent = new Intent(this,ViewTenants.class);
        intent.putExtra("userid",userId);
        startActivity(intent);

    }

    private void gotoViewEnquiries() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Intent intent = new Intent(this,Enquiries.class);
        intent.putExtra("userid",userId);
        startActivity(intent);


    }

    private void goToAddProperty() {
        Intent intent  = new Intent(this,AddPropertyActivity.class);
        startActivity(intent);
    }
}