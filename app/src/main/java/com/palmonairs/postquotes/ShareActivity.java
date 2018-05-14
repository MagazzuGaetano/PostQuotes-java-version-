package com.palmonairs.postquotes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.squareup.picasso.Picasso;

public class ShareActivity extends AppCompatActivity {

    private Uri uri;
    private ImageView immg;
    private FloatingActionButton fab;
    private TextView quoteTxt, authorTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quoteTxt = findViewById(R.id.quote_txt);
        authorTxt = findViewById(R.id.author_txt);
        fab = findViewById(R.id.share_btn);
        immg = findViewById(R.id.imageview);

        quoteTxt.setText(getIntent().getExtras().get("text").toString());
        authorTxt.setText(getIntent().getExtras().get("author").toString());

        uri = (Uri) getIntent().getExtras().get("uri");
        Picasso.get().load(uri).resize(285, 285).into(immg);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareQuote = quoteTxt.getText().toString();
                String shareAuthor = authorTxt.getText().toString();
                Intent shareIntent;
                Bitmap bitmap = drawMultilineTextToBitmap(getApplicationContext(), immg, "“" + shareQuote + "”\n" + "- " + shareAuthor + " -");
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.png";
                OutputStream out = null;
                File file = new File(path);
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 70, out); //100
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                path = file.getPath();
                Uri bmpUri = Uri.parse("file://" + path);
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent,"Share with"));
            }
        });

    }

    public Bitmap drawMultilineTextToBitmap(Context gContext, ImageView imageView, String gText) {
        // prepare canvas
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);

        // new antialiased Paint
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(255, 255, 255)); //61
        // text size in pixels
        paint.setTextSize((int) (14 * scale)); //14
        // text shadow
        paint.setShadowLayer(5f, 0f, 5f, Color.BLACK);//1, 0, 1

        // set text width to canvas width minus 16dp padding
        int textWidth = canvas.getWidth() - (int) (16 * scale);

        // init StaticLayout for text
        StaticLayout textLayout = new StaticLayout(
                gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // get height of multiline text
        int textHeight = textLayout.getHeight();

        // get position of text's top left corner
        float x = (bitmap.getWidth() - textWidth)/2;
        float y = (bitmap.getHeight() - textHeight)/2;

        // draw text to the Canvas center
        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bitmap;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent i = new Intent(getApplicationContext(), QuotesActivity.class);
        i.putExtra("uri", uri);
        startActivity(i);
        return true;
    }
}
