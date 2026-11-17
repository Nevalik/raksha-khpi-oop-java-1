package ua.khpi.oop1.lab11;

/**
 * Перетин двох добових інтервалів у хвилинах.
 * Інтервал [start, end): початок входить, кінець ні. Якщо end менше start,
 * інтервал переходить через опівніч; якщо start дорівнює end, він порожній.
 *
 * Кожний інтервал розбивається на дві частини: [start, firstPartEnd) і
 * [0, secondPartEnd). Порожня частина має кінець, що не перевищує початок.
 */
public final class Intervals {
    /** Кількість хвилин у добі. */
    public static final int MINUTES_PER_DAY = 1440;

    /** Клас містить лише статичні методи, тому об'єкти не створюються. */
    private Intervals() {
    }

    /**
     * Перевіряє межу інтервалу.
     *
     * @param minute хвилина доби
     * @return true, якщо minute від 0 до 1439
     */
    public static boolean isValidMinute(int minute) {
        return minute >= 0 && minute < MINUTES_PER_DAY;
    }

    /**
     * Рахує, скільки хвилин доби належать обом інтервалам.
     *
     * @param start1 початок першого інтервалу, 0..1439
     * @param end1   кінець першого інтервалу (не входить), 0..1439
     * @param start2 початок другого інтервалу, 0..1439
     * @param end2   кінець другого інтервалу (не входить), 0..1439
     * @return кількість спільних хвилин
     * @throws IllegalArgumentException якщо якась межа поза 0..1439
     */
    public static int intersectionMinutes(int start1, int end1, int start2, int end2) {
        if (!isValidMinute(start1) || !isValidMinute(end1)
                || !isValidMinute(start2) || !isValidMinute(end2)) {
            throw new IllegalArgumentException("хвилини мають бути в інтервалі 0...1439");
        }

        int first1End = firstPartEnd(start1, end1);
        int second1End = secondPartEnd(start1, end1);
        int first2End = firstPartEnd(start2, end2);
        int second2End = secondPartEnd(start2, end2);

        return overlap(start1, first1End, start2, first2End)
                + overlap(start1, first1End, 0, second2End)
                + overlap(0, second1End, start2, first2End)
                + overlap(0, second1End, 0, second2End);
    }

    /**
     * Повертає кінець першої частини [start, ...).
     *
     * @param start початок інтервалу
     * @param end   кінець інтервалу
     * @return end, якщо переходу через опівніч немає; 1440, якщо він є;
     *         start, якщо інтервал порожній
     */
    static int firstPartEnd(int start, int end) {
        if (start < end) {
            return end;
        }
        if (start > end) {
            return MINUTES_PER_DAY;
        }
        return start;
    }

    /**
     * Повертає кінець другої частини [0, ...).
     *
     * @param start початок інтервалу
     * @param end   кінець інтервалу
     * @return end, якщо інтервал переходить через опівніч; інакше 0 (частина порожня)
     */
    static int secondPartEnd(int start, int end) {
        if (start > end) {
            return end;
        }
        return 0;
    }

    /**
     * Рахує довжину перетину двох півінтервалів [from1, to1) і [from2, to2).
     *
     * @param from1 початок першого півінтервалу
     * @param to1   кінець першого півінтервалу
     * @param from2 початок другого півінтервалу
     * @param to2   кінець другого півінтервалу
     * @return довжина перетину, або 0, якщо вони не перетинаються
     */
    static int overlap(int from1, int to1, int from2, int to2) {
        int from = from1 > from2 ? from1 : from2;
        int to = to1 < to2 ? to1 : to2;
        return to > from ? to - from : 0;
    }
}