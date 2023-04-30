package com.example.pocket_money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pocket_money.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    ActivityLoginBinding binding;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,register.class));
            }
        });




firebaseAuth=FirebaseAuth.getInstance();



        binding.btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.progressBar.setVisibility(View.VISIBLE);
                String email = binding.edEmail.getText().toString().trim();
             String   password = binding.edPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(login.this, "please Enter Email", Toast.LENGTH_SHORT).show();                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(login.this, "Enter PAssword", Toast.LENGTH_SHORT).show();                    return;
                }

                if (password.length() < 8) {


                }


                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(login.this, "succefuly signin", Toast.LENGTH_SHORT).show();




                            startActivity(new Intent(getApplicationContext(), dashboard.class));



                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(login.this, "error", Toast.LENGTH_SHORT).show();


                        }
                    }

                });
            }

        });



        binding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText resetmail=new EditText(view.getContext());
                AlertDialog.Builder passwordresetdailog=new AlertDialog.Builder(view.getContext());
                passwordresetdailog.setTitle("Reset Password");
                passwordresetdailog.setMessage("Enter your Email To Received Reset Link");
                passwordresetdailog.setView(resetmail);

                passwordresetdailog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link
                        String mail=resetmail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(login.this,"Reset link sent to your Email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(login.this,"Error ! Reset Link is not sent"+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

                passwordresetdailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the Dailog
                    }
                });

                passwordresetdailog.create().show();
            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),dashboard.class));
            finish();
        }
    }
}