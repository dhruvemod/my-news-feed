package com.example.dhruv.mynewsfeed;

import java.io.Serializable;

/**
 * Created by dhruv on 11/8/2017.
 */

public class News implements Serializable {
    private String title;
    private String author;
    private String url;
    private String section;
    private String date;

    public News(String title, String author, String url, String section, String date) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.section = section;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
