package com.example.leasehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import model.LeaseRequestdetails;

public class DetailEnquiry extends AppCompatActivity {
    ImageView propImg;
    Button btnAssignLease,btnBack;

    TextView sizeProp,addressProp,tenantName,contactNo,email;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_enquiry);
        initialize();

    }

    private void initialize() {

        databaseReference = FirebaseDatabase.getInstance().getReference("LeaseRequest");

        LeaseRequestdetails leaseDetail = (LeaseRequestdetails) getIntent().getExtras().getSerializable("leaseDetails");

        sizeProp = findViewById(R.id.tvSizeProp);
        addressProp = findViewById(R.id.tvAddressProp);
        tenantName = findViewById(R.id.tvTenantName);
        contactNo = findViewById(R.id.tvContactNo);
        email = findViewById(R.id.tvEmail);
        propImg = findViewById(R.id.imgProp);
        btnAssignLease = findViewById(R.id.btnAssignLease);
        btnAssignLease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AssignLease
                String requestId = leaseDetail.getRequestId();
                databaseReference.child(requestId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            FirebaseDatabase.getInstance().getReference("AssignedLease").child(requestId).setValue(leaseDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(DetailEnquiry.this, "Lease Assigned ", Toast.LENGTH_SHORT).show();
                                        databaseReference.child(requestId).removeValue();

                                    } else {
                                        Toast.makeText(DetailEnquiry.this, "oops! Something went wrong, try again.", Toast.LENGTH_SHORT).show();
                                        
                                    }
                                }
                            });
                        }
                        
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        sizeProp.append(leaseDetail.getPropSize());
        addressProp.append(leaseDetail.getPropAdd());
        tenantName.append(leaseDetail.getTenantName());
        contactNo.append(leaseDetail.getContactNo());
        email.append(leaseDetail.getEmailId());
        Picasso.get().load(leaseDetail.getSrcImage()).into(propImg);

    }
}