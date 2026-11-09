package ua.khpi.oop1.lab08;

import java.util.Arrays;

/**
 * Обов'язковий рівень, варіант 1: суми та середні значення кожного рядка
 * нерегулярної таблиці.
 * Поглиблений рівень, завдання A: локальні піки температурної карти.
 */
public class Main {

    /** Таблиця коректна */
    static final int STATUS_OK = 0;
    /** Таблиця дорівнює null */
    static final int STATUS_TABLE_NULL = 1;
    /** Принаймні один рядок дорівнює null або рядки різної довжини(А)  */
    static final int STATUS_BAD_ROW = 2;
    /** Серед значень є NaN або нескінченність  */
    static final int STATUS_NOT_FINITE = 3;

    /** Лічильники протоколу перевірки */
    static int checksTotal = 0;
    static int checksPassed = 0;


    //варіант 1

    /**
     * Перевіряє таблицю до обчислення
     * Рядки можуть мати різну довжину, рядок може бути порожнім
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
     * Сума кожного рядка. Довжина результату дорівнює кількості рядків
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
     * Середнє значення кожного рядка
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


    //завдання A


    /** Зміщення рядка для чотирьох сусідів: вгору, вниз, ліворуч, праворуч */
    static final int[] NEIGHBOR_ROW_SHIFT = {-1, 1, 0, 0};
    /** Зміщення стовпця для сусідів. */
    static final int[] NEIGHBOR_COLUMN_SHIFT = {0, 0, -1, 1};

