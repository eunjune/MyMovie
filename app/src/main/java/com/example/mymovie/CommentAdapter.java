package com.example.mymovie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mymovie.data.CommentInfo;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private ArrayList<CommentInfo> items;
    private Context context;

    private int total;

    public CommentAdapter(ArrayList<CommentInfo> items, Context context) {
        this.items = items;

        this.context = context;
    }

    public ArrayList<CommentInfo> getItems() {
        return items;
    }

    public void setItems(ArrayList<CommentInfo> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

        if (convertView == null) {
            view = new CommentItemView(context);
        } else {
            view = (CommentItemView) convertView;
        }

        view.setReviewId(items.get(position).getId());
        view.addWriter(items.get(position).getWriter());
        view.addCommentRating(items.get(position).getRating());
        view.addContent(items.get(position).getContents());
        view.addCreateTime(items.get(position).getTime().toString(), context);
        view.addRecommendation(items.get(position).getRecommend(), context);

        return view;
    }
}
