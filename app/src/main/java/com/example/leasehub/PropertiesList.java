package com.example.leasehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.LeaseRequestdetails;
import model.Property;
import model.PropertyAdapter;

public class PropertiesList extends AppCompatActivity {

    ListView lvProperList;
    FirebaseUser currentUser;
    DatabaseReference dbAssignedLease, dbProperties;
    PropertyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties_list);
        initialize();
    }

    private void initialize() {
        lvProperList = findViewById(R.id.lvProperList);
        dbAssignedLease = FirebaseDatabase.getInstance().getReference("AssignedLease");
        dbProperties = FirebaseDatabase.getInstance().getReference("Properties");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = currentUser.getUid();
        String userType = getIntent().getExtras().getString("userType");

        ArrayList<Property> propList = new ArrayList<Property>();

        if(userType.equals("Tenant")){
            getTenantProperties(uId, propList);
        }
        else {
            getLandlordProperties(uId, propList);
        }

        lvProperList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Property prp = propList.get(i);
                Intent intent = new Intent(getApplicationContext(),DetailInformationActivity.class);
                intent.putExtra("PropDetail",prp);
                intent.putExtra("sourceActivity","propertyList");
                startActivity(intent);
            }
        });

    }

    private void getLandlordProperties(String uId, ArrayList<Property> propList) {
        dbProperties.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Property prop;
                    for(DataSnapshot aProp : dataSnapshot.getChildren() ){
                        prop = aProp.getValue(Property.class);
                        if(prop.getUserId().equals(uId)){
                            propList.add(prop);
                        }

                    }
                    adapter = new PropertyAdapter(PropertiesList.this, propList);
                    lvProperList.setAdapter(adapter);

                }
            }
        });
    }

    private void getTenantProperties(String uId, ArrayList<Property> propList) {
        dbAssignedLease.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    LeaseRequestdetails LeaseDetail;
                    String propId = null;
                    for (DataSnapshot aLeaseRec : dataSnapshot.getChildren()){
                        LeaseDetail = aLeaseRec.getValue(LeaseRequestdetails.class);
                        if(LeaseDetail.getTenantId().equals(uId)){
                            propId = LeaseDetail.getPropId();
                        }

                        String finalPropId = propId;
                        dbProperties.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    Property prop;
                                    for(DataSnapshot aProp : dataSnapshot.getChildren() ){
                                        prop = aProp.getValue(Property.class);
                                        if(prop.getPropertyId().equals(finalPropId)){
                                            propList.add(prop);
                                        }

                                    }
                                    adapter = new PropertyAdapter(PropertiesList.this, propList);
                                    lvProperList.setAdapter(adapter);

                                }
                            }
                        });

                    }



                }

            }
        });
    }



}