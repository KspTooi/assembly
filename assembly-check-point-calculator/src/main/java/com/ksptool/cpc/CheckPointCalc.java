package com.ksptool.cpc;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

        /*
        * 检查点计算器
        *
        * 任务生成规则
        A): 候选任务
        1.候选任务生成 定时器每分钟执行一次 每次生成若干个候选任务
        以检查点计划 [首次执行时间] 为起始时间 迭加检查点计划周期进行轮询 每轮询一次生成一个候选任务 直到结束时间超过当前时间
        EX: 有一条检查点计划首次为 2025-01-01 09:00:00 周期设定 1:天
        现在时间为 2025-01-04 09:00:00
        候选任务为:
        1:2025-01-01 09:00:00~2025-01-02 09:00:00
        2:2025-01-02 09:00:00~2025-01-03 09:00:00
        3:2025-01-03 09:00:00~2025-01-04 09:00:00

        B):候选任务预处理 - 节假日
        该筛选器将会预处理A部分拿到的候选任务列表 判断每一个候选任务是否应该通过
        B1: 当候选任务开始、结束时间均位于节假日内 则丢弃该候选
        B2: 当候选任务开始、结束时间任意一个时间位于节假日之外 则保留该候选

        B3:候选任务检查点计划的周期 若为月、以及大于7天 则从候选任务起始时间进行按天轮询直到结束时间 若期间有任意一个时间点位于节假日之外则保留
        B4: 候选任务检查点计划周期小于7天 则开始时间须位于节假日之外 否则丢弃

        C):候选任务预处理 - 小时级任务上下班时间
        只有小时级任务会触发该筛选条件进行筛选，系统会获取候选任务列表中开始时间、结束时间的时间进行判断
        EX: 检查点计划为小时 上下班时间为09:00 ~ 18:00
        则开始时间与结束时间位于09:00 ~ 18:00区间的候选任务保留
        开始时间与结束时间任意一个位于09:00 ~ 18:00之外 则丢弃该候选

        D:)候选任务预处理 - 白名单
        白名单由多个时间区间组成，由用户设置补休的时间为白名单。
        EX: 用户设置 2025-01-04 00:00:00 ~ 2025-01-05 23:59:59为补休 则该时间段为白名单
        拿到候选任务列表后进行预处理过滤 若任务开始时间与结束时间都位于白名单内 则无视B、C两个筛选条件 保留该候选
        * */


public class CheckPointCalc {

    public static final ZoneId DEFAULT_TZ = ZoneId.of("Asia/Shanghai");

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final AtomicLong CALC_ID_GENERATOR = new AtomicLong(0);

    private String calcId;

    private static final Logger log = LoggerFactory.getLogger(CheckPointCalc.class);

    //计算历史检查点
    private final boolean calcHistoryCheckpoint = false;

    //过滤 - 丢弃一周中的第几天(星期几)
    private final List<DayOfWeek> filterDropDayOfWeek = new ArrayList<>();

    //过滤 - 丢弃某个时间段(日期-时间)
    private final List<DateTimeWindow> filterDropWin = new ArrayList<>();

    //过滤 - 丢弃一天中的某个时间段
    private final List<TimeWindow> filterLimitTimeWin = new ArrayList<>();

    //过滤 - 保留某个时间段(不受任何Drop影响)
    private final List<DateTimeWindow> filterWhiteListWin = new ArrayList<>();

    //过滤 - 丢弃开始时间大于现在N分钟的点 -1:关闭此功能
    private Long dropWhenStartTimeGreaterThanNow = 30L;

    //过滤 - 丢弃开始时间小于现在N分钟的点 -1:关闭此功能
    private Long dropWhenStartTimeLessThanNow = 30L;

    private final Integer cycle;

    private final ChronoUnit cycleUnit;

    private final ZonedDateTime firstStartTime;

    private ZonedDateTime now;

    private final ZoneId tz;

