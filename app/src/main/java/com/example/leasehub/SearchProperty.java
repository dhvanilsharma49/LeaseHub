package com.example.leasehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import model.Property;
import model.PropertyAdapter;

public class SearchProperty extends AppCompatActivity {

    ListView lvProperties;
    ArrayList<Property> propertyList;
    PropertyAdapter propertyAdapter;
    DatabaseReference  dbProperties;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_property);

        initialize();
    }

    private void initialize() {


        lvProperties = findViewById(R.id.lvPropList);
        //lvPlayers.setOnItemClickListener(this);
        dbProperties = FirebaseDatabase.getInstance().getReference("Properties");

        //function here
        ArrayList<Property> tempList = new ArrayList<Property>();
        dbProperties.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Property tempProp;
                    for (DataSnapshot aProp: dataSnapshot.getChildren())
                    {
                        tempProp = aProp.getValue(Property.class);
                        tempList.add(tempProp);
                    }
                    propertyAdapter = new PropertyAdapter(SearchProperty.this,tempList);
                    lvProperties.setAdapter(propertyAdapter);

                }
                else {
                    Toast.makeText(SearchProperty.this, "No Rec Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Property prp = tempList.get(i);
                Intent intent = new Intent(getApplicationContext(),DetailInformationActivity.class);
                intent.putExtra("PropDetail",prp);
                intent.putExtra("sourceActivity","searchProperty");
                startActivity(intent);
            }
        });

    }
}