package com.example.leasehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin, btnSignUp;
    EditText edEmail, edPassword;
    FirebaseAuth mAuth;

    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        edEmail = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edUserPassword);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case  R.id.btnLogin:
                loginUser();
                break;
            case R.id.btnSignUp:
                gotoSignUpActivity();
        }

    }

    private void loginUser() {
        String emailId = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        
        if(!(emailId.isEmpty() && password.isEmpty())){
            mAuth.signInWithEmailAndPassword(emailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        edPassword.setText(null);
                        openUserActivity();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Invalid username or password. Please check your credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Enter Username and Password", Toast.LENGTH_SHORT).show();
            edEmail.requestFocus();
        }

       

    }

    private void openUserActivity() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = currentUser.getUid();
        final String accountType = "";

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile.getUserType().equals("Tenant")) {
                    Intent intent = new Intent(getApplicationContext(),TenantActivity.class);
                    intent.putExtra("userDetail", userProfile);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(),LandlordActivity.class);
                    intent.putExtra("userDetail", userProfile);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void gotoSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}