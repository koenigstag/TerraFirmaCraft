/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.api.calendar;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import net.dries007.tfc.util.Helpers;

public interface ICalendarFormatted extends ICalendar
{
    /* Total calculation methods */

    static long getTotalMonths(long time, long daysInMonth)
    {
        return time / (daysInMonth * TICKS_IN_DAY);
    }

    static long getTotalYears(long time, long daysInMonth)
    {
        return 1000 + (time / (MONTHS_IN_YEAR * daysInMonth * TICKS_IN_DAY));
    }

    /* Fraction Calculation Methods */

    static int getMinuteOfHour(long time)
    {
        return (int) ((time % TICKS_IN_HOUR) / TICKS_IN_MINUTE);
    }

    static int getHourOfDay(long time)
    {
        return (int) ((time / TICKS_IN_HOUR) % HOURS_IN_DAY);
    }

    static int getDayOfMonth(long time, long daysInMonth)
    {
        return 1 + (int) ((time / TICKS_IN_DAY) % daysInMonth);
    }

    static Month getMonthOfYear(long time, long daysInMonth)
    {
        return Month.valueOf((int) ((time / (TICKS_IN_DAY * daysInMonth)) % MONTHS_IN_YEAR));
    }

    /* Format Methods */

    static ITextComponent getTimeAndDate(long time, long daysInMonth)
    {
        return getTimeAndDate(getHourOfDay(time), getMinuteOfHour(time), getMonthOfYear(time, daysInMonth), getDayOfMonth(time, daysInMonth), getTotalYears(time, daysInMonth));
    }

    static ITextComponent getTimeAndDate(int hour, int minute, Month month, int day, long years)
    {
        return new TranslationTextComponent("tfc.tooltip.full_date", hour, minute, new TranslationTextComponent(Helpers.getEnumTranslationKey(month)), day, years);
    }

    /**
     * Gets the current time, according to the specific calendar's offset and settings
     *
     * @return a time
     */
    @Override
    long getTicks();

    /**
     * Gets the number of days in a month, from the calendar's instance values
     *
     * @return a number of days in a month
     */
    long getDaysInMonth();

    default ITextComponent getTimeAndDate()
    {
        return getTimeAndDate(getTicks(), getDaysInMonth());
    }

    /**
     * Get the total number of years for display (i.e 1000, 1001, etc.)
     */
    default long getTotalYears()
    {
        return getTotalYears(getTicks(), getDaysInMonth());
    }

    /**
     * Get the equivalent total world time
     * World time 0 = 6:00 AM, which is calendar time 6000
     *
     * @return a value in [0, 24000) which should match the result of {@link ServerWorld#getDayTime()}
     */
    default long getDayTime()
    {
        return (getTicks() - (6 * ICalendar.TICKS_IN_HOUR)) % ICalendar.TICKS_IN_DAY;
    }

    /**
     * Calculate the total amount of months
     *
     * @return an amount of months
     */
    default long getTotalMonths()
    {
        return getTicks() / (getDaysInMonth() * TICKS_IN_DAY);
    }

    /**
     * Calculates the current month from a calendar time
     */
    default Month getMonthOfYear()
    {
        return getMonthOfYear(getTicks(), getDaysInMonth());
    }

    /**
     * Calculates the day of a month from the calendar time (i.e. 01 - ??)
     */
    default int getDayOfMonth()
    {
        return getDayOfMonth(getTicks(), getDaysInMonth());
    }

    /**
     * Calculates the hour of the day from a calendar time, military time (i.e 00 - 23)
     */
    default int getHourOfDay()
    {
        return getHourOfDay(getTicks());
    }

    /**
     * Calculates the minute of the hour from a calendar time (i.e. 00 - 59)
     */
    default int getMinuteOfHour()
    {
        return getMinuteOfHour(getTicks());
    }

    /**
     * Gets the total number of ticks in a month.
     */
    default long getTicksInMonth()
    {
        return getTotalMonths() * TICKS_IN_DAY;
    }

    /**
     * Gets the total number of ticks in a year.
     */
    default long getTicksInYear()
    {
        return getTotalMonths() * MONTHS_IN_YEAR * TICKS_IN_DAY;
    }
}