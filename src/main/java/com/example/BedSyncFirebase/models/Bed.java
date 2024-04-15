package com.example.BedSyncFirebase.models;

import com.example.BedSyncFirebase.UsageInterval;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data

public class Bed {

    private String id;
    private String wardId;
    private String hospitalId;
    private boolean isAvailable = true;
    private List<UsageInterval> usageIntervals = new ArrayList<>();
    private boolean isInUse = false;
    private ZonedDateTime lastUsageStartTime;
    private String bedNumber;
    private String bedType;
    private String patientId;
    private boolean isClean = true;
    private LocalDateTime timestamp;

    // Fields for bed transfer request
    private boolean isTransferRequested = false;
    private String requestingHospitalId;
    private String transferReason;
    private TransferStatus transferStatus = TransferStatus.PENDING;
    private String transferResponseReason;





    // Method to start using the bed
    public void setInUse(boolean isInUse) {
        if (this.isInUse != isInUse) { // Check if the state has changed
            this.isInUse = isInUse;
            if (isInUse) {
                lastUsageStartTime = ZonedDateTime.now();
            } else {
                usageIntervals.add(new UsageInterval(lastUsageStartTime, ZonedDateTime.now()));
            }
        }
    }

    // Method to get total usage duration
    public Duration getTotalUsageDuration() {
        Duration totalDuration = Duration.ZERO;
        for (UsageInterval interval : usageIntervals) {
            totalDuration = totalDuration.plus(Duration.between(interval.getStartTime(), interval.getEndTime()));
        }
        // If bed is currently in use, add the duration from lastUsageStartTime to now
        if (isInUse) {
            totalDuration = totalDuration.plus(Duration.between(lastUsageStartTime, ZonedDateTime.now()));
        }
        return totalDuration;
    }

    public enum TransferStatus {
        PENDING, ACCEPTED, DECLINED
    }
}

