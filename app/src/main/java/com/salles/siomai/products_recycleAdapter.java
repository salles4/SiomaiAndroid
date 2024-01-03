package com.salles.siomai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class products_recycleAdapter extends RecyclerView.Adapter<products_recycleAdapter.MyViewHolder> {
    Context context;
    ArrayList<productsModel> model;
    public products_recycleAdapter(Context context, ArrayList<productsModel> model){
        this.context = context;
        this.model = model;
    }
    @NonNull
    @Override
    public products_recycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new products_recycleAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull products_recycleAdapter.MyViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.productName.setText(model.get(position).getName());
        holder.counter.setText(model.get(position).getCount());
        holder.stock.setText(model.get(position).getStock());

        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView productName, counter, stock;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameView);
            counter = itemView.findViewById(R.id.counterView);
            stock = itemView.findViewById(R.id.stockDisplayView);
        }
    }
}
