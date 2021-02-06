package com.manymaidsinprovo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Model.User;
import com.manymaidsinprovo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.AdminUserHolder> {

    private ArrayList<User> userArrayList;

    public AdminUserAdapter(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public AdminUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_user,parent,false);
        return new AdminUserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserHolder holder, int position) {

        holder.tvUserName.setText(userArrayList.get(position).getFirstName()+" "+userArrayList.get(position).getLastName());
        holder.tvEmail.setText(userArrayList.get(position).getEmail());

        if(userArrayList.get(position).getImageLink()!=null && userArrayList.get(position).getImageLink().length()>0){
            Picasso.get().load(ApiUrl.uploadsFolder+userArrayList.get(position).getImageLink())
                    .placeholder(R.drawable.ic_profile_gray).error(R.drawable.ic_profile_gray)
                    .into(holder.civProfile);
        }

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class  AdminUserHolder extends RecyclerView.ViewHolder{

        CircleImageView civProfile;
        TextView tvUserName,tvEmail;
        public AdminUserHolder(@NonNull View itemView) {
            super(itemView);

            civProfile=itemView.findViewById(R.id.civProfile);
            tvUserName=itemView.findViewById(R.id.tvUserName);
            tvEmail=itemView.findViewById(R.id.tvEmail);
        }
    }
}
