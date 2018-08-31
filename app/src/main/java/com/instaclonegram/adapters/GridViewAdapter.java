package com.instaclonegram.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.instaclonegram.R;
import com.instaclonegram.models.Photo;

import java.util.ArrayList;

/**
 * Created by lamine on 08/04/2016.
 */
public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Photo> data = new ArrayList<>();
    private int width;

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data, int width) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.width = width;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView)row.findViewById(R.id.imageView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Photo photo = data.get(position);
        //holder.image.setImageBitmap(photo.getPhoto());
        holder.image.setMinimumWidth(width);
        holder.image.setMinimumHeight(width);
        return row;
    }

    static class ViewHolder {
        ImageView image;
        int width;
    }

}
