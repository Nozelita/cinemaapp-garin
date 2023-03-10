package com.garin.cinema.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.garin.cinema.model.MovieModel;
import com.garin.cinema.retrofit.Constant;
import com.garin.cinema.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final List<MovieModel.Results> results;
    private final AdapterListener listener;

    public MainAdapter(Context ignoredContext, List<MovieModel.Results> results, AdapterListener listener) {
        this.results    = results ;
        this.listener   = listener ;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main,
                        parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder viewHolder, int i) {
        final MovieModel.Results result = results.get(i);

        viewHolder.txtTitle.setText( result.getTitle());

        Picasso.get()
                .load(Constant.BACKDROP_PATH+ result.getBackdrop_path())
                .placeholder(R.drawable.placeholder_portrait)
                .fit(). centerCrop()
                .into(viewHolder.imgPoster);

        viewHolder.constraintLayout.setOnClickListener(v -> {

            Constant.MOVIE_ID = String.valueOf( result.getId() );
            Constant.MOVIE_TITLE = result.getTitle();

            listener.onClick();
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        ImageView imgPoster;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            constraintLayout = itemView.findViewById(R.id.container);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<MovieModel.Results> newResults) {
        results.clear();
        results.addAll(newResults);
        notifyDataSetChanged();
//        notifyItemRangeRemoved(0, newResults.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataNextPage(List<MovieModel.Results> newResults) {
        results.addAll(newResults);
        notifyDataSetChanged();
    }

    public interface AdapterListener {
        void onClick();
    }
}
