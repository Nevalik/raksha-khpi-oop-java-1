package ua.khpi.oop1.lab06;

public class PeriodSync {

    // Максимальне значення типу long
    static final long MAX_LONG = 9223372036854775807L;

    // Періоди коректні, якщо обидва додатні
    static boolean isValidPeriods(long periodAMs, long periodBMs) {
        return periodAMs > 0 && periodBMs > 0;
    }

    // НСД за алгоритмом Евкліда
    static long gcd(long periodAMs, long periodBMs) {
        if (!isValidPeriods(periodAMs, periodBMs)) {
            throw new IllegalArgumentException("періоди мають бути додатними");
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

    // Перевірка, що НСК не переповнить long (ділимо перед множенням)
    static boolean isLcmInRange(long periodAMs, long periodBMs) {
        return periodAMs / gcd(periodAMs, periodBMs) <= MAX_LONG / periodBMs;
    }

    // НСК = (a / НСД) * b
    static long lcm(long periodAMs, long periodBMs) {
        if (!isLcmInRange(periodAMs, periodBMs)) {
            throw new ArithmeticException("lcm виходить за діапазон long");
        }
        return periodAMs / gcd(periodAMs, periodBMs) * periodBMs;
    }

    // Виводить НСД і НСК для пари періодів з перевірками
    static void printCheck(long periodAMs, long periodBMs) {
        System.out.print("a=" + periodAMs + ", b=" + periodBMs + ": ");
        if (!isValidPeriods(periodAMs, periodBMs)) {
            System.out.println("період має бути додатним");
            return;
        }
        System.out.print("gcd=" + gcd(periodAMs, periodBMs) + ", ");
        if (!isLcmInRange(periodAMs, periodBMs)) {
            System.out.println("lcm відхилено (переповнення long)");
            return;
        }
        System.out.println("lcm=" + lcm(periodAMs, periodBMs));
    }

    // Покрокове трасування алгоритму Евкліда
    static void printGcdTrace(long periodAMs, long periodBMs) {
        System.out.println("Трасування Евкліда для (" + periodAMs + ", " + periodBMs + "):");
        long a = periodAMs;
        long b = periodBMs;
        while (b > 0) {
            System.out.println("a=" + a + ", b=" + b + ", a % b=" + (a % b));
            long tmp = b;
            b = a % tmp;
            a = tmp;
        }
        System.out.println("b=0, gcd=" + a);
    }

    public static void main(String[] args) {
        // Звичайні випадки
        printCheck(20, 30);
        printCheck(17, 19);
        printCheck(250, 1000);
        printCheck(7, 7);
        // Некоректні періоди
        printCheck(0, 5);
        printCheck(-3, 5);
        // Граничний випадок: переповнення НСК
        printCheck(MAX_LONG, MAX_LONG - 1);
        // Трасування алгоритму
        printGcdTrace(20, 30);
    }
}