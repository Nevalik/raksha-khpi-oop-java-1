package ua.khpi.oop1.lab11;

/**
 * Демонстрація і перевірки для Sensor та Intervals.
 * Без аргументів запускає всі перевірки; з аргументом sensor-error
 * або interval-error показує виняток.
 */
public class Main {
    /** Кількість успішних перевірок. */
    private static int passed;
    /** Кількість невдалих перевірок. */
    private static int failed;

    /** Клас містить лише статичні методи, тому об'єкти не створюються. */
    private Main() {
    }

    /**
     * Точка входу.
     *
     * @param args без аргументів: усі перевірки; sensor-error або interval-error:
     *             показ винятку
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            demonstrateError(args[0]);
            return;
        }

        System.out.println("Обов'язковий рівень: Sensor ");
        testSensor();
        System.out.println();
        System.out.println("Поглиблений рівень A: Intervals");
        testIntervals();
        System.out.println();
        System.out.println("Passed: " + passed + ", failed: " + failed);
    }

    /**
     * Виконує недопустимий виклик. Виняток не перехоплюється,
     * тому програма завершується з IllegalArgumentException.
     *
     * @param mode sensor-error або interval-error
     */
    private static void demonstrateError(String mode) {
        if (mode.equals("sensor-error")) {
            Sensor sensor = new Sensor("S-1", "°C", 1.0);
            System.out.println(sensor);
            System.out.println("Виклик update(NaN):");
            sensor.update(Double.NaN);
        } else if (mode.equals("interval-error")) {
            System.out.println("Виклик intersectionMinutes(-1, 10, 0, 10):");
            Intervals.intersectionMinutes(-1, 10, 0, 10);
        } else {
            System.out.println("Невідомий аргумент. Допустимо: sensor-error, interval-error");
        }
    }

    /** Перевірки обов'язкового рівня (Sensor). */
    private static void testSensor() {
        Sensor sensor = new Sensor("S-7", "°C", 24.5);
        System.out.println(sensor);
        check("1 типовий: створення", "Sensor S-7: 24.5 °C", sensor.toString());
        check("1б читання: getId і getUnit", "S-7 °C", sensor.getId() + " " + sensor.getUnit());

        sensor.update(25.0);
        check("2 коректне оновлення", "25.0", String.valueOf(sensor.getValue()));

        sensor.update(-273.15);
        check("3 граничне: від'ємне значення", "-273.15", String.valueOf(sensor.getValue()));

        sensor.update(0.0);
        check("4 мінімальний: нуль", "0.0", String.valueOf(sensor.getValue()));

        // недопустимі значення: оновлення відхиляється, стан (0.0) зберігається
        checkRejectedUpdate("5 tryUpdate(NaN)", sensor, Double.NaN);
        checkRejectedUpdate("6 tryUpdate(+Infinity)", sensor, Double.POSITIVE_INFINITY);
        checkRejectedUpdate("7 tryUpdate(-Infinity)", sensor, Double.NEGATIVE_INFINITY);

        // умови, за якими конструктор кидає IllegalArgumentException
        check("8 id = null недопустимий", "false", String.valueOf(Sensor.isValidText(null)));
        check("9 id з пробілів недопустимий", "false", String.valueOf(Sensor.isValidText("  ")));
        check("10 unit = null недопустимий", "false", String.valueOf(Sensor.isValidText(null)));
        check("11 unit порожній недопустимий", "false", String.valueOf(Sensor.isValidText("")));
        check("12 value = NaN недопустиме", "false",
                String.valueOf(Sensor.isValidValue(Double.NaN)));
        check("13 value = Infinity недопустиме", "false",
                String.valueOf(Sensor.isValidValue(Double.POSITIVE_INFINITY)));

        // два незалежні об'єкти та псевдонім
        Sensor a = new Sensor("A", "V", 1.0);
        Sensor b = new Sensor("B", "V", 2.0);
        Sensor alias = a;
        alias.update(5.0);
        check("14 псевдонім: зміна видна через a", "5.0", String.valueOf(a.getValue()));
        check("15 незалежний об'єкт b не змінився", "2.0", String.valueOf(b.getValue()));
    }

