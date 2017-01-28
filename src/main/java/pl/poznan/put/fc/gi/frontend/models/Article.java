package pl.poznan.put.fc.gi.frontend.models;

/**
 * @author Kamil Walkowiak
 */
public class Article {
    private int id;
    private String title;
    private int date;
    private String authors;

    public Article(int id, String title, int date, String authors) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.authors = authors;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDate() {
        return date;
    }

    public String getAuthors() {
        return authors;
    }
}
