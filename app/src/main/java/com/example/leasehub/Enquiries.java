package com.example.leasehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.LeaseEnquiryAdapter;
import model.LeaseRequest;
import model.LeaseRequestdetails;
import model.Property;
import model.User;

public class Enquiries extends AppCompatActivity {

    TextView tvPropList;
    ListView lvEnquiryList;

    DatabaseReference dbLeaseRequest;
    DatabaseReference dbUser;
    DatabaseReference dbProp;
    FirebaseUser currentuser;
    LeaseEnquiryAdapter leaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiries);
        initialize();
    }

    private void initialize() {
        tvPropList = findViewById(R.id.tvPropList);
        String userId = getIntent().getExtras().getString("userid");

        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        lvEnquiryList = findViewById(R.id.lvEnquiryList);

        dbLeaseRequest = FirebaseDatabase.getInstance().getReference("LeaseRequest");
        dbUser = FirebaseDatabase.getInstance().getReference("Users");
        dbProp = FirebaseDatabase.getInstance().getReference("Properties");

        ArrayList<LeaseRequestdetails> leaseRequestdetailsList = new ArrayList<LeaseRequestdetails>();
        LeaseRequestdetails aLease = new LeaseRequestdetails();

        dbLeaseRequest.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    LeaseRequest tempReq;

                    for (DataSnapshot aReq: dataSnapshot.getChildren())
                    {
                        tempReq = aReq.getValue(LeaseRequest.class);
                        final String tenId = tempReq.getTenantId();
                        final String propId = tempReq.getPropertyId();
                        aLease.setRequestId(tempReq.getRequestId());
                        aLease.setTenantId(tempReq.getTenantId());

                        if(tempReq.getLandlordId().equals(currentuser.getUid()))
                        {
                            dbUser.child(tenId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        User user = snapshot.getValue(User.class);
                                        aLease.setTenantName(user.getFirstName() + " " + user.getLastName());
                                        aLease.setContactNo(user.getContactNo());
                                        aLease.setEmailId(user.getEmailId());

                                        dbProp.child(propId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Property prop = snapshot.getValue(Property.class);
                                                    aLease.setPropSize(prop.getPropSize());
                                                    aLease.setPropAdd(prop.getPropAddress());
                                                    aLease.setSrcImage(prop.getSrcImage());
                                                    aLease.setPropId(prop.getPropertyId());
                                                    aLease.setLandlordId(prop.getUserId());

                                                    leaseRequestdetailsList.add(aLease);
                                                }
                                                leaseAdapter = new LeaseEnquiryAdapter(Enquiries.this,leaseRequestdetailsList);
                                                lvEnquiryList.setAdapter(leaseAdapter);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                }
            }
        });
        lvEnquiryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LeaseRequestdetails details = leaseRequestdetailsList.get(i);
                Intent intent = new Intent(getApplicationContext(),DetailEnquiry.class);
                intent.putExtra("leaseDetails",details);
                startActivity(intent);

            }
        });






    }
}