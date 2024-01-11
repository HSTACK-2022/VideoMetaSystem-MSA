package org.hstack.vmeta.extraction.basic.time;

import java.sql.Time;

public class TimeConverter {

    public static Time convert2Time(int sec) {

        int hh = sec/3600;
        sec %= 3600;

        int mm = sec/60;
        sec %= 60;

        return new Time(hh, mm, sec);
    }

}
