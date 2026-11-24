package ua.khpi.oop1.lab12;

/**
 *  варіант 1: реєстр пристроїв із пошуком за ідентифікатором і сумою потужностей
 *  поглиблений рівень A (стабільне вилучення дублікатів).
 */
public class Main {
    /**
     * Створює реєстр місткістю 6, повністю заповнений пристроями.
     * Потужність кожного пристрою дорівнює {@code id * 10} Вт.
     *
     * @return масив із шести пристроїв
     */
    static Device[] sixDevices() {
        Device[] data = new Device[6];
        for (int index = 0; index < data.length; index++) {
            data[index] = new Device(index + 1, (index + 1) * 10.0);
        }
        return data;
    }

    /**
     * Формує текстове подання ідентифікаторів усього масиву.
     * Переглядається весь масив, тому вільні комірки позначаються словом {@code null} Приклад результату:
     * {@code [1, 2, 3, null, null]}.
     * @param data масив пристроїв
     * @return рядок вигляду {@code [id, id, null, ...]}
     */
    static String ids(Device[] data) {
        StringBuilder sb = new StringBuilder("[");
        for (int index = 0; index < data.length; index++) {
            if (index > 0) {
                sb.append(", ");
            }
            if (data[index] == null) {
                sb.append("null");
            } else {
                sb.append(data[index].id());
            }
        }
        return sb.append(']').toString();
    }

    /**
     * Виводить результат однієї перевірки й оновлює лічильники.
     * @param name      назва перевірки
     * @param condition {@code true}, якщо перевірка пройдена
     * @param result    лічильники: {@code result[0]} - пройдені,
     *                  {@code result[1]} - не пройдені перевірки
     */
    static void check(String name, boolean condition, int[] result) {
        if (condition) {
            result[0]++;
            System.out.println("OK    " + name);
        } else {
            result[1]++;
            System.out.println("FAIL  " + name);
        }
    }

