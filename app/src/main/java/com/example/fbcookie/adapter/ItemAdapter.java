package com.example.fbcookie.adapter;

import com.example.fbcookie.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbcookie.activity.VirtualActivity;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private Context context;

    public ItemAdapter(List<Item> itemList, Context context) {
    this.itemList = itemList;
    this.context = context;
}
    
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

   @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position); // Get the Item object
        holder.titleTextView.setText(item.getTitle());
        holder.itemView.setOnClickListener(view -> {
            if (context instanceof VirtualActivity) {
                ((VirtualActivity) context).openItemWebView(item); // Pass the Item object
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}