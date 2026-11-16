package ua.khpi.oop1.lab10.measurement;

/**
 * Утилітарні операції над числовими вимірюваннями: обмеження, масштабування
 * та нормалізація. Усі значення є числами {@code double}; одиницю вимірювання
 * визначає викликач. {@code NaN} і нескінченності не приймаються: методи
 * відхиляють їх через {@link IllegalArgumentException}. Округлення не
 * виконується. Клас не має стану.
 *
 * <p>Приклад: код АЦП 2048 з діапазону 0..4095 у напругу 0..5 В:
 * <pre>{@code
 * double volts = MeasurementMath.scale(2048, 0, 4095, 0, 5);
 * }</pre>
 */
public final class MeasurementMath {

    private MeasurementMath() {
    }

    /**
     * Обмежує значення замкненим інтервалом [minimum, maximum].
     *
     * @param value   скінченне вхідне значення
     * @param minimum скінченна нижня межа
     * @param maximum скінченна верхня межа, не менша за {@code minimum}
     * @return {@code value}, якщо воно в інтервалі, інакше найближча межа
     * @throws IllegalArgumentException якщо аргумент не є скінченним або
     *                                  {@code minimum} більший за {@code maximum}
     */
    public static double clamp(double value, double minimum, double maximum) {
        requireFinite(value, "value");
        requireFinite(minimum, "minimum");
        requireFinite(maximum, "maximum");
        if (minimum > maximum) {
            throw new IllegalArgumentException("minimum більший за maximum");
        }
        return Math.max(minimum, Math.min(value, maximum));
    }

    /**
     * Лінійно переносить значення з вхідного інтервалу у вихідний.
     * Значення не обмежується: якщо воно поза вхідним інтервалом, результат
     * буде поза вихідним. Межі кожного інтервалу можна задавати в будь-якому
     * порядку, тому відображення може змінювати напрямок.
     *
     * @param value  скінченне вхідне значення
     * @param inMin  скінченна перша межа вхідного інтервалу
     * @param inMax  скінченна друга межа вхідного інтервалу, має відрізнятися
     *               від {@code inMin} (нульова ширина відхиляється)
     * @param outMin скінченна перша межа вихідного інтервалу
     * @param outMax скінченна друга межа вихідного інтервалу
     * @return значення, виражене у вихідному інтервалі
     * @throws IllegalArgumentException якщо аргумент не є скінченним, вхідний
     *                                  інтервал має нульову ширину або
     *                                  результат не є скінченним
     */
    public static double scale(double value, double inMin, double inMax,
                               double outMin, double outMax) {
        requireFinite(value, "value");
        requireFinite(inMin, "inMin");
        requireFinite(inMax, "inMax");
        requireFinite(outMin, "outMin");
        requireFinite(outMax, "outMax");
        if (inMin == inMax) {
            throw new IllegalArgumentException("вхідний інтервал має нульову ширину");
        }
        double fraction = (value - inMin) / (inMax - inMin);
        double result = outMin + fraction * (outMax - outMin);
        requireFinite(result, "result");
        return result;
    }

    /**
     * Нормалізує значення до діапазону від 0.0 до 1.0. Спочатку значення
     * обмежується вхідним інтервалом, тому результат завжди лежить у [0, 1].
     *
     * @param value скінченне вхідне значення
     * @param min   скінченна перша межа вхідного інтервалу
     * @param max   скінченна друга межа вхідного інтервалу, має відрізнятися
     *              від {@code min}
     * @return 0.0 у точці {@code min}, 1.0 у точці {@code max},
     *         пропорційне значення між ними
     * @throws IllegalArgumentException якщо аргумент не є скінченним або
     *                                  інтервал має нульову ширину
     */
    public static double normalize(double value, double min, double max) {
        double low = Math.min(min, max);
        double high = Math.max(min, max);
        double limited = clamp(value, low, high);
        return scale(limited, min, max, 0.0, 1.0);
    }

    private static void requireFinite(double number, String name) {
        if (!Double.isFinite(number)) {
            throw new IllegalArgumentException(name + " має бути скінченним числом");
        }
    }
}