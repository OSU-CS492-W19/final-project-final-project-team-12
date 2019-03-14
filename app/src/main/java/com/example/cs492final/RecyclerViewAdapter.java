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
    private OnTempItemClickListener mOnTempItemClickListener;

    public interface OnTempItemClickListener{
        void onTempItemClick(String s); //Change string s to our data type.
    }

    public RecyclerViewAdapter(OnTempItemClickListener clickListener){
        mOnTempItemClickListener = clickListener;
        mChatList = new ArrayList<String>();
    }

    public void addChat(String message){
        //Puts the most recent message at the bottom of the displayed list
        mChatList.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if(mChatList != null) {
            return mChatList.size();
        }
        else{
            return 0;
        }
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

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mChatTV;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mChatTV = itemView.findViewById(R.id.tv_rvitem);
            itemView.setOnClickListener(this);
        }

        public void bind(String chat){
            mChatTV.setText(chat);
        }

        @Override
        public void onClick(View v) {
            String s = mChatList.get(getAdapterPosition());
            mOnTempItemClickListener.onTempItemClick(s);
        }
    }
}
