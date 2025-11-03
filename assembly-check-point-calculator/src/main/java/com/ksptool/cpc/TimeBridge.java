package com.ksptool.cpc;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class TimeBridge {

    private static ZoneId tz = ZoneId.of("Asia/Shanghai");

    public static ZonedDateTime toZonedDateTime(Date date) {
        if(date == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(date.toInstant(), tz);
    }

    public static Date toDate(ZonedDateTime zonedDateTime) {
        if(zonedDateTime == null) {
            return null;
        }
        return Date.from(zonedDateTime.toInstant());
    }

    public static ZoneId getTz() {
        return tz;
    }

    public static void setTz(ZoneId tz) {
        TimeBridge.tz = tz;
    }
}
