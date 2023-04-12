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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import model.LeaseRequest;
import model.Property;
import model.User;

public class DetailInformationActivity extends AppCompatActivity {

    ImageView propImg;
    Button btnSendRequest,btnBack;

    TextView propSize,propAddress,propDesc,propAmen, propType,propRent;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_information);
        initialize();
    }

    private void initialize() {

        propSize = findViewById(R.id.tvSizeProp);
        propAddress = findViewById(R.id.tvAddressProp);
        propDesc = findViewById(R.id.tvDescProp);
        propAmen = findViewById(R.id.tvAmenitiesProp);
        propType = findViewById(R.id.tvTypeProp);
        propRent = findViewById(R.id.tvRentProp);
        propImg = findViewById(R.id.imgProp);

        btnSendRequest = findViewById(R.id.btnSendRequest);
        btnBack = findViewById(R.id.btnBack);

        Property property;
        property = (Property) getIntent().getExtras().getSerializable("PropDetail");

        String sourceActivity= getIntent().getExtras().getString("sourceActivity");

        if (sourceActivity.equals("propertyList")){
            btnSendRequest.setEnabled(false);
        }


        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String tenantId = currentUser.getUid();
                String propertyId = property.getPropertyId();
                String landlordId = property.getUserId();
                String requestId = UUID.randomUUID().toString();

                LeaseRequest leaseRequest = new LeaseRequest(requestId,tenantId,propertyId,landlordId);

                FirebaseDatabase.getInstance().getReference("LeaseRequest").child(requestId).setValue(leaseRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(DetailInformationActivity.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailInformationActivity.this, "Unable to send request at this moment . Please try after sometime", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Toast.makeText(DetailInformationActivity.this, propertyId, Toast.LENGTH_SHORT).show();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setValues(property);

    }

    private void setValues(Property property) {
        propSize.append(property.getPropSize());
        propAddress.append(property.getPropAddress());
        propDesc.append(property.getPropDesc());
        propAmen.append(property.getPropAmenities());
        propType.append(property.getPropType());
        propRent.append(property.getPrice().toString());
        Picasso.get().load(property.getSrcImage()).into(propImg);
    }
}