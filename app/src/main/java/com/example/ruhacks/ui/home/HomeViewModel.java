package com.example.ruhacks.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> missions;

    public HomeViewModel() {
        missions = new MutableLiveData<>();
        missions.setValue(new ArrayList<String>() {
            {
                add("Make a Smoothie \uD83C\uDF53");
                add("Drink a Glass of Water \uD83D\uDEB0");
                add("Get Some Sunlight ⛅");
                add("Go for a Jog \uD83C\uDFC3");
            }
        });
    }

    public LiveData<ArrayList<String>> getMissions() {
        return missions;
    }

}