package com.example.lucian.guardiannews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Guardian>> {

    public static final String URL_TO_GO = "url";
    private static final int NEWS_LOADER_ID = 1;
    private GuardianAdapter mAdapter;

    private static String Guardian_REQUEST_URL = "https://content.guardianapis.com/search?&show-tags=contributor&api-key=263a5a74-376d-4b37-aa07-0de0ba5106da";
    //order-by=newest&show-tags=contributor&api-key=263a5a74-376d-4b37-aa07-0de0ba5106da

    // mEmptyTextView is displayed when the list has not have data to display
    @BindView(R.id.noData)
    TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind the view using butterknife
        ButterKnife.bind(this);

        final ListView guardianListView = (ListView) findViewById(R.id.listNews);


        mAdapter = new GuardianAdapter(this, new ArrayList<Guardian>());
        guardianListView.setAdapter(mAdapter);


        // Set an item click listener on the ListView, which sends an intent to DetailActivity to open the Guardian website with the selected news
        guardianListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Find the news that was clicked on
                Guardian clickedNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsURI = Uri.parse(clickedNews.getmUrl());

                String urlToString = newsURI.toString();


                // Create a new intent to view the news URI
                Intent webNewsIntent = new Intent(MainActivity.this, DetailActivity.class);
                webNewsIntent.putExtra(URL_TO_GO, urlToString);


                // Send the intent to launch a new activity
                startActivity(webNewsIntent);

            }
        });


        // Find noData TextView in activity_main.xml and setEmptyViewState
        mEmptyStateTextView = findViewById(R.id.noData);
        guardianListView.setEmptyView(mEmptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Guardian>> onCreateLoader(int i, Bundle bundle) {


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String OrderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(Guardian_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("order-by", OrderBy);

        // Return the completed uri "https://content.guardianapis.com/search?order-by=newest&show-tags=contributor&api-key=263a5a74-376d-4b37-aa07-0de0ba5106da
        // Create a new loader for the given URL
        return new GuardianLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Guardian>> loader, List<Guardian> news) {

        //Hide loading progress bar indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news are  found.Please check your internet conexion"
        mEmptyStateTextView.setText(R.string.no_news_error);


        mAdapter.clear();


        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Guardian>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
