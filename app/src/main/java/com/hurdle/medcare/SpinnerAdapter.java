package com.hurdle.medcare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<CustomSpinner> {
    public SpinnerAdapter(Context context, ArrayList<CustomSpinner> spinner){
        super(context, 0,spinner);
    }


    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent) {
        return initview(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initview(position,convertView,parent);
    }
    private  View initview(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView==null){
            convertView=LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_selector,parent,false
            );
        }
        TextView name=convertView.findViewById(R.id.name_selector);
        if(name!=null) {
            CustomSpinner spinner = getItem(position);
            name.setText(spinner.getPatientname());
        }
        return convertView;
    }
}
