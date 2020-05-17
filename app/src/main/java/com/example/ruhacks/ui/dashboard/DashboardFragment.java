package com.example.ruhacks.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ruhacks.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    String [] values = {"test", "alpha", "beta", "gamma", "test"};
    int[] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final GridView gridView = root.findViewById(R.id.gridview);

        GridAdapter gridAdapter = new GridAdapter( getActivity(), values, images);
        gridView.setAdapter(gridAdapter);
        
        return root;
    }
}
