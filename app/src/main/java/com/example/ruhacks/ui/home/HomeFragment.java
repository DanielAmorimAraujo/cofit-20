package com.example.ruhacks.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.ruhacks.MainActivity;
import com.example.ruhacks.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    // private int points = 0;

    // These are labels are shouldn't really be modified by I instantiated them anyways
    TextView tvWelcome;
    TextView tvCurrentBalance;
    TextView tvYourMissions;

    // This is the actual balance we should be modifying based on the data returned by Google Fit
    TextView tvCurrentBalanceValue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // TODO: Remove legacy code
        // To get the current points from MainActivity
        /* Bundle bundle = getArguments();
        if (bundle != null) {
            points = bundle.getInt("points");
        } */

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) { // getView() only works AFTER onCreateView is executed
        tvWelcome = getView().findViewById(R.id.tvWelcome);
        tvCurrentBalance = getView().findViewById(R.id.tvCurrentBalance);
        tvYourMissions = getView().findViewById(R.id.tvYourMissions);

        tvCurrentBalanceValue = getView().findViewById(R.id.tvCurrentBalanceValue);
        tvCurrentBalanceValue.setText(Integer.toString(((MainActivity)getActivity()).getPoints()));
        ListView lvMissions = getView().findViewById(R.id.lvMissions);

        // Missions from HomeViewModel
        homeViewModel.getMissions().observe(this, missions -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.mission_view, R.id.tvMissionTitle, missions);
            lvMissions.setAdapter(adapter);
        });

        lvMissions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                TextView missionTitle = v.findViewById(R.id.tvMissionTitle);
                String mission = missionTitle.getText().toString();
                ((MainActivity)getActivity()).takePictureFromCamera(mission);
            }
        });


    }

    public void updateBalance(String newBalance) {
        tvCurrentBalanceValue.setText(newBalance);
    }
}