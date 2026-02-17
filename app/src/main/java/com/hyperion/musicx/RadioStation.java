package com.hyperion.musicx;

public class RadioStation {
    String name, genre, url;
    boolean isHeader; // New flag

    // Normal station constructor
    RadioStation(String name, String genre, String url) {
        this.name = name;
        this.genre = genre;
        this.url = url;
        this.isHeader = false;
    }

    // Header/Divider constructor
    RadioStation(String headerName) {
        this.name = headerName;
        this.isHeader = true;
    }
}

