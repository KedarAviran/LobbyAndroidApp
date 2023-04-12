package com.example.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList<roomCard> roomList;
    public recyclerAdapter(ArrayList<roomCard> lst)
    {
        roomList = lst;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView userTxt;
        private Button joinButtun;

        public MyViewHolder(final View view) {
            super(view);
            this.userTxt = view.findViewById(R.id.userTextView);
            this.joinButtun = view.findViewById(R.id.button);
        }
    }
    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String name = roomList.get(position).getUserName();
        holder.userTxt.setText(name);

    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}

