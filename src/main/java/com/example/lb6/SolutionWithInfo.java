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

    public static List<List<String>> simple2() {
        long count = 15;
        List<List<String>> steps = new ArrayList<>();

        try {
            steps.add(readFile(a));
            for (long i = 1; i < count; i *= 2) {
                simpleSortSplit(i);
                steps.add(readFile(b));
                steps.add(readFile(c));
                simpleSortCombine(i);
                steps.add(readFile(a));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return steps;
    }

    public static List<List<String>> simple1() {
        long count = 15;
        List<List<String>> steps = new ArrayList<>();
        try {
            steps.add(readFile(a));
            toTwoFiles();
            steps.add(readFile(b));
            steps.add(readFile(c));
            boolean direction = true;
            for (long i = 1; i < count / 2; i *= 2) {
                if (direction) {
                    switchFiles(i, b, c, d, e);
                    steps.add(readFile(d));
                    steps.add(readFile(e));
                    direction = false;
                } else {
                    switchFiles(i, d, e, b, c);
                    steps.add(readFile(b));
                    steps.add(readFile(c));
                    direction = true;
                }
            }
            if (direction)
                toOneFile(count, b, c);
            else
                toOneFile(count, d, e);
            steps.add(readFile(a));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return steps;
    }

    private static void simpleSortSplit(long len) throws IOException {
        PrintWriter writerToB = new PrintWriter(new FileWriter(b));
        PrintWriter writerToC = new PrintWriter(new FileWriter(c));
        InputStream readerA = new FileInputStream(a);
        long curLen = len;
        int dir = 0;
        while (readerA.available() != 0) {
            int elem = getNumber(readerA);
            if (curLen > 0) {
                curLen--;
            } else {
                dir = dir == 0 ? 1 : 0;
                curLen = len - 1;
            }
            if (dir == 0) {
                writerToB.write(elem + " ");
            } else {
                writerToC.write(elem + " ");
            }
        }
        writerToB.flush();
        writerToC.flush();
        writerToB.close();
        writerToC.close();
        readerA.close();
    }

    private static void simpleSortCombine(long len) throws IOException {
        PrintWriter writerToA = new PrintWriter(new FileWriter(a));
        InputStream readerB = new FileInputStream(b);
        InputStream readerC = new FileInputStream(c);
        int first = getNumber(readerB);
        int second = getNumber(readerC);
        while (first != -1 || second != -1) {
            long curB = len;
            long curC = len;
            while (curB != 0 && curC != 0 && first != -1 && second != -1) {
                if (first < second) {
                    writerToA.write(first + " ");
                    curB--;
                    first = getNumber(readerB);
                } else {
                    writerToA.write(second + " ");
                    curC--;
                    second = getNumber(readerC);
                }
            }
            while (curB != 0 && first != -1) {
                writerToA.write(first + " ");
                curB--;
                first = getNumber(readerB);
            }
            while (curC != 0 && second != -1) {
                writerToA.write(second + " ");
                curC--;
                second = getNumber(readerC);
            }
        }
        writerToA.flush();
        readerB.close();
        readerC.close();
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

    private static void toTwoFiles() throws IOException {
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter(b));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter(c));
        InputStream readerA = new FileInputStream(a);
        boolean direction = true;
        int elem = getNumber(readerA);
        while (elem != -1) {
            if (direction) {
                writerToFirstFile.write(elem + " ");
                direction = false;
            } else {
                writerToSecondFile.write(elem + " ");
                direction = true;
            }
            elem = getNumber(readerA);
        }
        writerToFirstFile.flush();
        writerToSecondFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.close();
    }
    private static void toOneFile(long count, String firstFileToRead, String secondFileToRead) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(a));
        InputStream readerFirst = new FileInputStream(firstFileToRead);
        InputStream readerSecond = new FileInputStream(secondFileToRead);
        int first = getNumber(readerFirst);
        int second = getNumber(readerSecond);
        while (first != -1 || second != -1) {
            long localFirst = count;
            long localSecond = count;
            while (localFirst != 0 && localSecond != 0 && first != -1 && second != -1) {
                if (first < second) {
                    writer.write(first + " ");
                    localFirst--;
                    first = getNumber(readerFirst);
                } else {
                    writer.write(second + " ");
                    localSecond--;
                    second = getNumber(readerSecond);
                }
            }
            while (localFirst != 0 && first != -1) {
                writer.write(first + " ");
                localFirst--;
                first = getNumber(readerFirst);
            }
            while (localSecond != 0 && second != -1) {
                writer.write(second + " ");
                localSecond--;
                second = getNumber(readerSecond);
            }
        }
        writer.flush();
    }
    private static void switchFiles(long count, String firstToRead, String secondToRead, String firstToWrite, String secondToWrite) throws IOException {
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter(firstToWrite));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter(secondToWrite));
        InputStream readerFirst = new FileInputStream(firstToRead);
        InputStream readerSecond = new FileInputStream(secondToRead);
        boolean direction = true;
        int first = getNumber(readerFirst);
        int second = getNumber(readerSecond);
        while (first != -1 || second != -1) {
            long localFirst = count;
            long localSecond = count;
            while (localFirst != 0 && localSecond != 0 && first != -1 && second != -1) {
                if (first < second) {
                    if (direction)
                        writerToFirstFile.write(first + " ");
                    else writerToSecondFile.write(first + " ");
                    localFirst--;
                    first = getNumber(readerFirst);
                } else {
                    if (direction)
                        writerToFirstFile.write(second + " ");
                    else writerToSecondFile.write(second + " ");
                    localSecond--;
                    second = getNumber(readerSecond);
                }
            }
            while (localFirst != 0 && first != -1) {
                if (direction)
                    writerToFirstFile.write(first + " ");
                else writerToSecondFile.write(first + " ");
                localFirst--;
                first = getNumber(readerFirst);
            }
            while (localSecond != 0 && second != -1) {
                if (direction)
                    writerToFirstFile.write(second + " ");
                else writerToSecondFile.write(second + " ");
                localSecond--;
                second = getNumber(readerSecond);
            }
            if (direction) {
                direction = false;
            } else direction = true;
        }
        writerToFirstFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.flush();
        writerToSecondFile.close();
    }
}
