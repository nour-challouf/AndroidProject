package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private List<Delivery> deliveryList;
    private Context context;

    public DeliveryAdapter(Context context, List<Delivery> deliveryList) {
        this.context = context;
        this.deliveryList = deliveryList;
    }

    @Override
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliveryViewHolder holder, int position) {
        Delivery delivery = deliveryList.get(position);
        // Bind your data to the views
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public void updateDeliveryList(List<Delivery> newDeliveryList) {
        this.deliveryList = newDeliveryList;
        notifyDataSetChanged(); // Notify that the dataset has changed
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        public DeliveryViewHolder(View itemView) {
            super(itemView);
            // Initialize your views
        }
    }
}
