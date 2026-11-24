# Ракша Вадим КН-925А

## Збірка та запуск

```bash
mvn compile
java -cp target/classes ua.khpi.oop1.lab12.Main
```

## Генерація Javadoc

```bash
javadoc -encoding UTF-8 -package -sourcepath src/main/java -d docs/api ua.khpi.oop1.lab12
```


Результат з'являється в `docs/api/`; відкрийте `docs/api/index.html` у браузері. Усі класи пакета передаються разом: інакше `javadoc` не знайде залежних класів (помилка `cannot find symbol` для `Device`). Каталог `docs/api/` згенерований, його додано до `.gitignore` і до Git не потрапляє.

## Звіти з лабораторних робіт

1. [Звіт лабораторної роботи №1](docs/lab01/REPORT.md)
2. [Звіт лабораторної роботи №2](docs/lab02/REPORT.md)
3. [Звіт лабораторної роботи №3](docs/lab03/REPORT.md)
4. [Звіт лабораторної роботи №4](docs/lab04/REPORT.md)
5. [Звіт лабораторної роботи №5](docs/lab05/REPORT.md)
6. [Звіт лабораторної роботи №6](docs/lab06/REPORT.md)
7. [Звіт лабораторної роботи №7](docs/lab07/REPORT.md)
8. [Звіт лабораторної роботи №8](docs/lab08/REPORT.md)
9. [Звіт лабораторної роботи №9](docs/lab09/REPORT.md)
10. [Звіт лабораторної роботи №10](docs/lab10/REPORT.md)
11. [Звіт лабораторної роботи №11](docs/lab11/REPORT.md)
12. [Звіт лабораторної роботи №12](docs/lab12/REPORT.md)