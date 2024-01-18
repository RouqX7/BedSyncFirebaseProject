package com.example.BedSyncFirebase.states;

import com.example.BedSyncFirebase.models.Bed;

public class AvailableState implements BedState {
    @Override
    public void handleStateChange(Bed bed) {
        // Logic for when a bed becomes available
        bed.setAvailable(true);
    }

    @Override
    public String getStatus() {
        return "AVAILABLE";
    }
}


// Similar classes for DirtyState, UnderMaintenanceState, etc.
