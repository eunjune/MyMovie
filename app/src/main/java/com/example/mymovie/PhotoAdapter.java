package com.example.mymovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mymovie.network.ImageLoadTask;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private String[] items;
    private OnItemClickListener listener;
    private int videoStartIndex;

    public static interface OnItemClickListener {
        void onItemClick(ViewHolder holder, String url, int position);
    }

    public PhotoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.photo_item_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String url = items[position];
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setItem(url);

        viewHolder.setOnItemClickListener(listener);
        viewHolder.setVideoStartIndex(videoStartIndex);

    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public int getVideoStartIndex() {
        return videoStartIndex;
    }

    public void setVideoStartIndex(int videoStartIndex) {
        this.videoStartIndex = videoStartIndex;
    }

    public void addItems(String[] items) {
        this.items = items;
    }

    public String getItem(int position) {
        return items[position];
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private OnItemClickListener listener;
        private ImageView imageView;
        private int videoStartIndex;
        private String url;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(ViewHolder.this, url, getAdapterPosition());
                    }
                }
            });
        }

        public void setItem(String url) {
            this.url = url;
            ImageLoadTask imageLoadTask = new ImageLoadTask(url,imageView);
            imageLoadTask.execute();
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public int getVideoStartIndex() {
            return videoStartIndex;
        }

        public void setVideoStartIndex(int videoStartIndex) {
            this.videoStartIndex = videoStartIndex;
        }
    }

}
