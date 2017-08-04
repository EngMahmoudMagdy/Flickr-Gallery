package com.magdy.flickrgallery.UI;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.magdy.flickrgallery.Adapters.GridRecyclerAdapter;
import com.magdy.flickrgallery.BuildConfig;
import com.magdy.flickrgallery.Data.Contract;
import com.magdy.flickrgallery.GridInfoListener;
import com.magdy.flickrgallery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements GridInfoListener {

    GridRecyclerAdapter gridAdapter;
    RecyclerView gridView;
    List<String> images, partLinks;
    SwipeRefreshLayout swipeRefreshLayout;
    StaggeredGridLayoutManager layoutManager;
    int lastitem, total;
    int[] lasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        images = new ArrayList<>();
        partLinks = new ArrayList<>();
        gridView = (RecyclerView) findViewById(R.id.recyclerView);
        gridAdapter = new GridRecyclerAdapter(this, partLinks, this);

        if (getResources().getConfiguration().orientation == 2) {
            layoutManager = new StaggeredGridLayoutManager(3, 1);
        } else {
            layoutManager = new StaggeredGridLayoutManager(2, 1);
        }
        gridView.setHasFixedSize(false);
        gridView.setLayoutManager(layoutManager);
        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();


        gridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                total = layoutManager.getItemCount();
                lasts = layoutManager.findLastVisibleItemPositions(null);
                if (lasts != null) {
                    lastitem = lasts[0];
                    for (int i = 1; i < lasts.length; i++) {
                        if (lastitem < lasts[i])
                            lastitem = lasts[i];
                    }
                    if (total <= lastitem + 20) {
                        if (images != null) {
                            for (int i = 0; (i < images.size() / 5) && (images.size() > i + lastitem); i++) {
                                partLinks.add(images.get(i + lastitem));
                            }
                            gridAdapter.notifyDataSetChanged();
                        }

                    }
                }

            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }

        }, 0, 60000);
        //one minute before updating

    }

    void update() {
        swipeRefreshLayout.setRefreshing(true);
        FetchImagesTask imagesTask = new FetchImagesTask();
        imagesTask.execute();
    }

    @Override
    public void setSelected(int imagePosition) {
        Intent intent = new Intent(this, FullScreenActivity.class); //pass the position
        intent.putExtra("pos", imagePosition);
        startActivity(intent);
        //Toast.makeText(this,"Pos: "+imagePosition,Toast.LENGTH_SHORT).show();

    }


    private class FetchImagesTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String imagesJsonStr = null;

            try {

                //https://api.flickr.com/services/rest/?
                // method=flickr.interestingness.getList
                // &api_key=29d19cae84892fc8f991e377ff92fe0e
                // &format=json
                // &nojsoncallback=1
                final String MOVIE_DB_BASE_URL = "https://api.flickr.com/services/rest/?";
                final String METHOD = "method";
                final String APPID_PARAM = "api_key";
                final String FORMAT = "format";
                final String NOJSONCB = "nojsoncallback";


                final String mthodType = "flickr.interestingness.getList";
                final String formaatType = "json";
                final String NOJSONCBType = "1";


                Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                        .appendQueryParameter(METHOD, mthodType)
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_FLICR_API_KEY)
                        .appendQueryParameter(FORMAT, formaatType)
                        .appendQueryParameter(NOJSONCB, NOJSONCBType)
                        .build();


                URL url = new URL(builtUri.toString());

                Log.v("message", "Built URI " + builtUri.toString());
                // Create the request to Flickr API, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                imagesJsonStr = buffer.toString();
                Log.v("message", "Images JSON String " + imagesJsonStr);

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {

                return getImagesData(imagesJsonStr);


            } catch (JSONException e) {
                Log.e("message", e.getMessage(), e);
                e.printStackTrace();


            }
            return null;
        }

        private List<String> getImagesData(String imagesJsonStr) throws JSONException {
            List<String> strings = new ArrayList<>();
            final String PHOTOS_BASE = "photos";
            final String PHOTO = "photo";
            final String ID = "id";
            final String SECRET = "secret";
            final String SERVER = "server";
            final String FARM = "farm";
            JSONObject imagesJson = new JSONObject(imagesJsonStr);
            JSONArray imagesArray = imagesJson.getJSONObject(PHOTOS_BASE).getJSONArray(PHOTO);
            for (int i = 0; i < imagesArray.length(); i++) {
                JSONObject image = imagesArray.getJSONObject(i);
                String s = String.format(Locale.US,
                        "https://farm%d.staticflickr.com/%s/%s_%s.jpg",
                        image.getInt(FARM),
                        image.getString(SERVER),
                        image.getString(ID),
                        image.getString(SECRET));
                strings.add(s);
            }

            return strings;
        }

        void changeDB(List<String> strings) {
            Cursor c = getContentResolver().query(Contract.Image.URI, null, Contract.Image.COLUMN_IMAGE_LINK + " = \"" + strings.get(0) + "\"", null, null);
            if (c != null) {
                if (c.getCount() == 0) {
                    getContentResolver().delete(Contract.Image.URI, null, null);
                    for (String s : strings) {
                        ContentValues quoteCV = new ContentValues();
                        quoteCV.put(Contract.Image.COLUMN_IMAGE_LINK, s);
                        getContentResolver().insert(Contract.Image.URI, quoteCV);
                    }
                    Intent dataUpdatedIntent = new Intent(Contract.ACTION_DATA_UPDATED);
                    sendBroadcast(dataUpdatedIntent);
                }
                c.close();
            }

        }

        @Override

        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            if (strings != null) {
                images.clear();
                partLinks.clear();
                for (String s : strings)
                    images.add(s);
                for (int i = 0; i < images.size() / 5; i++) {
                    partLinks.add(strings.get(i));
                }

                gridAdapter.notifyDataSetChanged();
                changeDB(strings);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
