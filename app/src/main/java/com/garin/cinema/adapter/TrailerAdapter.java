package com.garin.cinema.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.garin.cinema.model.TrailerModel;
import com.garin.cinema.R;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private final List<TrailerModel.Results> results;
    private final OnAdapterListener listener;

    public TrailerAdapter(Context ignoredContext, List<TrailerModel.Results> results, OnAdapterListener listener) {
        this.results    = results ;
        this.listener   = listener ;
    }

    @NonNull
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_trailer,
                        parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerAdapter.ViewHolder viewHolder, int i) {
        final TrailerModel.Results result = results.get(i);
        viewHolder.textView.setText( result.getName());

        viewHolder.textView.setOnClickListener(v -> listener.OnClick( result.getKey() ));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<TrailerModel.Results> newResults){
        results.clear();
        results.addAll(newResults);
        notifyDataSetChanged();
        listener.OnVideo( results.get(0).getKey() );
    }

    public interface OnAdapterListener {
        void OnClick(String key);
        void OnVideo(String key);
    }
}
