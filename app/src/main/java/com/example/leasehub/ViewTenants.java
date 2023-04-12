package com.example.leasehub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import model.LeaseRequestdetails;
import model.TenantAdapter;

public class ViewTenants extends AppCompatActivity {

    ListView lvTenantList;
    DatabaseReference dbAssignedLease;
    FirebaseUser currentUser;
    TenantAdapter tenantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tenants);
        initialize();
    }

    private void initialize() {
        lvTenantList = findViewById(R.id.lvTenantList);
        String userid = getIntent().getExtras().getString("userid");

        dbAssignedLease = FirebaseDatabase.getInstance().getReference("AssignedLease");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ArrayList<LeaseRequestdetails> leaseRequestdetailsList = new ArrayList<LeaseRequestdetails>();

        dbAssignedLease.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    LeaseRequestdetails tempLeaseDetail;
                    for(DataSnapshot aTenant : dataSnapshot.getChildren())
                    {
                        tempLeaseDetail = aTenant.getValue(LeaseRequestdetails.class);
                        if(tempLeaseDetail.getLandlordId().equals(currentUser.getUid())){
                            leaseRequestdetailsList.add(tempLeaseDetail);
                        }
                    }
                    tenantAdapter = new TenantAdapter(ViewTenants.this,leaseRequestdetailsList);
                    lvTenantList.setAdapter(tenantAdapter);
                }

            }
        });

    }
}