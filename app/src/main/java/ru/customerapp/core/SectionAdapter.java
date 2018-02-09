package ru.customerapp.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.customerapp.R;

/**
 * Created by rash on 06.02.2018.
 */

public class SectionAdapter extends ArrayAdapter<String> {

    Context context;
    List<Section> sectionList;

    public SectionAdapter(Context context, List<Section> sectionList) {
        super(context, R.layout.section_item);
        this.context = context;
        this.sectionList = sectionList;
    }

    @Override
    public int getCount() {
        return sectionList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.section_item, parent, false);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Section section = sectionList.get(position);
        holder.tvName.setText(section.Name);
        holder.tvName.setTag(section.Id);
        return view;
    }

    static class ViewHolder {
        TextView tvName;
    }
}

