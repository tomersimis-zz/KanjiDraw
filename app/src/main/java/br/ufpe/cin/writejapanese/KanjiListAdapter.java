package br.ufpe.cin.writejapanese;

import br.ufpe.cin.writejapanese.entity.Kanji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class KanjiListAdapter extends ArrayAdapter<Kanji> {

    public KanjiListAdapter(Context context, List<Kanji> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Kanji item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.starred_list_item, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.list_name);
        ImageView image = (ImageView) convertView.findViewById(R.id.list_image);
        // Populate the data into the template view using the data object
        name.setText(item.getName());
        image.setImageDrawable(item.getImage(getContext()));

        // Return the completed view to render on screen
        return convertView;
    }
}