package com.bbhackathon.trashformer.equipment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbhackathon.trashformer.R;

public class ViewFragment1 extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.txt_label);
//        textView.setText("ViewFragment1");
        return rootView;
    }
}
