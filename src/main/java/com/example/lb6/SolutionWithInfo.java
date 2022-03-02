package com.example.lb6;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SolutionWithInfo {
    private final static String a = "a2.txt";
    private final static String b = "b2.txt";
    private final static String c = "c2.txt";
    private final static String d = "d2.txt";
    private final static String e = "e2.txt";
    public static List<List<String>> natural2() {

        List<List<String>> steps = new ArrayList<>();

        try {
            do {
                steps.add(readFile(a));
                naturalSortSplit();
                steps.add(readFile(b));
                steps.add(readFile(c));
            } while (naturalSortCombine());
            steps.add(readFile(a));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return steps;
    }

    public static List<List<String>> natural1() {

        List<List<String>> steps = new ArrayList<>();
        try {
            steps.add(readFile(a));
            toTwoFilesNatural();
            steps.add(readFile(b));
            steps.add(readFile(c));
            boolean direction = true;
            boolean flag = true;
            while (flag) {
                if (direction) {
                    flag = switchFilesNatural(b, c, d, e);
                    steps.add(readFile(d));
                    steps.add(readFile(e));
                    direction = false;
                } else {
                    flag = switchFilesNatural(d, e, b, c);
                    steps.add(readFile(b));
                    steps.add(readFile(c));
                    direction = true;
                }
            }
            if (direction) {
                toOneFileNatural(b, c);
            }
            else {
                toOneFileNatural(d, e);
            }
            steps.add(readFile(a));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return steps;
    }

    private static void naturalSortSplit() throws IOException {
        PrintWriter writerToB = new PrintWriter(new FileWriter(b));
        PrintWriter writerToC = new PrintWriter(new FileWriter(c));
        InputStream readerA = new FileInputStream(a);
        int num = getNumber(readerA);
        boolean dir = true;
        int last = -1;
        while (num != -1) {
            if (num < last) {
                if (dir) {
                    dir = false;
                    writerToC.write(num + " ");
                } else {
                    dir = true;
                    writerToB.write(num + " ");
                }
            } else {
                if (dir) {
                    writerToB.write(num + " ");
                } else {
                    writerToC.write(num + " ");
                }
            }
            last = num;
            num = getNumber(readerA);
        }
        writerToB.flush();
        writerToB.close();
        writerToC.flush();
        writerToC.close();
        readerA.close();
    }

    private static boolean naturalSortCombine() throws IOException {
        PrintWriter writerToA = new PrintWriter(new FileWriter(a));
        InputStream readerB = new FileInputStream(b);
        InputStream readerC = new FileInputStream(c);
        int first = getNumber(readerB);
        int second = getNumber(readerC);
        int lastFirst = -1;
        int lastSecond = -1;
        int counter = 0;
        while (first != -1 || second != -1) {
            while (first != -1 && second != -1 && first >= lastFirst && second >= lastSecond) {
                if (first < second) {
                    writerToA.write(first + " ");
                    lastFirst = first;
                    first = getNumber(readerB);
                } else {
                    writerToA.write(second + " ");
                    lastSecond = second;
                    second = getNumber(readerC);
                }
            }
            counter++;
            while (first != -1 && first >= lastFirst) {
                writerToA.write(first + " ");
                lastFirst = first;
                first = getNumber(readerB);
            }
            while (second != -1 && second >= lastSecond) {
                writerToA.write(second + " ");
                lastSecond = second;
                second = getNumber(readerC);
            }
            lastFirst = -1;
            lastSecond = -1;
        }
        writerToA.flush();
        writerToA.close();
        readerB.close();
        readerC.close();
        return counter > 1;
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
        while (first != -1 || second != -1) {
            while (first != -1 && second != -1 && first >= lastFirst && second >= lastSecond) {
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
            }
            while (first != -1 && first >= lastFirst) {
                if (direction)
                    writerToFirst.write(first + " ");
                else writerToSecond.write(first + " ");
                lastFirst = first;
                first = getNumber(readerFromFirst);
            }
            while (second != -1 && second >= lastSecond) {
                if (direction)
                    writerToFirst.write(second + " ");
                else writerToSecond.write(second + " ");
                lastSecond = second;
                second = getNumber(readerFromSecond);
            }
            lastFirst = -1;
            lastSecond = -1;
            direction = !direction;
            counter++;
        }
        writerToFirst.flush();
        writerToSecond.flush();
        writerToFirst.close();
        writerToSecond.close();
        return counter > 2;
    }

    private static void toOneFileNatural(String firstFile, String secondFile) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(a));
        InputStream readerFromFirstFile = new FileInputStream(firstFile);
        InputStream readerFromSecondFile = new FileInputStream(secondFile);
        int first = getNumber(readerFromFirstFile);
        int second = getNumber(readerFromSecondFile);
        int lastFirst = -1;
        int lastSecond = -1;
        while (first != -1 || second != -1) {
            while (first != -1 && second != -1 && first >= lastFirst && second >= lastSecond) {
                if (first < second) {
                    writer.write(first + " ");
                    lastFirst = first;
                    first = getNumber(readerFromFirstFile);
                } else {
                    writer.write(second + " ");
                    lastSecond = second;
                    second = getNumber(readerFromSecondFile);
                }
            }
            while (first != -1 && first >= lastFirst) {
                writer.write(first + " ");
                lastFirst = first;
                first = getNumber(readerFromFirstFile);
            }
            while (second != -1 && second >= lastSecond) {
                writer.write(second + " ");
                lastSecond = second;
                second = getNumber(readerFromSecondFile);
            }
            lastFirst = -1;
            lastSecond = -1;
        }
        writer.flush();
        writer.close();
    }

    private static void toTwoFilesNatural() throws IOException {
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter(b));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter(c));
        InputStream reader = new FileInputStream(a);
        int num = getNumber(reader);
        boolean direction = true;
        int last = -1;
        while (num != -1) {
            if (num < last) {
                if (direction) {
                    direction = false;
                    writerToSecondFile.write(num + " ");
                } else {
                    direction = true;
                    writerToFirstFile.write(num + " ");
                }
            } else {
                if (direction) {
                    writerToFirstFile.write(num + " ");
                } else {
                    writerToSecondFile.write(num + " ");
                }
            }
            last = num;
            num = getNumber(reader);
        }
        writerToFirstFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.flush();
        writerToSecondFile.close();
    }

    private static List<String> readFile(String file) throws IOException {
        List<String> list = new ArrayList<>();
        switch(file){
            case a:
                list.add("a");
                break;
            case b:
                list.add("b");
                break;
            case c:
                list.add("c");
                break;
            case d:
                list.add("d");
                break;
            case e:
                list.add("e");
                break;
        }
        InputStream reader = new FileInputStream(file);
        int num;
        while ((num = getNumber(reader))!= -1){
            list.add(num+"");
        }
        while (list.size()!=16){
            list.add("");
        }
        return list;
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
