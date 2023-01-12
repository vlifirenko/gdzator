package com.gdzator.rest;

import android.util.Log;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final String TAG = LoggingInterceptor.class.getName();

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        Log.d(TAG, "To     : " + request.getURI());
        Log.d(TAG, "Method : " + request.getMethod().name());
        Log.d(TAG, "Data   : " + new String(data));

        for (Object key : request.getHeaders().keySet()) {
            Log.d(TAG, "Header <" + key + ">: " + request.getHeaders().get(key));
        }
        Log.d(TAG, "Body: " + new String(data, "UTF-8"));
        return clientHttpRequestExecution.execute(request, data);
    }
}
