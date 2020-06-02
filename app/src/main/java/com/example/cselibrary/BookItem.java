package com.example.cselibrary;

public class BookItem {

    String title;
    String id;
    String image;
    String pdf;
    String category;

    public BookItem() {
    }

    public BookItem(String title, String id, String image, String pdf, String category) {
        this.title = title;
        this.id = id;
        this.image = image;
        this.pdf = pdf;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
