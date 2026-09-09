# Звіт з лабораторної роботи №1

Об'єктно-орієнтоване програмування (Java) Частина 1
**Тема:** Робоче середовище, Git і GitHub, компіляція та перший запуск Java-програми

**Виконав:** Вадим Ракша
**Група:** КН-925А
---
## 1. Мета роботи та фактично використані інструменти
Метою роботи є опанування відтворюваного циклу розроблення Java-програми: налаштування середовища Java SE 21, ручна компіляція і запуск, зіставлення командного рядка з IntelliJ IDEA, а також публікація результату в окремій гілці навчального репозиторію GitHub через Pull Request.

Використані інструменти:
- Операційна система: Windows 11
- Термінал: Windows PowerShell
- JDK 21 
- IntelliJ IDEA (Community 2026.2.2)
- Git

---

## 2. Конфігурація середовища

| Компонент | Значення |
|---|---|
| Операційна система | Windows 11 |
| Постачальник JDK | Microsoft |
| Версія JDK | 21.0.12 |
| Версія Git | 2.53.0.windows.1|
| Версія IntelliJ IDEA | 2026.2.2 |

---

## 3. Репозиторій GitHub, гілка та Pull Request

- URL навчального репозиторію: `https://github.com/Nevalik/raksha-khpi-oop-java-1`
- Назва гілки лабораторної роботи: `lab01`
- URL Pull Request до `main`: https://github.com/Nevalik/raksha-khpi-oop-java-1/pull/1

---

## 4. Дерево каталогів

```
raksha-khpi-oop-java-1/
├── .gitignore
├── README.md
├── evidence/
└── src/
    └── ua/khpi/oop1/lab01/
        ├── HelloJava.java
        └── EnvironmentReport.java
```

Пакет `ua.khpi.oop1.lab01` відповідає шляху `src/ua/khpi/oop1/lab01/` — кожен компонент відображається окремим вкладеним каталогом.

---

## 5. Вихідний код

### HelloJava.java

```java
package ua.khpi.oop1.lab01;

public class HelloJava {
    public static void main(String[] args) {
        System.out.println("Hello, Java!");
    }
}
```

### EnvironmentReport.java

```java
package ua.khpi.oop1.lab01;

public class EnvironmentReport {
    public static void main(String[] args) {
        String javaVersion = System.getProperty("java.version");
        String javaVendor = System.getProperty("java.vendor");
        String operatingSystem = System.getProperty("os.name");
        String currentUser = System.getProperty("user.name");
        String workingDirectory = System.getProperty("user.dir");

        System.out.println("Java version: " + javaVersion);
        System.out.println("Java vendor: " + javaVendor);
        System.out.println("Operating system: " + operatingSystem);
        System.out.println("User: " + currentUser);
        System.out.println("Working directory: " + workingDirectory);
    }
}
```

---

## 6. Команди ручної компіляції, запуску та javap

```powershell
New-Item -ItemType Directory -Force out
javac -d out src\ua\khpi\oop1\lab01\HelloJava.java
java -cp out ua.khpi.oop1.lab01.HelloJava
javap -classpath out -c ua.khpi.oop1.lab01.HelloJava
```

**Результат запуску javac -d out src\ua\khpi\oop1\lab01\HelloJava.java:**
```
Якщо команда виконана без помилок то компiлятор не виводить повiдомлення при успiху
```


**Результат запуску HelloJava:**
```
Hello, Java!
```

