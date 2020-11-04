package com.hxgis.duty.test;

import com.hxgis.duty.util.HolidayCalculate;
import org.junit.jupiter.api.Test;

/**
 * @author wrh
 * @version 1.0
 * @date 2020/11/4 16:15
 * @describe
 */
public class HolidayCalculateTest {

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

}
