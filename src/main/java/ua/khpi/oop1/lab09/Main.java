package ua.khpi.oop1.lab09;

/**
 * Лабораторна робота № 9. Варіант 1 (обов'язковий рівень).
 * Регулярні вирази не використовуються.
 */
public class Main {

    // Стани автомата для розбору командного рядка (завдання A)
    static final int STATE_START = 0;        // початок поля (очікуємо символ або ';')
    static final int STATE_PLAIN = 1;        // звичайне поле
    static final int STATE_QUOTED = 2;       // всередині подвійних лапок
    static final int STATE_AFTER_QUOTE = 3;  // після закривної лапки (очікуємо ';' або кінець)

    // Коди помилок, які повертає parseCommand
    static final int ERROR_UNCLOSED_QUOTE = -1;      // лапки відкриті, але не закриті до кінця рядка
    static final int ERROR_QUOTE_IN_PLAIN_FIELD = -2; // лапка всередині звичайного поля
    static final int ERROR_TEXT_AFTER_QUOTE = -3;     // текст після закривної лапки до ';'

    // Лічильники для протоколу тестів
    static int total = 0;
    static int passed = 0;

    // Обчислення

    /**
     * Замінює кожну послідовність пробільних символів одним пробілом і прибирає пробіли по краях.
     * Вхід не може бути null.
     */
    static String normalize(String source) {
        StringBuilder result = new StringBuilder(source.length());
        boolean separatorPending = false;

        for (int index = 0; index < source.length(); index++) {
            char symbol = source.charAt(index);
            if (Character.isWhitespace(symbol)) {
                // пробіл додаємо лише тоді коли вже є текст
                separatorPending = result.length() > 0;
            } else {
                if (separatorPending) {
                    result.append(' ');
                    separatorPending = false;
                }
                result.append(symbol);
            }
        }
        return result.toString();
    }

    /**
     * Кількість вилучених одиниць UTF-16: різниця довжин початкового і нормалізованого рядків
     */
    static int removedUnits(String source, String normalized) {
        return source.length() - normalized.length();
    }

    /**
     * Завдання A: розбір командного рядка на поля.
     *   - поля розділяються символом ';';
     *   - порожні поля дозволені , "a;;c" -> три поля: "a", "", "c");
     *   - поле може бути взяте в подвійні лапки: "a;b";c -> поля "a;b" і "c";
     *   - всередині лапок подвоєна лапка "" означає одну лапку: "a""b" -> a"b;
     *   - лапка всередині звичайного поля - помилка ERROR_QUOTE_IN_PLAIN_FIELD;
     *   - будь-який текст після закривної лапки до ';' - помилка ERROR_TEXT_AFTER_QUOTE;
     *   - якщо лапки не закрито до кінця рядка - помилка ERROR_UNCLOSED_QUOTE.

     */
    static int parseCommand(String line, String[] fields) {
        StringBuilder field = new StringBuilder(); // накопичувач поточного поля
        int count = 0;                             // скільки полів уже збережено
        int state = STATE_START;                   // поточний стан автомата

        for (int index = 0; index < line.length(); index++) {
            char symbol = line.charAt(index);

            if (state == STATE_START) {
                // На початку поля: ';' дає порожнє поле, '"' відкриває цитату,
                // будь-який інший символ починає звичайне поле.
                if (symbol == ';') {
                    fields[count] = "";
                    count++;
                } else if (symbol == '"') {
                    state = STATE_QUOTED;          // переходимо всередину лапок
                } else {
                    field.append(symbol);
                    state = STATE_PLAIN;
                }
            } else if (state == STATE_PLAIN) {
                // У звичайному полі: ';' завершує поле, '"' - заборонена,
                // інші символи просто додаються.
                if (symbol == ';') {
                    fields[count] = field.toString();
                    count++;
                    field.setLength(0);           // очищаємо накопичувач для наступного поля
                    state = STATE_START;
                } else if (symbol == '"') {
                    return ERROR_QUOTE_IN_PLAIN_FIELD;
                } else {
                    field.append(symbol);
                }
            } else if (state == STATE_QUOTED) {
                // Всередині лапок: "" - це одна літеральна лапка (екранування),
                // одинарна " закриває цитату, решта символів (включно з ';') - частина поля.
                if (symbol == '"') {
                    boolean doubledQuote = index + 1 < line.length()
                            && line.charAt(index + 1) == '"';
                    if (doubledQuote) {
                        field.append('"');         // екранована лапка
                        index++;                   // пропускаємо другу лапку пари
                    } else {
                        state = STATE_AFTER_QUOTE; // цитату закрито
                    }
                } else {
                    field.append(symbol);
                }
            } else { // STATE_AFTER_QUOTE
                // Після закривної лапки допустимий лише ';' або кінець рядка.
                if (symbol == ';') {
                    fields[count] = field.toString();
                    count++;
                    field.setLength(0);
                    state = STATE_START;
                } else {
                    return ERROR_TEXT_AFTER_QUOTE; // текст після лапки - помилка
                }
            }
        }

        // Якщо рядок закінчився всередині лапок - цитата не закрита
        if (state == STATE_QUOTED) {
            return ERROR_UNCLOSED_QUOTE;
        }

        // Зберігаємо останнє поле
        // Це коректно і для STATE_START (порожнє поле), і для STATE_PLAIN, і для STATE_AFTER_QUOTE
        fields[count] = field.toString();
        count++;
        return count;
    }

