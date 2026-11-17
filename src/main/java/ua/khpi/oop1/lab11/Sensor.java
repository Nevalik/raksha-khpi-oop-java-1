package ua.khpi.oop1.lab11;

/**
 * Датчик з ідентифікатором, одиницею вимірювання та поточним показом
 */
public final class Sensor {
    /** Ідентифікатор датчика */
    private final String id;

    /** Одиниця вимірювання */
    private final String unit;

    /** Поточний показ */
    private double value;

    /**
     * Створює датчик із початковим показом
     *
     * @param id    ідентифікатор датчика, не null і не порожній
     * @param unit  одиниця вимірювання, не null і не порожня
     * @param value початковий показ, скінченне число
     * @throws IllegalArgumentException якщо id чи unit порожні або value не скінченне
     */
    public Sensor(String id, String unit, double value) {
        if (!isValidText(id)) {
            throw new IllegalArgumentException("id є обов’язковим");
        }
        if (!isValidText(unit)) {
            throw new IllegalArgumentException("unit є обов’язковим");
        }
        if (!isValidValue(value)) {
            throw new IllegalArgumentException("value має бути скінченним");
        }
        this.id = id;
        this.unit = unit;
        this.value = value;
    }

    /**
     * Перевіряє текст для id або unit
     *
     * @param text текст для перевірки
     * @return true якщо текст не null і не порожній
     */
    public static boolean isValidText(String text) {
        return text != null && !text.isBlank();
    }

    /**
     * Перевіряє показ датчика
     *
     * @param value показ для перевірки
     * @return true якщо це скінченне число
     */
    public static boolean isValidValue(double value) {
        return Double.isFinite(value);
    }

    /**
     * Повертає ідентифікатор датчика
     *
     * @return ідентифікатор
     */
    public String getId() {
        return id;
    }

    /**
     * Повертає одиницю вимірювання
     *
     * @return одиниця вимірювання
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Повертає поточний показ
     *
     * @return поточний показ
     */
    public double getValue() {
        return value;
    }

    /**
     * Оновлює показ. Якщо значення недопустиме, старий показ не змінюється
     *
     * @param newValue новий показ, скінченне число
     * @throws IllegalArgumentException якщо newValue не скінченне
     */
    public void update(double newValue) {
        if (!isValidValue(newValue)) {
            throw new IllegalArgumentException("value must be finite");
        }
        value = newValue;
    }

    /**
     * Те саме, що update, але без винятку
     *
     * @param newValue новий показ
     * @return true, якщо показ оновлено; false, якщо значення недопустиме
     */
    public boolean tryUpdate(double newValue) {
        if (!isValidValue(newValue)) {
            return false;
        }
        value = newValue;
        return true;
    }

    /**
     * Повертає текстове подання датчика
     *
     * @return рядок виду "Sensor S-4: 19.8 °C"
     */
    @Override
    public String toString() {
        return "Sensor " + id + ": " + value + " " + unit;
    }
}