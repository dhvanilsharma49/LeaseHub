package com.example.leasehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

import model.Property;
import model.PropertyAdapter;
import model.User;

public class TenantActivity extends AppCompatActivity {

    TextView tvTitleName;
    Button btnSearch,btnMyProperties,btnLogOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        initialize();
    }

    private void initialize() {
        tvTitleName = findViewById(R.id.tvTitleName);
        User activeUser;
        activeUser = (User) getIntent().getExtras().getSerializable("userDetail");
        tvTitleName.append(activeUser.getFirstName() + " " + activeUser.getLastName());

        btnMyProperties = findViewById(R.id.btnMyProperties);
        btnMyProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PropertiesList.class);
                intent.putExtra("userType","Tenant");
                startActivity(intent);
            }
        });


        btnSearch = findViewById(R.id.btnSearchProperty);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchProperty.class);
                startActivity(intent);
            }
        });

        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });



    }


}
