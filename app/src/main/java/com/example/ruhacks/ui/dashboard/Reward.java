package com.example.ruhacks.ui.dashboard;

public class Reward {
    String value;
    int image;
    Boolean unlocked;

    // Constructor
    public Reward(String valueParam, int imageParam, Boolean unlockedParam) {
        value = valueParam;
        image = imageParam;
        unlocked = unlockedParam;
    }

    // Getters
    public String getValue() {
        return value;
    }

    public int getImage() {
        return image;
    }

    public Boolean getUnlocked() {
        return unlocked;
    }

    public void unlock() {
        unlocked = true;
    }
}
