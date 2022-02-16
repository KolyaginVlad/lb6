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

    public static long[] simple2(long count) {
        long time = System.currentTimeMillis();
        countCompare = 0L;
        read = 0L;
        write = 0L;
        try {
            for (long i = 1; i < count; i *= 2) {
                simpleSortSplit(i);
                simpleSortCombine(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new long[]{countCompare, read, write, System.currentTimeMillis() - time};
    }

    public static long[] simple1(long count) {
        long time = System.currentTimeMillis();
        countCompare = 0L;
        read = 0L;
        write = 0L;
        try {
            toTwoFiles();
            boolean direction = true;
            for (long i = 1; i < count / 2; i *= 2) {
                if (direction) {
                    switchFiles(i, b, c, d, e);
                    direction = false;
                } else {
                    switchFiles(i, d, e, b, c);
                    direction = true;
                }
            }
            if (direction)
                toOneFile(count, b, c);
            else
                toOneFile(count, d, e);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return new long[]{countCompare, read, write, System.currentTimeMillis() - time};
    }

    private static void simpleSortSplit(long len ) throws IOException {
        PrintWriter writerToB = new PrintWriter(new FileWriter("b.txt"));
        PrintWriter writerToC = new PrintWriter(new FileWriter("c.txt"));
        InputStream readerA = new FileInputStream("a.txt");
        long curLen = len;
        int dir = 0;
        while (readerA.available() != 0) {
            int elem = getNumber(readerA);
            read++;
            if (curLen > 0) {
                curLen--;
            } else {
                dir = dir == 0 ? 1 : 0;
                curLen = len - 1;
            }
            write++;
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

    private static void simpleSortCombine(long len ) throws IOException {
        PrintWriter writerToA = new PrintWriter(new FileWriter("a.txt"));
        InputStream readerB = new FileInputStream("b.txt");
        InputStream readerC = new FileInputStream("c.txt");
        int first = getNumber(readerB);
        int second = getNumber(readerC);
        while (first != -1 || second != -1) {
            long curB = len;
            long curC = len;
            while (curB != 0 && curC != 0 && first != -1 && second != -1) {
                countCompare++;
                write++;
                read++;
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
                write++;
                read++;
                writerToA.write(first + " ");
                curB--;
                first = getNumber(readerB);
            }
            while (curC != 0 && second != -1) {
                write++;
                read++;
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

    private static void toTwoFiles( ) throws IOException {
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter(b));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter(c));
        InputStream readerA = new FileInputStream(a);
        boolean direction = true;
        int elem = getNumber(readerA);
        while (elem != -1) {
            read++;
            write++;
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
    private static void toOneFile(long count, String firstFileToRead, String secondFileToRead ) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(a));
        InputStream readerFirst = new FileInputStream(firstFileToRead);
        InputStream readerSecond = new FileInputStream(secondFileToRead);
        int first = getNumber(readerFirst);
        int second = getNumber(readerSecond);
        while (first != -1 || second != -1) {
            long localFirst = count;
            long localSecond = count;
            while (localFirst != 0 && localSecond != 0 && first != -1 && second != -1) {
                countCompare++;
                read++;
                write++;
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
                read++;
                write++;
                writer.write(first + " ");
                localFirst--;
                first = getNumber(readerFirst);
            }
            while (localSecond != 0 && second != -1) {
                read++;
                write++;
                writer.write(second + " ");
                localSecond--;
                second = getNumber(readerSecond);
            }
        }
        writer.flush();
    }
    private static void switchFiles(long count, String firstToRead, String secondToRead, String firstToWrite, String secondToWrite ) throws IOException {
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
                countCompare++;
                write++;
                read++;
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
                write++;
                read++;
                if (direction)
                    writerToFirstFile.write(first + " ");
                else writerToSecondFile.write(first + " ");
                localFirst--;
                first = getNumber(readerFirst);
            }
            while (localSecond != 0 && second != -1) {
                write++;
                read++;
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
