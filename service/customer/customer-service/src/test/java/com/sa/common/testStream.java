package com.sa.common;

import com.sa.customer.domain.BatchTaskItem;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class testStream {
    @Test
    public void xx(){

        List<BatchTaskItem> successList = new ArrayList<>();
        BatchTaskItem item = new BatchTaskItem();
        item.setCustomerName("1");
        item.setCustomerAge(1);
        item.setCustomerHome("1");

        BatchTaskItem item1 = new BatchTaskItem();
        item1.setCustomerName("1");
        item1.setCustomerAge(1);
        item1.setCustomerHome("1");

        BatchTaskItem item2 = new BatchTaskItem();
        item2.setCustomerName("2");
        item2.setCustomerAge(2);
        item2.setCustomerHome("2");



        successList.add(item);
        successList.add(item2);


        List<BatchTaskItem> taskItems = new ArrayList<>();
        taskItems.add(item1);

        List<BatchTaskItem> list = successList.stream().filter(parseItem->
                        taskItems.stream().anyMatch(findItem->
                                !findItem.getCustomerName().equals(parseItem.getCustomerName())
                                        && !findItem.getCustomerAge().equals(parseItem.getCustomerAge())
                                        && !findItem.getCustomerHome().equals(parseItem.getCustomerHome())))
                .collect(Collectors.toList());
        System.out.println(list);

    }

    @Test
    public void xxx(){

        Optional<String> s = Optional.of("123");
        s.ifPresentOrElse(System.out::println,()->{
            System.out.println("没有值");
        });

        s = Optional.empty();
        s.ifPresentOrElse(System.out::println,()->{
            System.out.println("没有值");
        });
    }

    @Test
    public void testLocalDate(){
        //使用LocalDate 创建指定日期
        LocalDate date = LocalDate.of(1999, 9, 18);
        System.out.println(date);
        //使用LocalDate 创建当前日期
        LocalDate nowDate = LocalDate.now();
        System.out.println(nowDate);
        //使用LocalDate 获取日期信息
        System.out.println("年: " + nowDate.getYear());
        System.out.println("月: " + nowDate.getMonthValue());
        System.out.println("日: " + nowDate.getDayOfMonth());
        System.out.println("星期: " + nowDate.getDayOfWeek());
    }

    @Test
    public void testLocalTime(){
        //使用LocalDate 创建指定时间
        LocalTime date = LocalTime.of(23, 9, 18,112312321);
        System.out.println(date);
        //使用LocalDate 创建当前时间
        LocalTime nowTime = LocalTime.now();
        System.out.println(nowTime);
        // 获取时间信息
        System.out.println("小时: " + nowTime.getHour());
        System.out.println("分钟: " + nowTime.getMinute());
        System.out.println("秒: " + nowTime.getSecond());
        System.out.println("纳秒: " + nowTime.getNano());
    }

    @Test
    public void test03() {
        LocalDateTime localDateTime = LocalDateTime.of(1985, 9, 23, 9, 10, 20);
        // 1985-09-23T09:10:20
        System.out.println("localDateTime = " + localDateTime);
        // 得到当前日期时间
        LocalDateTime now = LocalDateTime.now();
        //2022-08-26T16:12:26.949352
        System.out.println("now = " + now);

        System.out.println(now.getYear());
        System.out.println(now.getMonthValue());
        System.out.println(now.getDayOfMonth());
        System.out.println(now.getHour());
        System.out.println(now.getMinute());
        System.out.println(now.getSecond());
        System.out.println(now.getNano());
    }
    @Test
    public void test04() {
        Instant now = Instant.now();
        System.out.println("当前时间戳 = " + now);
// 获取从1970年1月1日 00:00:00的秒
        System.out.println(now.getNano());
        System.out.println(now.getEpochSecond());
        System.out.println(now.toEpochMilli());
        System.out.println(System.currentTimeMillis());
        Instant instant = Instant.ofEpochSecond(5);
        System.out.println(instant);
    }
    @Test
    /**
     * 相差的天数:0
     * 相差的小时数:1
     * 相差的分钟数:93
     * 相差的秒数:5602
     * 相差的年:24
     * 相差的月:0
     * 相差的天:19
     */
    public void test08() {
        // Duration计算时间的距离
        LocalTime now = LocalTime.now();
        LocalTime time = LocalTime.of(14, 15, 20);
        Duration duration = Duration.between(time, now);
        System.out.println("相差的天数:" + duration.toDays());
        System.out.println("相差的小时数:" + duration.toHours());
        System.out.println("相差的分钟数:" + duration.toMinutes());
        System.out.println("相差的秒数:" + duration.toSeconds());
        // Period计算日期的距离
        LocalDate nowDate = LocalDate.now();
        LocalDate date = LocalDate.of(1998, 8, 8);
        // 让后面的时间减去前面的时间
        Period period = Period.between(date, nowDate);
        System.out.println("相差的年:" + period.getYears());
        System.out.println("相差的月:" + period.getMonths());
        System.out.println("相差的天:" + period.getDays());
    }


    // TemporalAdjuster类:自定义调整时间
    @Test
    public void test09() {
        LocalDateTime now = LocalDateTime.now();
        // 得到下一个月的第一天
//        TemporalAdjuster firsWeekDayOfNextMonth = new TemporalAdjuster() {
//            @Override
//            public Temporal adjustInto(Temporal temporal) {
//                LocalDateTime dateTime = (LocalDateTime) temporal;
//                LocalDateTime nextMonth = dateTime.plusMonths(1).withDayOfMonth(1);
//                System.out.println("nextMonth = " + nextMonth);
//                return nextMonth;
//            }
//        };
        TemporalAdjuster firsWeekDayOfNextMonth = temporal -> {
            LocalDateTime dateTime = (LocalDateTime) temporal;
            LocalDateTime nextMonth = dateTime.plusMonths(1).withDayOfMonth(1);
            System.out.println("nextMonth = " + nextMonth);
            return nextMonth;
        };
        LocalDateTime nextMonth = now.with(firsWeekDayOfNextMonth);
        System.out.println("nextMonth = " + nextMonth);
    }

    // 设置日期时间的时区
    @Test
    /**
     * now = 2022-08-27T16:17:39.846402
     * bz = 2022-08-27T08:17:39.850954Z
     * now1 = 2022-08-27T16:17:39.851451+08:00[Asia/Shanghai]
     * now2 = 2022-08-27T01:17:39.852908-07:00[America/Vancouver]
     */
    public void test10() {
        // 1.获取所有的时区ID
        // ZoneId.getAvailableZoneIds().forEach(System.out::println);
        // 不带时间,获取计算机的当前时间
        LocalDateTime now = LocalDateTime.now(); // 中国使用的东八区的时区.比标准时间早8个小时
        System.out.println("now = " + now);
        // 2.操作带时区的类
        // now(Clock.systemUTC()): 创建世界标准时间
        ZonedDateTime bz = ZonedDateTime.now(Clock.systemUTC());
        System.out.println("bz = " + bz);
        // now(): 使用计算机的默认的时区,创建日期时间
        ZonedDateTime now1 = ZonedDateTime.now();
        System.out.println("now1 = " + now1); // 2019-10-19T16:19:44.007153500+08:00[Asia/Shanghai]
        // 使用指定的时区创建日期时间
        ZonedDateTime now2 = ZonedDateTime.now(ZoneId.of("America/Vancouver"));
        System.out.println("now2 = " + now2); // 2019-10-19T01:21:44.248794200-07:00[America/Vancouver]
    }
}
