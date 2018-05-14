package com.palmonairs.postquotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Quote item);
    }

    private List<Quote> quotes;
    private final OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text, author;

        public MyViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.text);
            author = (TextView) view.findViewById(R.id.author);
        }

        public void bind(final Quote quote, final OnItemClickListener listener) {
            text.setText(quote.getQuoteText());
            author.setText(quote.getQuoteAuthor());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(quote);
                }
            });
        }
    }

    public QuotesAdapter(List<Quote> quotes, OnItemClickListener listener) {
        this.quotes = quotes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Quote quote = quotes.get(position);
        holder.text.setText(quote.getQuoteText());
        holder.author.setText(quote.getQuoteAuthor());
        holder.bind(quote, listener);
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }
}