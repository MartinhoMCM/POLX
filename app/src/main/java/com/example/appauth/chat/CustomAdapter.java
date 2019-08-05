package com.example.appauth.chat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appauth.classes.CostumItems;
import com.example.appauth.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<CostumItems> {


    public CustomAdapter(@NonNull Context context,  @NonNull ArrayList<CostumItems> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return customView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return customView(position, convertView, parent);
    }

    private View customView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        if(convertView==null)
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.custom_spinner_layout, parent, false);

        LinearLayout linearLayout =convertView.findViewById(R.id.CategoriesLinear);
        linearLayout.setGravity(Gravity.START);
        TextView spinnerText =convertView.findViewById(R.id.textCustomSpinner);
        spinnerText.setGravity(Gravity.START);
        CostumItems items =getItem(position);

        if(items!=null)
        {
            spinnerText.setText(items.getSpinnerText());

        }
        return  convertView;
    }
}