    /**
     * Допоміжний метод для тестів: перетворює рядок у текстове подання полів
     * виду "[][]..." або повертає назву помилки
     */
    static String parseToText(String line) {
        String[] fields = new String[line.length() + 1]; // максимум полів = довжина + 1
        int count = parseCommand(line, fields);

        // Перетворюємо коди помилок у рядки
        if (count == ERROR_UNCLOSED_QUOTE) {
            return "ERROR_UNCLOSED_QUOTE";
        }
        if (count == ERROR_QUOTE_IN_PLAIN_FIELD) {
            return "ERROR_QUOTE_IN_PLAIN_FIELD";
        }
        if (count == ERROR_TEXT_AFTER_QUOTE) {
            return "ERROR_TEXT_AFTER_QUOTE";
        }

        // Формуємо результат у вигляді [a][b][c]
        StringBuilder text = new StringBuilder();
        for (int index = 0; index < count; index++) {
            text.append('[').append(fields[index]).append(']');
        }
        return text.toString();
    }

    //Консольне подання

    static void check(String input, String expected, String actual) {
        total++;
        boolean ok = expected.equals(actual);
        if (ok) {
            passed++;
        }
        System.out.println((ok ? "ОК       " : "ПОМИЛКА  ") + "вхід=" + input + " очікувано=" + expected + " фактично=" + actual);
    }

    /** Перевірка: очікуваний текст і кількість вилучених одиниць */
    static void checkNormalize(String source, String expected, int expectedRemoved) {
        if (source == null) {            // відсутній вхід обробляється явно
            System.out.println("Немає входу: null не допускається");
            return;
        }
        String normalized = normalize(source);
        int removed = removedUnits(source, normalized);
        check("\"" + source + "\"",
                "\"" + expected + "\", " + expectedRemoved,
                "\"" + normalized + "\", " + removed);
    }

    static void checkCommand(String line, String expected) {
        if (line == null) {
            System.out.println("Немає входу: null не допускається");
            return;
        }
        check(line, expected, parseToText(line));
    }

    public static void main(String[] args) {
        System.out.println("Варіант 1: normalize");
        checkNormalize("  Java   text  ", "Java text", 6);
        checkNormalize("A\tB \nC ", "A B C", 2);
        checkNormalize("   ", "", 3);
        checkNormalize("sensor ready", "sensor ready", 0);
        checkNormalize("", "", 0);
        checkNormalize("F", "F", 0);
        checkNormalize(" 5", "5", 1);
        checkNormalize("a ", "a", 1);
        checkNormalize("Y   N", "Y N", 2);
        // символ поза базовою площиною (сурогатна пара) не розривається
        checkNormalize(" H\uD83D\uDE0E    I ", "H\uD83D\uDE0E I", 5);
        checkNormalize(null, "", 0);

        System.out.println();
        System.out.println("Завдання A: parseCommand");
        checkCommand("a;b;c", "[a][b][c]");                 // три звичайні поля
        checkCommand("a;;c", "[a][][c]");                   // порожнє поле посередині
        checkCommand("\"a;b\";c", "[a;b][c]");              // ';' всередині лапок не розділяє
        checkCommand("\"a\"\"b\";c", "[a\"b][c]");          // подвоєна лапка -> одна літеральна
        checkCommand("\"abc;d", "ERROR_UNCLOSED_QUOTE");    // лапки не закрито
        checkCommand("", "[]");                             // порожній рядок = одне порожнє поле
        checkCommand("Р", "[Р]");                           // один символ
        checkCommand(";", "[][]");                          // два порожні поля
        checkCommand("a;", "[a][]");                        // порожнє поле в кінці
        checkCommand(";a", "[][a]");                        // порожнє поле на початку
        checkCommand("\"\"", "[]");                         // порожнє поле в лапках
        checkCommand("a\"b;c", "ERROR_QUOTE_IN_PLAIN_FIELD"); // лапка в звичайному полі
        checkCommand("\"a\"b;c", "ERROR_TEXT_AFTER_QUOTE");  // текст після закривної лапки
        checkCommand("\"\"\"\"", "[\"]");                    // чотири лапки = одна літеральна лапка
        checkCommand("\"a\";", "[a][]");                     // лапки + порожнє поле після ';'
        checkCommand("a;\"\";b", "[a][][b]");                // порожнє поле в лапках посередині
        checkCommand(";\"a;b\"", "[][a;b]");                 // порожнє поле + поле з ';' у лапках
        checkCommand(null, "");

        System.out.println();
        System.out.println("Пройдено " + passed + " з " + total);
    }
}