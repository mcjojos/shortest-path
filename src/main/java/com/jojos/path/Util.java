package com.jojos.path;

import com.jojos.path.ApplicationException;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Utilities class
 *
 * @author karanikasg@gmail.com
 */
public class Util {

    public static String longDuration(long start) {
        long millis = System.currentTimeMillis() - start;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
        long secondsToMillis = TimeUnit.SECONDS.toMillis(seconds);
        long minutesToMillis = TimeUnit.MINUTES.toMillis(minutes);
        long mills = millis - secondsToMillis - minutesToMillis;
        return String.format("%02dmin%02dsec%02dms", minutes, seconds, mills);
    }

    /**
     * Creates a path list of values separated by " -> ".
     *
     * @param values the values
     * @param <T>    the type of the values
     * @return a string like "a, b, c"
     */
    public static <T> String toPrettyPathString(Collection<T> values) {
        return values.stream().map(Object::toString).collect(Collectors.joining(" -> ", "[", "]"));
    }

    public static String validateNameOrThrow(String name) {
        if (name == null || name.isEmpty()) {
            throw new ApplicationException("Name attribute must have a value");
        }
        return name;
    }

    public static long validateLongOrThrow(String longValue) {
        if (longValue == null || longValue.isEmpty()) {
            throw new ApplicationException("long must have a value");
        } else {
            try {
                return Long.parseLong(longValue);
            } catch (NumberFormatException e) {
                throw new ApplicationException(longValue + " is not something that can be parsed into a long number");
            }
        }
    }

    public static double validateDoubleOrThrow(String doubleValue) {
        if (doubleValue == null || doubleValue.isEmpty()) {
            throw new ApplicationException("double must have a valid value");
        } else {
            try {
                return Double.parseDouble(doubleValue);
            } catch (NumberFormatException e) {
                if (doubleValue.equals("~")) {
                    return Double.POSITIVE_INFINITY;
                }
                throw new ApplicationException(doubleValue + " is not something that can be parsed into a double number");
            }
        }
    }

}
