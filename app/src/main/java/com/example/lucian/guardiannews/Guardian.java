package com.example.lucian.guardiannews;

public class Guardian {

    private String publicationDate;
    private String Title;
    private String mUrl;
    private String SectionName;
    private String Author;

    public Guardian( String publicationDate, String title, String mUrl, String sectionName, String author) {
        this.publicationDate = publicationDate;
        Title = title;
        this.mUrl = mUrl;
        SectionName = sectionName;
        Author = author;
    }



    public String getPublicationDate() {
        return publicationDate;
    }

    public String getTitle() {
        return Title;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getSectionName() {
        return SectionName;
    }

    public String getAuthor() {
        return Author;
    }
}
