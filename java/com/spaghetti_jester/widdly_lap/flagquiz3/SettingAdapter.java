package com.spaghetti_jester.widdly_lap.flagquiz3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Widdly-Lap on 3/5/2016.
 */
public class SettingAdapter extends ArrayAdapter<Setting> {

    Context context;
    int layoutResourceId;
    Setting data[] = null;

    public SettingAdapter(Context context, int layoutResourceId, Setting[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SettingHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.listview_layout, parent, false);

            holder = new SettingHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.t1);
            holder.txtDesc = (TextView)row.findViewById(R.id.t2);


            row.setTag(holder);
        }
        else
        {
            holder = (SettingHolder)row.getTag();
        }

        Setting setting = data[position];
        holder.txtTitle.setText(setting.myTitle);
        holder.txtDesc.setText(setting.description);

        return row;
    }

    static class SettingHolder
    {
        TextView txtDesc;
        TextView txtTitle;
    }
}