package ua.khpi.oop1.lab11;

/**
 * Перетин двох добових інтервалів у хвилинах
 * Інтервал [start, end): початок входить, кінець ні. Якщо end менше start, інтервал переходить через опівніч; якщо start дорівнює end, він порожній
 */
public final class Intervals {
    /** Кількість хвилин у добі */
    public static final int MINUTES_PER_DAY = 1440;

    /** Клас містить лише статичні методи, тому об'єкти не створюються */
    private Intervals() {
    }

    /**
     * Перевіряє межу інтервалу
     *
     * @param minute хвилина доби
     * @return true, якщо minute від 0 до 1439
     */
    public static boolean isValidMinute(int minute) {
        return minute >= 0 && minute < MINUTES_PER_DAY;
    }

    /**
     * Рахує, скільки хвилин доби належать обом інтервалам
     *
     * @param start1 початок першого інтервалу, 0...1439
     * @param end1   кінець першого інтервалу (не входить), 0...1439
     * @param start2 початок другого інтервалу, 0...1439
     * @param end2   кінець другого інтервалу (не входить), 0...1439
     * @return кількість спільних хвилин
     * @throws IllegalArgumentException якщо якась межа поза 0...1439
     */
    public static int intersectionMinutes(int start1, int end1, int start2, int end2) {
        if (!isValidMinute(start1) || !isValidMinute(end1)
                || !isValidMinute(start2) || !isValidMinute(end2)) {
            throw new IllegalArgumentException("хвилини мають бути в інтервалі  0...1439");
        }

        int total = 0;
        for (int minute = 0; minute < MINUTES_PER_DAY; minute++) {
            if (contains(start1, end1, minute) && contains(start2, end2, minute)) {
                total++;
            }
        }
        return total;
    }

    /**
     * Перевіряє, чи входить хвилина в інтервал [start, end),
     * враховуючи перехід через опівніч
     *
     * @param start  початок інтервалу
     * @param end    кінець інтервалу (не входить)
     * @param minute хвилина, яку перевіряємо
     * @return true, якщо хвилина належить інтервалу
     */
    static boolean contains(int start, int end, int minute) {
        if (start <= end) {
            return minute >= start && minute < end;
        }
        return minute >= start || minute < end;
    }
}