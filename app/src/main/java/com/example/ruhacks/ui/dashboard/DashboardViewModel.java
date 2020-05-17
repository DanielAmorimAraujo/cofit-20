package com.example.ruhacks.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ruhacks.R;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Reward>> rewards;

    public DashboardViewModel() {
        rewards = new MutableLiveData<>();
        rewards.setValue(new ArrayList<Reward>() {
            {
                add(new Reward("image1", R.drawable.image1, false));
                add(new Reward("image2", R.drawable.image2, false));
                add(new Reward("image3", R.drawable.image3, false));
                add(new Reward("image4", R.drawable.image4, false));
                add(new Reward("image5", R.drawable.image5, false));
            }
        });
    }

    public LiveData<ArrayList<Reward>> getRewards() {
        return rewards;
    }
}