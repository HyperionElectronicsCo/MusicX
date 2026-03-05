package com.hyperion.musicx.libraryadapters.settings;

public class CacheItem {
    private String name;
    private long sizeInBytes;

    public CacheItem(String name, long sizeInBytes) {
        this.name = name;
        this.sizeInBytes = sizeInBytes;
    }

    public String getName() { return name; }
    public long getSizeInBytes() { return sizeInBytes; }
}

