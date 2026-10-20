package ua.khpi.oop1.lab06;

public class PeriodSync {

    static final long MAX_LONG = 9223372036854775807L;

    static boolean isValidPeriods(long periodAMs, long periodBMs) {
        return periodAMs > 0 && periodBMs > 0;
    }

    static long gcd(long periodAMs, long periodBMs) {
        if (!isValidPeriods(periodAMs, periodBMs)) {
            throw new IllegalArgumentException("періоди мають бути від'ємними);
        }
        long a = periodAMs;
        long b = periodBMs;
        while (b > 0) {
            long tmp = b;
            b = a % tmp;
            a = tmp;
        }
        return a;
    }

    static boolean isLcmInRange(long periodAMs, long periodBMs) {
        return periodAMs / gcd(periodAMs, periodBMs) <= MAX_LONG / periodBMs;
    }

    static long lcm(long periodAMs, long periodBMs) {
        if (!isLcmInRange(periodAMs, periodBMs)) {
            throw new ArithmeticException("lcm перевищує дальність дії");
        }
        return periodAMs / gcd(periodAMs, periodBMs) * periodBMs;
    }
}