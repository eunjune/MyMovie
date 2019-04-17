package com.example.mymovie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private ArrayList<CommentItem> items;
    private Context context;

    public CommentAdapter(ArrayList<CommentItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public ArrayList<CommentItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CommentItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentItemView view;

        if(convertView == null) {
            view = new CommentItemView(context);
        } else {
            view = (CommentItemView)convertView;
        }

        view.addId(items.get(position).getId());
        view.addCommentRating(items.get(position).getRating());
        view.addContent(items.get(position).getContent());
        view.addCreateTime(items.get(position).getCreateTime(),context);
        view.addRecommendation(items.get(position).getRecommendation(),context);

        return view;
    }
}
