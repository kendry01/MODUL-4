package com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4.Model.MenuProduk;
import com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4.R;

import java.util.List;

public class MenuProdukAdapter extends RecyclerView.Adapter<MenuProdukAdapter.MenuProdukViewHolder> {

    private Context mCtx;
    private List<MenuProduk> productList;

    public MenuProdukAdapter(Context mCtx, List<MenuProduk> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MenuProdukViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MenuProdukViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.layout_product, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MenuProdukViewHolder holder, int i) {

        MenuProduk product = productList.get(i);

        holder.textViewName.setText(product.getNamaMenu());
        holder.textViewDesc.setText(product.getDeskripsi());
        holder.textViewPrice.setText("Rp.  " + product.getHarga());
        holder.textViewPrice.setTag(product.getImage());

        Glide.with(mCtx).load(product.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class MenuProdukViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDesc, textViewPrice;
        ImageView imageView;

        public MenuProdukViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.menuname);
            textViewDesc = itemView.findViewById(R.id.hrgamenu);
            textViewPrice = itemView.findViewById(R.id.deskmenu);
            imageView = itemView.findViewById(R.id.gambarmenu);

        }
    }
}
