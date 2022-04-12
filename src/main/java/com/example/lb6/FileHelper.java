package com.example.lb6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileHelper {
    private RandomAccessFile raf;
    private int size;
    //Конструктор, который принимает имя файла
    public FileHelper(String nameFile, int maxNumber) {
        try {
            size = (maxNumber+"").length();
            raf = new RandomAccessFile(new File(nameFile), "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //Метод для чтения числа для метода поглощения. Принимает позицию числа в файле
    public int readNumber(long pos) {
        try {
            //если позиция меньше нуля или превышает число чисел в файле, то возвращаем огромное число
            if (pos < 0 || raf.length() - 1 < pos * size) {
                return Integer.MAX_VALUE;
            }
            //Перемещаем курсор к позиции
            raf.seek(pos * size);
            //Создаём продвинутый объект для хранения строк
            StringBuilder buffer = new StringBuilder();
            //переменная для очередного символа
            int ch;
            //Читаем символ и записываем в переменную, после чего сразу проверяем его на то пробел ли это
            while ((ch = raf.read()) != 32) {
                //Если не пробел, то добавляем символ к строке
                buffer.append((char) ch);
            }
            //Если пробел, то приводим к числу и возвращаем
            return Integer.parseInt(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Если случилось что-то с файлом во время выполнения, то ломаем алгоритм и возвращаем -1
        return -1;
    }
    //Метод для записи числа в файл для метода поглощения. Принимает позицию и само число
    public void writeNumber(long pos, int num){
        try {
            //если позиция меньше нуля или превышает число чисел в файле, то прерываем запись. В целом при полностью правильном алгоритме никогда не сработает, но если что-то пойдёт не так в алгоритме, то если это убрать, то программа сломается
            if (pos < 0 || raf.length() - 1 < pos * size) {
                return;
            }
            //Перемещаемся на позицию
            raf.seek(pos * size);
            //Записываем, сохраняя формат числа
            String s = String.format("%0"+(size-1)+"d", num) +" ";
            raf.writeBytes(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Метод для освобождения ресурсов
    public void close() {
        try {
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
