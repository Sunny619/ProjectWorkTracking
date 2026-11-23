package com.example.ProjectWorkTracking;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests verifying date formatting, parsing, and deadline countdown calculations.
 */
public class DateUtilsTest {

    @Test
    public void formatDate_padsSingleDigitsProperly() {
        // day 5, month 1 (February, 0-indexed), year 2026
        String formatted = DateUtils.formatDate(5, 1, 2026);
        assertEquals("05/02/2026", formatted);
    }

    @Test
    public void formatDate_handlesDoubleDigits() {
        String formatted = DateUtils.formatDate(25, 11, 2026);
        assertEquals("25/12/2026", formatted);
    }

    @Test
    public void parseDate_parsesValidStandardDate() {
        Date date = DateUtils.parseDate("15/08/2026");
        assertNotNull(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.AUGUST, cal.get(Calendar.MONTH));
        assertEquals(2026, cal.get(Calendar.YEAR));
    }

    @Test
    public void parseDate_handlesNullOrInvalidInput() {
        assertNull(DateUtils.parseDate(null));
        assertNull(DateUtils.parseDate(""));
        assertNull(DateUtils.parseDate("invalid-date"));
        assertNull(DateUtils.parseDate("32/01/2026"));
    }

    @Test
    public void isValidDate_validatesCorrectly() {
        assertTrue(DateUtils.isValidDate("01/01/2026"));
        assertFalse(DateUtils.isValidDate("not a date"));
        assertFalse(DateUtils.isValidDate(null));
    }

    @Test
    public void calculateDaysRemaining_futureDate_isPositive() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 10);
        String futureDateStr = DateUtils.formatDate(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

        Long days = DateUtils.calculateDaysRemaining(futureDateStr);
        assertNotNull(days);
        assertTrue(days >= 9 && days <= 11);
        assertFalse(DateUtils.isOverdue(futureDateStr));
    }

    @Test
    public void calculateDaysRemaining_pastDate_isNegativeAndOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -5);
        String pastDateStr = DateUtils.formatDate(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

        Long days = DateUtils.calculateDaysRemaining(pastDateStr);
        assertNotNull(days);
        assertTrue(days < 0);
        assertTrue(DateUtils.isOverdue(pastDateStr));
    }
}
