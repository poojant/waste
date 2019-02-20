package com.example.android.wastemanagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class VolunteerAdapterViewHolder extends RecyclerView.ViewHolder {

    Context context;
    TextView name,email,mobile;
    ImageView profile, aadhar;
    Boolean tapOpen = false;
    ImageView accept, reject;
    TextView tap;
    public VolunteerAdapterViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.volunteerName);
        email = itemView.findViewById(R.id.volunteerEmail);
        name = itemView.findViewById(R.id.volunteerMobile);
        profile = itemView.findViewById(R.id.volunteerImage);
        aadhar = itemView.findViewById(R.id.volunteerAadhar);
        accept = itemView.findViewById(R.id.accept);
        reject = itemView.findViewById(R.id.reject);
        tap = itemView.findViewById(R.id.tap);

        tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tapOpen){
                    tapOpen = true;
                    aadhar.setVisibility(View.VISIBLE);
                    tap.setText("Tap to Close");
                }else{
                    tapOpen = false;
                    aadhar.setVisibility(View.GONE);
                    tap.setText("Tap to view Aadhar card");
                }
            }
        });
    }

    public void setContext(Context context){
        this.context = context;
    }
    public void setName(String username){
        name.setText(username);
    }
    public void setEmail(String userEmail){
        email.setText(userEmail);
    }
    public void setMobile(long userContact){
        mobile.setText(String.valueOf(userContact));
    }
    public void setImage(String image){
        Glide.with(context).load(image).into(profile);
    }
    public void setAadhar(String aadharUri){
        Glide.with(context).load(aadharUri).into(aadhar);
    }
}
