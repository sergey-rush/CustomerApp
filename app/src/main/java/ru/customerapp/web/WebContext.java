package ru.customerapp.web;

import android.text.TextUtils;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.customerapp.core.AppMode;
import ru.customerapp.core.HttpMethod;
import ru.customerapp.core.Product;
import ru.customerapp.core.Section;
import ru.customerapp.core.UrlObject;
import ru.customerapp.core.UrlType;
import ru.customerapp.core.User;

/**
 * Created by rash on 06.02.2018.
 */
public class WebContext {

    private static WebContext current = new WebContext();
    public static WebContext getInstance(){
        return current;
    }

    public User User;
    public String Imei;
    public List<Section> SectionList;
    public List<Product> ProductList;

    private java.net.CookieManager CookieManager = new CookieManager();

    private WebContext() {

        if (Mode != AppMode.Product){
            //User = new User();
        }
        initUrls();
    }

    /*
     * Persists cookie in current session.
     */
    public void setCookie(Map<String, List<String>> headers)
    {
        final String COOKIES_HEADER = "Set-Cookie";
        List<String> cookiesHeaders = headers.get(COOKIES_HEADER);
        if (cookiesHeaders != null) {
            for (String cookie : cookiesHeaders) {
                CookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
    }

    public void attachCookieTo(HttpURLConnection connection)
    {
        if (CookieManager.getCookieStore().getCookies().size() > 0) {
            connection.setRequestProperty("Cookie", TextUtils.join(";",  CookieManager.getCookieStore().getCookies()));
        }
    }

    public AppMode Mode = AppMode.Develop;
    Map<AppMode, Map<UrlType, UrlObject>> urlMap = new HashMap<AppMode, Map<UrlType, UrlObject>>();

    private void initUrls()
    {
        Map<UrlType, UrlObject> developMap = new HashMap<UrlType, UrlObject>();
        developMap.put(UrlType.Login, new UrlObject(HttpMethod.POST, "http://10.0.1.99/api/accounts/"));
        developMap.put(UrlType.Register, new UrlObject(HttpMethod.PUT, "http://10.0.1.99/api/accounts/"));
        developMap.put(UrlType.Sections, new UrlObject(HttpMethod.GET, "http://10.0.1.99/api/sections/"));
        developMap.put(UrlType.Products, new UrlObject(HttpMethod.GET, "http://10.0.1.99/api/products/"));
        urlMap.put(AppMode.Develop, developMap);

        Map<UrlType, UrlObject> testMap = new HashMap<UrlType, UrlObject>();
        testMap.put(UrlType.Login, new UrlObject(HttpMethod.POST, "http://10.0.1.99/Metro.Web/api/accounts/"));
        testMap.put(UrlType.Register, new UrlObject(HttpMethod.PUT, "http://10.0.1.99/Metro.Web/api/accounts/"));
        testMap.put(UrlType.Sections, new UrlObject(HttpMethod.GET, "http://10.0.1.99/Metro.Web/api/sections/"));
        testMap.put(UrlType.Products, new UrlObject(HttpMethod.GET, "http://10.0.1.99/api/Metro.Web/products/"));
        urlMap.put(AppMode.Test, testMap);

        Map<UrlType, UrlObject> prodMap = new HashMap<UrlType, UrlObject>();
        prodMap.put(UrlType.Login, new UrlObject(HttpMethod.POST, "http://10.0.1.99/api/accounts/"));
        prodMap.put(UrlType.Register, new UrlObject(HttpMethod.PUT, "http://10.0.1.99/api/accounts/"));
        prodMap.put(UrlType.Sections, new UrlObject(HttpMethod.GET, "http://10.0.1.99/api/sections/"));
        prodMap.put(UrlType.Products, new UrlObject(HttpMethod.GET, "http://10.0.1.99/api/products/"));
        urlMap.put(AppMode.Product, prodMap);
    }

    public UrlObject getUrl(UrlType urlType) {
        return urlMap.get(Mode).get(urlType);
    }
}


