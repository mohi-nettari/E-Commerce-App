package com.example.ecommerce.ViewHolder;

import static android.os.Build.VERSION_CODES.R;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtPname,txtPprice,txtPquantity;
    public ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtPname = itemView.findViewById(com.example.ecommerce.R.id.pnamec);
        txtPprice = itemView.findViewById(com.example.ecommerce.R.id.ppricec);
        txtPquantity = itemView.findViewById(com.example.ecommerce.R.id.pquantityc);

    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
