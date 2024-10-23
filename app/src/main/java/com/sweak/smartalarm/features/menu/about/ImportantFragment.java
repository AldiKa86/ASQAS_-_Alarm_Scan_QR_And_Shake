package com.sweak.smartalarm.features.menu.about;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sweak.smartalarm.R;

public class ImportantFragment extends Fragment {


    public ImportantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_important, container, false);

        TextView textview = rootView.findViewById(R.id.textview);
        textview.setMovementMethod(LinkMovementMethod.getInstance());

        return inflater.inflate(R.layout.fragment_important, container, false);
    }

}