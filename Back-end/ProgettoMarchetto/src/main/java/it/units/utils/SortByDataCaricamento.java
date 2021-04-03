package it.units.utils;

import it.units.entities.proxies.FilesInfo;

import java.util.Comparator;

public class SortByDataCaricamento implements Comparator<FilesInfo> {
    public int compare(FilesInfo a, FilesInfo b) {
        return b.getDataCaricamento().compareTo(a.getDataCaricamento());
    }
}
