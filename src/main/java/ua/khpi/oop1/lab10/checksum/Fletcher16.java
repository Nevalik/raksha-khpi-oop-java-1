package ua.khpi.oop1.lab10.checksum;

/**
 * Контрольна сума Fletcher-16 для масиву байтів.
 * Результат є {@code int} у діапазоні 0..65535: старший байт — друга сума,
 * молодший байт — перша сума. Порожній масив дає 0, {@code null} не
 * приймається. Контрольна сума виявляє частину помилок передавання, але не є
 * криптографічним хешем. Клас не має стану.
 *
 * <p>Приклад: для байтів 1, 2, 3 результат дорівнює {@code 0x0A06}:
 * <pre>{@code
 * int checksum = Fletcher16.compute(new byte[] {1, 2, 3});
 * }</pre>
 */
public final class Fletcher16 {

    private static final int MODULUS = 255;
    private static final int MAX_VALUE = 65535;

    private Fletcher16() {
    }

    /**
     * Обчислює контрольну суму Fletcher-16.
     * Кожний байт перетворюється на беззнакове значення (0..255) перед
     * додаванням.
     *
     * @param data вхідні байти, не {@code null}
     * @return контрольна сума від 0 до 65535, тобто
     *         {@code (sum2 << 8) | sum1}
     * @throws IllegalArgumentException якщо {@code data} дорівнює {@code null}
     */
    public static int compute(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("data не може бути null");
        }
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i < data.length; i++) {
            int unsigned = data[i] & 0xFF;
            sum1 = (sum1 + unsigned) % MODULUS;
            sum2 = (sum2 + sum1) % MODULUS;
        }
        return (sum2 << 8) | sum1;
    }

    /**
     * Перевіряє, що контрольна сума даних дорівнює очікуваному значенню.
     *
     * @param data     вхідні байти, не {@code null}
     * @param expected очікувана контрольна сума в діапазоні 0..65535
     * @return {@code true}, якщо обчислена сума дорівнює {@code expected}
     * @throws IllegalArgumentException якщо {@code data} дорівнює {@code null}
     *                                  або {@code expected} поза діапазоном
     *                                  0..65535
     */
    public static boolean verify(byte[] data, int expected) {
        if (data == null) {
            throw new IllegalArgumentException("data не може бути null");
        }
        if (expected < 0 || expected > MAX_VALUE) {
            throw new IllegalArgumentException("expected поза діапазоном 0..65535");
        }
        return compute(data) == expected;
    }
}