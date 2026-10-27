
package ua.khpi.oop1.lab08;

import java.util.Arrays;

/**
 * Обов'язковий рівень, варіант 1:
 * суми та середні значення кожного рядка нерегулярної таблиці.
 */
public class Main {

    /** Таблиця коректна */
    static final int STATUS_OK = 0;

    /** Таблиця дорівнює null */
    static final int STATUS_TABLE_NULL = 1;

    /** Принаймні один рядок дорівнює null */
    static final int STATUS_BAD_ROW = 2;

    /** Лічильники протоколу перевірки */
    static int checksTotal = 0;
    static int checksPassed = 0;

    /**
     * Перевіряє таблицю до обчислення.
     * Рядки можуть мати різну довжину, рядок може бути порожнім.
     */
    static int validateJagged(int[][] table) {
        if (table == null) {
            return STATUS_TABLE_NULL;
        }

        for (int row = 0; row < table.length; row++) {
            if (table[row] == null) {
                return STATUS_BAD_ROW;
            }
        }

        return STATUS_OK;
    }

    /**
     * Обчислює суму кожного рядка.
     * Довжина результату дорівнює кількості рядків.
     */
    static long[] rowSums(int[][] table) {
        if (validateJagged(table) != STATUS_OK) {
            return null;
        }

        long[] sums = new long[table.length];

        for (int row = 0; row < table.length; row++) {
            for (int column = 0; column < table[row].length; column++) {
                sums[row] += table[row][column];
            }
        }

        return sums;
    }

    /**
     * Обчислює середнє значення кожного рядка.
     * Для некоректної таблиці повертає null.
     */
    static double[] rowAverages(int[][] table) {
        long[] sums = rowSums(table);

        if (sums == null) {
            return null;
        }

        double[] averages = new double[table.length];

        for (int row = 0; row < table.length; row++) {
            if (table[row].length == 0) {
                averages[row] = Double.NaN;
            } else {
                averages[row] = (double) sums[row] / table[row].length;
            }
        }

        return averages;
    }

    /**
     * Повертає текст із сумами та середніми значеннями рядків.
     */
    static String jaggedText(int[][] table) {
        return Arrays.toString(rowSums(table)) + " "
                + Arrays.toString(rowAverages(table));
    }

    /**
     * Виводить таблицю, суму та середнє значення кожного рядка.
     */
    static void printJagged(String title, int[][] table) {
        System.out.println(title + " " + Arrays.deepToString(table));

        long[] sums = rowSums(table);
        double[] averages = rowAverages(table);

        for (int row = 0; row < table.length; row++) {
            if (table[row].length > 0) {
                System.out.println("  рядок " + row
                        + ": сума = " + sums[row]
                        + ", середнє = " + averages[row]);
            } else {
                System.out.println("  рядок " + row
                        + ": сума = " + sums[row]
                        + ", середнього немає (рядок порожній)");
            }
        }
    }

    /**
     * Перевіряє обов'язковий рівень, варіант 1.
     */
    static void testVariant1() {
        System.out.println(
                "Обов'язковий рівень. Варіант 1: суми та середні рядків");

        printJagged("Приклад методички:",
                new int[][] {{4, 7, 1}, {2, 9, 3}});

        printJagged("Нерегулярна таблиця:",
                new int[][] {{5}, {1, 2, 3}, {}, {10, -4}});

        System.out.println();
        System.out.println("Протокол перевірки (варіант 1)");
        printProtocolHeader();

        check("Мінімальний: [[5]]",
                "[5] [5.0]",
                jaggedText(new int[][] {{5}}));

        check("Типовий: [[4,7,1],[2,9,3]]",
                "[12, 14] [4.0, 4.666666666666667]",
                jaggedText(new int[][] {{4, 7, 1}, {2, 9, 3}}));

        check("Нерегулярні: [[5],[1,2,3]]",
                "[5, 6] [5.0, 2.0]",
                jaggedText(new int[][] {{5}, {1, 2, 3}}));

        check("Граничний: порожній рядок [[],[8]]",
                "[0, 8] [NaN, 8.0]",
                jaggedText(new int[][] {{}, {8}}));

        check("Граничний: порожня таблиця []",
                "[] []",
                jaggedText(new int[0][]));

        check("Граничний: від'ємні [[-3,3]]",
                "[0] [0.0]",
                jaggedText(new int[][] {{-3, 3}}));

        check("Граничний: переповнення int, суми",
                "[4294967294]",
                Arrays.toString(rowSums(
                        new int[][] {{Integer.MAX_VALUE, Integer.MAX_VALUE}})));

        check("Помилка: таблиця null",
                "null null",
                jaggedText(null));

        check("Помилка: рядок null [[1],null]",
                "null null",
                jaggedText(new int[][] {{1}, null}));

        System.out.println();
    }

    /** Повертає текстовий опис статусу */
    static String statusText(int status) {
        if (status == STATUS_OK) {
            return "OK";
        }
        if (status == STATUS_TABLE_NULL) {
            return "ПОМИЛКА: таблиця null";
        }
        return "ПОМИЛКА: некоректний рядок (null)";
    }

    /** Один запис протоколу перевірки */
    static void check(String name, String expected, String actual) {
        boolean ok = expected.equals(actual);

        checksTotal++;

        if (ok) {
            checksPassed++;
        }

        System.out.println("| " + name + " | " + expected + " | "
                + actual + " | " + (ok ? "PASS" : "FAIL") + " |");
    }

    /** Виводить заголовок протоколу перевірки */
    static void printProtocolHeader() {
        System.out.println("| Сценарій | Очікувано | Фактично | Статус |");
        System.out.println("|---|---|---|---|");
    }

    public static void main(String[] args) {
        testVariant1();

        System.out.println("Підсумок: пройдено "
                + checksPassed + " з " + checksTotal + " перевірок.");
    }
}