    public CheckPointCalc(ZonedDateTime firstStartTime, Integer cycle, ChronoUnit cycleUnit, ZonedDateTime now) {
        calcId = "#CPC"+CALC_ID_GENERATOR.getAndIncrement();
        this.cycle = cycle;
        this.cycleUnit = cycleUnit;
        tz = DEFAULT_TZ;
        this.firstStartTime = firstStartTime;

        if(now != null){
            this.now = now;
        }
        if(now == null){
            this.now = ZonedDateTime.now(tz);
        }
    }

    public CheckPointCalc(Date fst, Integer cycle, ChronoUnit cycleUnit){
        this(fst.toInstant().atZone(DEFAULT_TZ),cycle,cycleUnit,ZonedDateTime.now(DEFAULT_TZ));
    }

    public CheckPointCalc(ZonedDateTime fst, Integer cycle, ChronoUnit cycleUnit){
        this(fst,cycle,cycleUnit,ZonedDateTime.now(DEFAULT_TZ));
    }


    public List<DateTimeWindow> execute(){

        var result = new ArrayList<DateTimeWindow>();

        if(firstStartTime == null){
            log.info("[#CPC{}]检查点计算失败,start为空.",calcId);
            return result;
        }
        if(cycle == null){
            log.info("[{}]检查点计算失败,cycle为空.",calcId);
            return result;
        }
        if(cycle <= 0){
            log.info("[{}]检查点计算失败,cycle为负数:{}.",calcId,cycle);
            return result;
        }
        if(cycleUnit == null){
            log.info("[{}]检查点计算失败,cycleUnit为空.",calcId);
            return result;
        }
        if(isInFuture(firstStartTime)){
            log.info("[{}]检查点计算失败,首次开始时间:{} 位于未来。",calcId,DTF.format(firstStartTime));
            return result;
        }

        ZonedDateTime checkPointDeadline = now.plus(cycle, cycleUnit);

        TimeWindowIterator twi = new TimeWindowIterator(firstStartTime,cycle,cycleUnit);

        log.info("[{}]新检查点计算计划 计算历史检查点:{}",calcId,calcHistoryCheckpoint);

        //计算所有任务点
        while (twi.getStart().isBefore(checkPointDeadline)){

            //如果不计算历史检查点 则跳过开始时间为历史时间的检查点
            if(!calcHistoryCheckpoint){

                if(isBefore(twi.getStart()) && isBefore(twi.getEnd())){
                    twi.next();
                    continue;
                }
            }

            log.info("[{}]预创建检查窗口: {} ~ {}",calcId,DTF.format(twi.getStart()),DTF.format(twi.getEnd()));
            result.add(new DateTimeWindow(twi.getStart(),twi.getEnd()));
            twi.next();
        }

        List<DateTimeWindow> finalWin = filter(result);

        if(!finalWin.isEmpty()){
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(calcId).append("]");
            for(DateTimeWindow w : finalWin){
                sb.append("最终检查窗口:").
                        append(DTF.format(w.getStart())).append(" ~ ").append(DTF.format(w.getEnd()))
                        .append("\n");
            }
            log.info("{}",sb.toString());
        }
        if(finalWin.isEmpty()){
            log.info("[{}]没有创建任何最终检查窗口",calcId);
        }

        return finalWin;
    }

