package com.ksptool.cpc;


import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;


public class TimeWindowIterator {

    private final Integer cycle;

    private final ChronoUnit cycleUnit;

    private final ZonedDateTime firstStartTime;

    private ZonedDateTime start;

    private ZonedDateTime end;

    public TimeWindowIterator(ZonedDateTime firstStartTime, Integer cycle, ChronoUnit cycleUnit) {
        this.firstStartTime = firstStartTime;
        this.cycle = cycle;
        this.cycleUnit = cycleUnit;
        start = firstStartTime;
        end = firstStartTime.plus(cycle, cycleUnit);
    }

    public void next() {
        start = start.plus(cycle, cycleUnit);
        end = start.plus(cycle, cycleUnit);
    }

    public Integer getCycle() {
        return cycle;
    }

    public ChronoUnit getCycleUnit() {
        return cycleUnit;
    }

    public ZonedDateTime getFirstStartTime() {
        return firstStartTime;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }
}