**Помічені елементи байткоду (javap -c):**
1. `getstatic` — отримання статичного поля `System.out` (об'єкт `PrintStream`)
2. `ldc` — завантаження рядкової константи `"Hello, Java!"` з пулу констант класу
3. `invokevirtual` — виклик віртуального методу `println`

---

## 7. Протокол навмисної помилки

| Параметр | Значення |
|---|---|
| Файл | `HelloJava.java` |
| Змінений рядок | `System.out.println("Hello, Java!")` (прибрано `;`) |
| Повідомлення компілятора | *error: ';' expectedSystem.out.println("Hello, Java!")* |
| Діагноз | Компілятор очікував крапку з комою в кінці оператора, але її не було |
| Виправлення | Крапку з комою повернуто; повторна компіляція пройшла без помилок |

---

## 8. Основна програма EnvironmentReport

Команди:
```powershell
javac -d out src\ua\khpi\oop1\lab01\EnvironmentReport.java
java -cp out ua.khpi.oop1.lab01.EnvironmentReport
```

**Фактичний результат:**
```
Java version: 21.0.12
Java vendor: Microsoft
Operating system: Windows 11
User: vadim
Working directory: C:\Users\vadim\IdeaProjects\raksha-khpi-oop-java-1
```

---

## 9. Git-докази

```powershell
git branch
git status
git diff
git add src README.md .gitignore
git diff --cached
git commit -m "Add environment report and CLI instructions"
git log --oneline --decorate --graph --all
git push -u origin lab01
```

**Активна гілка:**
*lab01
main

**Статус (чисте робоче дерево):**
```
On branch lab01
Your branch is up to date with 'origin/lab01'.
nothing to commit, working tree clean
```

**Історія комітів:**
```
eddd6d9 (HEAD -> lab01, origin/lab01, origin/HEAD) Add initial README content for lab work
1fd7f2d Add laboratory report for Java programming
0e0e709 Remove misplaced doc.md from package directory
2aa1280 Added md documentation
a897f5c Add environment report and CLI instructions
a162434 (origin/main, main) Initial empty commit
```

**Перевірка ігнорування:**
```powershell
git check-ignore -v out/ua/khpi/oop1/lab01/HelloJava.class
```
Результат: `.gitignore:1:out/    out/ua/khpi/oop1/lab01/HelloJava.class`

**Публікація гілки:** гілка `lab01` опублікована на GitHub, синхронізована з `origin/lab01`.




Перевірка ігнорування:
```powershell
git check-ignore -v out/ua/khpi/oop1/lab01/HelloJava.class
```



---

## 10. Зіставлення CLI та IntelliJ IDEA

| Крок | У терміналі | В IntelliJ IDEA |
|---|---|---|
| Вибір JDK | JDK 21.0.12 (через PATH) | Project SDK: JDK 21.0.12 |
| Джерела | Явний шлях після `javac` | Sources Root — `src/` |
| Результати | `out/` (параметр `-d out`) | `out\production\raksha-khpi-oop-java-1\` (власний каталог складання IDE) |
| Classpath | `-cp out` | Формується автоматично конфігурацією запуску |
| Main class | Повне ім'я після `java` | `ua.khpi.oop1.lab01.HelloJava` (поле Main class) |
| Робочий каталог | Каталог поточного shell | Поле Working directory |

**Фактичний вивід запуску HelloJava через IntelliJ IDEA:**
```
Hello, Java!
Process finished with exit code 0
```

Результат виконання (`Hello, Java!`) повністю збігається з CLI. Версія JDK також однакова (21.0.12). Каталог складання відрізняється: CLI використовує `out/`, тоді як IDE автоматично створює власний каталог `out\production\<назва проєкту>\` 

**Виявлена відмінність:** постачальник JDK відрізняється — CLI використовує 
Microsoft Build of OpenJDK (через PATH), тоді як IntelliJ IDEA використовує 
Oracle JDK (Project SDK налаштовано на інший встановлений дистрибутив). 



## 11. Протокол налагодження (Debug)

| Крок | Дія | Результат |
|---|---|---|
| 1 | Встановлено breakpoint на першому рядку `System.out.println` (рядок з `javaVersion`) | Точка зупину активна |
| 2 | Запущено `EnvironmentReport.main` командою Debug | Програма зупинилась на breakpoint, `args = []` |
| 3 | Виконано три кроки Step Over | Змінні `javaVersion`, `javaVendor`, `operatingSystem` отримали значення: `"21.0.12"`, `"Oracle Corporation"`, `"Windows 11"` |
| 4 | Переглянуто вікно Threads & Variables | Значення змінних видимі та коректні |
| 5 | Виконано Resume Program | Процес завершився з кодом 0 |

---

## 12. Таблиця протоколу перевірки

| Перевірка | Очікується | Фактичний доказ | Висновок |
|---|---|---|---|
| `javac -version` | основна версія 21 | 21.0.12 | пройдено |
| Ручна компіляція | два class-файли в пакетному шляху | `out/ua/khpi/oop1/lab01/*.class` | пройдено |
| Ручний запуск | обидва класи запускаються | вивід наведено вище | пройдено |
| Помилковий код | діагностика з рядком і позицією | п. 7 звіту | пройдено |
| Запуск в IDE | той самий результат | п. 10 звіту | пройдено |
| Налагодження | зупинка на breakpoint, перегляд змінних | п. 11 звіту | пройдено |
| Git ignore | артефакти не відстежуються | `git status`, `git check-ignore` | пройдено |
| Історія | main і lab01, змістовні коміти | `git log` | пройдено |
| GitHub | гілка опублікована, Pull Request створено | URL PR (розділ 3) | https://github.com/Nevalik/raksha-khpi-oop-java-1/pull/1|

---

## 13. Висновок

У ході виконання роботи опановано відтворюваний цикл розроблення Java-програми: встановлено та перевірено JDK 21, вручну скомпільовано та запущено дві програми (`HelloJava`, `EnvironmentReport`) через `javac`/`java`, досліджено байткод через `javap`, відтворено та проаналізовано синтаксичну помилку компілятора. Проєкт відкрито в IntelliJ IDEA з підключеним JDK 21, виконано сеанс налагодження з переглядом локальних змінних. Результати роботи опубліковано в окремій гілці `lab01` навчального репозиторію GitHub, створено Pull Request до `main`.

Шлях `.java → .class → JVM`: вихідний код (`.java`) компілюється компілятором `javac` у платформонезалежний байткод (`.class`), який завантажується та виконується JVM (`java`) — саме ця модель відрізняє Java від традиційної C/C++-збірки в нативний виконуваний файл.
