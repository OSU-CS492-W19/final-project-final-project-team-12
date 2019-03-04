package com.example.cs492final;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    private ArrayList<String> mChatList;

    public RecyclerViewAdapter(){
        mChatList = new ArrayList<String>();
    }

    public void addChat(String message){
        //Puts the most recent message at the bottom of the displayed list
        mChatList.add(0,message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return mChatList.size();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.chat_list_item, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i){
        String chat = mChatList.get(mChatList.size()-i-1);
        recyclerViewHolder.bind(chat);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView mChatTV;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mChatTV = itemView.findViewById(R.id.tv_rvitem);
        }

        public void bind(String chat){
            mChatTV.setText(chat);
        }
    }
}
