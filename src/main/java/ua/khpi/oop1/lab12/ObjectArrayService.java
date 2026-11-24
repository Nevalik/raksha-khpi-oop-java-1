package ua.khpi.oop1.lab12;

/**
 * Операції над заповненою частиною масиву {@link Device}{@code []}.
 */
final class ObjectArrayService {
    /**
     * Забороняє створення екземплярів службового класу
     */
    private ObjectArrayService() {
    }

    /**
     * Перевіряє, що масив існує, а логічний розмір лежить у межах
     * {@code 0 <= size <= data.length}.
     * @param data масив пристроїв
     * @param size логічний розмір
     * @throws IllegalArgumentException якщо масив дорівнює {@code null} або розмір поза допустимими межами
     */
    private static void checkRange(Device[] data, int size) {
        if (data == null || size < 0 || size > data.length) {
            throw new IllegalArgumentException("нелогічний розмір");
        }
    }

    /**
     * Шукає перший пристрій із заданим ідентифікатором у логічній частині масиву
     * @param data масив пристроїв
     * @param size логічний розмір
     * @param id   шуканий ідентифікатор
     * @return індекс знайденого пристрою або {@code -1}, якщо його немає
     * @throws IllegalArgumentException якщо {@code data} дорівнює {@code null} або {@code size} недопустимий
     */
    static int indexOf(Device[] data, int size, int id) {
        checkRange(data, size);
        for (int index = 0; index < size; index++) {
            if (data[index].id() == id) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Знаходить пристрій за ідентифікатором
     * @param data масив пристроїв
     * @param size логічний розмір
     * @param id   шуканий ідентифікатор
     * @return знайдений пристрій або {@code null}, якщо його немає
     * @throws IllegalArgumentException якщо {@code data} дорівнює {@code null}або {@code size} недопустимий
     */
    static Device find(Device[] data, int size, int id) {
        int index = indexOf(data, size, id);
        return index < 0 ? null : data[index];
    }

    /**
     * Обчислює суму потужностей усіх пристроїв логічної частини
     * @param data масив пристроїв
     * @param size логічний розмір
     * @return сума потужностей у ватах; для {@code size = 0} дорівнює {@code 0.0}
     * @throws IllegalArgumentException якщо {@code data} дорівнює {@code null}або {@code size} недопустимий
     */
    static double totalPower(Device[] data, int size) {
        checkRange(data, size);
        double sum = 0.0;
        for (int index = 0; index < size; index++) {
            sum += data[index].power();
        }
        return sum;
    }

    /**
     * Додає пристрій у кінець логічної частини
     * Пристрій не додається, якщо він дорівнює {@code null}, реєстр заповненийабо пристрій із таким самим ідентифікатором уже є. У цих випадках розмір не змінюється
     * @param data   масив пристроїв
     * @param size   поточний логічний розмір
     * @param device пристрій, який потрібно додати
     * @return новий логічний розмір: {@code size + 1} після додаванняабо попередній {@code size}, якщо додавання відхилено
     * @throws IllegalArgumentException якщо {@code data} дорівнює {@code null} або {@code size} недопустимий
     */
    static int add(Device[] data, int size, Device device) {
        checkRange(data, size);
        if (device == null || size == data.length) {
            return size;
        }
        if (indexOf(data, size, device.id()) >= 0) {
            return size;
        }
        data[size] = device;
        return size + 1;
    }

    /**
     * Видаляє пристрій за ідентифікатором зі зсувом хвоста ліворуч
     * @param data масив пристроїв
     * @param size поточний логічний розмір
     * @param id   ідентифікатор пристрою, який потрібно видалити
     * @return новий логічний розмір: {@code size - 1} після видаленняабо попередній {@code size}, якщо пристрою немає
     * @throws IllegalArgumentException якщо {@code data} дорівнює {@code null} або {@code size} недопустимий
     */
    static int removeAt(Device[] data, int size, int id) {
        checkRange(data, size);
        int index = indexOf(data, size, id);
        if (index < 0) {
            return size;
        }
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        return size - 1;
    }

    /**
     * Поглиблений рівень A: стабільне вилучення дублікатів за ідентифікатором
     * Операція виконується в тому самому масиві. Залишається перший об'єкт із кожним ідентифікатором, порядок решти елементів зберігається.
     * @param data масив пристроїв
     * @param size логічний розмір до вилучення
     * @return новий логічний розмір; комірки від нового розміру до старого {@code size} заповнені {@code null}
     * @throws IllegalArgumentException якщо {@code data} дорівнює {@code null} або {@code size} недопустимий
     * @see #removeDuplicatesTraced(Device[], int, StringBuilder)
     */
    static int removeDuplicates(Device[] data, int size) {
        return removeDuplicatesTraced(data, size, null);
    }

    /**
     * Вилучає дублікати так само, як {@link #removeDuplicates(Device[], int)},і за потреби записує проміжні кроки
     * @param data  масив пристроїв
     * @param size  логічний розмір до вилучення
     * @param trace буфер для тексту проміжних кроків або {@code null},якщо трасування не потрібне; текст лише дописується, виводити його має викликач
     * @return новий логічний розмір
     * @throws IllegalArgumentException якщо {@code data} дорівнює {@code null} або {@code size} недопустимий
     */
    static int removeDuplicatesTraced(Device[] data, int size, StringBuilder trace) {
        checkRange(data, size);
        int unique = 0;
        for (int read = 0; read < size; read++) {
            Device candidate = data[read];
            boolean is_unique = true;
            for (int index = 0; index < unique; index++) {
                if (data[index].id() == candidate.id()) {
                    is_unique = false;
                    break;
                }
            }
            if (is_unique) {
                data[unique] = candidate;
                unique++;
            }
            if (trace != null) {
                trace.append("read=").append(read)
                        .append(" id=").append(candidate.id())
                        .append(!is_unique ? " дублікат, пропуск" : " унікальний, запис")
                        .append(" | unique=").append(unique).append('\n');
            }
        }
        for (int index = unique; index < size; index++) {
            data[index] = null;
        }
        if (trace != null) {
            trace.append("очищення хвоста | size=").append(unique).append('\n');
        }
        return unique;
    }
}