    /**
     * Запускає всі перевірки.
     * Наприкінці виводить кількість пройдених і не пройдених перевірок.
     * @param args аргументи командного рядка не використовуються
     */
    public static void main(String[] args) {
        int[] result = new int[2];

        //Обов'язковий рівень: Реєстр пристроїв із пошуком за ідентифікатором і сумою потужностей
        Device[] data = new Device[5];
        int size = 0;
        size = ObjectArrayService.add(data, size, new Device(1, 12.5));
        check("add у порожній реєстр: size=1", size == 1 && data[0].id() == 1, result);
        size = ObjectArrayService.add(data, size, new Device(2, 18.0));
        size = ObjectArrayService.add(data, size, new Device(3, 30.0));
        System.out.println("Після додавання: " + ids(data) + " size=" + size);

        check("find існуючого id=2", ObjectArrayService.find(data, size, 2).power() == 18.0, result);
        check("find відсутнього id=9", ObjectArrayService.find(data, size, 9) == null, result);
        check("indexOf id=3", ObjectArrayService.indexOf(data, size, 3) == 2, result);
        check("indexOf відсутнього id", ObjectArrayService.indexOf(data, size, 9) == -1, result);
        check("totalPower = 60.5", ObjectArrayService.totalPower(data, size) == 60.5, result);
        check("totalPower при size=0", ObjectArrayService.totalPower(data, 0) == 0.0, result);
        check("find при size=0", ObjectArrayService.find(data, 0, 1) == null, result);
        check("indexOf при size=0", ObjectArrayService.indexOf(data, 0, 1) == -1, result);

        // елемент поза size не переглядається
        data[size] = new Device(7, 100.0);
        check("елемент поза size не знайдено", ObjectArrayService.find(data, size, 7) == null, result);
        check("елемент поза size не в сумі", ObjectArrayService.totalPower(data, size) == 60.5, result);
        data[size] = null;

        // пристрій з нульовою потужністю допустимий
        Device[] zero = { new Device(4, 0.0), null };
        check("потужність 0.0: пристрій знайдено", ObjectArrayService.find(zero, 1, 4) != null, result);
        check("потужність 0.0: сума 0.0", ObjectArrayService.totalPower(zero, 1) == 0.0, result);

        // Додавання
        int before = size;
        size = ObjectArrayService.add(data, size, new Device(2, 5.0));
        check("дублікат id не додається", size == before && data[size] == null, result);
        size = ObjectArrayService.add(data, size, null);
        check("null не додається", size == before && data[size] == null, result);
        size = ObjectArrayService.add(data, size, new Device(4, 1.0));
        check("додавання в останню вільну комірку: size=4", size == 4, result);
        size = ObjectArrayService.add(data, size, new Device(5, 2.0));
        check("реєстр заповнено: size=5", size == 5 && data[4].id() == 5, result);
        size = ObjectArrayService.add(data, size, new Device(6, 3.0));
        check("переповнення відхилено", size == 5 && data[4].id() == 5, result);
        Device[] noCapacity = new Device[0];
        check("add при місткості 0", ObjectArrayService.add(noCapacity, 0, new Device(1, 1.0)) == 0, result);

        // Видалення зі зсувом
        Device[] six = sixDevices();
        int sixSize = 6;
        System.out.println("До видалення id=3: " + ids(six) + " size=" + sixSize);
        sixSize = ObjectArrayService.removeId(six, sixSize, 3);
        System.out.println("Після видалення id=3: " + ids(six) + " size=" + sixSize);
        check("remove середнього: size=5", sixSize == 5, result);
        check("remove середнього: хвіст зсунуто", six[0].id() == 1 && six[1].id() == 2
                && six[2].id() == 4 && six[3].id() == 5 && six[4].id() == 6, result);
        check("remove середнього: остання комірка null", six[5] == null, result);
        check("після remove id=3 не знайдено", ObjectArrayService.find(six, sixSize, 3) == null, result);
        check("після remove сума = 180.0", ObjectArrayService.totalPower(six, sixSize) == 180.0, result);

        six = sixDevices();
        sixSize = ObjectArrayService.removeId(six, 6, 1);
        check("remove першого: size=5", sixSize == 5, result);
        check("remove першого: усі зсунуто", six[0].id() == 2 && six[1].id() == 3
                && six[2].id() == 4 && six[3].id() == 5 && six[4].id() == 6, result);
        check("remove першого: остання комірка null", six[5] == null, result);

        six = sixDevices();
        sixSize = ObjectArrayService.removeId(six, 6, 6);
        check("remove останнього: size=5", sixSize == 5, result);
        check("remove останнього: початок без змін", six[0].id() == 1 && six[1].id() == 2
                && six[2].id() == 3 && six[3].id() == 4 && six[4].id() == 5, result);
        check("remove останнього: комірка null", six[5] == null, result);

        Device[] single = { new Device(1, 1.0), null, null };
        int singleSize = ObjectArrayService.removeId(single, 1, 1);
        check("remove єдиного: size=0, комірка null", singleSize == 0 && single[0] == null, result);

        check("remove при size=0", ObjectArrayService.removeId(six, 0, 1) == 0, result);

        six = sixDevices();
        sixSize = ObjectArrayService.removeId(six, 6, 99);
        check("remove відсутнього: size=6, масив без змін", sixSize == 6 && six[0].id() == 1
                && six[2].id() == 3 && six[5].id() == 6, result);

        six = sixDevices();
        check("remove: елемент поза size не зачіпається",
                ObjectArrayService.removeId(six, 5, 6) == 5 && six[5] != null && six[5].id() == 6, result);

        // Поглиблений рівень A: стабільне вилучення дублікатів
        System.out.println();
        System.out.println("Поглиблений рівень A");

        Device[] dup = {
                new Device(1, 1.0), new Device(2, 2.0), new Device(1, 9.0),
                new Device(3, 3.0), new Device(2, 8.0), new Device(1, 7.0), null
        };
        Device firstOne = dup[0];
        int dupSize = 6;
        System.out.println("До:    " + ids(dup) + " size=" + dupSize);
        StringBuilder trace = new StringBuilder();
        dupSize = ObjectArrayService.removeDuplicatesTraced(dup, dupSize, trace);
        System.out.println("Проміжні кроки:");
        System.out.print(trace);
        System.out.println("Після: " + ids(dup) + " size=" + dupSize);
        check("A: новий size=3", dupSize == 3, result);
        check("A: порядок 1,2,3", dup[0].id() == 1 && dup[1].id() == 2 && dup[2].id() == 3, result);
        check("A: залишилися перші об'єкти", dup[0] == firstOne && dup[1].power() == 2.0
                && dup[2].power() == 3.0, result);
        check("A: хвіст null", dup[3] == null && dup[4] == null && dup[5] == null && dup[6] == null, result);
        check("A: трасування не порожнє", trace.length() > 0, result);

        Device[] empty = new Device[3];
        check("A: порожня частина", ObjectArrayService.removeDuplicates(empty, 0) == 0, result);

        Device[] one = { new Device(1, 1.0), null };
        check("A: один елемент", ObjectArrayService.removeDuplicates(one, 1) == 1 && one[0].id() == 1, result);

        Device[] noDup = { new Device(4, 1.0), new Device(5, 1.0), new Device(6, 1.0) };
        check("A: без повторів, size=length", ObjectArrayService.removeDuplicates(noDup, 3) == 3
                && noDup[0].id() == 4 && noDup[1].id() == 5 && noDup[2].id() == 6, result);

        Device[] adjacent = { new Device(8, 1.0), new Device(8, 2.0), new Device(9, 3.0), null };
        int adjSize = ObjectArrayService.removeDuplicates(adjacent, 3);
        check("A: сусідні повтори", adjSize == 2 && adjacent[0].id() == 8 && adjacent[0].power() == 1.0
                && adjacent[1].id() == 9 && adjacent[2] == null, result);

        Device[] edges = {
                new Device(1, 1.0), new Device(1, 2.0), new Device(2, 3.0),
                new Device(3, 4.0), new Device(3, 5.0)
        };
        int edgesSize = ObjectArrayService.removeDuplicates(edges, 5);
        check("A: повтори на початку й у кінці, масив повний", edgesSize == 3 && edges[0].id() == 1
                && edges[1].id() == 2 && edges[2].id() == 3 && edges[3] == null && edges[4] == null, result);

        Device[] same = { new Device(5, 1.0), new Device(5, 2.0), new Device(5, 3.0) };
        int sameSize = ObjectArrayService.removeDuplicates(same, 3);
        check("A: однакові ключі всіх", sameSize == 1 && same[0].power() == 1.0
                && same[1] == null && same[2] == null, result);

        Device[] beyond = { new Device(1, 1.0), new Device(1, 2.0), new Device(1, 3.0), new Device(2, 4.0) };
        int beyondSize = ObjectArrayService.removeDuplicates(beyond, 2);
        check("A: елементи поза size не зачіпаються", beyondSize == 1 && beyond[1] == null
                && beyond[2] != null && beyond[3] != null, result);

        // Недопустимі дані та недопустимий стан
        System.out.println();
        System.out.println("Помилки");

        boolean thrown;

        thrown = false;
        try {
            new Device(0, 1.0);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("Device: id <= 0 відхилено", thrown, result);

        thrown = false;
        try {
            new Device(1, -5.0);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("Device: від'ємна потужність відхилена", thrown, result);

        thrown = false;
        try {
            new Device(1, Double.NaN);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("Device: NaN відхилено", thrown, result);

        thrown = false;
        try {
            ObjectArrayService.find(data, -1, 1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("find: size < 0", thrown, result);

        thrown = false;
        try {
            ObjectArrayService.find(data, data.length + 1, 1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("find: size > length", thrown, result);

        thrown = false;
        try {
            ObjectArrayService.find(null, 0, 1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("find: масив null", thrown, result);

        thrown = false;
        try {
            Device[] hole = { new Device(1, 1.0), null, new Device(2, 2.0) };
            ObjectArrayService.totalPower(hole, 3);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("totalPower: null у логічній частині", thrown, result);

        thrown = false;
        try {
            ObjectArrayService.removeDuplicates(data, data.length + 1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("A: removeDuplicates, size > length", thrown, result);


        System.out.println();
        System.out.println("Пройдено: " + result[0] + ", помилок: " + result[1]);
    }
}