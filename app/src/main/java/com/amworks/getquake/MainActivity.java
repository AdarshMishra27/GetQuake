package com.amworks.getquake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthquakeData>> {

    private RecyclerView recyclerView;
    private CustomAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<EarthquakeData> data;

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String SITE_URL="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        //entering dataSet i.e. is eathquake's place where it happened
//        data=new ArrayList<>();
//        data.add(new EarthquakeData(7.2f,"San Francisco","Feb 2, 2016"));
//        data.add(new EarthquakeData(6.1f,"London","July 20, 2015"));
//        data.add(new EarthquakeData(3.9f,"Tokyo","Nov 10, 2014"));
//        data.add(new EarthquakeData(5.4f,"Mexico City","May 3, 2014"));
//        data.add(new EarthquakeData(2.8f,"Moscow","Jan 31, 2013"));
//        data.add(new EarthquakeData(4.9f,"Rio de Janeiro","Aug 19, 2012"));
//        data.add(new EarthquakeData(1.6f,"Paris","Oct 30, 2011"));
//          data=QueryUtils.extractEarthquakes();


        //USING ASYNC TASK
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(SITE_URL);

        //USING LOADERMANAGER
//        LoaderManager loaderManager=getSupportLoaderManager();
//        loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
//        getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID,null,this);

    }

    @NonNull
    @Override
    public Loader<List<EarthquakeData>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(this,SITE_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthquakeData>> loader, List<EarthquakeData> earthquakeData) {
        myAdapter=new CustomAdapter(MainActivity.this,earthquakeData);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthquakeData>> loader) {

    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<EarthquakeData>>
    {
        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {Earthquake}s as the result.
         */
        @Override
        protected List<EarthquakeData> doInBackground(String... urls) {

            if(urls.length<1||urls[0]==null)
                return null;

            List<EarthquakeData> listData=Utils.fetchData(urls[0]);
            return listData;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<EarthquakeData> earthquakeData) {

            myAdapter=new CustomAdapter(MainActivity.this,earthquakeData);
            recyclerView.setAdapter(myAdapter);

        }
    }

    private class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeData>>
    {
        private String url;

        private final String LOG_TAG = EarthquakeLoader.class.getName();

        public EarthquakeLoader(@NonNull Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Nullable
        @Override
        public List<EarthquakeData> loadInBackground() {
            if(url==null)
                return null;

            List<EarthquakeData> listData=Utils.fetchData(url);
            return listData;
        }
    }

}