    /** Перевірки поглибленого рівня (задача A). */
    private static void testIntervals() {
        // дані з умови (хвилини = години * 60)
        check("A1 09:00-12:00 з 10:00-14:00", "120",
                String.valueOf(Intervals.intersectionMinutes(540, 720, 600, 840)));
        check("A2 22:00-02:00 з 01:00-03:00", "60",
                String.valueOf(Intervals.intersectionMinutes(1320, 120, 60, 180)));
        check("A3 неперетинні 08:00-09:00 з 09:00-10:00", "0",
                String.valueOf(Intervals.intersectionMinutes(480, 540, 540, 600)));
        check("A4 порожній (5:00-5:00) з 00:00-23:59", "0",
                String.valueOf(Intervals.intersectionMinutes(300, 300, 0, 1439)));

        // власний граничний набір
        check("A5 обидва через опівніч 22:00-02:00 з 23:00-01:00", "120",
                String.valueOf(Intervals.intersectionMinutes(1320, 120, 1380, 60)));
        check("A6 кінець 00:00: 23:00-00:00 з 22:00-01:00", "60",
                String.valueOf(Intervals.intersectionMinutes(1380, 0, 1320, 60)));
        check("A7 вкладений 10:00-11:00 у 22:00-12:00", "60",
                String.valueOf(Intervals.intersectionMinutes(600, 660, 1320, 720)));
        check("A8 однакові 100-200 з 100-200", "100",
                String.valueOf(Intervals.intersectionMinutes(100, 200, 100, 200)));
        check("A9 повна доба: 1-0 з 0-1439", "1438",
                String.valueOf(Intervals.intersectionMinutes(1, 0, 0, 1439)));
        check("A10 межа 1439: 1439-0 з 1439-1", "1",
                String.valueOf(Intervals.intersectionMinutes(1439, 0, 1439, 1)));
        check("A11 симетрія", "60",
                String.valueOf(Intervals.intersectionMinutes(60, 180, 1320, 120)));
        check("A11б два порожні (0,0) і (7,7)", "0",
                String.valueOf(Intervals.intersectionMinutes(0, 0, 7, 7)));
        check("A11в межа 1439 допустима: (1439,1439) з (0,1439)", "0",
                String.valueOf(Intervals.intersectionMinutes(1439, 1439, 0, 1439)));

        // межі поза 0..1439: метод кидає IllegalArgumentException
        check("A12 start1 = -1 недопустима", "false", String.valueOf(Intervals.isValidMinute(-1)));
        check("A13 end1 = 1440 недопустима", "false", String.valueOf(Intervals.isValidMinute(1440)));
        check("A14 start2 = 1440 недопустима", "false", String.valueOf(Intervals.isValidMinute(1440)));
        check("A15 end2 = -5 недопустима", "false", String.valueOf(Intervals.isValidMinute(-5)));

        System.out.println();
        System.out.println("Проміжні кроки для 22:00-02:00 з 01:00-03:00:");
        printTrace(1320, 120, 60, 180);
    }

    /**
     * Показує проміжні кроки: які хвилини спільні для двох інтервалів.
     *
     * @param start1 початок першого інтервалу
     * @param end1   кінець першого інтервалу
     * @param start2 початок другого інтервалу
     * @param end2   кінець другого інтервалу
     */
    private static void printTrace(int start1, int end1, int start2, int end2) {
        int first1End = Intervals.firstPartEnd(start1, end1);
        int second1End = Intervals.secondPartEnd(start1, end1);
        int first2End = Intervals.firstPartEnd(start2, end2);
        int second2End = Intervals.secondPartEnd(start2, end2);

        System.out.println("  інтервал 1: " + part(start1, first1End) + " + " + part(0, second1End));
        System.out.println("  інтервал 2: " + part(start2, first2End) + " + " + part(0, second2End));

        int p1 = Intervals.overlap(start1, first1End, start2, first2End);
        int p2 = Intervals.overlap(start1, first1End, 0, second2End);
        int p3 = Intervals.overlap(0, second1End, start2, first2End);
        int p4 = Intervals.overlap(0, second1End, 0, second2End);
        System.out.println("  " + part(start1, first1End) + " x " + part(start2, first2End) + " -> " + p1);
        System.out.println("  " + part(start1, first1End) + " x " + part(0, second2End) + " -> " + p2);
        System.out.println("  " + part(0, second1End) + " x " + part(start2, first2End) + " -> " + p3);
        System.out.println("  " + part(0, second1End) + " x " + part(0, second2End) + " -> " + p4);
        System.out.println("  сума = " + (p1 + p2 + p3 + p4));
    }

    /**
     * Повертає текст частини інтервалу.
     *
     * @param from початок частини
     * @param to   кінець частини
     * @return рядок виду [from,to) або "порожня"
     */
    private static String part(int from, int to) {
        if (to <= from) {
            return "порожня";
        }
        return "[" + from + "," + to + ")";
    }

    /**
     * Перевіряє, що tryUpdate відхиляє значення і не змінює датчик.
     *
     * @param name   назва перевірки
     * @param sensor датчик
     * @param value  недопустиме значення
     */
    private static void checkRejectedUpdate(String name, Sensor sensor, double value) {
        String before = sensor.toString();
        boolean accepted = sensor.tryUpdate(value);
        check(name + ": відхилено, стан збережено", "true",
                String.valueOf(!accepted && before.equals(sensor.toString())));
    }

    /**
     * Порівнює очікуване з фактичним, друкує [OK] або [FAIL] і рахує результати.
     *
     * @param name     назва перевірки
     * @param expected очікуваний результат
     * @param actual   фактичний результат
     */
    private static void check(String name, String expected, String actual) {
        boolean ok = expected.equals(actual);
        if (ok) {
            passed++;
        } else {
            failed++;
        }
        System.out.println((ok ? "[OK]   " : "[FAIL] ") + name
                + " | очікувано: " + expected + " | фактично: " + actual);
    }
}