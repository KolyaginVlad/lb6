package com.example.lb6;

public class InfoAboutSort {
    private String nameSorting;
    private long countCompare;
    private long read;
    private long write;
    private long time;

    public long getWrite() {
        return write;
    }

    public void setWrite(long write) {
        this.write = write;
    }

    private InfoAboutSort(String nameSorting, long countCompare, long read, long write, long time) {
        this.nameSorting = nameSorting;
        this.countCompare = countCompare;
        this.read = read;
        this.write = write;
        this.time = time;
    }

    public static InfoAboutSort create(String name, long[] params) {
        return new InfoAboutSort(name, params[0], params[1], params[2], params[3]);
    }


    public String getNameSorting() {
        return this.nameSorting;
    }
    public void setNameSorting(String nameSorting) {
        this.nameSorting = nameSorting;
    }

    public long getCountCompare() {
        return this.countCompare;
    }
    public void setCountCompare(long countCompare) {
        this.countCompare = countCompare;
    }

    public long getRead() {
        return this.read;
    }
    public void setRead(long read) {
        this.read = read;
    }

    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }

}