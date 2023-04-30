package com.example.pocket_money;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pocket_money.databinding.ActivityAddDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class add_details extends AppCompatActivity {

    ActivityAddDetailsBinding binding;
    FirebaseAuth firebaseAuth;
    String imageurl;
    Uri uri;

    String gettotal,days,productprice;
    int newtotal,oldtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth=FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Expence");

        FirebaseDatabase.getInstance().getReference().child("Total").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map getmap = (Map) snapshot.getValue();
                    gettotal = (String) getmap.get("Total Money");
                    days= (String) getmap.get("Total Days");





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 7);
            }
        });

        binding.Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uri==null){



                    HashMap map = new HashMap();
                    map.put("Name", binding.edName.getText().toString());
                    map.put("ProductName", binding.productname.getText().toString());
                    map.put("ProductPrice", binding.productprice.getText().toString());

                    productprice=binding.productprice.getText().toString();



                    oldtotal=Integer.parseInt(gettotal);
                    newtotal=Integer.parseInt(productprice);

                    HashMap totalmap=new HashMap();
                    totalmap.put("Total Money",String.valueOf(oldtotal-newtotal));
                    totalmap.put("Total Days",days);

                    FirebaseDatabase.getInstance().getReference().child("Total").child(firebaseAuth.getUid()).setValue(totalmap);
                    FirebaseDatabase.getInstance().getReference().child("Expence").child(firebaseAuth.getUid()).push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            startActivity(new Intent(getApplicationContext(),checkexpence.class));
                            finish();

                        }
                    });
                }
                else {

                    FirebaseStorage.getInstance().getReference().child("Expence/").child(firebaseAuth.getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            productprice=binding.productprice.getText().toString();


                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) ;
                            imageurl = uriTask.getResult().toString();
                            HashMap map = new HashMap();
                            map.put("Name", binding.edName.getText().toString());
                            map.put("ProductName", binding.productname.getText().toString());
                            map.put("ProductPrice", binding.productprice.getText().toString());

                            map.put("Image", imageurl);

                            oldtotal=Integer.parseInt(gettotal);
                            newtotal=Integer.parseInt(productprice);

                            HashMap totalmap=new HashMap();
                            totalmap.put("Total Money",String.valueOf(oldtotal-newtotal));
                            totalmap.put("Total Days",days);

                            FirebaseDatabase.getInstance().getReference().child("Total").child(firebaseAuth.getUid()).setValue(totalmap);
                            FirebaseDatabase.getInstance().getReference().child("Expence").child(firebaseAuth.getUid()).push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                    startActivity(new Intent(getApplicationContext(),checkexpence.class));
                                    finish();


                                }
                            });

                        }
                    });

                }

            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            uri=data.getData();

            binding.image.setImageURI(uri);
        }
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











