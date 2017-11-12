package com.example.dhruv.mynewsfeed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dhruve on 10/18/2017.
 */

public class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<News> newsList;
    CustomItemClickListener listener;//for the implementation of onItemClickListener on recyclerViewAdapter

    public RecyclerNewsAdapter(List<News> list, CustomItemClickListener customItemClickListener) {
        newsList = list;
        listener = customItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);
        //implementing the onItemClickListener using the interface class created
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mv) {
                listener.onItemClick(mv, holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News b = newsList.get(position);
        ((MyViewHolder) holder).titleTextView.setText(b.getTitle());
        ((MyViewHolder) holder).authorTextView.setText(b.getAuthor());
        ((MyViewHolder) holder).dateView.setText(b.getDate());
        ((MyViewHolder) holder).sectionView.setText(b.getSection());

    }

    @Override
    public int getItemCount() {
        return null!=newsList?newsList.size():0;
    }

    @Override
    public void onClick(View view) {

    }

    public News getItem(int position) {
        return newsList.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        public TextView titleTextView;
        @BindView(R.id.author)
        TextView authorTextView;
        @BindView(R.id.date)
        TextView dateView;
        @BindView(R.id.section) TextView sectionView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
