package com.yfo.urlshortener;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.URL;

public class ResultActivity extends AppCompatActivity {
    TextView tvResultUrl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvResultUrl = (TextView)findViewById(R.id.tv_result);
        final String shortUrl = getIntent().getStringExtra(MainActivity.URL_SHORT);
        tvResultUrl.setText(shortUrl);
        tvResultUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(shortUrl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(uri);
                startActivity(browserIntent);
            }
        });
    }

    /*@Override
    protected void onNewIntent(Intent intent){
        final String shortUrl  = intent.getStringExtra(MainActivity.URL_SHORT);

    }*/
}
