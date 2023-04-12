package com.example.leasehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import model.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    EditText edFirstName, edLastName, edContactNo, edEmail, edPassword, edConfPass;
    RadioGroup rgLoginType;
    CheckBox termCondition;
    Button btnRegister;

    TextView tvAccountExist;

    String userType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize();
    }

    private void initialize() {

        mAuth = FirebaseAuth.getInstance();

        edFirstName = findViewById(R.id.edFirstName);
        edLastName = findViewById(R.id.edLastName);
        edContactNo = findViewById(R.id.edContactNo);
        edEmail = findViewById(R.id.edEmailId);
        edPassword = findViewById(R.id.edPassword);
        edConfPass = findViewById(R.id.edConfPass);
        rgLoginType = findViewById(R.id.rgLoginType);
        termCondition = findViewById(R.id.term_condition);
        tvAccountExist = findViewById(R.id.tvAccountExist);

        btnRegister = findViewById(R.id.btnRegister);

        tvAccountExist.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnRegister.setEnabled(false);

        termCondition.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.tvAccountExist:
                finish();
                break;
            case R.id.btnRegister:
                registerUser();
            case R.id.term_condition:
                enableButton();
        }

    }

    private void enableButton() {
        if(termCondition.isChecked()){
            btnRegister.setEnabled(true);
        }
        else {
            btnRegister.setEnabled(false);
        }
    }

    private void registerUser() {

        String firstName = edFirstName.getText().toString().trim();
        String lastName = edLastName.getText().toString().trim();
        String contactNo = edContactNo.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String confPassword = edConfPass.getText().toString().trim();


        int rbid = rgLoginType.getCheckedRadioButtonId();
        switch (rbid){
            case R.id.rbTenant:
                userType = "Tenant";
                break;
            case R.id.rblandLord:
                userType = "Landlord";
                break;
        }
        ValidateDetails(firstName, lastName, contactNo, email, password, confPassword);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(firstName,lastName,contactNo,email,password,userType);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(SignUpActivity.this, "failed to register ! Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(SignUpActivity.this, "failed to register ! Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void ValidateDetails(String firstName, String lastName, String contactNo, String email, String password, String confPassword) {
        if(firstName.isEmpty()){
            edFirstName.setError("First name is required");
            edFirstName.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            edLastName.setError("Last name is required");
            edLastName.requestFocus();
            return;
        }
        if(contactNo.isEmpty()){
            edContactNo.setError("Contact number is required");
            edContactNo.requestFocus();
            return;
        }
        if(email.isEmpty()){
            edEmail.setError("Email id is required");
            edEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edEmail.setError("Enter a valid emial id");
            edEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edPassword.setError("Password is required");
            edPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            edPassword.setError("Min password length should be 6 characters !");
            edPassword.requestFocus();
            return;
        }

        if(confPassword.isEmpty()){
            edConfPass.setError("Enter password to verify");
            edConfPass.requestFocus();
            return;
        }
        if (!password.equals(confPassword)){
            edConfPass.setError("Password does't match");
            edConfPass.requestFocus();
            return;
        }
    }
}