package ru.customerapp.web;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;

import ru.customerapp.core.UrlObject;
import ru.customerapp.core.UrlType;

/**
 * Created by rash on 06.02.2018.
 */

public class SectionProvider extends BaseProvider {

    public int getSections() {
        HttpURLConnection connection = null;
        URL url;
        try {
            UrlObject urlObject = webContext.getUrl(UrlType.Sections);
            url = new URL(urlObject.Url);
            connection = (HttpURLConnection) url.openConnection();
            webContext.attachCookieTo(connection);
            connection.connect();
            responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String output = deserializeToString(connection);
                webContext.SectionList = parseToSectionList(output);
            } else {
                return responseCode;
            }
        } catch (ParseException pex) {
            pex.printStackTrace();
        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return responseCode;
    }
}