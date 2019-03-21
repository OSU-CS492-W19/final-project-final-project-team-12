package com.example.cs492final;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cs492final.data.HistoryItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<HistoryItem> mInfo;

    OnHistoryClickListener mHistoryClickListener;

    public interface OnHistoryClickListener{
        void onHistoryClick(HistoryItem info);
    }

    public HistoryAdapter(OnHistoryClickListener clickListener){
        mHistoryClickListener = clickListener;
    }

    public void updateSearchResults(List<HistoryItem> infos){
        mInfo = infos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if(mInfo != null){
            return mInfo.size();
        }
        else{
            return 0;
        }
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_list_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position){
        holder.bind(mInfo.get(position));
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView mLocation;

        public HistoryViewHolder(View itemView){
            super(itemView);
            mLocation = itemView.findViewById(R.id.tv_history);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    HistoryItem historyItem = mInfo.get(getAdapterPosition());
                    mHistoryClickListener.onHistoryClick(historyItem);
                }
            });
        }

        public void bind(HistoryItem info){
            mLocation.setText(info.StockName);
        }
    }

}
