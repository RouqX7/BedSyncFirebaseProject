package com.example.BedSyncFirebase;

import lombok.Data;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Data
public class UsageInterval {
    private final ZonedDateTime startTime;
    private final ZonedDateTime endTime;

    public UsageInterval(ZonedDateTime startTime, ZonedDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
