package com.example.ruhacks.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.ruhacks.MainActivity;
import com.example.ruhacks.R;

public class DashboardFragment extends Fragment implements GridAdapter.EventListener {

    private DashboardViewModel dashboardViewModel;

    /* String [] values = {"image1", "image2", "image3", "image4", "image5"};
    int[] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5
    }; */

    @Override
    public int getPoints() {
        return ((MainActivity)getActivity()).getPoints();
    }

    @Override
    public void deductPoints(int points) {
        ((MainActivity)getActivity()).deductPoints(points);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final GridView gridView = root.findViewById(R.id.gridview);

        // Rewards from DashboardViewModel
        dashboardViewModel.getRewards().observe(this, rewards -> {
            GridAdapter adapter = new GridAdapter(getActivity(),
                    rewards, this);
            gridView.setAdapter(adapter);
        });

        return root;
    }
}