    /**
     * Перевіряє температурну карту до пошуку: не null, рядки не null,
     * форма прямокутна, усі значення скінченні
     */
    static int validateTemperatureMap(double[][] map) {
        if (map == null) {
            return STATUS_TABLE_NULL;
        }
        if (map.length == 0) {
            return STATUS_OK;
        }
        if (map[0] == null) {
            return STATUS_BAD_ROW;
        }
        int columns = map[0].length;
        for (int row = 0; row < map.length; row++) {
            if (map[row] == null || map[row].length != columns) {
                return STATUS_BAD_ROW;
            }
        }
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < columns; column++) {
                if (!Double.isFinite(map[row][column])) {
                    return STATUS_NOT_FINITE;
                }
            }
        }
        return STATUS_OK;
    }

    /** Чи лежить комірка в межах матриці */
    static boolean isInside(double[][] map, int row, int column) {
        return row >= 0 && row < map.length
                && column >= 0 && column < map[row].length;
    }

    /**
     * Чи є комірка локальним піком
     * Передумова: карта пройшла validateTemperatureMap, індекси в межах матриці
     */
    static boolean isLocalPeak(double[][] map, int row, int column) {
        for (int direction = 0; direction < NEIGHBOR_ROW_SHIFT.length; direction++) {
            int neighborRow = row + NEIGHBOR_ROW_SHIFT[direction];
            int neighborColumn = column + NEIGHBOR_COLUMN_SHIFT[direction];
            if (isInside(map, neighborRow, neighborColumn)
                    && map[row][column] <= map[neighborRow][neighborColumn]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Будує матрицю boolean з позначками піків
     * Для некоректної карти повертає null
     */
    static boolean[][] peakMap(double[][] map) {
        if (validateTemperatureMap(map) != STATUS_OK) {
            return null;
        }
        boolean[][] peaks = new boolean[map.length][];
        for (int row = 0; row < map.length; row++) {
            peaks[row] = new boolean[map[row].length];
            for (int column = 0; column < map[row].length; column++) {
                peaks[row][column] = isLocalPeak(map, row, column);
            }
        }
        return peaks;
    }

    /** Кількість піків у матриці */
    static int countPeaks(boolean[][] peaks) {
        int count = 0;
        for (int row = 0; row < peaks.length; row++) {
            for (int column = 0; column < peaks[row].length; column++) {
                if (peaks[row][column]) {
                    count++;
                }
            }
        }
        return count;
    }


    // Консольне подання
    static String statusText(int status) {
        if (status == STATUS_OK) {
            return "OK";
        }
        if (status == STATUS_TABLE_NULL) {
            return "ПОМИЛКА: таблиця null";
        }
        if (status == STATUS_BAD_ROW) {
            return "ПОМИЛКА: некоректний рядок (null або різна довжина)";
        }
        return "ПОМИЛКА: значення не скінченне (NaN або нескінченність)";
    }

    /** Один запис протоколу перевірки: сценарій, очікуване, фактичне, статус */
    static void check(String name, String expected, String actual) {
        boolean ok = expected.equals(actual);
        checksTotal++;
        if (ok) {
            checksPassed++;
        }
        System.out.println("| " + name + " | " + expected + " | " + actual + " | "
                + (ok ? "PASS" : "FAIL") + " |");
    }

    static void printProtocolHeader() {
        System.out.println("| Сценарій | Очікувано | Фактично | Статус |");
        System.out.println("|---|---|---|---|");
    }

    /** Суми та середні рядків в одному тексті для протоколу */
    static String jaggedText(int[][] table) {
        return Arrays.toString(rowSums(table)) + " " + Arrays.toString(rowAverages(table));
    }

    static void printJagged(String title, int[][] table) {
        System.out.println(title + " " + Arrays.deepToString(table));
        long[] sums = rowSums(table);
        double[] averages = rowAverages(table);
        for (int row = 0; row < table.length; row++) {
            if (table[row].length > 0) {
                System.out.println("  рядок " + row + ": сума = " + sums[row]
                        + ", середнє = " + averages[row]);
            } else {
                System.out.println("  рядок " + row + ": сума = " + sums[row]
                        + ", середнього немає (рядок порожній)");
            }
        }
    }

    static void testVariant1() {
        System.out.println("Обов'язковий рівень. Варіант 1: суми та середні рядків");
        printJagged("Приклад методички:", new int[][] {{4, 7, 1}, {2, 9, 3}});
        printJagged("Нерегулярна таблиця:", new int[][] {{5}, {1, 2, 3}, {}, {10, -4}});
        System.out.println();
        System.out.println("Протокол перевірки (варіант 1)");
        printProtocolHeader();

        check("Мінімальний: [[5]]", "[5] [5.0]",
                jaggedText(new int[][] {{5}}));
        check("Типовий: [[4,7,1],[2,9,3]]", "[12, 14] [4.0, 4.666666666666667]",
                jaggedText(new int[][] {{4, 7, 1}, {2, 9, 3}}));
        check("Нерегулярні: [[5],[1,2,3]]", "[5, 6] [5.0, 2.0]",
                jaggedText(new int[][] {{5}, {1, 2, 3}}));
        check("Граничний: порожній рядок [[],[8]]", "[0, 8] [NaN, 8.0]",
                jaggedText(new int[][] {{}, {8}}));
        check("Граничний: порожня таблиця []", "[] []",
                jaggedText(new int[0][]));
        check("Граничний: від'ємні [[-3,3]]", "[0] [0.0]",
                jaggedText(new int[][] {{-3, 3}}));
        check("Граничний: переповнення int, суми", "[4294967294]",
                Arrays.toString(rowSums(new int[][] {{Integer.MAX_VALUE, Integer.MAX_VALUE}})));
        check("Помилка: таблиця null", "null null",
                jaggedText(null));
        check("Помилка: рядок null [[1],null]", "null null",
                jaggedText(new int[][] {{1}, null}));
        System.out.println();
    }

    /** Проміжні кроки для одного нетривіального набору: порівняння кожної комірки із сусідами. */
    static void traceCells(double[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                System.out.print("  (" + row + "," + column + ") = " + map[row][column]
                        + ", сусіди:");
                for (int direction = 0; direction < NEIGHBOR_ROW_SHIFT.length; direction++) {
                    int neighborRow = row + NEIGHBOR_ROW_SHIFT[direction];
                    int neighborColumn = column + NEIGHBOR_COLUMN_SHIFT[direction];
                    if (isInside(map, neighborRow, neighborColumn)) {
                        System.out.print(" " + map[neighborRow][neighborColumn]);
                    }
                }
                System.out.println(" => " + (isLocalPeak(map, row, column) ? "ПІК" : "не пік"));
            }
        }
    }

    static String peaksText(double[][] map) {
        boolean[][] peaks = peakMap(map);
        if (peaks == null) {
            return "null";
        }
        return countPeaks(peaks) + " " + Arrays.deepToString(peaks);
    }

    static void testTaskA() {
        System.out.println("Поглиблений рівень. Завдання A: локальні піки");
        double[][] trace = {
                {9.0, 1.0, 2.0},
                {3.0, 4.0, 3.0},
                {2.0, 1.0, 8.0}
        };
        System.out.println("Нетривіальний набір 3x3: " + Arrays.deepToString(trace));
        System.out.println("Проміжні кроки (порівняння з наявними сусідами):");
        traceCells(trace);
        System.out.println("Кількість піків: " + countPeaks(peakMap(trace)));
        System.out.println();

        System.out.println("Протокол перевірки (завдання A): кількість піків і матриця позначок");
        printProtocolHeader();

        check("Порожня матриця 0x0", "0 []",
                peaksText(new double[0][]));
        check("Одна комірка 1x1", "1 [[true]]",
                peaksText(new double[][] {{5.0}}));
        check("Один рядок [1,3,2,5,4]", "2 [[false, true, false, true, false]]",
                peaksText(new double[][] {{1.0, 3.0, 2.0, 5.0, 4.0}}));
        check("Один стовпець [1,4,2]", "1 [[false], [true], [false]]",
                peaksText(new double[][] {{1.0}, {4.0}, {2.0}}));
        check("Плато 2x2 з рівними значеннями", "0 [[false, false], [false, false]]",
                peaksText(new double[][] {{2.0, 2.0}, {2.0, 2.0}}));
        check("Дві рівні сусідні комірки [7,7,1]", "0 [[false, false, false]]",
                peaksText(new double[][] {{7.0, 7.0, 1.0}}));
        check("Кілька піків 3x3 (два кути і центр)",
                "3 [[true, false, false], [false, true, false], [false, false, true]]",
                peaksText(trace));
        check("Пік у куті [[5,1],[1,0]]", "1 [[true, false], [false, false]]",
                peaksText(new double[][] {{5.0, 1.0}, {1.0, 0.0}}));
        check("Від'ємні температури [[-5,-9],[-8,-7]]", "2 [[true, false], [false, true]]",
                peaksText(new double[][] {{-5.0, -9.0}, {-8.0, -7.0}}));
        check("Помилка: карта null", "null",
                peaksText(null));
        check("Помилка: нерегулярна форма", "null",
                peaksText(new double[][] {{1.0, 2.0}, {3.0}}));
        check("Помилка: NaN у карті", "null",
                peaksText(new double[][] {{1.0, Double.NaN}}));
        check("Помилка: нескінченність", statusText(STATUS_NOT_FINITE),
                statusText(validateTemperatureMap(new double[][] {{Double.POSITIVE_INFINITY}})));
        System.out.println();
    }

    public static void main(String[] args) {
        testVariant1();
        testTaskA();
        System.out.println("Підсумок: пройдено " + checksPassed + " з " + checksTotal + " перевірок.");
    }
}