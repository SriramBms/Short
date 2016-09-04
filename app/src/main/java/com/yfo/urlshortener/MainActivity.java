package com.yfo.urlshortener;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    public static final String URL_SHORT="url";
    private static final String URL_TINYURL = "https://tinyurl.com/api-create.php?url=";
    HashMap<String, String> urlCache = new HashMap();

    Button shortenButton;
    EditText titleText=null;
    EditText urlText=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shortenButton = (Button)findViewById(R.id.button_shorten);
        titleText = (EditText)findViewById(R.id.title_input);
        urlText = (EditText)findViewById(R.id.url_input);

        //TODO: populate cache, read from file

        shortenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create req
                String title = titleText.getText().toString();
                String url = urlText.getText().toString();
                checkAndQuery(title, url);
            }
        });

    }

    private void checkAndQuery(String title, String url){
        boolean isInputInvalid = false;
        if(title=="" || title==null ){
            titleText.setError(getString(R.string.error_title));
            isInputInvalid = true;
        }
        if(url=="" || url==null){
            urlText.setError(getString(R.string.error_url));
            isInputInvalid |= true;
        }
        //TODO: Input validation. URLs need to start with http://

        if(isInputInvalid) return;

        if(urlCache.containsKey(url)){
            launchResultActivity(urlCache.get(url));
            return;
        }

        getShortUrl(url);



    }

    private void launchResultActivity(String result){
        Intent intent = new Intent(this,ResultActivity.class);
        intent.putExtra(URL_SHORT, result);
        startActivity(intent);

    }

    private void getShortUrl(final String urlString){
        new AsyncTask<String, Void, String>(){
            String resultUrl;


            @Override
            protected String doInBackground(String... params) {
                //TODO: progress bar
                String url = params[0];
                String tinyURLQuery = URL_TINYURL+url;
                URL urlLong = null;
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                try {
                    urlLong = new URL(tinyURLQuery);
                    connection = (HttpURLConnection)urlLong.openConnection();
                    inputStream = connection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    resultUrl = bufferedReader.readLine();
                    Utils.printLog(TAG, "1. Result:"+resultUrl);

                }catch(MalformedURLException e1){
                    e1.printStackTrace();

                }catch(IOException e){
                    e.printStackTrace();

                }finally{
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if(connection!=null){
                            connection.disconnect();
                        }
                        if(bufferedReader!=null){
                            bufferedReader.close();
                        }

                    }catch(IOException e2){
                        e2.printStackTrace();
                    }
                }
                return resultUrl;
            }

            @Override
            protected void onPostExecute(String url){
                Utils.printLog(TAG, "2. Result url:"+url);
                urlCache.put(urlString, url);
                launchResultActivity(url);
            }
        }.execute(urlString);


    }


    @Override
    protected void onDestroy(){
        //TODO:  save cache

        super.onDestroy();
    }




}
