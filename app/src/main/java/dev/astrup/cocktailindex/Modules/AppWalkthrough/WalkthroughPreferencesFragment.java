package dev.astrup.cocktailindex.Modules.AppWalkthrough;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import dev.astrup.cocktailindex.R;


public class WalkthroughPreferencesFragment extends Fragment {
    private Switch radio_prelist;
    private Switch metric_prelist;


    public WalkthroughPreferencesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static WalkthroughPreferencesFragment newInstance(String param1, String param2) {
        WalkthroughPreferencesFragment fragment = new WalkthroughPreferencesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public boolean isPredefinedList() {
        return radio_prelist.isChecked();
    }

    public boolean isMetricChecked() {
        return metric_prelist.isChecked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walkthrough_preferences, container, false);
        radio_prelist = view.findViewById(R.id.walkthrough_switch_premade);
        metric_prelist = view.findViewById(R.id.walkthrough_switch_metric);

        radio_prelist.setChecked(true);
        metric_prelist.setChecked(true);
        /// Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
