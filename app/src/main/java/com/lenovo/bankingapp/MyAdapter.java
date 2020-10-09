package com.lenovo.bankingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<UserModel> userModels;
    Context context;

    public String name2;
    public String balance2;
    public String mail;

    public MyAdapter(Context context,List<UserModel> userModels) {
        this.userModels = userModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_user,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        name2=userModels.get(position).getName();
        balance2=userModels.get(position).getBalance();
        mail=userModels.get(position).getEmail();

        holder.setData(name2,balance2);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,DetailsActivity.class);
                i.putExtra("email",userModels.get(position).getEmail());
                i.putExtra("name",name2);
                i.putExtra("balance",balance2);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView balance;
        LinearLayout view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            balance=itemView.findViewById(R.id.balance);
            view=itemView.findViewById(R.id.view1);
        }
        private void setData(String name1,String balance1)
        {
            name.setText(name1);
            balance.setText("Rs."+balance1+"/-");
        }
    }
}
