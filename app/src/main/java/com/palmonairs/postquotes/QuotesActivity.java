package com.palmonairs.postquotes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuotesActivity extends AppCompatActivity{

    private QuotesService quotesService;
    private ArrayList<String> keywords;
    private ProgressBar progress;
    private ArrayList<Quote> quotes;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uri = (Uri) getIntent().getExtras().get("uri");

        quotesService = ApiClient.getQuotesService();
        progress = findViewById(R.id.progress);
        ImageView image = findViewById(R.id.chosen_image);

        Picasso.get().load(uri).resize(285, 285).into(image);

        keywords = new ArrayList<String>();
        keywords.add("sweet");

        getQuotes(keywords);
    }

    private void getQuotes(ArrayList<String> keywords) {
        quotesService.getQuotes(new Payload(keywords)).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if(response.isSuccessful()) {
                    quotes = response.body().getData().getQuotes();
                    doStuff(quotes);
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void doStuff(final ArrayList<Quote> quotes) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        QuotesAdapter mAdapter = new QuotesAdapter(quotes, new QuotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Quote item) {
                Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                intent.putExtra("text", item.getQuoteText());
                intent.putExtra("author", item.getQuoteAuthor());
                intent.putExtra("uri", uri);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

}
