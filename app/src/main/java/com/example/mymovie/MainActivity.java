package com.example.mymovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView likeCountView,dislikeCountView;
    private ImageView likeButton,dislikeButton;

    private int likeCount;
    private  int dislikeCount;
    private boolean likeState = false;
    private boolean dislikeState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        likeButton = (ImageView) findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likeState) {
                    descLikeCount();
                } else {
                    if(dislikeState) {
                        descDisLikeCount();
                        dislikeState = !dislikeState;
                        dislikeButton.setSelected(dislikeState);
                    }
                    incrLikeCount();
                }

                likeState = !likeState;
                likeButton.setSelected(likeState);

            }
        });

        likeCountView = (TextView) findViewById(R.id.likeCountView);
        likeCount = Integer.valueOf(likeCountView.getText().toString());

        dislikeButton = (ImageView) findViewById(R.id.dislikeButton);
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dislikeState) {
                    descDisLikeCount();
                } else {
                    if(likeState) {
                        descLikeCount();
                        likeState = !likeState;
                        likeButton.setSelected(likeState);
                    }
                    incrDisLikeCount();
                }

                dislikeState = !dislikeState;
                dislikeButton.setSelected(dislikeState);

            }
        });

        dislikeCountView = (TextView) findViewById(R.id.dislikeCountView);
        dislikeCount = Integer.valueOf(dislikeCountView.getText().toString());

        ListView listView = (ListView)findViewById(R.id.listView);
        CommentAdapter adapter = new CommentAdapter();

        adapter.items.add(new CommentItem("kym71**"
                , "적당히 재밌다. 오랜만에 잠 안오는 영화봤네요."
                , 10, 5, 0));

        adapter.items.add(new CommentItem("kym71**"
                , "적당히 재밌다. 오랜만에 잠 안오는 영화봤네요."
                , 10, 5, 0));

        listView.setAdapter(adapter);

    }

    public void incrLikeCount() {
        likeCount++;
        likeCountView.setText(String.valueOf(likeCount));
    }

    public void descLikeCount() {
        likeCount--;
        likeCountView.setText(String.valueOf(likeCount));
    }

    public void incrDisLikeCount() {
        dislikeCount++;
        dislikeCountView.setText(String.valueOf(dislikeCount));
    }

    public void descDisLikeCount() {
        dislikeCount--;
        dislikeCountView.setText(String.valueOf(dislikeCount));
    }

    class CommentAdapter extends BaseAdapter {

        private ArrayList<CommentItem> items = new ArrayList<CommentItem>();

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
                view = new CommentItemView(getApplicationContext());
            } else {
                view = (CommentItemView)convertView;
            }

            view.addId(items.get(position).getId());
            view.addCommentRating(items.get(position).getRating());
            view.addContent(items.get(position).getContent());
            view.addCreateTime(items.get(position).getCreateTime());
            view.addRecommendation(items.get(position).getRecommendation());

            return view;
        }
    }
}
