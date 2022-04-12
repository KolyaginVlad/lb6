package com.example.lb6;

public class Solution {
    static long countCompare = 0L;
    static long read = 0L;
    static long write = 0L;

    static long[] absorbSort(String fileName,long count, int n, int max) {
        countCompare = 0;
        read = 0;
        write = 0;
        //Сохраняем время
        long time = System.currentTimeMillis();
        //Создаём класс-обёртку для взаимодействия с файлом "a"
        FileHelper fileHelper = new FileHelper(fileName, max);
        //Создаём буффер размера n
        int[] buffer = new int[n];
        //Заполняем буффер числами с конца файла
        for (int i = 0; i < n; i++) {
            buffer[i] = fileHelper.readNumber(count - 1 - i);
            read++;
        }
        //Сортируем буффер
        bubbleSort(buffer);
        //Проверяем не больше ли размер буффера чем размер файла
        if (n > count) {
            //Если размер буффера больше, то поскольку он уже отсортирован, то записываем его в файл
            //fileHelper.readNumber возвращет очень большое число, которое точно больше всех остальных чисел, если файл закончился и читать нечего
            //Поэтому при сортировке эти несуществующие числа будут в конце буффера.
            //Записываем количество чисел, которое изначально было в файле
            for (int i = 0; i < count; i++) {
                fileHelper.writeNumber(i, buffer[i]);
                write++;
            }
            //Освобождаем ресурсы
            fileHelper.close();
            //Возвращаем время т.к. сортировка окончена
            return new long[]{countCompare, read, write, System.currentTimeMillis() - time};
        }
        //Если буффер всё же меньше размера файла, то записываем его на прежнее место, только в отсортированном виде
        for (int i = 0; i < n; i++) {
            fileHelper.writeNumber(count - n + i, buffer[i]);
            write++;
        }
        //Количество прошедших повторений записи в буфер и сортировки. Нужно для определения положения чтения и записи
        int x = 1;
        //Цикл пока количество итераций меньше либо равно числу итераций, которое должно пройти. -1 нужен для учёта того, что после цикла придётся сортировать элементы, которые не занимают полностью буффер
        while (x <= count / n - 1) {
            //Заполняем буффер
            for (int i = 0; i < n; i++) {
                /*Как получили число в скобках:
                Число элементов count
                Отчёт элементов с 0, поэтому последний индекс числа в файле count -1
                n - размер буфера, x - количество итераций. При перемножении дают количество чисел, которые уже были отсортированы
                i - вычитание индекса в буффере
                 */
                buffer[i] = fileHelper.readNumber(count - 1 - (long) n * x - i);
                read++;
            }
            //Сортируем буффер
            bubbleSort(buffer);
            //Переменные для определения сколько чисел уже было записано из буффера и из файла
            int kNums = 0;
            int kFiles = 0;
            //Повторяем цикл столько раз, сколько отсортированных элементов у нас уже есть + размер буффера
            for (int i = 0; i < n + n * x; i++) {
                //Если элемент буффера меньше элемента файла, то пишем из буффера,  иначе из файла
                read++;
                if (buffer[kNums] <= fileHelper.readNumber(count - (long) n * x + kFiles)) {
                    fileHelper.writeNumber(count - (long) n * (x + 1) + i, buffer[kNums]);
                    write++;
                    //Прибавляем единицу к счётчику чисел, которые добавили из буффера
                    kNums++;
                    //Если все числа из буффера добавлены в файл, то значит, что остальная часть отсортирована и можно завершать цикл
                    if (kNums == n) break;
                } else {
                    /*Как получили число в скобках:
                    Число элементов count
                    -1 не нужен т.к. положение числа настраивается с помощью i
                    n - размер буфера, x+1 - количество итераций. При перемножении дают количество чисел, которые уже были отсортированы
                    i - прибавление индекса
                    */
                    write++;
                    fileHelper.writeNumber(count - (long) n * (x + 1) + i, fileHelper.readNumber(count - (long) n * x + kFiles));
                    //Прибавляем единицу к переменной счётчика цифр, которые добавлены из файла
                    kFiles++;
                }
            }
            //Прибавляем итерацию после её завершения
            x++;
        }
        //Количество элементов, которые останутся после сортировки, но не смогут полностью заполнить буффер. 10 по 3, останется 1 элемент
        int lost = (int) (count % n);
        if (lost != 0) {
            //Заполняем буффер этими элементами
            for (int i = 0; i < lost; i++) {
                buffer[i] = fileHelper.readNumber(i);
                read++;
            }
            //Заполняем буффер "большими числами", чтобы при сортировке они ушли в конец и не мешались
            for (int i = lost; i < n; i++) {
                buffer[i] = Integer.MAX_VALUE;
            }
            //Сортируем
            bubbleSort(buffer);
            //Переменные для определения сколько чисел уже было записано из буффера и из файла
            int kNums = 0;
            int kFiles = 0;
            //Проходим по всему файлу
            for (int i = 0; i < count - 1; i++) {
                //Сравниваем числа из буффера с числами из файла и записываем
                read++;
                if (buffer[kNums] <= fileHelper.readNumber(lost + kFiles)) {
                    fileHelper.writeNumber(i, buffer[kNums]);
                    kNums++;
                    if (kNums == lost) break;
                } else {
                    fileHelper.writeNumber(i, fileHelper.readNumber(lost + kFiles));
                    kFiles++;
                }
                write++;
            }
        }
        //Освобождаем ресурсы
        fileHelper.close();
        return new long[]{countCompare, read, write, System.currentTimeMillis() - time};
    }

    public static void bubbleSort(int[] arr) {
        int last = arr.length - 1;

        while (last > 0) {
            countCompare++;
            int barrier = 0;
            for (int i = 0; i < last; i++) {
                countCompare++;
                if (arr[i] > arr[i + 1]) {
                    countCompare++;
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                    barrier = i;
                }
            }
            last = barrier;
        }
    }
}
