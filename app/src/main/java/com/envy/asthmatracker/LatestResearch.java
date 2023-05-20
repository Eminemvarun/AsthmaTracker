package com.envy.asthmatracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.envy.asthmatracker.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LatestResearch extends AppCompatActivity {

    NewsAdapter mynewsadapter;
    RecyclerView myRecyclerView;
    LinearLayout linearLayout, linearLayoutLoading;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_research);
        //Setup R references and empty Recycler View
        swipeRefreshLayout = findViewById(R.id.swipeRefresh1);
        linearLayoutLoading = findViewById(R.id.llLoading);
        linearLayout = findViewById(R.id.layout1);
        myRecyclerView = findViewById(R.id.recyclerViewResearch);
        progressBar2 = findViewById(R.id.progressTwo);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
        layoutManager = new GridLayoutManager(LatestResearch.this, 2, GridLayoutManager.VERTICAL, false);
        }
        else{
            layoutManager = new GridLayoutManager(LatestResearch.this, 1, GridLayoutManager.VERTICAL, false);
        }
        myRecyclerView.setLayoutManager(layoutManager);
        mynewsadapter = new NewsAdapter(LatestResearch.this,new ArrayList<>());
        myRecyclerView.setAdapter(mynewsadapter);
        myRecyclerView.setHasFixedSize(true);


        if(isCacheFileExists(LatestResearch.this)){
            linearLayout.setVisibility(View.VISIBLE);
            linearLayoutLoading.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            ArrayList<NewsDataClass> mylist = readDataFromCache(LatestResearch.this);
            if(!mylist.isEmpty()){
                mylist = readImagesFromCache(LatestResearch.this,mylist);
            myRecyclerView.setHasFixedSize(true);
            mynewsadapter.addAll(mylist);
            mynewsadapter.notifyDataSetChanged();
            }
            else{
                Toast.makeText(LatestResearch.this, "Data Returned is empty", Toast.LENGTH_SHORT).show();
            }
        }else{

        downloadData downloadData = new downloadData();
        downloadData.execute();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar2.setVisibility(View.VISIBLE);
                downloadData downloadData = new downloadData();
                downloadData.execute();
            }
        });
        // End of On Create
    }

    public class downloadData extends AsyncTask<String,zMyDataType, ArrayList<NewsDataClass>> {
        TextView loadingtext;
        ProgressBar progressBar2;
        LinearLayout linearLayoutLoading;
        LinearLayout linearLayout;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2 = findViewById(R.id.progressTwo);
            loadingtext = findViewById(R.id.tvDownloading);
            linearLayoutLoading = findViewById(R.id.llLoading);
            linearLayout = findViewById(R.id.layout1);
            mynewsadapter.clearAll();
            }

        @Override
        protected void onProgressUpdate(zMyDataType... values) {
            int progress = values[0].getPosition();
            String text = "Downloading \n";
            loadingtext.setText(text);
            progressBar2.setIndeterminate(false);
            progressBar2.setProgress(progress,true);

            if(values[0].getData() != null) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayoutLoading.setVisibility(View.GONE);
                mynewsadapter.addData(values[0].getData());
                mynewsadapter.notifyDataSetChanged();
            }

            super.onProgressUpdate(values);

        }

        @Override
        protected ArrayList<NewsDataClass> doInBackground(String... params) {

            try {
                Log.i("vlogs", "doInBackground trying");
                String query = "asthma%20OR%20allergy";
                URL url = new URL("https://newsdata.io/api/1/news?apikey=pub_22043a7d9e1ae8f0b692643c00ed38cc08faa&language=en&qInTitle=" + query);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("X-ACCESS-KEY", "pub_22043a7d9e1ae8f0b692643c00ed38cc08faa");
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == 200) {
                    publishProgress(new zMyDataType(null,20));
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String data = "";
                    String line = "";
                    while (line != null) {
                        line = bufferedReader.readLine();
                        data = data + line;
                    }
                    Log.i("vlogs", "Data Recieved Sucessfully: " + data.toString());
                    publishProgress(new zMyDataType(null,50));
                    //
                    //Date saved to data string
                    //

                    //
                    ArrayList<NewsDataClass> dataObject = new ArrayList<>();
                    JSONObject rootJSON = new JSONObject(data);
                    JSONArray results = rootJSON.getJSONArray("results");
                    int length = results.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject articleone = results.getJSONObject(i);
                        String two = articleone.getString("title");
                        String three = articleone.getString("link");
                        String one = articleone.getString("image_url");
                        String four = articleone.getString("pubDate");
                        String five = articleone.getString("description");
                        String six = articleone.getString("source_id");
                        NewsDataClass newsDataClass = new NewsDataClass(one, two, three,four,five,six);
                        Log.i("vlogs", "Image url is :" + one);
                        //
                        //Downloading Image
                        //
                        if(one!=null && !one.equals("null")) {
                            URL url2 = new URL(one);
                            HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            if (connection.getResponseCode() == 200) {
                                InputStream input = connection.getInputStream();
                                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                                Log.e("vlogs", "Bitmap returned of size: " + myBitmap.getByteCount()/1024/1024 +"Mb");
                                if(myBitmap.getByteCount()/1024/1024 <100){
                                newsDataClass.setImage(myBitmap);
                                newsDataClass.setImageAvailable(true);
                                }
                            }
                        }
                        dataObject.add(newsDataClass);
                        publishProgress(new zMyDataType(newsDataClass,(int)(i+1)*100/length/2 +50));
                    }
                    return dataObject;
                }
                Log.i("vlogs", "Request Incomplete Error Code : " + httpURLConnection.getResponseCode()
                + "\n Error Details : " + httpURLConnection.getResponseMessage() );
            } catch (Exception e) {
                Log.i("vlogs", "Exception caught: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsDataClass> s) {
            super.onPostExecute(s);
            if (s != null) {
                progressBar2.setVisibility(View.GONE);
                myRecyclerView = findViewById(R.id.recyclerViewResearch);
                swipeRefreshLayout.setRefreshing(false);
                myRecyclerView.setHasFixedSize(true);
                writeDataToCache(s,LatestResearch.this);
                writeImagesToCache(s,LatestResearch.this);
            } else{
                Toast.makeText(LatestResearch.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                if(!isCacheFileExists(LatestResearch.this)){
                    finish();
                }
                else{
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar2.setVisibility(View.GONE);
                }
            }
        }
    }

    public void writeDataToCache(ArrayList<NewsDataClass> data, Context context) {

        // Get a reference to the cache directory
        File cacheDir = context.getCacheDir();

        // Create a file in the cache directory to store the data
        File cacheFile = new File(cacheDir, "my_data.json");

        // Convert the data to a JSON string
        Gson gson = new Gson();
        String jsonData = gson.toJson(data);

        // Log the JSON string
        Log.i("vlogs", "JSON data to be written: " + jsonData);

        // Write the data to the cache file
        try {
            FileOutputStream fos = new FileOutputStream(cacheFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(jsonData);
            writer.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error Writing cache", Toast.LENGTH_SHORT).show();
        }
    }



    public ArrayList<NewsDataClass> readDataFromCache(Context context) {
        ArrayList<NewsDataClass> data = null;

        // Get a reference to the cache directory
        File cacheDir = context.getCacheDir();

        // Create a file object for the cache file
        File cacheFile = new File(cacheDir, "my_data.json");

        // Read the data from the cache file
        try {
            FileReader reader = new FileReader(cacheFile);
            StringBuilder builder = new StringBuilder();

            int character;
            while ((character = reader.read()) != -1) {
                builder.append((char) character);
            }

            String jsonData = builder.toString();
            Log.i("vlogs", "readDataFromCache is  " + jsonData);
            JSONArray jsonArray = new JSONArray(jsonData);
            data = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                NewsDataClass item = new NewsDataClass(jsonObject.getString("imageLink"),jsonObject.getString("title"),
                        jsonObject.getString("link"),jsonObject.getString("date"),jsonObject.getString("description")
                ,jsonObject.getString("source"));
                        item.setImageAvailable(jsonObject.getBoolean("imageAvailable"));
                data.add(item);
            }

            reader.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error Reading cache" +e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return data;
    }

    public boolean isCacheFileExists(Context context) {
        // Get a reference to the cache directory
        File cacheDir = context.getCacheDir();

        // Create a file object for the cache file
        File cacheFile = new File(cacheDir, "my_data.json");

        // Check if the cache file exists
        return cacheFile.exists();
    }

    public void writeImagesToCache(ArrayList<NewsDataClass> data, Context context) {
        // Get a reference to the cache directory
        File cacheDir = context.getCacheDir();

        for (NewsDataClass item : data) {
            if(item.isImageAvailable()) {
                // Create a file object for the image file
                File imageFile = new File(cacheDir, item.getImageName());

                // Write the image to the file
                try {
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    item.getImage().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error Writing image cache", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public ArrayList<NewsDataClass> readImagesFromCache(Context context, ArrayList<NewsDataClass> data) {
        // Get a reference to the cache directory
        File cacheDir = context.getCacheDir();

        for (NewsDataClass item : data) {
            if(item.imageAvailable) {
                // Create a file object for the image file
                File imageFile = new File(cacheDir, item.getImageName());

                // Read the image from the file
                try {
                    FileInputStream inputStream = new FileInputStream(imageFile);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    item.setImage(bitmap);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("vlogs","Error Reading image cache: " + e.getMessage());
                }
            }
        }

        return data;
    }


}