    public List<DateTimeWindow> filter(List<DateTimeWindow> windows){
        return windows.stream().filter((win)->{

            ZonedDateTime start = win.getStart();
            ZonedDateTime end = win.getEnd();

            //过滤 - 开始时间大于现在N分钟 如果关闭此功能则跳过此过滤
            if(dropWhenStartTimeGreaterThanNow != -1){
                long gtNow = ChronoUnit.MINUTES.between(now, win.getStart());
                if(gtNow > dropWhenStartTimeGreaterThanNow){
                    log.info("[{}]丢弃检查窗口:{} ~ {} 原因: 开始时间大于现在时间超过:{}分钟 现在时间:{} 距离:{}",
                            calcId,
                            DTF.format(win.getStart()),
                            DTF.format(win.getEnd()),
                            dropWhenStartTimeGreaterThanNow,
                            DTF.format(now),
                            gtNow
                    );
                    return false;
                }
            }

            //过滤 - 开始时间小于现在N分钟 如果计算历史检查点 则跳过此过滤
            if(!calcHistoryCheckpoint){
                long ltNow = ChronoUnit.MINUTES.between(win.getStart(),now);
                if(ltNow > dropWhenStartTimeLessThanNow){
                    log.info("[{}]丢弃检查窗口:{} ~ {} 原因: 开始时间小于现在时间超过:{}分钟 现在时间:{} 距离:{}",
                            calcId,
                            DTF.format(win.getStart()),
                            DTF.format(win.getEnd()),
                            dropWhenStartTimeLessThanNow,
                            DTF.format(now),
                            ltNow
                    );
                    return false;
                }
            }

            //过滤 -- 按照某个时间段过滤（白名单也需要过滤开始结束时间）
            for(TimeWindow dropWin : filterLimitTimeWin){

                if(!win.isStartWithin(dropWin)){
                    log.info("[{}]丢弃检查窗口:{} ~ {} 原因: 开始时间位于限制时间窗口外:{} ~ {}",calcId,DTF.format(start),DTF.format(end),TF.format(dropWin.getStart()),TF.format(dropWin.getEnd()));
                    return false;
                }
                if(!win.isEndWithin(dropWin)){
                    log.info("[{}]丢弃检查窗口:{} ~ {} 原因: 结束时间位于限制时间窗口外:{} ~ {}",calcId,DTF.format(start),DTF.format(end),TF.format(dropWin.getStart()),TF.format(dropWin.getEnd()));
                    return false;
                }
            }

            //检查开始与结束是否位于白名单中
            for (DateTimeWindow whiteListWin : filterWhiteListWin) {
                //如果时间窗口完全位于任意一个白名单窗口内 则直接放行(忽略后续所有Drop)
                if(win.isWithin(whiteListWin)){
                    log.info("[{}]保留检查窗口:{} ~ {} 原因: 位于白名单窗口:{} ~ {}",calcId,DTF.format(start),DTF.format(end),DTF.format(whiteListWin.getStart()),DTF.format(whiteListWin.getEnd()));
                    return true;
                }
            }


            //七天及以上包括者月、年 过滤时间段内是否某天为工作日，有则保留
            if((cycle>=7&&cycleUnit==ChronoUnit.DAYS)||cycleUnit==ChronoUnit.MONTHS||cycleUnit==ChronoUnit.YEARS){
                //七天以上不判断周六日
                //判断是否完全位于某个时间段(节假日)之中
                for (DateTimeWindow dropWin : filterDropWin) {
                    if (win.isWithin(dropWin)) {
                        log.info("[{}]丢弃检查窗口:{} ~ {} 原因: 完全位于Drop窗口中:{} ~ {}", calcId, DTF.format(start), DTF.format(end), DTF.format(dropWin.getStart()), DTF.format(dropWin.getEnd()));
                        return false;
                    }
                }
            }else {
                //七天以下，判断开始时间是否在周六周日或者节假日
                DayOfWeek startDOW = start.getDayOfWeek();
                DayOfWeek endDOW = end.getDayOfWeek();
                for (DayOfWeek dayOfWeek : filterDropDayOfWeek) {
                    if (startDOW.equals(dayOfWeek)) {
                        log.info("[{}]丢弃检查窗口:{} ~ {} 原因: 开始时间位于:{}", calcId, DTF.format(start), DTF.format(end), dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA));
                        return false;
                    }
                }

                //过滤 -- 按照某个日期+时间段过滤
                for (DateTimeWindow dropWin : filterDropWin) {
                    //如果开始时间位于Drop窗口内，则直接丢弃，结束时间不限制
                    if (win.isStartWithin(dropWin)) {
                        log.info("[{}]丢弃检查窗口:{} ~ {} 原因: 开始时间位于Drop窗口中:{} ~ {}", calcId, DTF.format(start), DTF.format(end), DTF.format(dropWin.getStart()), DTF.format(dropWin.getEnd()));
                        return false;
                    }

                }
            }

            return true;
        }).toList();
    }



    /**
     * 判断日期是否在未来
     * 
     * @param date 要判断的日期
     * @return 如果日期在未来返回true，否则返回false
     */
    private boolean isInFuture(ZonedDateTime date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(now);
    }

    /**
     * 判断日期是否在当前时间之前
     * 
     * @param date 要判断的日期
     * @return 如果date在当前时间之前返回true，否则返回false
     */
    private boolean isBefore(ZonedDateTime date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(now);
    }

    public String getCalcId() {
        return ""+calcId;
    }

    public void setCalcId(String calcId) {
        if(calcId == null){
            return;
        }
        this.calcId = "#CPC"+calcId;
    }
    public void setCalcId(Long calcId) {
        if(calcId == null){
            return;
        }
        this.calcId = "#CPC"+calcId;
    }

    //丢弃星期几
    public CheckPointCalc drop(DayOfWeek dow){
        filterDropDayOfWeek.add(dow);
        return this;
    }

    //丢弃日期+时间窗口
    public CheckPointCalc drop(DateTimeWindow window){
        filterDropWin.add(window);
        return this;
    }

    //丢弃一天中的某个时间窗口
    public CheckPointCalc limit(TimeWindow window){
        filterLimitTimeWin.add(window);
        return this;
    }

    //白名单
    public CheckPointCalc save(DateTimeWindow window){
        filterWhiteListWin.add(window);
        return this;
    }

    public static ZonedDateTime ofZonedDateTime(String zdt){
        LocalDateTime ldt = LocalDateTime.parse(zdt,DTF);
        return ldt.atZone(DEFAULT_TZ);
    }

    public static List<DayOfWeek> getAllDaysOfWeek(ZonedDateTime start, ZonedDateTime end) {
        Set<DayOfWeek> uniqueDays = new LinkedHashSet<>(); // 使用LinkedHashSet保持顺序并去重

        // 确保使用正确的时区
        ZoneId zoneId = start.getZone();
        ZonedDateTime current = start.withZoneSameInstant(zoneId);
        ZonedDateTime endDate = end.withZoneSameInstant(zoneId);

        // 添加开始日期的星期几
        uniqueDays.add(current.getDayOfWeek());

        // 如果窗口跨越多天，则逐天检查
        if (!current.toLocalDate().equals(endDate.toLocalDate())) {
            // 从开始日期的第二天开始检查
            ZonedDateTime nextDay = current.toLocalDate().plusDays(1).atStartOfDay(zoneId);

            while (!nextDay.isAfter(endDate.toLocalDate().atStartOfDay(zoneId))) {
                uniqueDays.add(nextDay.getDayOfWeek());
                nextDay = nextDay.plusDays(1);
            }
        }

        // 添加结束日期的星期几（如果还没有添加）
        uniqueDays.add(endDate.getDayOfWeek());

        return new ArrayList<>(uniqueDays);
    }

    public Long getDropWhenStartTimeGreaterThanNow() {
        return dropWhenStartTimeGreaterThanNow;
    }

    public Long getDropWhenStartTimeLessThanNow() {
        return dropWhenStartTimeLessThanNow;
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

    public ZonedDateTime getNow() {
        return now;
    }

    public ZoneId getTz() {
        return tz;
    }

    public void setDropWhenStartTimeLessThanNow(Long dropWhenStartTimeLessThanNow) {
        this.dropWhenStartTimeLessThanNow = dropWhenStartTimeLessThanNow;
    }

    public void setDropWhenStartTimeGreaterThanNow(Long dropWhenStartTimeGreaterThanNow) {
        this.dropWhenStartTimeGreaterThanNow = dropWhenStartTimeGreaterThanNow;
    }
}
