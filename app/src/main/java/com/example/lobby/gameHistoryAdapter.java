package com.example.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class gameHistoryAdapter extends RecyclerView.Adapter<gameHistoryAdapter.MyViewHolderGameHistory> {
    private ArrayList<roomCard> roomList;

    public gameHistoryAdapter(ArrayList<roomCard> lst) {
        roomList = lst;
    }

    public class MyViewHolderGameHistory extends RecyclerView.ViewHolder {
        private TextView userTxt;
        private Button joinButtun;

        public MyViewHolderGameHistory(final View view) {
            super(view);
            this.userTxt = view.findViewById(R.id.userTextView);
            this.joinButtun = view.findViewById(R.id.viewGameBtn);

        }
    }

    @NonNull
    @Override
    public gameHistoryAdapter.MyViewHolderGameHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_history_item, parent, false);
        return new MyViewHolderGameHistory(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull gameHistoryAdapter.MyViewHolderGameHistory holder, int position) {
        String name = roomList.get(position).getUserName();
        holder.userTxt.setText(name);
        holder.joinButtun.setTag(roomList.get(position).getRoomID() + "," + name);
        //if(roomList.get(position).getPlayerTwo().equals("null"))  ACTIVE THEN 2 PLAYERS
        //    holder.joinButtun.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}

