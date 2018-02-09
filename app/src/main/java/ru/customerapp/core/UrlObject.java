package ru.customerapp.core;

/**
 * Created by rash on 06.02.2018.
 */

public class UrlObject {

    public HttpMethod HttpMethod;
    public String Url;

    public UrlObject(HttpMethod httpMethod, String url) {
        HttpMethod = httpMethod;
        Url = url;
    }
}
