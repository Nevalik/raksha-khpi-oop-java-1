package ua.khpi.oop1.lab10.app;

import java.util.Locale;
import ua.khpi.oop1.lab10.checksum.Fletcher16;
import ua.khpi.oop1.lab10.measurement.MeasurementMath;

/** Демонстрація та самоперевірка утилітарних класів лабораторної роботи № 10. */
public final class Main {

    private Main() {
    }

    /**
     * Виконує перевірки та виводить очікувані й фактичні результати.
     * Якщо результат не збігається з очікуваним, програма завершується
     * помилкою {@link IllegalStateException}. Виклики з некоректними
     * аргументами наведено в кінці методу у вигляді коментарів: кожний із
     * них завершує програму винятком {@link IllegalArgumentException}.
     *
     * @param args аргументи командного рядка, не використовуються
     */
    public static void main(String[] args) {
        System.out.println(" Варіант 1: MeasurementMath");
        checkMeasurementMath();
        System.out.println();
        System.out.println("Поглиблений рівень A: Fletcher16");
        checkFletcher16();
        System.out.println();
        System.out.println("Усі перевірки пройдено.");

        // Некоректні дані: розкоментуйте один рядок, щоб побачити
        // IllegalArgumentException (програма завершиться з помилкою).
        // MeasurementMath.clamp(5, 10, 0);
        // MeasurementMath.scale(1, 5, 5, 0, 1);
        // MeasurementMath.normalize(Double.NaN, 0, 1);
        // MeasurementMath.normalize(5, 3, 3);
        // Fletcher16.compute(null);
        // Fletcher16.verify(new byte[] {1}, -1);
        // Fletcher16.verify(new byte[] {1}, 65536);
    }

    private static void checkMeasurementMath() {
        showDouble("clamp(35, 0, 100)", MeasurementMath.clamp(35, 0, 100), 35.0);
        showDouble("clamp(-8, 0, 100)", MeasurementMath.clamp(-8, 0, 100), 0.0);
        showDouble("clamp(135, 0, 100)", MeasurementMath.clamp(135, 0, 100), 100.0);
        showDouble("clamp(0, 0, 100)", MeasurementMath.clamp(0, 0, 100), 0.0);
        showDouble("clamp(100, 0, 100)", MeasurementMath.clamp(100, 0, 100), 100.0);
        showDouble("scale(0, 0..4095 -> 0..5)",
                MeasurementMath.scale(0, 0, 4095, 0, 5), 0.0);
        showDouble("scale(4095, 0..4095 -> 0..5)",
                MeasurementMath.scale(4095, 0, 4095, 0, 5), 5.0);
        showDouble("scale(2048, 0..4095 -> 0..5)",
                MeasurementMath.scale(2048, 0, 4095, 0, 5), 2.5006105006105);
        showDouble("scale(25, 0..100 -> 32..212)",
                MeasurementMath.scale(25, 0, 100, 32, 212), 77.0);
        showDouble("scale(0, 0..10 -> 10..0)",
                MeasurementMath.scale(0, 0, 10, 10, 0), 10.0);
        showDouble("normalize(25, 0..100)", MeasurementMath.normalize(25, 0, 100), 0.25);
        showDouble("normalize(150, 0..100)", MeasurementMath.normalize(150, 0, 100), 1.0);
        showDouble("normalize(-5, 0..100)", MeasurementMath.normalize(-5, 0, 100), 0.0);
        showDouble("scale(150, 0..100 -> 0..1)",
                MeasurementMath.scale(150, 0, 100, 0, 1), 1.5);
        showDouble("scale(25, 100..0 -> 0..1)",
                MeasurementMath.scale(25, 100, 0, 0, 1), 0.75);
        showDouble("normalize(25, 100..0)", MeasurementMath.normalize(25, 100, 0), 0.75);
    }

    private static void checkFletcher16() {
        showChecksum("порожній масив", new byte[0], 0);
        showChecksum("[1, 2, 3]", new byte[] {1, 2, 3}, 0x0A06);
        showChecksum("[0x00]", new byte[] {0x00}, 0);
        showChecksum("[0xFF]", new byte[] {(byte) 0xFF}, 0);
        showChecksum("[1, 2, 4]", new byte[] {1, 2, 4}, 0x0B07);
        showVerify("verify([1, 2, 3], 0x0A06)", new byte[] {1, 2, 3}, 0x0A06, true);
        showVerify("verify([1, 2, 4], 0x0A06)", new byte[] {1, 2, 4}, 0x0A06, false);
        showVerify("verify(порожній, 0)", new byte[0], 0, true);
        showVerify("verify([1, 2, 3], 65535)", new byte[] {1, 2, 3}, 65535, false);
        showChecksum("[200, 100]", new byte[] {(byte) 200, 100}, 0xF52D);
    }

    private static void showDouble(String title, double actual, double expected) {
        boolean ok = Math.abs(actual - expected) < 1e-9;
        System.out.printf(Locale.ROOT, "%-32s -> %.4f (очікувалось %.4f) %s%n",
                title, actual, expected, status(ok));
        requireOk(title, ok);
    }

    private static void showChecksum(String title, byte[] data, int expected) {
        int actual = Fletcher16.compute(data);
        boolean ok = actual == expected;
        System.out.printf(Locale.ROOT, "%-26s -> 0x%04X (очікувалось 0x%04X) %s%n",
                title, actual, expected, status(ok));
        requireOk(title, ok);
    }

    private static void showVerify(String title, byte[] data, int checksum,
                                   boolean expected) {
        boolean actual = Fletcher16.verify(data, checksum);
        boolean ok = actual == expected;
        System.out.printf(Locale.ROOT, "%-26s -> %b (очікувалось %b) %s%n",
                title, actual, expected, status(ok));
        requireOk(title, ok);
    }

    private static String status(boolean ok) {
        return ok ? "OK" : "FAIL";
    }

    private static void requireOk(String title, boolean ok) {
        if (!ok) {
            throw new IllegalStateException("Перевірка не пройдена: " + title);
        }
    }
}