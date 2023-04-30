package com.example.pocket_money;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pocket_money.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    ActivityRegisterBinding binding;

    String name, email, password, confirmpassword;

    Uri uri;
    String Imageurl;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 37);
            }
        });


        binding.btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                binding.progressBar.setVisibility(View.VISIBLE);
                name = binding.edName.getText().toString();
                email = binding.edEmail.getText().toString();
                password = binding.edPassword.getText().toString();
                confirmpassword = binding.edConfirmpassword.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(register.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    binding.progressBar.setVisibility(View.GONE);

                    Toast.makeText(register.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(register.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmpassword)) {
                    binding.progressBar.setVisibility(View.GONE);

                    Toast.makeText(register.this, "Please Enter ConfirmPassword", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 8) {
                    binding.progressBar.setVisibility(View.GONE);

                    Toast.makeText(register.this, "Please enter 8 digit password", Toast.LENGTH_SHORT).show();
                }
                if (confirmpassword.length() < 8) {
                    binding.progressBar.setVisibility(View.GONE);

                    Toast.makeText(register.this, "Please enter 8 digit confirmpassword", Toast.LENGTH_SHORT).show();
                }

                if (!password.equals(confirmpassword)) {
                    binding.progressBar.setVisibility(View.GONE);

                    Toast.makeText(register.this, "password and confirm password not equal", Toast.LENGTH_SHORT).show();

                } else {

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {

                                binding.progressBar.setVisibility(View.GONE);
                                Toast.makeText(register.this, "registerd succefully", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(register.this, dashboard.class);
                                intent.putExtra("password",password);
                                startActivity(intent);
                                finish();
                                if (uri == null) {


                                    HashMap map = new HashMap();
                                    map.put("Name", binding.edName.getText().toString());
                                    map.put("Email", binding.edEmail.getText().toString());

                                    FirebaseDatabase.getInstance().getReference().child("user").child(firebaseAuth.getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            binding.edName.setText("");
                                            binding.edEmail.setText("");
                                            binding.edPassword.setText("");
                                            binding.edConfirmpassword.setText("");

                                        }
                                    });
                                } else {

                                    FirebaseStorage.getInstance().getReference().child("user/").child(firebaseAuth.getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!uriTask.isComplete()) ;
                                            Imageurl = uriTask.getResult().toString();
                                            HashMap map = new HashMap();
                                            map.put("Name", binding.edName.getText().toString());
                                            map.put("Email", binding.edEmail.getText().toString());
                                            map.put("Image", Imageurl);
                                            FirebaseDatabase.getInstance().getReference().child("user").child(firebaseAuth.getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    binding.edEmail.setText("");
                                                    binding.edName.setText("");
                                                    binding.edPassword.setText("");
                                                    binding.edConfirmpassword.setText("");
                                                    binding.profileImage.setImageResource(R.drawable.profileimage);
                                                    Intent intent=new Intent(register.this, dashboard.class);



                                                }
                                            });

                                        }
                                    });


                                }


                            } else {
                                Toast.makeText(register.this, "Authentication Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            binding.profileImage.setImageURI(uri);
        }
    }

}