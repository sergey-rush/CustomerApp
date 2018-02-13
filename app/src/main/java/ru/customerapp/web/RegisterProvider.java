package ru.customerapp.web;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ru.customerapp.core.UrlObject;
import ru.customerapp.core.UrlType;

/**
 * Created by rash on 12.02.2018.
 */

public class RegisterProvider extends BaseProvider {
    public int logIn(String postData) {
        URL url;
        try {
            UrlObject urlObject = webContext.getUrl(UrlType.Register);
            url = new URL(urlObject.Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(urlObject.HttpMethod.toString());
            connection.setDoInput(true);
            connection.setDoOutput(true);

            serialisePost(connection, postData);
            responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                webContext.setCookie(connection.getHeaderFields());
                String output = deserializeToString(connection);
                webContext.User = parseToUser(output);
            } else {
                return responseCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseCode;
    }
}
