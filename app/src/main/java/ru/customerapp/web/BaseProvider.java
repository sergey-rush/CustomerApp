package ru.customerapp.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.customerapp.core.Product;
import ru.customerapp.core.Section;

/**
 * Created by rash on 06.02.2018.
 */

public class BaseProvider {
    protected WebContext webContext = WebContext.getInstance();

    protected int responseCode = 0;

    protected List<Product> parseToProductList(String input) throws JSONException, ParseException {
        JSONArray items = new JSONArray(input);
        ArrayList<Product> productList = new ArrayList<Product>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            Product product = new Product();
            product.ProductUid = item.getString("ProductUid");
            product.Name = item.getString("Name");
            product.Quantity = item.getInt("Quantity");
            product.Price = item.getString("Price");
            product.Discount = item.getString("Discount");
            product.Sku = item.getString("Sku");
            product.BoxSize = item.getString("BoxSize");
            product.ImageUrl = item.getString("ImageUrl");
            productList.add(product);
        }
        return productList;
    }

    protected List<Section> parseToSectionList(String input) throws JSONException, ParseException {
        JSONArray items = new JSONArray(input);
        ArrayList<Section> sectionList = new ArrayList<Section>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            Section section = new Section();
            section.SectionId = item.getInt("Id");
            section.ParentId = item.getInt("ParentId");
            section.ChildIndex = item.getInt("ChildIndex");
            section.Name = item.getString("Name");
            section.QueryString = item.getString("QueryString");
            sectionList.add(section);
        }
        return sectionList;
    }

    protected void serialisePost(HttpURLConnection connection, String postData) throws IOException {
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(postData);
        writer.flush();
        writer.close();
        os.close();
    }

    protected String deserializeToString(HttpURLConnection connection) throws IOException {
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();

        String output = buffer.toString();
        return output;
    }
}
