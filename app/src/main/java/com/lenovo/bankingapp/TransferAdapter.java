package com.lenovo.bankingapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {

    List<UserModel> nameModels;
    Context context;

    int row_index=-1;

    public TransferAdapter(List<UserModel> nameModels, Context context) {
        this.nameModels = nameModels;
        this.context = context;
    }

    @NonNull
    @Override
    public TransferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);

        return new TransferAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransferAdapter.ViewHolder holder, int position) {
        holder.name.setText(nameModels.get(position).getName());

        holder.setItemClickInterface(new ItemClickInterface() {
            @Override
            public void onClick(View view, int position) {
                row_index=position;
                Common.currentItem=nameModels.get(position);
                notifyDataSetChanged();
            }
        });
        if (row_index==position)
            holder.itemView.setBackgroundColor(Color.parseColor("#3700B3"));
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));

    }

    @Override
    public int getItemCount() {
        return nameModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView name;
        ItemClickInterface itemClickInterface;

        public void setItemClickInterface(ItemClickInterface itemClickInterface) {
            this.itemClickInterface = itemClickInterface;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name5);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickInterface.onClick(v,getAdapterPosition());
        }
    }
}
