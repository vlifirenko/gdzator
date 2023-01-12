package com.gdzator.rest;

import com.gdzator.content.rest.AuthorsResponse;
import com.gdzator.content.rest.BooksResponse;
import com.gdzator.content.rest.SectionsResponse;
import com.gdzator.content.rest.SubjectsResponse;
import com.gdzator.content.rest.TasksResponse;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@Rest(rootUrl = "http://gdzator.ru",
        converters = {FormHttpMessageConverter.class, GsonHttpMessageConverter.class},
        interceptors = {HeadersRequestInterceptor.class, LoggingInterceptor.class})
public interface RestClient extends RestClientSupport {

    @Get("/api/v1/authors")
    @Accept(MediaType.APPLICATION_JSON)
    AuthorsResponse getAuthors();

    @Get("/api/v1/books?class={clazz}&subjectId={subjectId}")
    @Accept(MediaType.APPLICATION_JSON)
    BooksResponse getBooks(String clazz, String subjectId);

    @Get("/api/v1/sections?book={bookId}")
    @Accept(MediaType.APPLICATION_JSON)
    SectionsResponse getSections(String bookId);

    @Get("/api/v1/subjects?class={clazz}")
    @Accept(MediaType.APPLICATION_JSON)
    SubjectsResponse getSubjects(String clazz);

    @Get("/api/v1/tasks?book={bookId}")
    @Accept(MediaType.APPLICATION_JSON)
    TasksResponse getTasks(String bookId);

    @Get("/api/v1/tasks?book={bookId}&section={sectionId}")
    @Accept(MediaType.APPLICATION_JSON)
    TasksResponse getTasksBySection(String bookId, String sectionId);

    void setHeader(String name, String value);

    String getHeader(String name);
}
