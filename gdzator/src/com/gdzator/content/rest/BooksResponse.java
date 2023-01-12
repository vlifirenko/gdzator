package com.gdzator.content.rest;

import com.gdzator.content.model.Book;

import java.util.List;

public class BooksResponse {

    public int status;
    public List<Book> result;
    public String error;

    public static class Image {
        public String url;
    }

}
