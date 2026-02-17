package com.hyperion.musicx;

public class RssItem {
    private String title;
    private String link;

    public RssItem(String title, String link) {
        this.title = title;
        this.link = link;
    }
    public String getTitle() { return title; }
    public String getLink() { return link; }

    // This determines what is shown in the simple ListView row
    @Override
    public String toString() { return title; }
}

