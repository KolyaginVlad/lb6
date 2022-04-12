package com.example.lb6;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SolutionWithInfo {
    private final static String a = "a2.txt";

    public static List<List<String>> absorb() {
        int count = 15; int n = 3; int max = 100;
        List<List<String>> steps = new ArrayList<>();
        try {
            steps.add(readFile(a));

            FileHelper fileHelper = new FileHelper(a, max);
            int[] buffer = new int[n];
            for (int i = 0; i < n; i++) {
                buffer[i] = fileHelper.readNumber(count - 1 - i);
            }
            bubbleSort(buffer);
            steps.add(arrayToStringList(buffer));
            if (n > count) {
                for (int i = 0; i < count; i++) {
                    fileHelper.writeNumber(i, buffer[i]);
                }
                fileHelper.close();
                steps.add(readFile(a));
                return steps;
            }
            for (int i = 0; i < n; i++) {
                fileHelper.writeNumber(count - n + i, buffer[i]);
            }
            steps.add(readFile(a));
            int x = 1;
            while (x <= count / n - 1) {
                for (int i = 0; i < n; i++) {
                    buffer[i] = fileHelper.readNumber(count - 1 - (long) n * x - i);
                }
                bubbleSort(buffer);
                steps.add(arrayToStringList(buffer));
                int kNums = 0;
                int kFiles = 0;
                for (int i = 0; i < n + n * x; i++) {
                    if (buffer[kNums] <= fileHelper.readNumber(count - (long) n * x + kFiles)) {
                        fileHelper.writeNumber(count - (long) n * (x + 1) + i, buffer[kNums]);
                        kNums++;
                        if (kNums == n) break;
                    } else {
                        fileHelper.writeNumber(count - (long) n * (x + 1) + i, fileHelper.readNumber(count - (long) n * x + kFiles));
                        kFiles++;
                    }
                }
                steps.add(readFile(a));
                x++;
            }
            int lost = count % n;
            if (lost != 0) {
                for (int i = 0; i < lost; i++) {
                    buffer[i] = fileHelper.readNumber(i);
                }
                for (int i = lost; i < n; i++) {
                    buffer[i] = Integer.MAX_VALUE;
                }
                bubbleSort(buffer);
                steps.add(arrayToStringList(buffer));
                int kNums = 0;
                int kFiles = 0;
                for (int i = 0; i < count - 1; i++) {
                    if (buffer[kNums] <= fileHelper.readNumber(lost + kFiles)) {
                        fileHelper.writeNumber(i, buffer[kNums]);
                        kNums++;
                        if (kNums == lost) break;
                    } else {
                        fileHelper.writeNumber(i, fileHelper.readNumber(lost + kFiles));
                        kFiles++;
                    }
                }
                steps.add(readFile(a));
            }
            fileHelper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return steps;
    }

    public static void bubbleSort(int[] arr) {
        int last = arr.length - 1;

        while (last > 0) {
            int barrier = 0;
            for (int i = 0; i < last; i++) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                    barrier = i;
                }
            }
            last = barrier;
        }
    }

    private static List<String> readFile(String file) throws IOException {
        List<String> list = new ArrayList<>();
        if (a.equals(file)) {
            list.add("a");
        }
        InputStream reader = new FileInputStream(file);
        int num;
        while ((num = getNumber(reader)) != -1) {
            list.add(num + "");
        }
        while (list.size() != 16) {
            list.add("");
        }
        return list;
    }

    private static List<String> arrayToStringList(int[] buffer) {
        List<String> list = new ArrayList<>();
        list.add("");
        for (int num : buffer) {
            list.add(num + "");
        }
        for (int i = 0; i < 15 - buffer.length; i++) {
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
