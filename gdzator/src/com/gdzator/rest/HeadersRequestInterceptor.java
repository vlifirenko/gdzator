package com.gdzator.rest;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class HeadersRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final String TAG = HeadersRequestInterceptor.class.getName();

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        request.getHeaders().set("Connection", "Close");
        return clientHttpRequestExecution.execute(request, data);
    }
}
