package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

public class AdminOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView username,phone,adrress,datetime,totalprice,state;
    private ItemClickListener itemClickListener;
    public Button btnShow;

    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.pnameo);
        phone = itemView.findViewById(R.id.phoneo);
        adrress = itemView.findViewById(R.id.addreso);
        datetime = itemView.findViewById(R.id.timeo);
        totalprice = itemView.findViewById(R.id.totalpriceo);
        btnShow = itemView.findViewById(R.id.btnshow);
        state = itemView.findViewById(R.id.stateo);


    }
    @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

    }


