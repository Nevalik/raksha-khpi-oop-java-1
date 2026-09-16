package main.java.ua.khpi.oop1.lab02;

public class TimeDecomposer {

    public static void main(String[] args) {
        long[] testValues = {0L, 999_999L, 60_000_000L, 90_061_000_001L};

        for (long value : testValues) {
            decomposeAndVerify(value);
            System.out.println();
        }
    }

    private static void decomposeAndVerify(long totalMicros) {
        long microsperSecond = 1_000_000L;
        long microsperMinute = 60L * microsperSecond;
        long microsperHour   = 60L * microsperMinute;
        long microsperDay    = 24L * microsperHour;

        long remaining = totalMicros;

        long days = remaining / microsperDay;
        remaining = remaining % microsperDay;

        long hours = remaining / microsperHour;
        remaining = remaining % microsperHour;

        long minutes = remaining / microsperMinute;
        remaining = remaining % microsperMinute;

        long seconds = remaining / microsperSecond;
        long micros = remaining % microsperSecond;

        long converted = days * microsperDay
                + hours * microsperHour
                + minutes * microsperMinute
                + seconds * microsperSecond
                + micros;

        System.out.printf("вхід: %d microseconds%n", totalMicros);
        System.out.printf("  дні=%d години=%d хвилини=%d секнди=%d мікросекунди=%d%n",
                days, hours, minutes, seconds, micros);
        System.out.printf("  конвертоване значення=%d, порівняння=%b%n",
                converted, converted == totalMicros);
    }
}