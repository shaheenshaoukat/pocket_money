package com.example.pocket_money;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pocket_money.databinding.ItemBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class myadapter extends FirebaseRecyclerAdapter<model, myadapter.myviewholder> {


    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull model model) {


        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        holder.binding.name.setText(model.getName());
        holder.binding.productname.setText(model.getProductName());
        holder.binding.productprice.setText(model.getProductPrice());
        Glide.with(holder.binding.profileImage.getContext()).load(model.getImage()).into(holder.binding.profileImage);
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.binding.profileImage.getContext());
                builder.setTitle("Are You Sure");
                builder.setMessage("Deleted");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Expence").child(firebaseAuth.getUid())
                                .child(getRef(position).getKey()).removeValue();


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }

        });

        holder.binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(holder.binding.profileImage.getContext());
                dialog.setContentView(R.layout.dialogcontent);

                final EditText image=dialog.findViewById(R.id.image);
                final EditText name=dialog.findViewById(R.id.name);
                final EditText productname=dialog.findViewById(R.id.productname);
                final EditText productprice=dialog.findViewById(R.id.proprc);
                Button submit=dialog.findViewById(R.id.usubmit);

                image.setText(model.getImage());
                name.setText(model.getName());
                productname.setText(model.getProductName());
                productprice.setText(model.getProductPrice());

                dialog.show();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String,Object> map=new HashMap<>();
                        map.put("Image",image.getText().toString());
                        map.put("Name",name.getText().toString());
                        map.put("ProductName",productname.getText().toString());
                        map.put("ProductPrice",productprice.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Expence").child(firebaseAuth.getUid())
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();

                                    }
                                });
                    }
                });


            }
        });




    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        ItemBinding binding;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            binding = ItemBinding.bind(itemView);

        }
    }
}
