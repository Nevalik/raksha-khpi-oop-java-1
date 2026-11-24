package ua.khpi.oop1.lab12;

/**
 * Пристрій реєстру
 */
final class Device {
    private final int id;
    private final double power;
    /**
     * Створює пристрій із перевіркою вхідних даних
     *
     * @param id    додатний ідентифікатор пристрою
     * @param power потужність у ватах, скінченна й невід'ємна
     * @throws IllegalArgumentException якщо {@code id <= 0}, потужність від'ємна, {@code NaN} або нескінченна
     */
    Device(int id, double power) {
        if (id <= 0 || !Double.isFinite(power) || power < 0.0) {
            throw new IllegalArgumentException("недійсні дані пристрою");
        }
        this.id = id;
        this.power = power;
    }

    /**
     * @return додатний ідентифікатор
     */
    int id() {
        return id;
    }

    /**
     * @return потужність у ватах
     */
    double power() {
        return power;
    }
}