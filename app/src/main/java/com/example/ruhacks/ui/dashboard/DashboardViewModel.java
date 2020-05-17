package com.example.ruhacks.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ruhacks.R;
import com.example.ruhacks.Reward;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {

    private static ArrayList<Reward> rewards;


    public DashboardViewModel() {
        rewards = new ArrayList<Reward>() {
            {
                add(new Reward("image1", R.drawable.image1, false));
                add(new Reward("image2", R.drawable.image2, false));
                add(new Reward("image3", R.drawable.image3, false));
                add(new Reward("image4", R.drawable.image4, false));
                add(new Reward("image5", R.drawable.image5, false));
            }
        };
    }

    public LiveData<ArrayList<Reward>> getRewards() {
        MutableLiveData<ArrayList<Reward>> data = new MutableLiveData<ArrayList<Reward>>();
        data.setValue(rewards);

        return data;
    }

    public boolean getUnlocked(int position) {
        return rewards.get(position).getUnlocked();
    }

    public void setUnlocked(int position) {
        rewards.get(position).unlock();
    }
}