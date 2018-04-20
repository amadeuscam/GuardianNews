package com.example.lucian.guardiannews;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GuardianAdapter extends ArrayAdapter<Guardian>  {



    public GuardianAdapter(Context context, List<Guardian> GuardianNews) {
        super(context, 0, GuardianNews);
    }

    /*CONECT LIST_ITEMS WITH THE DATA FORM API*/
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_items, parent, false);
        }
        Guardian currentNews = getItem(position);
        TextView txtTitle = (TextView) listItemView.findViewById(R.id.title);
        TextView txtType = (TextView) listItemView.findViewById(R.id.type);
        if (currentNews != null) {
            txtTitle.setText(currentNews.getTitle());
        }
        txtType.setText(currentNews.getSectionName());

        /*AUTHOR*/
        TextView txtAuthor = (TextView) listItemView.findViewById(R.id.author);
        txtAuthor.setText(currentNews.getAuthor());
        /*END AUTHOR*/

        /*DATE*/
        TextView datenews = (TextView) listItemView.findViewById(R.id.date);
        // Extract, format and display the current date news
        SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat myDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        try {
            Date dateNews = dateFormatJSON.parse(currentNews.getPublicationDate());
            String date = myDateFormat.format(dateNews);
            datenews.setText(date);
        } catch (ParseException e) {
            Log.e("News Date Parsing Error", "Error parsing json date:" + e.getMessage());
            e.printStackTrace();
        }
        /*END DATE*/

        return listItemView;
    }
}
