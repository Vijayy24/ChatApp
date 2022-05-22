package org.mycode.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.mycode.chatapp.Models.Users;
import org.mycode.chatapp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

     ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                if ((!binding.txtUserName.getText().toString().isEmpty())
                        &&(!binding.txtEmailId.getText().toString().isEmpty())
                        &&(!binding.txtPassword.getText().toString().isEmpty()))
                {
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtEmailId.getText().toString(),binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful())
                                    {
                                        Users users = new Users(binding.txtUserName.getText().toString()
                                        ,binding.txtEmailId.getText().toString(),binding.txtPassword.getText().toString());
                                        String id = task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(users);


                                        //Toast.makeText(SignUpActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                      {
                                        Toast.makeText(SignUpActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                    Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Enter Credential ", Toast.LENGTH_SHORT).show();
                }


            }
        });
        //go to sign in page:
        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

    }
}