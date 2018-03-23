package com.trivago.mp.casestudy;

/**
 * Simple class that specifies the arrival and departure day of a hotel stay. Stored as an integer in the format
 * YYYYMMDD
 */
public class DateRange {
    private final int startDate;
    private final int endDate;

    public DateRange(int startDate, int endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public int getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "DateRange{" + "startDate=" + startDate + ", endDate=" + endDate + '}';
    }
}
