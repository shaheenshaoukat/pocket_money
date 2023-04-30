package com.example.pocket_money;

import static androidx.browser.trusted.sharing.ShareTarget.FileFormField.KEY_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pocket_money.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class dashboard extends AppCompatActivity {

    ActivityDashboardBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();


        binding.addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(dashboard.this);
                dialog.setContentView(R.layout.dialog);
                EditText money = dialog.findViewById(R.id.edmoney);
                EditText days = dialog.findViewById(R.id.ed_days);
                Button btn_send = dialog.findViewById(R.id.btnsend);
                dialog.show();


                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        HashMap mapmoney = new HashMap();
                        mapmoney.put("All Money", money.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("completemoney").child(firebaseAuth.getUid()).setValue(mapmoney);



                        HashMap map = new HashMap();
                        map.put("Total Money", money.getText().toString());
                        map.put("Total Days", days.getText().toString());


                        FirebaseDatabase.getInstance().getReference().child("Total").child(firebaseAuth.getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {



                                dialog.dismiss();
                                money.setText("");

                            }
                        });

                    }
                });


            }
        });
     

        totalmoney();




        FirebaseDatabase.getInstance().getReference().child("user").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map getmap = (Map) snapshot.getValue();

                    String name = (String) getmap.get("Name");
                    String email = (String) getmap.get("Email");
                    String image = (String) getmap.get("Image");

                    binding.tvname.setText(name);
                    binding.tvemail.setText(email);

                    Glide.with(dashboard.this).load(image).into(binding.images);
                } else {
                    Toast.makeText(dashboard.this, "data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.checkexpence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), checkexpence.class));


            }
        });
        binding.addexpence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), add_details.class));


            }
        });



    }

    public void totalmoney() {
        FirebaseDatabase.getInstance().getReference().child("Total").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map getmap = (Map) snapshot.getValue();
                    String money = (String) getmap.get("Total Money");
                    String days = (String) getmap.get("Total Days");

                    binding.total.setText(money);
                    binding.days.setText(days);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}