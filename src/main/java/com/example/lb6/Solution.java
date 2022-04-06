package com.example.lb6;

import java.io.*;

public class Solution {
    private final static String a = "a1.txt";
    private final static String b = "b1.txt";
    private final static String c = "c1.txt";
    private final static String d = "d1.txt";
    private final static String e = "e1.txt";
    static Long countCompare = 0L;
    static Long read = 0L;
    static Long write = 0L;

    public static void bubbleSort(int[] numArray) {
        int n = numArray.length;
        int temp = 0;
    
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
    
                if (numArray[j - 1] > numArray[j]) {
                    temp = numArray[j - 1];
                    numArray[j - 1] = numArray[j];
                    numArray[j] = temp;
                    countCompare++;
                }
            }
        }
    }

    public static long[] natural1(String fileName, int sizeBuffer) {
        long time = System.currentTimeMillis();
        countCompare = 0L;
        read = 0L;
        write = 0L;
        try {
            splitAndSort(fileName,sizeBuffer);
            boolean direction = true;
            boolean flag = true;
            while (flag) {
                if (direction) {
                    flag = switchFilesNatural(b, c, d, e);
                    direction = false;
                } else {
                    flag = switchFilesNatural(d, e, b, c);
                    direction = true;
                }
            }
            if (direction)
                toOneFileNatural(fileName,b, c);
            else
                toOneFileNatural(fileName,d, e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new long[]{countCompare, read, write, System.currentTimeMillis() - time};
    }

    private static boolean switchFilesNatural(String firstToRead, String secondToRead, String firstToWrite, String secondToWrite) throws IOException {
        PrintWriter writerToFirst = new PrintWriter(new FileWriter(firstToWrite));
        PrintWriter writerToSecond = new PrintWriter(new FileWriter(secondToWrite));
        InputStream readerFromFirst = new FileInputStream(firstToRead);
        InputStream readerFromSecond = new FileInputStream(secondToRead);
        boolean direction = true;
        int counter = 0;
        int first = getNumber(readerFromFirst);
        int second = getNumber(readerFromSecond);
        int lastFirst = -1;
        int lastSecond = -1;
        read+=2;
        while (first != -1 || second != -1) {
            while (first != -1 && second != -1 && first >= lastFirst && second >= lastSecond) {
                countCompare++;
                if (first < second) {
                    if (direction) {
                        writerToFirst.write(first + " ");
                    } else {
                        writerToSecond.write(first + " ");
                    }
                    lastFirst = first;
                    first = getNumber(readerFromFirst);
                } else {
                    if (direction) {
                        writerToFirst.write(second + " ");
                    } else {
                        writerToSecond.write(second + " ");
                    }
                    lastSecond = second;
                    second = getNumber(readerFromSecond);
                }
                read++;
                write++;
            }
            while (first != -1 && first >= lastFirst) {
                if (direction)
                    writerToFirst.write(first + " ");
                else writerToSecond.write(first + " ");
                lastFirst = first;
                first = getNumber(readerFromFirst);
                read++;
                write++;
            }
            while (second != -1 && second >= lastSecond) {
                if (direction)
                    writerToFirst.write(second + " ");
                else writerToSecond.write(second + " ");
                lastSecond = second;
                second = getNumber(readerFromSecond);
                read++;
                write++;
            }
            lastFirst = -1;
            lastSecond = -1;
            read++;
            direction = !direction;
            counter++;
        }
        writerToFirst.flush();
        writerToSecond.flush();
        writerToFirst.close();
        writerToSecond.close();
        return counter > 2;
    }

    private static void toOneFileNatural(String fileName, String firstFile, String secondFile) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(fileName));
        InputStream readerFromFirstFile = new FileInputStream(firstFile);
        InputStream readerFromSecondFile = new FileInputStream(secondFile);
        int first = getNumber(readerFromFirstFile);
        int second = getNumber(readerFromSecondFile);
        int lastFirst = -1;
        int lastSecond = -1;
        read+=2;
        while (first != -1 || second != -1) {
            while (first != -1 && second != -1 && first >= lastFirst && second >= lastSecond) {
                countCompare++;
                if (first < second) {
                    writer.write(first + " ");
                    lastFirst = first;
                    first = getNumber(readerFromFirstFile);
                } else {
                    writer.write(second + " ");
                    lastSecond = second;
                    second = getNumber(readerFromSecondFile);
                }
                read++;
                write++;
            }
            while (first != -1 && first >= lastFirst) {
                writer.write(first + " ");
                lastFirst = first;
                first = getNumber(readerFromFirstFile);
                read++;
                write++;
            }
            while (second != -1 && second >= lastSecond) {
                writer.write(second + " ");
                lastSecond = second;
                second = getNumber(readerFromSecondFile);
                read++;
                write++;
            }
            lastSecond = -1;
            lastFirst = -1;
            read+=2;
        }
        writer.flush();
        writer.close();
    }

    private static void splitAndSort(String fileName, int sizeBuffer) throws IOException {
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter(b));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter(c));
        InputStream reader = new FileInputStream(fileName);

        int num = getNumber(reader);
        read++;

        int[] buffer = new int[sizeBuffer];
        boolean direction = true;
        while (num != -1) {
            int i = 0;
            for(; i < sizeBuffer && num != -1; i++) {
                buffer[i] = num;
                num = getNumber(reader);
                read++;
            }
            bubbleSort(buffer);

            for(int index = 0; index < i; index++) {
                if (direction) {
                    writerToFirstFile.write(buffer[index] + " ");
                } else {
                    writerToSecondFile.write(buffer[index] + " ");
                }
                write++;
            }
        }

        writerToFirstFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.flush();
        writerToSecondFile.close();
    }

    private static int getNumber(InputStream reader) throws IOException {
        int character;
        StringBuilder currentElem = new StringBuilder();
        while (reader.available() != 0) {
            character = reader.read();
            if (character != 32) {
                currentElem.append((char) character);
            } else {
                return Integer.parseInt(currentElem.toString());
            }
        }
        return -1;
    }
}
