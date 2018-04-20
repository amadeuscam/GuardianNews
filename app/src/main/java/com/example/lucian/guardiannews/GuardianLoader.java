package com.example.lucian.guardiannews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class GuardianLoader extends AsyncTaskLoader<List<Guardian>> {

    /** Query URL */
    private String mUrl;

    public GuardianLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /*MAKE DATA IN BACKGROUND*/
    @Override
    public List<Guardian> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Guardian> newsData = QueryUtils.fetchNewsData(mUrl);
        return newsData;
    }
}
