# HolidayCalculate
粗略计算法定节假日时间

关于中国农历计算，采用：https://github.com/heqiao2010/LunarCalendar

### 调用

```
    @Test
    public void test() {
        HolidayCalculate.Holiday holiday = HolidayCalculate.calcHoliday(HolidayCalculate.HolidayEnum.LAO_DONG, 2020);
        System.out.println(holiday.toString());
    }

    @Test
    public void test2() {
        for (HolidayCalculate.HolidayEnum holidayEnum : HolidayCalculate.HolidayEnum.values()) {
            HolidayCalculate.Holiday holiday = HolidayCalculate.calcHoliday(holidayEnum, 2023);
            System.out.println(holiday.toString());
        }
    }
```

### 输出

```
Holiday{holidayName='元旦', holiday=2023-01-01, dayOfWeek=7, startTime=2022-12-31, endTime=2023-01-02, legalDays=1, days=3}
Holiday{holidayName='春节', holiday=2023-01-22, dayOfWeek=7, startTime=2023-01-21, endTime=2023-01-25, legalDays=3, days=5}
Holiday{holidayName='清明', holiday=2023-04-05, dayOfWeek=3, startTime=2023-04-05, endTime=2023-04-05, legalDays=1, days=1}
Holiday{holidayName='劳动', holiday=2023-05-01, dayOfWeek=1, startTime=2023-04-29, endTime=2023-05-01, legalDays=1, days=3}
Holiday{holidayName='端午', holiday=2023-06-22, dayOfWeek=4, startTime=2023-06-22, endTime=2023-06-24, legalDays=1, days=3}
Holiday{holidayName='中秋', holiday=2023-09-29, dayOfWeek=5, startTime=2023-09-29, endTime=2023-10-01, legalDays=1, days=3}
Holiday{holidayName='国庆', holiday=2023-10-01, dayOfWeek=7, startTime=2023-09-30, endTime=2023-10-04, legalDays=3, days=5}
```
