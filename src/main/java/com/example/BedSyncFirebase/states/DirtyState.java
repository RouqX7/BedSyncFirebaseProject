package com.example.BedSyncFirebase.states;

import com.example.BedSyncFirebase.models.Bed;

public class DirtyState implements BedState {
    @Override
    public void handleStateChange(Bed bed) {
        bed.setAvailable(false);
    }

    @Override
    public String getStatus() {
        return "DIRTY";
    }
}

