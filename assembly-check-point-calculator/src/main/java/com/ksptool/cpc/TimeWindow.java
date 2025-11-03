package com.ksptool.cpc;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 检查点时间窗口
 */
public class TimeWindow {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");

    public TimeWindow(){
    }

    public TimeWindow(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }
    public TimeWindow(String start, String end) {
        this.start = LocalTime.parse(start,DTF);
        this.end = LocalTime.parse(end,DTF);
    }

    private LocalTime start;

    private LocalTime end;


    public static TimeWindow of(String start, String end) {
        return new TimeWindow(start, end);
    }


    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
