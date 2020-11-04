package com.hxgis.duty.util;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author wrh
 * @version 1.0
 * @date 2020/11/4 11:35
 * @describe 粗略计算法定节假日时间
 */
public class HolidayCalculate {

    public static final String SOLAR_CALENDAR = "SOLAR_CALENDAR"; // 阳历
    public static final String LUNAR_CALENDAR = "LUNAR_CALENDAR"; // 农历

    public enum HolidayEnum {
        YUAN_DAN("元旦", 1, 1, SOLAR_CALENDAR, 1),
        CHUN_JIE("春节", 1, 1, LUNAR_CALENDAR, 3),
        QING_MIN("清明", 4, null, "", 1),
        LAO_DONG("劳动", 5, 1, SOLAR_CALENDAR, 1),
        DUAN_WU("端午", 5, 5, LUNAR_CALENDAR, 1),
        ZHOGN_QIU("中秋", 8, 15, LUNAR_CALENDAR, 1),
        GUO_QING("国庆", 10, 1, SOLAR_CALENDAR, 3),
        ;

        private String holidayName;
        private Integer month;
        private Integer dayOfMonth;
        private String Type;
        private int days;

        HolidayEnum(String holidayName, Integer month, Integer dayOfMonth, String type, int days) {
            this.holidayName = holidayName;
            this.month = month;
            this.dayOfMonth = dayOfMonth;
            Type = type;
            this.days = days;
        }
    }

    public static class Holiday {
        private String holidayName;
        private LocalDateTime holiday;
        private int dayOfWeek;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private int legalDays;
        private int days;

        public String getHolidayName() {
            return holidayName;
        }

        public void setHolidayName(String holidayName) {
            this.holidayName = holidayName;
        }

        public LocalDateTime getHoliday() {
            return holiday;
        }

        public void setHoliday(LocalDateTime holiday) {
            this.holiday = holiday;
        }

        public int getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }

        public int getLegalDays() {
            return legalDays;
        }

        public void setLegalDays(int legalDays) {
            this.legalDays = legalDays;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        @Override
        public String toString() {
            return "Holiday{" +
                    "holidayName='" + holidayName + '\'' +
                    ", holiday=" + LocalDateTimeUtil.format(holiday, "yyyy-MM-dd") +
                    ", dayOfWeek=" + dayOfWeek +
                    ", startTime=" + LocalDateTimeUtil.format(startTime, "yyyy-MM-dd") +
                    ", endTime=" + LocalDateTimeUtil.format(endTime, "yyyy-MM-dd") +
                    ", legalDays=" + legalDays +
                    ", days=" + days +
                    '}';
        }
    }

    /**
     * @description : 清明节日期在公历和农历中都不固定,计算清明节的日期(可计算范围: 1700-3100)
     */
    public static int qing(int year) {
        if (year == 2232) {
            return 4;
        }
        if (year < 1700) {
            throw new RuntimeException("1700年以前暂时不支持");
        }
        if (year >= 3100) {
            throw new RuntimeException("3100年以后暂时不支持");
        }
        double[] coefficient = {5.15, 5.37, 5.59, 4.82, 5.02, 5.26, 5.48, 4.70, 4.92, 5.135, 5.36, 4.60, 4.81, 5.04,
                5.26};
        int mod = year % 100;
        return (int) (mod * 0.2422 + coefficient[year / 100 - 17] - mod / 4);
    }

    public static boolean isLeapYear(int year) {
        boolean flag1 = (year % 4 == 0);
        boolean flag2 = (year % 100 == 0);
        boolean flag3 = (year % 400 == 0);
        return (flag1 && !flag2) || (flag3);
    }

    public static LocalDateTime lunar2Solar(int year, int month, int dayOfMonth) {
        LunarCalendar lunar = new LunarCalendar(year, month, dayOfMonth, isLeapYear(year));
        Calendar solar = LunarCalendar.lunar2Solar(lunar.getLunarYear(), lunar.getLunarMonth(), lunar.getDayOfLunarMonth(), lunar.isLeapMonth());
        return solar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Holiday calcHoliday(HolidayEnum holidayEnum, int year) {
        LocalDateTime solar;
        if (holidayEnum.equals(HolidayEnum.QING_MIN)) {
            solar = LocalDateTime.of(year, holidayEnum.month, qing(year), 0, 0);
        } else if (holidayEnum.Type.equals(SOLAR_CALENDAR)) {
            solar = LocalDateTime.of(year, holidayEnum.month, holidayEnum.dayOfMonth, 0, 0);
        } else if (holidayEnum.Type.equals(LUNAR_CALENDAR)) {
            solar = lunar2Solar(year, holidayEnum.month, holidayEnum.dayOfMonth);
        } else {
            throw new RuntimeException("无效的类型：" + holidayEnum.Type);
        }
        int dayOfWeek = solar.getDayOfWeek().getValue();
        int legalDays = holidayEnum.days;
        int days = legalDays;
        LocalDateTime startTime, endTime;
        if (legalDays == 1) {
            switch (dayOfWeek) {
                case 1:
                case 2:
                    startTime = solar.minusDays(2);
                    endTime = solar;
                    days = 3;
                    break;
                case 3:
                    startTime = solar;
                    endTime = solar;
                    break;
                case 4:
                case 5:
                case 6:
                    startTime = solar;
                    endTime = solar.plusDays(2);
                    days = 3;
                    break;
                case 7:
                    startTime = solar.minusDays(1);
                    endTime = solar.plusDays(1);
                    days = 3;
                    break;
                default:
                    throw new RuntimeException("不可能发生的异常");
            }
        } else if (legalDays == 3) {
            switch (dayOfWeek) {
                case 1:
                    startTime = solar.minusDays(2);
                    endTime = solar.plusDays(2);
                    days = 5;
                    break;
                case 2:
                    startTime = solar.minusDays(2);
                    endTime = solar.plusDays(4);
                    days = 7;
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                    startTime = solar;
                    endTime = solar.plusDays(4);
                    days = 5;
                    break;
                case 7:
                    startTime = solar.minusDays(1);
                    endTime = solar.plusDays(3);
                    days = 5;
                    break;
                default:
                    throw new RuntimeException("无效的类型：" + holidayEnum.Type);
            }
        } else {
            throw new RuntimeException("不可能发生的异常");
        }
        Holiday holiday = new Holiday();
        holiday.setHolidayName(holidayEnum.holidayName);
        holiday.setHoliday(solar);
        holiday.setStartTime(startTime);
        holiday.setEndTime(endTime);
        holiday.setDayOfWeek(dayOfWeek);
        holiday.setLegalDays(legalDays);
        holiday.setDays(days);
        return holiday;
    }

    public static List<Holiday> calcHolidayList(int year) {
        List<Holiday> list = new ArrayList<>();
        for (HolidayCalculate.HolidayEnum holidayEnum : HolidayCalculate.HolidayEnum.values()) {
            HolidayCalculate.Holiday holiday = HolidayCalculate.calcHoliday(holidayEnum, year);
            list.add(holiday);
        }
        return list;
    }


    public static void main(String[] args) {
        LocalDateTime localDateTime = lunar2Solar(2020, 5, 5);
        System.out.println(LocalDateTimeUtil.format(localDateTime, "yyyy-MM-dd"));
    }

}
