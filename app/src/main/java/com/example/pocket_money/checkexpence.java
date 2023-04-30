package com.example.pocket_money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pocket_money.databinding.ActivityCheckexpenceBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class checkexpence extends AppCompatActivity {

    ActivityCheckexpenceBinding binding;
    myadapter adapter;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCheckexpenceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Check Expence");



        FirebaseDatabase.getInstance().getReference().child("completemoney").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Map getmap = (Map) snapshot.getValue();
                    String money = (String) getmap.get("All Money");

                    binding.tvtotalmoney.setText(money);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("totalmoney").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Map getmap = (Map) snapshot.getValue();
                    String money = (String) getmap.get("TotalMoney");

                    binding.tvtotalmoney.setText(money);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        FirebaseDatabase.getInstance().getReference().child("Total").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Map getmap = (Map) snapshot.getValue();
                    String money = (String) getmap.get("Total Money");
                    String days = (String) getmap.get("Total Days");

                    binding.remaingmoney.setText(money);
                    binding.tvtotaldays.setText(days);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(  FirebaseDatabase.getInstance().getReference().child("Expence").child(firebaseAuth.getUid()), model.class)
                        .build();

        adapter=new myadapter(options);
        binding.recyclerview .setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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