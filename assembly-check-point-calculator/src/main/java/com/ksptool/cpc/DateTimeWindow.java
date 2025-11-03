package com.ksptool.cpc;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 检查点时间窗口
 */
public class DateTimeWindow {

    public static final ZoneId DEFAULT_TZ = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DateTimeWindow(){
    }

    public DateTimeWindow(ZonedDateTime start, ZonedDateTime end) {
        this.start = start;
        this.end = end;
    }
    public DateTimeWindow(Date start, Date end) {
        this(ZonedDateTime.ofInstant(start.toInstant(), CheckPointCalc.DEFAULT_TZ), ZonedDateTime.ofInstant(end.toInstant(), CheckPointCalc.DEFAULT_TZ));
    }

    private ZonedDateTime start;

    private ZonedDateTime end;

    /**
     * 判断当前时间窗口是否完全位于另一个时间窗口内
     * @param other 另一个时间窗口
     * @return 如果当前时间窗口完全位于另一个时间窗口内，则返回 true；否则返回 false
     */
    public boolean isWithin(DateTimeWindow other) {
        if (other == null || other.start == null || other.end == null) {
            return false;
        }
        if (this.start == null || this.end == null) {
            // 当前窗口无效，无法判断
            return false;
        }
        // other.start <= this.start && this.end <= other.end
        return !other.start.isAfter(this.start) && !this.end.isAfter(other.end);
    }

    /**
     * 判断当前时间窗口的 start 是否位于另一个时间窗口内 (包含边界)
     * @param other 另一个时间窗口
     * @return 如果 start 位于 other 窗口内，则返回 true；否则返回 false
     */
    public boolean isStartWithin(DateTimeWindow other) {
        if (other == null || other.start == null || other.end == null) {
            return false;
        }
        if (this.start == null) {
            // 当前窗口 start 无效，无法判断
            return false;
        }
        // other.start <= this.start <= other.end
        return !other.start.isAfter(this.start) && !this.start.isAfter(other.end);
    }

    /**
     * 判断当前时间窗口的 end 是否位于另一个时间窗口内 (包含边界)
     * @param other 另一个时间窗口
     * @return 如果 end 位于 other 窗口内，则返回 true；否则返回 false
     */
    public boolean isEndWithin(DateTimeWindow other) {
        if (other == null || other.start == null || other.end == null) {
            return false;
        }
        if (this.end == null) {
            // 当前窗口 end 无效，无法判断
            return false;
        }
        // other.start <= this.end <= other.end
        return !other.start.isAfter(this.end) && !this.end.isAfter(other.end);
    }

    /**
     * 判断当前 DateTimeWindow 的时间部分是否完全位于 TimeWindow 内 (包含边界)
     * @param timeWindow 时间窗口
     * @return 如果时间部分完全位于 timeWindow 内，则返回 true；否则返回 false
     */
    public boolean isWithin(TimeWindow timeWindow) {
        if (timeWindow == null || timeWindow.getStart() == null || timeWindow.getEnd() == null) {
            return false;
        }
        if (this.start == null || this.end == null) {
            // 当前窗口无效，无法判断
            return false;
        }
        LocalTime startTime = this.start.toLocalTime();
        LocalTime endTime = this.end.toLocalTime();
        LocalTime windowStart = timeWindow.getStart();
        LocalTime windowEnd = timeWindow.getEnd();

        // windowStart <= startTime && endTime <= windowEnd
        return !windowStart.isAfter(startTime) && !endTime.isAfter(windowEnd);
    }

    /**
     * 判断当前 DateTimeWindow 的 start 时间部分是否位于 TimeWindow 内 (包含边界)
     * @param timeWindow 时间窗口
     * @return 如果 start 时间部分位于 timeWindow 内，则返回 true；否则返回 false
     */
    public boolean isStartWithin(TimeWindow timeWindow) {
        if (timeWindow == null || timeWindow.getStart() == null || timeWindow.getEnd() == null) {
            return false;
        }
        if (this.start == null) {
            // 当前窗口 start 无效，无法判断
            return false;
        }
        LocalTime startTime = this.start.toLocalTime();
        LocalTime windowStart = timeWindow.getStart();
        LocalTime windowEnd = timeWindow.getEnd();

        // windowStart <= startTime <= windowEnd
        return !windowStart.isAfter(startTime) && !startTime.isAfter(windowEnd);
    }

    /**
     * 判断当前 DateTimeWindow 的 end 时间部分是否位于 TimeWindow 内 (包含边界)
     * @param timeWindow 时间窗口
     * @return 如果 end 时间部分位于 timeWindow 内，则返回 true；否则返回 false
     */
    public boolean isEndWithin(TimeWindow timeWindow) {
        if (timeWindow == null || timeWindow.getStart() == null || timeWindow.getEnd() == null) {
            return false;
        }
        if (this.end == null) {
            // 当前窗口 end 无效，无法判断
            return false;
        }
        LocalTime endTime = this.end.toLocalTime();
        LocalTime windowStart = timeWindow.getStart();
        LocalTime windowEnd = timeWindow.getEnd();

        // windowStart <= endTime <= windowEnd
        return !windowStart.isAfter(endTime) && !endTime.isAfter(windowEnd);
    }

    public static DateTimeWindow of(String start,String end){
        return new DateTimeWindow(
                LocalDateTime.parse(start,DTF).atZone(DEFAULT_TZ),
                LocalDateTime.parse(end,DTF).atZone(DEFAULT_TZ)
        );
    }
    public static DateTimeWindow of(Date start,Date end){
        return new DateTimeWindow(
                start,end
        );
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }
}
