package com.example.BedSyncFirebase.states;

import com.example.BedSyncFirebase.models.Bed;

public interface BedState {
    void handleStateChange(Bed bed);

    String getStatus();
}
