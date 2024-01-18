package com.example.BedSyncFirebase.states;


import com.example.BedSyncFirebase.models.Bed;

public class CleanState implements BedState{
    @Override
    public void handleStateChange(Bed bed) {
        bed.setAvailable(true);
    }

    @Override
    public String getStatus() {
        return "CLEAN";
    }
}
